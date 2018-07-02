package loadblance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.weibo.api.motan.cluster.loadbalance.AbstractLoadBalance;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.extension.SpiMeta;
import com.weibo.api.motan.rpc.Referer;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.util.MathUtil;

@SpiMeta(name = "consistent-ext")
public class ConsistentHashLoadBalanceExt<T> extends AbstractLoadBalance<T> {

	private List<Referer<T>> consistentHashReferers;

    @Override
    public void onRefresh(List<Referer<T>> referers) {
    	 Collections.sort(referers, new Comparator<Referer<T>>() {
         	
 			@Override
 			public int compare(Referer<T> o1, Referer<T> o2) {
 				return o1.getServiceUrl().getServerPortStr().compareTo(o1.getServiceUrl().getServerPortStr());
 			}
 		});
    	 
    	super.onRefresh(referers);

        List<Referer<T>> copyReferers = new ArrayList<Referer<T>>(referers);
        
       
        
        List<Referer<T>> tempRefers = new ArrayList<Referer<T>>();
        for (int i = 0; i < MotanConstants.DEFAULT_CONSISTENT_HASH_BASE_LOOP; i++) {
            Collections.shuffle(copyReferers);
            for (Referer<T> ref : copyReferers) {
                tempRefers.add(ref);
            }
        }
        
        consistentHashReferers = tempRefers;
    }

    @Override
    protected Referer<T> doSelect(Request request) {

        int hash = getHash(request);
        Referer<T> ref;
        for (int i = 0; i < getReferers().size(); i++) {
            ref = consistentHashReferers.get((hash + i) % consistentHashReferers.size());
            if (ref.isAvailable()) {
                return ref;
            }
        }
        return null;
    }

    @Override
    protected void doSelectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = getReferers();

        int hash = getHash(request);
        for (int i = 0; i < referers.size(); i++) {
            Referer<T> ref = consistentHashReferers.get((hash + i) % consistentHashReferers.size());
            if (ref.isAvailable()) {
                refersHolder.add(ref);
            }
        }
    }

    private int getHash(Request request) {
        int hashcode;
        if (request.getArguments() == null || request.getArguments().length == 0) {
            hashcode = request.hashCode();
        } else {
            hashcode = request.getArguments()[0].hashCode();
        }
        return MathUtil.getPositive(hashcode);
    }
}
