package net.liuxuan.utils.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Gson使用，当传过来的String Array出问题是，保证可以继续处理下去，无法处理的会跳过
 * @date 2019-11-29
 **/
@Log
public class FailSafeStringArrayXGTypeAdapter extends TypeAdapter<String[]> {

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
        } else if (peek == JsonToken.STRING) {
            String str = in.nextString();
            return new String[]{str};
        } else if (peek == JsonToken.BOOLEAN) {
            /* coerce booleans to strings for backwards compatibility */
            String str = Boolean.toString(in.nextBoolean());
            return new String[]{str};
        } else if (peek == JsonToken.BEGIN_ARRAY) {
            ArrayList<String> strings = new ArrayList<>();
            in.beginArray();
            while (in.hasNext()) {
                JsonToken peek1 = in.peek();
                if (peek1 == JsonToken.NULL) {
                    in.nextNull();
                } else if (peek1 == JsonToken.STRING) {
                    String str = in.nextString();
                    strings.add(str);
                } else {
                    log.severe("转换时数组中发现了非字符串类型数据:" + peek1.toString());
                    //星光数据特殊处理
                    if (peek1 == JsonToken.BEGIN_OBJECT) {
                        TreeMap<String, Object> treeMap = GsonAdapterUtils.readObjectToMap(in);
                        String url = (String) treeMap.get("url");
                        if (url != null) {
                            strings.add(url);
                        }
                    } else {
//                    try {
                        in.skipValue();
                    }
//                    strings.add(in.nextString());
//                    } catch (IllegalStateException ex) {
//                        log.warning("Json Expected String in Array But Got Another ,Pos :" + in.getPath());
//                        in.skipValue();
//                        strings.add("");
//                    }
                }
            }
            in.endArray();
            return strings.toArray(new String[0]);
        } else if (peek == JsonToken.BEGIN_OBJECT) {
            log.severe("转换时数组中发现了对象类型数据");
//            JsonObject object = new JsonObject();
            TreeMap<String, Object> treeMap = GsonAdapterUtils.readObjectToMap(in);
            String url = (String) treeMap.get("url");

            if (url != null) {
                return new String[]{url};
            } else {
                return null;
            }
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
