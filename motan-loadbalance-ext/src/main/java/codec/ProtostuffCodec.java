package codec;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;

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

@SpiMeta(name="proto-codec")
public class ProtostuffCodec implements Codec {

	private static final short MAGIC = (short) 0xF0F0;

	private static final byte MASK = 0x07;

	private Schema<RequestProtocol> requestSchema = RuntimeSchema.createFrom(RequestProtocol.class);
	
	private Schema<ResponseProtocol> responseSchema = RuntimeSchema.createFrom(ResponseProtocol.class);

	
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
		if (data.length <= RpcProtocolVersion.VERSION_1.getHeaderLength()) {
			throw new MotanFrameworkException("decode error: format problem",
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

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
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private byte[] encodeRequest(Channel channel, Request request)
			throws IOException {
		
		RequestProtocol requestProtocol = new RequestProtocol();
		
		requestProtocol.interfaceName = request.getInterfaceName();
		requestProtocol.methodName = request.getMethodName();
		requestProtocol.paramtersDesc = request.getParamtersDesc();
		requestProtocol.arguments = request.getArguments();
		requestProtocol.attachments = request.getAttachments();
		
		
		byte[] body = null;
		
		LinkedBuffer buffer = LinkedBuffer.allocate();
		try{
			
			body = ProtobufIOUtil.toByteArray(requestProtocol, requestSchema, buffer);
		}catch(Throwable e){
			
		}finally{
			buffer.clear();
		}
		
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
		
		ResponseProtocol responseProtocol = new ResponseProtocol();
		byte flag = 0;

		if (value.getException() != null) {
			responseProtocol.className = value.getException().getClass().getName();
			responseProtocol.value = value.getException();
            flag = MotanConstants.FLAG_RESPONSE_EXCEPTION;
        } else if (value.getValue() == null) {
            flag = MotanConstants.FLAG_RESPONSE_VOID;
        } else {
   		 	responseProtocol.className = value.getValue().getClass().getName();
   		 responseProtocol.value = value.getValue();
            flag = MotanConstants.FLAG_RESPONSE;
        }

		byte[] body = null;

		LinkedBuffer buffer = LinkedBuffer.allocate();
		try{
			
			body = ProtobufIOUtil.toByteArray(responseProtocol, responseSchema, buffer);
		}finally{
			buffer.clear();
		}
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
		
		RequestProtocol requestProtocol = new RequestProtocol();
		ProtobufIOUtil.mergeFrom(body, requestProtocol, requestSchema);

		

		DefaultRequest rpcRequest = new DefaultRequest();
		rpcRequest.setRequestId(requestId);
		rpcRequest.setInterfaceName( requestProtocol.interfaceName);
		rpcRequest.setMethodName(requestProtocol.methodName);
		rpcRequest.setParamtersDesc(requestProtocol.paramtersDesc);
		rpcRequest.setArguments(requestProtocol.arguments);
		rpcRequest.setAttachments(requestProtocol.attachments);


		return rpcRequest;
	}


	private Object decodeResponse(byte[] body, byte dataType, long requestId,
			Serialization serialization) throws IOException,
			ClassNotFoundException {

		ResponseProtocol responseProtocol = new ResponseProtocol();
		
		ProtobufIOUtil.mergeFrom(body, responseProtocol, responseSchema);
		
		long processTime = responseProtocol.proessTime;

		DefaultResponse response = new DefaultResponse();
		response.setRequestId(requestId);
		response.setProcessTime(processTime);

		if (dataType == MotanConstants.FLAG_RESPONSE_VOID) {
			return response;
		}

		Object result = responseProtocol.value;

		if (dataType == MotanConstants.FLAG_RESPONSE) {
			response.setValue(result);
		} else if (dataType == MotanConstants.FLAG_RESPONSE_EXCEPTION) {
			response.setException((Exception) result);
		} else {
			throw new MotanFrameworkException(
					"decode error: response dataType not support " + dataType,
					MotanErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
		}

		response.setRequestId(requestId);

		return response;
	}

}
