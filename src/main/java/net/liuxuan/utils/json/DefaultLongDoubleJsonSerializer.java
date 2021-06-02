package net.liuxuan.utils.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2019-12-31
 **/
public class DefaultLongDoubleJsonSerializer implements JsonSerializer<Double> {
    @Override
    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == src.longValue())
            return new JsonPrimitive(src.longValue());
        return new JsonPrimitive(src);
    }
}
