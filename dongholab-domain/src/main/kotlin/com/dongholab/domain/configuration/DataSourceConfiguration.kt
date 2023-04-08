package com.dongholab.domain.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(private val env: Environment) {
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    fun masterHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    fun slaveHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun mysqlDataSource(
        @Qualifier("masterHikariConfig") masterHikariConfig: HikariConfig,
        @Qualifier("slaveHikariConfig") slaveHikariConfig: HikariConfig,
    ): DataSource {
        val masterDataSource = createDatasource(masterHikariConfig)
        val slaveDataSource = createDatasource(slaveHikariConfig)

        val targetDataSources: Map<Any, Any> = mapOf(
            Pair(DatasourceType.MASTER, masterDataSource),
            Pair(DatasourceType.SLAVE, slaveDataSource)
        )

        val routingDataSource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any =
                if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) DatasourceType.SLAVE else DatasourceType.MASTER
        }
        routingDataSource.setTargetDataSources(targetDataSources)
        routingDataSource.setDefaultTargetDataSource(masterDataSource)
        routingDataSource.afterPropertiesSet()
        return LazyConnectionDataSourceProxy(routingDataSource)
    }

    @Bean
    fun mysqlJdbcTemplate(@Qualifier("mysqlDataSource") mysqlDataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(mysqlDataSource)
    }

    private fun createDatasource(hikariConfig: HikariConfig): HikariDataSource {
        if (env.activeProfiles.contains("local") || env.activeProfiles.contains("test") || env.activeProfiles.isEmpty()) {
            return HikariDataSource(hikariConfig)
        }
        return HikariDataSource(hikariConfig)
    }
}

enum class DatasourceType {
    MASTER, SLAVE
}
