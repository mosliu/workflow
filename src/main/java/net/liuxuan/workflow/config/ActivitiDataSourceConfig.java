package net.liuxuan.workflow.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-19
 **/
@Configuration
public class ActivitiDataSourceConfig {

    /**
     * activiti数据源
     *
     * @return
     */
    @Bean()
    @ConfigurationProperties(prefix = "spring.datasource.activiti")
    public DataSource activitiDataSource() {
//        return DataSourceBuilder.create().build();
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 工作流引擎相关配置
     *
     * @param dataSource
     * @return
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(@Qualifier("activitiDataSource") DataSource dataSource) {
        StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setDataSource(dataSource);
        //自动更新表结构，自动建表
        standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
        //保存历史数据级别设置为full最高级别
        standaloneProcessEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);
        //检查历史表是否存在
        standaloneProcessEngineConfiguration.setDbHistoryUsed(true);
        return standaloneProcessEngineConfiguration;
    }

    /**
     * 工作流流程引擎
     *
     * @param processEngineConfiguration
     * @return
     */

    @Bean
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }

    /**
     * RuntimeService
     *
     * @param processEngine
     * @return
     */

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    /**
     * TaskService
     *
     * @param processEngine
     * @return
     */

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    /**
     * RepositoryService
     *
     * @param processEngine
     * @return
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    /**
     * HistoryService
     *
     * @param processEngine
     * @return
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    /**
     * ManagementService
     *
     * @param processEngine
     * @return
     */
    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    /**
     * DynamicBpmnService
     *
     * @param processEngine
     * @return
     */
    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }
}
