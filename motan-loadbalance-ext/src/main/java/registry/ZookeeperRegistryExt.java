package registry;

/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;

import com.weibo.api.motan.exception.MotanFrameworkException;
import com.weibo.api.motan.log.LogService;
import com.weibo.api.motan.registry.support.command.CommandListener;
import com.weibo.api.motan.registry.support.command.ServiceListener;
import com.weibo.api.motan.registry.zookeeper.ZkNodeType;
import com.weibo.api.motan.registry.zookeeper.ZkUtils;
import com.weibo.api.motan.registry.zookeeper.ZookeeperRegistry;
import com.weibo.api.motan.rpc.URL;
import com.weibo.api.motan.util.ConcurrentHashSet;
import com.weibo.api.motan.util.LoggerUtil;

public class ZookeeperRegistryExt extends ZookeeperRegistry {

	private ZkClient zkClient;
    private Set<URL> availableServices = new ConcurrentHashSet<URL>();
    private ConcurrentHashMap<URL, ConcurrentHashMap<ServiceListener, IZkChildListener>> serviceListeners = new ConcurrentHashMap<URL, ConcurrentHashMap<ServiceListener, IZkChildListener>>();
    private ConcurrentHashMap<URL, ConcurrentHashMap<CommandListener, IZkDataListener>> commandListeners = new ConcurrentHashMap<URL, ConcurrentHashMap<CommandListener, IZkDataListener>>();
    private final ReentrantLock clientLock = new ReentrantLock();
    private final ReentrantLock serverLock = new ReentrantLock();
    
    public ZookeeperRegistryExt(URL url, ZkClient client) {
        super(url,client);
        this.zkClient = client;
        IZkStateListener zkStateListener = new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                // do nothing
            }

            @Override
            public void handleNewSession() throws Exception {
                LoggerUtil.info("zkRegistry get new session notify.");
                reconnectService();
                reconnectClient();
            }
        };
        zkClient.subscribeStateChanges(zkStateListener);
    }


    @Override
    protected void doRegister(URL url) {
        try {
            serverLock.lock();
            // 防止旧节点未正常注销
            removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
            removeNode(url, ZkNodeType.AVAILABLE_SERVER);
            createNode(url, ZkNodeType.UNAVAILABLE_SERVER,false);
        } catch (Throwable e) {
            throw new MotanFrameworkException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doUnregister(URL url) {
        try {
            serverLock.lock();
            removeNode(url, ZkNodeType.AVAILABLE_SERVER);
            removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
            removeStatusNode(url);
        } catch (Throwable e) {
            throw new MotanFrameworkException(String.format("Failed to unregister %s to zookeeper(%s), cause: %s", url, getUrl(), e.getMessage()), e);
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doAvailable(URL url) {
        try{
            serverLock.lock();
            if (url == null) {
                availableServices.addAll(getRegisteredServiceUrls());
                for (URL u : getRegisteredServiceUrls()) {
                    removeNode(u, ZkNodeType.UNAVAILABLE_SERVER);
                    createNode(u, ZkNodeType.AVAILABLE_SERVER,true);
                    removeStatusNode(u);
                    createStatusNode(u);
                }
            } else {
                availableServices.add(url);
                removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
                createNode(url, ZkNodeType.AVAILABLE_SERVER,true);
                removeStatusNode(url);
                createStatusNode(url);
            }
        } finally {
            serverLock.unlock();
        }
    }

    @Override
    protected void doUnavailable(URL url) {
        try{
            serverLock.lock();
            if (url == null) {
                availableServices.removeAll(getRegisteredServiceUrls());
                for (URL u : getRegisteredServiceUrls()) {
                    removeNode(u, ZkNodeType.AVAILABLE_SERVER);

                    removeNode(u, ZkNodeType.UNAVAILABLE_SERVER);
                    createNode(u, ZkNodeType.UNAVAILABLE_SERVER,false);
                }
            } else {
                availableServices.remove(url);
                removeNode(url, ZkNodeType.UNAVAILABLE_SERVER);
                createNode(url, ZkNodeType.UNAVAILABLE_SERVER,false);
            }
        } finally {
            serverLock.unlock();
        }
    }

    private void createNode(URL url, ZkNodeType nodeType,boolean persistent) {
        String nodeTypePath = ZkUtils.toNodeTypePath(url, nodeType);
        if (!zkClient.exists(nodeTypePath)) {
            zkClient.createPersistent(nodeTypePath, true);
        }
        String nodePath = ZkUtils.toNodePath(url, nodeType);
        
		String data = url.toFullStr()+"&persistent="+persistent;
		
		if(persistent){
	         zkClient.createPersistent(nodePath, data);
        }else{
            zkClient.createEphemeral(nodePath,data);
        }
    }
    
    private void createStatusNode(URL url){
         String statusPath = ServiceStatus.toStatusPath(ZkUtils.toServicePath(url));
         if(!zkClient.exists(statusPath)){
             zkClient.createPersistent(statusPath, true);
         }
         String servicePath = ServiceStatus.toServiceStatusPath(statusPath,url.getServerPortStr());
         zkClient.createEphemeral(servicePath, url.toFullStr());
    }
    
    private void removeStatusNode(URL url){
    	
        String statusPath = ServiceStatus.toStatusPath(ZkUtils.toServicePath(url));

    	String serviceStatusPath = ServiceStatus.toServiceStatusPath(statusPath,url.getServerPortStr());
    	 
         if (zkClient.exists(serviceStatusPath)) {
             zkClient.delete(serviceStatusPath);
         }
    }
    
    private void removeNode(URL url, ZkNodeType nodeType) {
        String nodePath = ZkUtils.toNodePath(url, nodeType);
        if (zkClient.exists(nodePath)) {
            zkClient.delete(nodePath);
        }
    }
    
    private void reconnectService() {
        Collection<URL> allRegisteredServices = getRegisteredServiceUrls();
        if (allRegisteredServices != null && !allRegisteredServices.isEmpty()) {
            try {
                serverLock.lock();
                for (URL url : getRegisteredServiceUrls()) {
                    doRegister(url);
                }
                LoggerUtil.info("[{}] reconnect: register services {}", registryClassName, allRegisteredServices);

                for (URL url : availableServices) {
                    if (!getRegisteredServiceUrls().contains(url)) {
                        LoggerUtil.warn("reconnect url not register. url:{}", url);
                        continue;
                    }
                    doAvailable(url);
                }
                LoggerUtil.info("[{}] reconnect: available services {}", registryClassName, availableServices);
            } finally {
                serverLock.unlock();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void reconnectClient() {
        if (serviceListeners != null && !serviceListeners.isEmpty()) {
            try {
                clientLock.lock();
                for (Map.Entry entry : serviceListeners.entrySet()) {
                    URL url = (URL) entry.getKey();
                    ConcurrentHashMap<ServiceListener, IZkChildListener> childChangeListeners = serviceListeners.get(url);
                    if (childChangeListeners != null) {
                        for (Map.Entry e : childChangeListeners.entrySet()) {
                            subscribeService(url, (ServiceListener) e.getKey());
                        }
                    }
                }
                for (Map.Entry entry : commandListeners.entrySet()) {
                    URL url = (URL) entry.getKey();
                    ConcurrentHashMap<CommandListener, IZkDataListener> dataChangeListeners = commandListeners.get(url);
                    if (dataChangeListeners != null) {
                        for (Map.Entry e : dataChangeListeners.entrySet()) {
                            subscribeCommand(url, (CommandListener) e.getKey());
                        }
                    }
                }
                LoggerUtil.info("[{}] reconnect all clients", registryClassName);
            } finally {
                clientLock.unlock();
            }
        }
    }
}

