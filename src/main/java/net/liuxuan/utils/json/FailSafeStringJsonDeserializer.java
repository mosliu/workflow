package net.liuxuan.utils.json;

import com.google.gson.*;
import lombok.extern.java.Log;

import java.lang.reflect.Type;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2019-11-29
 **/
@Log
public class FailSafeStringJsonDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonPrimitive()) {
            log.warning("Json Expected String But Got Another");
            return null;
        }
        try {
            final JsonPrimitive primitive = (JsonPrimitive) element;
            String asString = primitive.getAsString();
            return asString;
//            final Number number = primitive.getAsNumber();
//            return number.doubleValue();
        } catch (ClassCastException ex) {
            log.warning("ClassCastException!" + ex.getMessage());
            return null;
        } catch (IllegalStateException ex) {
            log.warning("IllegalStateException!" + ex.getMessage());
            return null;
        }
//
//        JsonObject jsonObj = element.getAsJsonObject();
//        String service = jsonObj.get("service").getAsString();
//        String hostname = jsonObj.get("hostname").getAsString();
//        int port = jsonObj.get("port").getAsInt();
//        byte[] payload = context.deserialize(jsonObj.get("payload"), BYTE_ARRAY_TYPE);
//        InetSocketAddress address = new InetSocketAddress(hostname, port);
//        return new Discoverable(service, address, payload);

    }
}
