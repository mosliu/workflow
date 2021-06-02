package net.liuxuan.utils.json;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-03-22
 **/
public class GsonAdapterUtils {
    public static TreeMap<String, Object> readObjectToMap(JsonReader reader) throws IOException {
        TreeMap<String, Object> rtnMap = new TreeMap<>();
        reader.beginObject();
        while (reader.peek() != JsonToken.END_OBJECT) {
            String key = reader.nextName();
            switch (reader.peek()) {
                case BEGIN_OBJECT:
                    rtnMap.put(key, readObjectToMap(reader));
                    break;
                case NULL:
                    reader.nextNull();
                    break;
                case NUMBER:
                    String n = reader.nextString();
                    if (n.indexOf('.') != -1) {
                        rtnMap.put(key, Double.parseDouble(n));
                    }
                    rtnMap.put(key, Long.parseLong(n));
//                    rtnMap.put(key, reader.nextInt());
                    break;
                case BOOLEAN:
                    rtnMap.put(key, reader.nextBoolean());
                    break;
                case STRING:
                    rtnMap.put(key, reader.nextString());
                    break;
                case BEGIN_ARRAY:
                    reader.beginArray();
                    List<String> array = new LinkedList<>();
                    while (reader.peek() != JsonToken.END_ARRAY) {
                        array.add(reader.nextString());
                    }
                    reader.endArray();
                    rtnMap.put(key, array);
                    break;
            }
            reader.endObject();
        }
        // Value of @type should be short type name or full type name.
        // Doesn't support relative IRI or compact IRI for now.
        return rtnMap;
    }
}
