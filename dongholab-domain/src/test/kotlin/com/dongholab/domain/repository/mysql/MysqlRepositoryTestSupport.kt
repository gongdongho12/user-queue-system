package com.dongholab.domain.repository.mysql

import com.dongholab.domain.configuration.AuditConfiguration
import com.dongholab.domain.configuration.DataSourceConfiguration
import com.dongholab.domain.configuration.JpaConfiguration
import com.dongholab.domain.infrastructure.util.BeanUtil
import com.zaxxer.hikari.HikariConfig
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    DataSourceConfiguration::class,
    JpaConfiguration::class,
    AuditConfiguration::class,
    TestHikariConfig::class,
    BeanUtil::class
)
@ComponentScan(basePackages = ["com.dongholab.domain.repository.mysql"])
@EnableAspectJAutoProxy(proxyTargetClass = true)
class MysqlRepositoryTestSupport

@Configuration
class TestHikariConfig : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        if (bean is HikariConfig) {
            bean.maximumPoolSize = 1
            return bean
        }
        return bean
    }
}
