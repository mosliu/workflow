package net.liuxuan.utils.json;

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
 * @description Gson使用，当传过来的String Array出问题是，保证可以继续处理下去，无法处理的会跳过
 * @date 2019-11-29
 **/
@Log
public class FailSafeStringArrayTypeAdapter extends TypeAdapter<String[]> {

    @Override
    public void write(JsonWriter out, String[] value) throws IOException {
        out.beginArray();
        for (String e : value) {
            out.value(e);
        }
        out.endArray();
    }

    @Override
    public String[] read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (peek == JsonToken.STRING) {
            String str = in.nextString();
            return new String[]{str};
        }
        /* coerce booleans to strings for backwards compatibility */
        if (peek == JsonToken.BOOLEAN) {
            String str = Boolean.toString(in.nextBoolean());
            return new String[]{str};
        }
        if (peek == JsonToken.BEGIN_ARRAY) {
            ArrayList<String> strings = new ArrayList<>();
            in.beginArray();
            while (in.hasNext()) {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                } else {
//                    try {
                        strings.add(in.nextString());
//                    } catch (IllegalStateException ex) {
//                        log.warning("Json Expected String in Array But Got Another ,Pos :" + in.getPath());
//                        in.skipValue();
//                        strings.add("");
//                    }
                }
            }
            in.endArray();
            return strings.toArray(new String[0]);
        }
        if (peek == JsonToken.BEGIN_OBJECT) {
            log.warning("Json Expected String[] But Got Object:" + in.getPath());
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
}
