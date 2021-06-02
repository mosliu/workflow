package net.liuxuan.utils.json;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 2020-11-09 此方法适用于Gson在转换时，碰到了应该类型为数字，但是传的是空字符串的情况。
 * @date 2020-11-109
 **/
public class EmptyStringToNumberTypeAdapter<T extends Number> extends TypeAdapter<Number> {
    private Class<T> templateClass;

    public EmptyStringToNumberTypeAdapter(Class<T> templateClass) {
        this.templateClass = templateClass;
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            if (templateClass.isPrimitive()) {
                return 0;
            } else {
                return null;
            }
        }
        try {
            String value = in.nextString();
            if (value == null || "".equals(value)) {
                if (templateClass.isPrimitive()) {
                    return 0;
                } else {
                    return null;
                }
            }
            if (templateClass == int.class || templateClass == Integer.class) {
                return Integer.parseInt(value);
            }
            if (templateClass == long.class || templateClass == Long.class) {
                return Long.parseLong(value);
            }
            if (templateClass == byte.class || templateClass == Byte.class) {
                return Byte.parseByte(value);
            }
            if (templateClass == short.class || templateClass == Short.class) {
                return Short.parseShort(value);
            }
            if (templateClass == float.class || templateClass == Float.class) {
                return Float.parseFloat(value);
            }
            if (templateClass == double.class || templateClass == Double.class) {
                return Double.parseDouble(value);
            }
            return null;
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }

    static public class EmptyStringToIntTypeAdapter extends EmptyStringToNumberTypeAdapter<Integer> {
        public EmptyStringToIntTypeAdapter() {
            super(Integer.class);
        }
    }

    static public class EmptyStringToLongTypeAdapter extends EmptyStringToNumberTypeAdapter<Long> {
        public EmptyStringToLongTypeAdapter() {
            super(Long.class);
        }
    }

    static public class EmptyStringToShortTypeAdapter extends EmptyStringToNumberTypeAdapter<Short> {
        public EmptyStringToShortTypeAdapter() {
            super(Short.class);
        }
    }

    static public class EmptyStringToFloatTypeAdapter extends EmptyStringToNumberTypeAdapter<Float> {
        public EmptyStringToFloatTypeAdapter() {
            super(Float.class);
        }
    }

    static public class EmptyStringToDoubleTypeAdapter extends EmptyStringToNumberTypeAdapter<Double> {
        public EmptyStringToDoubleTypeAdapter() { super(Double.class); }
    }

    static public class EmptyStringToByteTypeAdapter extends EmptyStringToNumberTypeAdapter<Byte> {
        public EmptyStringToByteTypeAdapter() { super(Byte.class); }
    }

}
