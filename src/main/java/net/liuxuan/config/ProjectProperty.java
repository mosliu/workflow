package net.liuxuan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 项目的配置
 * @date 2021-06-15
 **/
@Data
@ConfigurationProperties(prefix = "moses")
@Component
public class ProjectProperty {
    Map<String, String> cache;
}
