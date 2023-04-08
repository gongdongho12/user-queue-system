package com.dongholab.domain.configuration

import jakarta.persistence.EntityManagerFactory
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.dongholab.domain"],
    transactionManagerRef = "dongholabTransactionManager",
    entityManagerFactoryRef = "dongholabEntityManagerFactory",
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASPECTJ,
            pattern = ["com.dongholab.domain..mysql.*Repository"]
        )
    ]
)
class JpaConfiguration(private val jpaProperties: JpaProperties) {

    @Primary
    @Bean
    fun dongholabEntityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val builder = EntityManagerFactoryBuilder(createJpaVendorAdapter(), jpaProperties.properties, null)
        return builder.dataSource(dataSource)
            .persistenceUnit("dongholabEntityManager")
            .packages("com.dongholab.domain")
            .build()
    }

    @Primary
    @Bean
    fun dongholabTransactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    private fun createJpaVendorAdapter(): JpaVendorAdapter {
        val adapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(jpaProperties.isShowSql)
        adapter.setDatabase(jpaProperties.database)
        adapter.setDatabasePlatform(jpaProperties.databasePlatform)
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl)
        return adapter
    }
}
