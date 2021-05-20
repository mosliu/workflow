package net.liuxuan.workflow;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.workflow.utils.SpringContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-19
 **/
//@SpringBootApplication(exclude = {ActivitiSpringIdentityAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = "net.liuxuan")
//@SpringBootApplication
@EnableScheduling
@Slf4j
public class MainApplication {
    public static void main(String[] args) {
        log.warn("main start");
        SpringApplication app = new SpringApplication(MainApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
        log.info(SpringContext.appname);
        log.warn("main end");
    }
}
