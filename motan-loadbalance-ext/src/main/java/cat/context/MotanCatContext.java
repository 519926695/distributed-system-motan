package cat.context;

import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.Cat;
import com.weibo.api.motan.rpc.Request;

public class MotanCatContext implements Cat.Context {

	private static final ThreadLocal<Cat.Context> CAT_CONTEXT = new ThreadLocal<Cat.Context>();

	private Map<String, String> properties = new HashMap<String, String>();

	@Override
	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	@Override
	public String getProperty(String key) {
		return properties.get(key);
	}

	public static Cat.Context getContext() {
		Cat.Context context = CAT_CONTEXT.get();
		if (context == null) {
			context = new MotanCatContext();
			CAT_CONTEXT.set(context);
		}
		return context;
	}

	public static void removeContext() {
		CAT_CONTEXT.remove();
	}

	public static void initRequest(Request request, Cat.Context context) {

		Map<String, String> attachments = request.getAttachments();
		if (attachments != null && attachments.size() > 0) {
			for (Map.Entry<String, String> entry : attachments.entrySet()) {
				if (Cat.Context.CHILD.equals(entry.getKey()) || Cat.Context.ROOT.equals(entry.getKey()) || Cat.Context.PARENT.equals(entry.getKey())) {
					context.addProperty(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	public static void fillRequest(Request request, Cat.Context context) {
		Map<String, String> attachments = request.getAttachments();
		if (attachments != null && attachments.size() > 0) {

			String rootId = context.getProperty(Cat.Context.ROOT);
			String parentId = context.getProperty(Cat.Context.PARENT);
			String childId = context.getProperty(Cat.Context.CHILD);

			if (rootId != null) {
				attachments.put(Cat.Context.ROOT, rootId);
			}
			if (parentId != null) {
				attachments.put(Cat.Context.PARENT, parentId);
			}
			if (childId != null) {
				attachments.put(Cat.Context.CHILD, childId);
			}
		}
	}
}
