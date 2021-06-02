package net.liuxuan.utils.json;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description  Gson使用，当传过来的String 出问题时，保证可以继续处理下去，无法处理的对象会跳过
 * @date 2019-11-29
 **/
@Log
public class FailSafeStringTypeAdapter extends TypeAdapter<String> {

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (peek == JsonToken.STRING) {
            return in.nextString();
        }
        /* coerce booleans to strings for backwards compatibility */
        if (peek == JsonToken.BOOLEAN) {
            return Boolean.toString(in.nextBoolean());
        }
        if (peek == JsonToken.BEGIN_ARRAY) {
            log.warning("期待String，发现Array，尝试解析！path:"+ in.getPath());
            JsonArray array = new JsonArray();
            in.beginArray();
            while (in.hasNext()) {
                array.add(read(in));
            }
            in.endArray();

            ArrayList<String> strings = new ArrayList<>();
            array.forEach(element -> {
                if (element.isJsonPrimitive()) {
                    //array是原始类型的则转化成原始内容，以逗号分隔
                    JsonPrimitive primitive = (JsonPrimitive) element;
                    strings.add(primitive.getAsString());
                } else {
                    //array非原始类型的，则返回null?
                    strings.add(element.toString());
                }
            });
            return Joiner.on(",").join(strings);
        }
        if (peek == JsonToken.BEGIN_OBJECT) {
            log.warning("期待String，发现Object，放弃！ path:" + in.getPath());
//            JsonObject object = new JsonObject();
            in.skipValue();
            return null;
//            in.beginObject();
//            in.
//            while (in.hasNext()) {
//                object.add(in.nextName(), read(in));
//            }
//            in.endObject();
//            return object;
        }
        //均不符合
        in.skipValue();
        return null;
//        return in.nextString();
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }
}
