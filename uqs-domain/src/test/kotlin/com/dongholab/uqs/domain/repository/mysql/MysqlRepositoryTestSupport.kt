package com.dongholab.uqs.domain.repository.mysql

import com.dongholab.uqs.domain.configuration.AuditConfiguration
import com.dongholab.uqs.domain.configuration.DataSourceConfiguration
import com.dongholab.uqs.domain.configuration.JpaConfiguration
import com.dongholab.uqs.domain.infrastructure.util.BeanUtil
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
@ComponentScan(basePackages = ["com.dongholab.uqs.domain.repository.mysql"])
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
