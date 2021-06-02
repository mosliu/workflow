package net.liuxuan.workflow.config.activiti;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 自定义id策略
 * @date 2021-05-20
 **/
@Component
public class ActivitiIdGeneratorConfiguration implements IdGenerator {

    @Override
    public String getNextId() {
        return UUID.randomUUID().toString();
//        return String.valueOf(IdWorker.getId());
    }
}