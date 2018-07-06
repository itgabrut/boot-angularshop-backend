package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.util.Base64;

import java.io.IOException;

/**
 * Created by imedvede on 05.07.2018.
 */
public  class BytesDeserializer extends StdDeserializer<byte[]> {

    private static final long serialVersionUID = 1514703510863497028L;

    public BytesDeserializer() {
        super(byte[].class);
    }

    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String base64 = node.asText();
        String[] separate = base64.split(",");
        if(separate.length > 1)base64 = separate[1];
        else base64 = separate[0];
        return Base64.getDecoder().decode(base64);
    }
}
