package net.liuxuan.db.page;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-23
 **/
@Data
@Slf4j
public class Page<T> {
    Pageable pageable;
    T list;
}
