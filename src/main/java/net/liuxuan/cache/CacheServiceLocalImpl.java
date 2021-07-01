package net.liuxuan.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 无redis时本地化缓存策略
 * @date 2021-06-15
 **/
@Slf4j
@Service
@ConditionalOnProperty(name = "moses.cache.type", havingValue = "local", matchIfMissing = true)
public class CacheServiceLocalImpl implements CacheService {

//    @Bean
//    public Cache<String, Object> caffeineCache() {
//        return Caffeine.newBuilder()
//                // 设置最后一次写入或访问后经过固定时间过期
//                .expireAfterWrite(60, TimeUnit.SECONDS)
//                // 初始的缓存空间大小
//                .initialCapacity(100)
//                // 缓存的最大条数
//                .maximumSize(1000)
//                .build();
//    }

    LoadingCache<String, TimeObject> caches = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, TimeObject>() {
                @Override
                public long expireAfterCreate(String key, TimeObject timeObject, long currentTime) {
                    return TimeUnit.NANOSECONDS.convert(timeObject.getTime(), timeObject.getUnit());
                }

                @Override
                public long expireAfterUpdate(String key, TimeObject graph,
                                              long currentTime, long currentDuration) {
                    return currentDuration;
                }

                @Override
                public long expireAfterRead(String key, TimeObject graph,
                                            long currentTime, long currentDuration) {
                    return currentDuration;
                }
            })
            .build(key -> createTimeObj(key));

    private TimeObject createTimeObj(String key) {
        return new TimeObject(1, TimeUnit.SECONDS, "");
    }

    @Override
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        caches.put(key, new TimeObject(timeout, unit, value));
    }

    @Override
    public Object getValue(String key) {
        TimeObject timeObject = caches.getIfPresent(key);
        if (timeObject == null || timeObject.getObj().equals("")) return null;
        return timeObject.getObj();
    }

    @Override
    public void delete(String key) {
        caches.invalidate(key);
    }


    @Data
    @AllArgsConstructor
    public class TimeObject {
        long time;
        TimeUnit unit;
        Object obj;
    }
}
