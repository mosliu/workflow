package net.liuxuan.utils.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.java.Log;

import java.lang.reflect.Type;
import java.util.*;

@Log
public class GsonUtils {

    public static final String TAG = "GsonUtils";

    public static final String EMPTY = "";
    /**
     * 空的 {@code JSON} 数据 - <code>"{}"</code>
     */
    public static final String EMPTY_JSON = "{}";
    /**
     * 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}
     */
    public static final String EMPTY_JSON_ARRAY = "[]";
    /**
     * 默认{@code JSON} 日期/时间字段的格式化模式
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    /**
     * {@code Google Gson} {@literal @Since} 注解常用的版本号常量 - {@code 1.0}
     */
    public static final Double SINCE_VERSION_10 = 1.0d;
    /**
     * {@code Google Gson} {@literal @Since} 注解常用的版本号常量 - {@code 1.1}
     */
    public static final Double SINCE_VERSION_11 = 1.1d;
    /**
     * {@code Google Gson} {@literal @Since} 注解常用的版本号常量 - {@code 1.2}
     */
    public static final Double SINCE_VERSION_12 = 1.2d;


    /**
     * 生产gson对象
     * 不序列化空值，不使用版本，时间使用yyyy-MM-dd HH:mm:ss SSS格式,排除掉所有没有expose的字段
     *
     * @return gson对象
     */
    public static Gson buildCommonGsonObject() {
        return buildCommonGsonObject(false, null, null, true);
    }

    /**
     * 生产gson对象
     *
     * @param isSerializeNulls            是否序列{@code null} 值字段
     * @param version                     字段的版本号注解
     * @param datePattern                 日期字段的格式化模式
     * @param excludesFieldsWithoutExpose 是否排除未标注{@literal @Expose} 注解的字段
     * @return gson对象
     */
    public static Gson buildCommonGsonObject(boolean isSerializeNulls, Double version, String datePattern,
                                             boolean excludesFieldsWithoutExpose) {
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls)
            builder.serializeNulls();
        if (version != null)
            builder.setVersion(version.doubleValue());
        if (isEmpty(datePattern))
            datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose)
            builder.excludeFieldsWithoutExposeAnnotation();
        String result = EMPTY;
        //改成默认是Long型
        builder.registerTypeAdapter(Double.class, new DefaultLongDoubleJsonSerializer());

        Gson gson = builder.create();

        return gson;
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串
     * <p/>
     * <strong>该方法转换发生错误时，不会抛出任何异常 若发生错误时，普通对象返回<code>"{}"</code>
     * 集合或数组对象返回 <code>"[]"</code></strong>
     *
     * @param target                      目标对象
     * @param targetType                  目标对象的类型
     * @param isSerializeNulls            是否序列{@code null} 值字段
     * @param version                     字段的版本号注解
     * @param datePattern                 日期字段的格式化模式
     * @param excludesFieldsWithoutExpose 是否排除未标注{@literal @Expose} 注解的字段
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Type targetType,
                                boolean isSerializeNulls, Double version, String datePattern,
                                boolean excludesFieldsWithoutExpose) {
        if (target == null)
            return EMPTY_JSON;
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls)
            builder.serializeNulls();
        if (version != null)
            builder.setVersion(version.doubleValue());
        if (isEmpty(datePattern))
            datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose)
            builder.excludeFieldsWithoutExposeAnnotation();
        String result = EMPTY;
        //改成默认是Long型
        builder.registerTypeAdapter(Double.class, new DefaultLongDoubleJsonSerializer());

        Gson gson = builder.create();
        try {
            if (targetType != null) {
                result = gson.toJson(target, targetType);
            } else {
                result = gson.toJson(target);
            }
        } catch (Exception ex) {

            if (target instanceof Collection || target instanceof Iterator
                    || target instanceof Enumeration
                    || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            } else
                result = EMPTY_JSON;
        }
        return result;
    }

    /**
     * 将给定的目标对象转换{@code JSON} 格式的字符串<strong>此方法只用来转换普通{@code JavaBean}
     * 对象</strong>
     * <ul>
     * <li>该方法只会转换标注{@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法会转换未标注或已标注{@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target 要转换成 {@code JSON} 的目标对象
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, true);
    }

    /**
     * 将给定的目标对象转换{@code JSON} 格式的字符串<strong>此方法只用来转换普通{@code JavaBean}
     * 对象</strong>
     * <ul>
     * <li>该方法只会转换标注{@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法会转换未标注或已标注{@literal @Since} 的字段；</li>
     * </ul>
     *
     * @param target      要转换成 {@code JSON} 的目标对象
     * @param datePattern 日期字段的格式化模式
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, String datePattern) {
        return toJson(target, null, false, null, datePattern, true);
    }

    /**
     * 将给定的目标对象转换{@code JSON} 格式的字符串<strong>此方法只用来转换普通{@code JavaBean}
     * 对象</strong>
     * <ul>
     * <li>该方法只会转换标注{@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target  要转换成 {@code JSON} 的目标对象
     * @param version 字段的版本号注解({@literal @Since})
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Double version) {
        return toJson(target, null, false, version, null, true);
    }

    /**
     * 将给定的目标对象转换{@code JSON} 格式的字符串
     * <strong>此方法只用来转换普通{@code JavaBean} 对象</strong>
     * <ul>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法会转换未标注或已标注{@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target                      要转换成 {@code JSON} 的目标对象
     * @param excludesFieldsWithoutExpose 是否排除未标注{@literal @Expose} 注解的字段
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null,
                excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换{@code JSON} 格式的字符串
     * <strong>此方法只用来转换普通{@code JavaBean} 对象</strong>
     * <ul>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target                      要转换成 {@code JSON} 的目标对象
     * @param version                     字段的版本号注解({@literal @Since})
     * @param excludesFieldsWithoutExpose 是否排除未标注{@literal @Expose} 注解的字段
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, version, null,
                excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换 {@code JSON} 格式的字符串 <strong>此方法常用来转换使用泛型的对象</strong>
     * <ul>
     * <li>该方法只会转换标注{@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法会转换未标注或已标注{@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认 日期/时间 格式化模式{@code yyyy-MM-dd HH:mm:ss SSSS}</li>
     * </ul>
     *
     * @param target     要转换成 {@code JSON} 的目标对象
     * @param targetType 目标对象的类型
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, true);
    }

    /**
     * 将给定的目标对象转换 {@code JSON} 格式的字符串 <strong>此方法常用来转换使用泛型的对象</strong>
     * <ul>
     * <li>该方法只会转换标注{@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法转换时使用默认 日期/时间 格式化模式{@code yyyy-MM-dd HH:mm:ss SSSS}</li>
     *
     * @param target     要转换成 {@code JSON} 的目标对象
     * @param targetType 目标对象的类型
     * @param version    字段的版本号注解({@literal @Since})
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, true);
    }

    /**
     * 将给定的目标对象转换 {@code JSON} 格式的字符串 <strong>此方法常用来转换使用泛型的对象</strong>
     * <ul>
     * <li>该方法会转换未标注或已标注{@literal @Since} 的字段；</li>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target                      要转换成 {@code JSON} 的目标对象
     * @param targetType                  目标对象的类型
     * @param excludesFieldsWithoutExpose 是否排除未标{@literal @Expose} 注解的字段
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null,
                excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换 {@code JSON} 格式的字符串
     * <strong>此方法常用来转换使用泛型的对象</strong>
     * <ul>
     * <li>该方法不会转换{@code null} 值字段；</li>
     * <li>该方法转换时使用默认日期/时间 格式化模式 {@code yyyy-MM-dd HH:mm:ss SSS}</li>
     * </ul>
     *
     * @param target                      要转换成 {@code JSON} 的目标对象
     * @param targetType                  目标对象的类型
     * @param version                     字段的版本号注解({@literal @Since})
     * @param excludesFieldsWithoutExpose 是否排除未标{@literal @Expose} 注解的字段
     * @return 返回目标对象 {@code JSON} 格式的字符串
     */
    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null,
                excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param token       {@code com.google.gson.reflect.TypeToken} 的类型指示类对象
     * @param datePattern 日期格式模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        return fromJson(json, token, datePattern, false);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param datePattern 日期格式模式
     * @param lenient     是否宽松模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, Type type, String datePattern, boolean lenient) {
        if (isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson;
        if (lenient) {
            gson = builder.setLenient().create();
        } else {
            gson = builder.create();
        }
        try {
            return gson.fromJson(json, type);
        } catch (Exception ex) {
            log.warning("Gson解析错误，错误：" + ex.getMessage() + "，原文：" + json);
            return null;
        }
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param token       {@code com.google.gson.reflect.TypeToken} 的类型指示类对象
     * @param datePattern 日期格式模式
     * @param lenient     是否宽松模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, TypeToken<T> token,
                                 String datePattern, boolean lenient) {
        if (isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.registerTypeAdapter(Date.class, new FailsafeDateDeserializer());

        builder.setDateFormat(datePattern);
        Gson gson;
        if (lenient) {
            gson = builder.setLenient().create();
        } else {
            gson = builder.create();
        }
        try {
            return gson.fromJson(json, token.getType());
        } catch (Exception ex) {
            log.warning("Gson解析错误，错误：" + ex.getMessage() + "，原文：" + json);
            return null;
        }
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象
     *
     * @param <T>   要转换的目标类型
     * @param json  给定{@code JSON} 字符串
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }

    public static <T> T fromJsonLenient(String json, TypeToken<T> token) {
        return fromJson(json, token, null, true);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong>
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        return fromJson(json, clazz, datePattern, false);
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong>
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @param lenient     是否宽松模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern, boolean lenient) {
        if (isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
//        builder = builder.registerTypeAdapter(String.class,new FailSafeStringJsonDeserializer());
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        //20191216添加，适配各种date
        builder.registerTypeAdapter(Date.class, new FailsafeDateDeserializer());

        //2020-11-09 接入短视频时修改, 修复空字符串引发的数字格式报错问题
//        builder.registerTypeAdapter(int.class, new EmptyStringToNumberTypeAdapter(int.class))
//                .registerTypeAdapter(Integer.class, new EmptyStringToNumberTypeAdapter(Integer.class))
//                .registerTypeAdapter(long.class, new EmptyStringToNumberTypeAdapter(long.class))
//                .registerTypeAdapter(Long.class, new EmptyStringToNumberTypeAdapter(Long.class))
//                .registerTypeAdapter(float.class, new EmptyStringToNumberTypeAdapter(float.class))
//                .registerTypeAdapter(Float.class, new EmptyStringToNumberTypeAdapter(Float.class))
//                .registerTypeAdapter(double.class, new EmptyStringToNumberTypeAdapter(double.class))
//                .registerTypeAdapter(Double.class, new EmptyStringToNumberTypeAdapter(Double.class));

        builder.setDateFormat(datePattern);
        Gson gson;

        if (lenient) {
            gson = builder.setLenient().create();
        } else {
            gson = builder.create();
        }
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception ex) {
            log.severe("Gson 解析错误！ERROR:" + ex.getMessage() + "，原文：" + json);
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong>
     *
     * @param <T>   要转换的目标类型
     * @param json  给定{@code JSON} 字符串
     * @param clazz 要转换的目标类
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong>
     *
     * @param <T>   要转换的目标类型
     * @param json  给定{@code JSON} 字符串
     * @param clazz 要转换的目标类
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJsonEx(String json, Class<T> clazz) throws JsonSyntaxException {
        return fromJsonEx(json, clazz, null);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong>
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJsonEx(String json, Class<T> clazz, String datePattern) throws JsonSyntaxException {
        return fromJsonEx(json, clazz, datePattern, false);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象<strong>此方法常用来转换普通的 {@code JavaBean}
     * 对象</strong> 抛出错误
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @param lenient     是否宽松模式
     * @return 给定{@code JSON} 字符串表示的指定的类型对象
     */
    public static <T> T fromJsonEx(String json, Class<T> clazz, String datePattern, boolean lenient) throws JsonSyntaxException {
        if (isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson;
        if (lenient) {
            gson = builder.setLenient().create();
        } else {
            gson = builder.create();
        }
        return gson.fromJson(json, clazz);
    }


    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型的Map<String,String>
     *
     * @param json 给定{@code JSON} 字符串
     * @return 指定的类型的Map
     */
    public static Map<String, String> fromJsonToMap(String json) {
        return fromJsonToMap(json, String.class);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型的Map
     *
     * @param json  给定{@code JSON} 字符串
     * @param clazz 要转换的目标类
     * @return 指定的类型的Map
     */
    public static <T> Map<String, T> fromJsonToMap(String json, Class<T> clazz) {
        return fromJsonToMap(json, clazz, null);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型的Map
     *
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @return 指定的类型的Map
     */
    public static <T> Map<String, T> fromJsonToMap(String json, Class<T> clazz, String datePattern) {
        return fromJsonToMap(json, clazz, datePattern, false);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型的Map
     *
     * @param <T>         要转换的目标类型
     * @param json        给定{@code JSON} 字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式模式
     * @param lenient     是否宽松模式
     * @return 指定的类型的Map
     */
    public static <T> Map<String, T> fromJsonToMap(String json, Class<T> clazz, String datePattern, boolean lenient) {
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        Map<String, T> stringTMap = fromJson(json, type, datePattern, lenient);
        stringTMap.entrySet().stream().filter(k -> !k.getValue().getClass().equals(clazz)).forEach(e -> {
            try {
                if (e.getValue().getClass() == String.class) {
                    //如果是字符串类型且需要转换为数值类型
                    if (clazz == Double.class) {
                        e.setValue(clazz.cast(Double.parseDouble((String) e.getValue())));
                    } else if (clazz == Float.class) {
                        e.setValue(clazz.cast(Float.parseFloat((String) e.getValue())));
                    } else if (clazz == Long.class) {
                        e.setValue(clazz.cast(Long.parseLong((String) e.getValue())));
                    } else if (clazz == Integer.class) {
                        e.setValue(clazz.cast(Integer.parseInt((String) e.getValue())));
                    } else if (clazz == Short.class) {
                        e.setValue(clazz.cast(Short.parseShort((String) e.getValue())));
                    } else if (clazz == Byte.class) {
                        e.setValue(clazz.cast(Byte.parseByte((String) e.getValue())));
                    } else if (clazz == Boolean.class) {
                        e.setValue(clazz.cast(Boolean.parseBoolean((String) e.getValue())));
                    }

                } else if (clazz == String.class) {
                    e.setValue((T) e.getValue().toString());
                } else {
                    e.setValue(clazz.cast(e.getValue()));
                }
            } catch (ClassCastException ex) {
                log.warning("gson转换map过程中出现ClassCastException:" + ex.getLocalizedMessage() + ",entity:" + e);
                e.setValue(null);
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                e.setValue(null);
//                e.setValue(clazz.cast(0));
                log.warning("gson转换map过程中出现NumberFormatException:" + ex.getLocalizedMessage() + ",entity:" + e);
                ex.printStackTrace();
            }
        });
        return stringTMap;
    }

    public static <T> T fromJsonLenient(String json, Class<T> clazz) {
        return fromJson(json, clazz, null, true);
    }

    public static boolean isEmpty(String inStr) {
        boolean reTag = false;
        if (inStr == null || "".equals(inStr)) {
            reTag = true;
        }
        return reTag;
    }

    /**
     * 获取Json字符串中第一项str
     *
     * @param str
     * @return
     */
    public static String getJsonArrayIndex0(String str) {

        JsonParser jsonParser = new JsonParser();
        JsonArray asJsonArray = jsonParser.parse(str).getAsJsonArray();
        if (asJsonArray.size() > 0) {
            return asJsonArray.get(0).getAsString();
        }
        return "";
    }

    public static void main(String[] args) {
        Map<String, Object> a = new HashMap<>();
        a.put("aaa", 123);
        a.put("b", 123.3);
        a.put("c", 124.0);
        a.put("d", "125");
        a.put("e", "126.0");

        String s = GsonUtils.toJson(a);
        Map<String, Object> stringStringMap = GsonUtils.fromJson(s, new TypeToken<Map<String, Object>>() {
        });
        System.out.println(s);
        System.out.println(stringStringMap);
        s = GsonUtils.toJson(stringStringMap);
        System.out.println(s);
        Map<String, String> stringStringMap1 = GsonUtils.fromJsonToMap(s);
        log.info(stringStringMap1.toString());
        Map<String, Object> stringObjectMap = GsonUtils.fromJsonToMap(s, Object.class);
        log.info(stringObjectMap.toString());
        Map<String, Double> stringDoubleMap = GsonUtils.fromJsonToMap(s, Double.class);
        log.info(stringDoubleMap.toString());
        Map<String, Integer> stringIntegerMap = GsonUtils.fromJsonToMap(s, Integer.class);
        log.info(stringIntegerMap.toString());

    }
}
