package net.liuxuan.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description cache层
 * @date 2021-06-15
 **/

public interface CacheService {

    void setValue(String key, Object value, long timeout, TimeUnit unit);

    Object getValue(String key);

    void delete(String key);
}
