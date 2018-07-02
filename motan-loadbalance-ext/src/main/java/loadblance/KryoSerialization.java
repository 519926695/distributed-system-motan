package loadblance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.weibo.api.motan.codec.Serialization;
import com.weibo.api.motan.extension.SpiMeta;

@SpiMeta(name = "kryo")
public class KryoSerialization implements Serialization {

	    @Override
	    public byte[] serialize(Object data) throws IOException {
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        Kryo kryo = new Kryo();
	        Output output = new Output(bos);
			kryo.writeObject(output , data);
			output.flush();
			output.close();
	        return bos.toByteArray();
	    }

	    @Override
	    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
	    	ByteArrayInputStream bis = new ByteArrayInputStream(data);
	        Kryo kryo = new Kryo();
	        Input input = new Input(bis);
	        input.close();
	        return kryo.readObject(input, clz);
	    }

}
