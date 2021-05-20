package net.liuxuan.workflow;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.db.DbSchemaCreate;

import java.io.File;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-19
 **/
@Slf4j
public class CreateDbWithConfigure {
    public static void main(String[] args) {
        log.info("{}",new File(".").getAbsolutePath());
        DbSchemaCreate.main(args);
    }
}
