package loadblance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import motan.dto.RpcDTO;

import com.google.protobuf.ByteString;
import com.weibo.api.motan.codec.Codec;
import com.weibo.api.motan.codec.Serialization;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.common.URLParamType;
import com.weibo.api.motan.extension.ExtensionLoader;
import com.weibo.api.motan.extension.SpiMeta;
import com.weibo.api.motan.exception.MotanErrorMsgConstant;
import com.weibo.api.motan.exception.MotanFrameworkException;
import com.weibo.api.motan.protocol.rpc.RpcProtocolVersion;
import com.weibo.api.motan.rpc.DefaultRequest;
import com.weibo.api.motan.rpc.DefaultResponse;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import com.weibo.api.motan.transport.Channel;
import com.weibo.api.motan.util.ByteUtil;
import com.weibo.api.motan.util.ExceptionUtil;
import com.weibo.api.motan.util.ReflectUtil;


public class MotanCodec implements Codec {

	private static final short MAGIC = (short) 0xF0F0;

	private static final byte MASK = 0x07;


	@Override
	public byte[] encode(Channel channel, Object message) throws IOException {
		try {
			if (message instanceof Request) {
				return encodeRequest(channel, (Request) message);
			} else if (message instanceof Response) {
				return encodeResponse(channel, (Response) message);
			}
		} catch (Exception e) {
			if (ExceptionUtil.isMotanException(e)) {
				throw (RuntimeException) e;
			} else {
				throw new MotanFrameworkException("encode error: isResponse="
						+ (message instanceof Response), e,
						MotanErrorMsgConstant.FRAMEWORK_ENCODE_ERROR);
			}
		}

		throw new MotanFrameworkException(
				"encode error: message type not support, " + message.getClass(),
				MotanErrorMsgConstant.FRAMEWORK_ENCODE_ERROR);
	}

	/**
	 * decode data
	 * 
	 * <pre>
	 * 		对于client端：主要是来自server端的response or exception
	 * 		对于server端: 主要是来自client端的request
	 * </pre>
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	@Override
	public Object decode(Channel channel, String remoteIp, byte[] data)
			throws IOException {

		short type = ByteUtil.bytes2short(data, 0);

		if (type != MAGIC) {
			throw new MotanFrameworkException("decode error: magic error",
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

		if (data[2] != RpcProtocolVersion.VERSION_1.getVersion()) {
			throw new MotanFrameworkException("decode error: version error",
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

		int bodyLength = ByteUtil.bytes2int(data, 12);

		if (RpcProtocolVersion.VERSION_1.getHeaderLength() + bodyLength != data.length) {
			throw new MotanFrameworkException(
					"decode error: content length error",
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

		byte flag = data[3];
		byte dataType = (byte) (flag & MASK);
		boolean isResponse = (dataType != MotanConstants.FLAG_REQUEST);

		byte[] body = new byte[bodyLength];

		System.arraycopy(data, RpcProtocolVersion.VERSION_1.getHeaderLength(),
				body, 0, bodyLength);

		long requestId = ByteUtil.bytes2long(data, 4);
		Serialization serialization = ExtensionLoader.getExtensionLoader(
				Serialization.class).getExtension(
				channel.getUrl().getParameter(URLParamType.serialize.getName(),
						URLParamType.serialize.getValue()));

		try {
			if (isResponse) { // response
				return decodeResponse(body, dataType, requestId, serialization);
			} else {
				return decodeRequest(body, requestId, serialization);
			}
		} catch (ClassNotFoundException e) {
			throw new MotanFrameworkException("decode "
					+ (isResponse ? "response" : "request")
					+ " error: class not found", e,
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		} catch (Exception e) {
			if (ExceptionUtil.isMotanException(e)) {
				throw (RuntimeException) e;
			} else {
				throw new MotanFrameworkException("decode error: isResponse="
						+ isResponse, e,
						MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
			}
		}
	}

	/**
	 * request body 数据：
	 * 
	 * <pre>
	 * 
	 * 	 body:
	 * 
	 * 	 byte[] data :  
	 * 
	 * 			serialize(interface_name, method_name, method_param_desc, method_param_value, attachments_size, attachments_value) 
	 * 
	 *   method_param_desc:  for_each (string.append(method_param_interface_name))
	 * 
	 *   method_param_value: for_each (method_param_name, method_param_value)
	 * 
	 * 	 attachments_value:  for_each (attachment_name, attachment_value)
	 * 
	 * </pre>
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private byte[] encodeRequest(Channel channel, Request request)
			throws IOException {

		Serialization serialization = ExtensionLoader.getExtensionLoader(
				Serialization.class).getExtension(
				channel.getUrl().getParameter(URLParamType.serialize.getName(),
						URLParamType.serialize.getValue()));
		RpcDTO.Request.Builder requestBuilder = RpcDTO.Request.newBuilder();
		
		requestBuilder.setInterfaceName(request.getInterfaceName());
		requestBuilder.setMethodName(request.getMethodName());
		requestBuilder.setParamtersDesc(request.getParamtersDesc());
		
		List<ByteString> arguments = new ArrayList<ByteString>();
		if (request.getArguments() != null && request.getArguments().length > 0) {
			for (Object obj : request.getArguments()) {

				if (obj == null) {
					arguments.add(ByteString.EMPTY);
				} else {
					arguments.add(ByteString.copyFrom(serialize(obj,
							serialization)));
				}
			}
		}
		requestBuilder.addAllArguments(arguments);

		List<RpcDTO.Request.attachment> attachments = new ArrayList<RpcDTO.Request.attachment>();
		RpcDTO.Request.attachment attachment = null;
		if (request.getAttachments() != null
				&& !request.getAttachments().isEmpty()) {
			for (Map.Entry<String, String> entry : request.getAttachments()
					.entrySet()) {
				attachment = RpcDTO.Request.attachment.newBuilder()
						.setKey(entry.getKey()).setValue(entry.getValue())
						.build();

				attachments.add(attachment);
			}
		}
		requestBuilder.addAllAttachments(attachments);
		byte[] body = requestBuilder.build().toByteArray();

		byte flag = MotanConstants.FLAG_REQUEST;

		return encode(body, flag, request.getRequestId());
	}

	/**
	 * response body 数据：
	 * 
	 * <pre>
	 * 
	 * body:
	 * 
	 * 	 byte[] :  serialize (result) or serialize (exception)
	 * 
	 * </pre>
	 * 
	 * @param channel
	 * @param value
	 * @return
	 * @throws IOException
	 */
	private byte[] encodeResponse(Channel channel, Response value)
			throws IOException {
		Serialization serialization = ExtensionLoader.getExtensionLoader(
				Serialization.class).getExtension(
				channel.getUrl().getParameter(URLParamType.serialize.getName(),
						URLParamType.serialize.getValue()));

		byte flag = 0;
		RpcDTO.Response.Builder responseBuilder = RpcDTO.Response.newBuilder();
		
		responseBuilder.setProcessTime(value.getProcessTime());

		if (value.getException() != null) {

			responseBuilder.setClassName(value.getException().getClass()
					.getName());
			responseBuilder.setData(ByteString.copyFrom(serialize(
					value.getException(), serialization)));
			flag = MotanConstants.FLAG_RESPONSE_EXCEPTION;
		} else if (value.getValue() == null) {
			flag = MotanConstants.FLAG_RESPONSE_VOID;
		} else {
			responseBuilder.setClassName(value.getValue().getClass().getName());
			responseBuilder.setData(ByteString.copyFrom(serialize(
					value.getValue(), serialization)));
			flag = MotanConstants.FLAG_RESPONSE;
		}

		byte[] body = responseBuilder.buildPartial().toByteArray();

		return encode(body, flag, value.getRequestId());
	}

	/**
	 * 数据协议：
	 * 
	 * <pre>
	 * 
	 * header:  16个字节 
	 * 
	 * 0-15 bit 	:  magic
	 * 16-23 bit	:  version
	 * 24-31 bit	:  extend flag , 其中： 29-30 bit: event 可支持4种event，比如normal, exception等,  31 bit : 0 is request , 1 is response 
	 * 32-95 bit 	:  request id
	 * 96-127 bit 	:  body content length
	 * 
	 * </pre>
	 * 
	 * @param body
	 * @param flag
	 * @param requestId
	 * @return
	 * @throws IOException
	 */
	private byte[] encode(byte[] body, byte flag, long requestId)
			throws IOException {
		byte[] header = new byte[RpcProtocolVersion.VERSION_1.getHeaderLength()];
		int offset = 0;

		// 0 - 15 bit : magic
		ByteUtil.short2bytes(MAGIC, header, offset);
		offset += 2;

		// 16 - 23 bit : version
		header[offset++] = RpcProtocolVersion.VERSION_1.getVersion();

		// 24 - 31 bit : extend flag
		header[offset++] = flag;

		// 32 - 95 bit : requestId
		ByteUtil.long2bytes(requestId, header, offset);
		offset += 8;

		// 96 - 127 bit : body content length
		ByteUtil.int2bytes(body.length, header, offset);

		byte[] data = new byte[header.length + body.length];

		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(body, 0, data, header.length, body.length);

		return data;
	}

	private Object decodeRequest(byte[] body, long requestId,
			Serialization serialization) throws IOException,
			ClassNotFoundException {

		RpcDTO.Request request = RpcDTO.Request.parseFrom(body);

		String interfaceName = request.getInterfaceName();
		String methodName = request.getMethodName();
		String paramtersDesc = request.getParamtersDesc();

		DefaultRequest rpcRequest = new DefaultRequest();
		rpcRequest.setRequestId(requestId);
		rpcRequest.setInterfaceName(interfaceName);
		rpcRequest.setMethodName(methodName);
		rpcRequest.setParamtersDesc(paramtersDesc);
		rpcRequest.setArguments(decodeRequestParameter(
				request.getArgumentsList(), paramtersDesc, serialization));
		rpcRequest.setAttachments(decodeRequestAttachments(request
				.getAttachmentsList()));

		return rpcRequest;
	}

	private Object[] decodeRequestParameter(List<ByteString> inputs,
			String parameterDesc, Serialization serialization)
			throws IOException, ClassNotFoundException {
		if (parameterDesc == null || parameterDesc.equals("")) {
			return null;
		}

		Class<?>[] classTypes = ReflectUtil.forNames(parameterDesc);

		Object[] paramObjs = new Object[classTypes.length];

		for (int i = 0; i < classTypes.length; i++) {
			paramObjs[i] = deserialize(inputs.get(i).toByteArray(),
					classTypes[i], serialization);
		}

		return paramObjs;
	}

	private Map<String, String> decodeRequestAttachments(
			List<RpcDTO.Request.attachment> attachments) throws IOException,
			ClassNotFoundException {

		if (attachments == null || attachments.size() <= 0) {
			return null;
		}

		Map<String, String> attachments1 = new HashMap<String, String>();

		int size = attachments.size();
		for (int i = 0; i < size; i++) {
			attachments1.put(attachments.get(i).getKey(), attachments.get(i)
					.getValue());
		}

		return attachments1;
	}

	private Object decodeResponse(byte[] body, byte dataType, long requestId,
			Serialization serialization) throws IOException,
			ClassNotFoundException {

		RpcDTO.Response response = RpcDTO.Response.parseFrom(body);

		DefaultResponse rpcResponse = new DefaultResponse();
		rpcResponse.setRequestId(requestId);
		rpcResponse.setProcessTime(response.getProcessTime());

		if (dataType == MotanConstants.FLAG_RESPONSE_VOID) {
			return rpcResponse;
		}

		String className = response.getClassName();
		Class<?> clz = ReflectUtil.forName(className);

		Object result = deserialize(response.getData().toByteArray(), clz,
				serialization);

		if (dataType == MotanConstants.FLAG_RESPONSE) {
			rpcResponse.setValue(result);
		} else if (dataType == MotanConstants.FLAG_RESPONSE_EXCEPTION) {
			rpcResponse.setException((Exception) result);
		} else {
			throw new MotanFrameworkException(
					"decode error: response dataType not support " + dataType,
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

		rpcResponse.setRequestId(requestId);

		return rpcResponse;
	}

	protected byte[] serialize(Object message, Serialization serialize)
			throws IOException {
		if (message == null) {
			return null;
		}

		return serialize.serialize(message);
	}

	protected Object deserialize(byte[] value, Class<?> type,
			Serialization serialize) throws IOException {
		if (value == null || value.length <= 0) {
			return null;
		}

		return serialize.deserialize(value, type);
	}

}
