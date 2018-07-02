package cat.filter;

import java.util.UUID;

import cat.context.MotanCatContext;
import cat.enums.CatErrorType;
import cat.enums.CatEventType;
import cat.enums.MotanNodeType;

import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.AbstractMessage;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.common.URLParamType;
import com.weibo.api.motan.extension.SpiMeta;
import com.weibo.api.motan.exception.MotanAbstractException;
import com.weibo.api.motan.exception.MotanBizException;
import com.weibo.api.motan.exception.MotanFrameworkException;
import com.weibo.api.motan.exception.MotanServiceException;
import com.weibo.api.motan.filter.Filter;
import com.weibo.api.motan.rpc.Caller;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import com.weibo.api.motan.rpc.RpcContext;
import com.weibo.api.motan.rpc.URL;
import com.weibo.api.motan.transport.support.DefaultRpcHeartbeatFactory;
import com.weibo.api.motan.util.ExceptionUtil;
import com.weibo.api.motan.util.LoggerUtil;

@SpiMeta(name = "cat")
public class CatFilter implements Filter{
	
	@Override
	public Response filter(Caller<?> caller, Request request) {
		
		
		if(!MotanCat.isEnable()){
			return caller.call(request);
		}
		String nodeType = caller.getUrl().getParameter(URLParamType.nodeType.getName());
		String loggerName = request.getInterfaceName()+"."+request.getMethodName();
		
		String type = "RpcClient";
		
		boolean provider = false;
		
		if(MotanConstants.NODE_TYPE_SERVICE.equalsIgnoreCase(nodeType)){
			type = "RpcServer";
			provider = true;
		}
		Transaction transaction = Cat.newTransaction(type, loggerName);
		
		try{
			Context context = null;
			
			if(provider){
				context =  MotanCatContext.getContext();
				MotanCatContext.initRequest(request,context);
				createProviderCross(caller.getUrl(), transaction);
		        Cat.logRemoteCallServer(context);
			}else{
				//添加cat所需的参数
				context =  MotanCatContext.getContext();
		        Cat.logRemoteCallClient(context);
		        MotanCatContext.fillRequest(request,context);
		        createRefererCross(caller.getUrl(), transaction);
			}
		}catch(Throwable e){
			LoggerUtil.error("CatFilter.filter error", e);
		}
		Response response = null;
		Exception e = null;
		Throwable e1 = null;

		try{
			try{
				response = caller.call(request);
				e = response.getException();
			}catch(Exception ee){
				e = ee;
			}
			if(e != null){
	 			
				
	 			MotanAbstractException wrapException = null;
	            Event event = null;
	            if(ExceptionUtil.isBizException(e)){
	            	wrapException = (MotanAbstractException) e;
	            	e1 = wrapException.getCause();
	            }else if(e instanceof MotanServiceException){
	            	wrapException = (MotanAbstractException) e;
	            	e1 = wrapException;
	            }else if(e instanceof MotanFrameworkException){
	            	wrapException = (MotanAbstractException) e;
	            	e1 = wrapException;
	            }else{
	            	e1 = e;
	            	wrapException = new MotanBizException(e);
	            }
            	event = Cat.newEvent(type,loggerName);
	            event.setStatus(e1);
	            completeEvent(event);
                transaction.addChild(event);

                LoggerUtil.error("", e1);
	            if(!provider){
		    		Cat.logError(e1.getMessage(),e1);
	            	throw wrapException;
	            }
	    	}
		}finally{
			if(e1 == null){
				transaction.setStatus(Event.SUCCESS);
			}else{
				transaction.setStatus(e1);
			}
            transaction.complete();
            MotanCatContext.removeContext();
		}
		
		return response;
	}
	
    private void completeEvent(Event event){
        AbstractMessage message = (AbstractMessage) event;
        message.setCompleted(true);
    }

    private void createProviderCross(URL url,Transaction transaction){
    	
    	Request request = RpcContext.getContext().getRequest();
    	
        
        String remoteHost = request.getAttachments().get(URLParamType.host.getName());
        
        Event refererAppEvent = Cat.newEvent(CatEventType.REFERER_REQUEST.getName(),remoteHost);
        Event crossAppEvent = Cat.newEvent(CatEventType.PROVIDER.getName(),url.getIdentity());
       
        refererAppEvent.setStatus(Event.SUCCESS);
        crossAppEvent.setStatus(Event.SUCCESS);
        transaction.addChild(refererAppEvent);
        transaction.addChild(crossAppEvent);
        completeEvent(refererAppEvent);
        completeEvent(crossAppEvent);
  }
    
    private void createRefererCross(URL url,Transaction transaction){
    	
    	
        Event refererEvent =   Cat.newEvent(CatEventType.REFERER.getName(),url.getHost());
        Event refererCallEvent =   Cat.newEvent(CatEventType.REFERER_CALL.getName(),url.getIdentity());
        Event crossPortEvent =   Cat.newEvent(CatEventType.REFERER_CALL_SERVER.getName(),url.getServerPortStr());
        refererEvent.setStatus(Event.SUCCESS);
        refererCallEvent.setStatus(Event.SUCCESS);
        crossPortEvent.setStatus(Event.SUCCESS);
        transaction.addChild(refererEvent);
        transaction.addChild(refererCallEvent);
        transaction.addChild(crossPortEvent);
        
        completeEvent(refererEvent);
        completeEvent(refererCallEvent);
        completeEvent(crossPortEvent);
    }
}
