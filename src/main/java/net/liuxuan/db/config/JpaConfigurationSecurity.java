package net.liuxuan.db.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2020-09-29
 **/
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryOpenApi",
        transactionManagerRef = "transactionManagerOpenApi",
        basePackages = {"net.liuxuan.db.repository"}
//        basePackageClasses = {SourceInfoRepository.class}
) //设置Repository所在位置
@EntityScan(basePackages = {"net.liuxuan.db.entity"})
public class JpaConfigurationSecurity {
    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource dataSource;

    @Primary
    @Bean(name = "entityManagerOpenApi")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryOpenApi(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryOpenApi")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryOpenApi(EntityManagerFactoryBuilder builder) {
//        Config config = ConfigService.getAppConfig();
        LocalContainerEntityManagerFactoryBean entityManagerFactory
                = builder
                .dataSource(dataSource)
                .packages("net.liuxuan.db.entity")//设置实体类所在位置
                .persistenceUnit("securityPersistenceUnit")//持久化单元创建一个默认即可，多个便要分别命名
                .build();
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "validate");

        entityManagerFactory.setJpaProperties(props);
        return entityManagerFactory;
    }

    @Primary
    @Bean(name = "transactionManagerOpenApi")
    public PlatformTransactionManager transactionManagerOpenApi(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryOpenApi(builder).getObject());
    }
}
