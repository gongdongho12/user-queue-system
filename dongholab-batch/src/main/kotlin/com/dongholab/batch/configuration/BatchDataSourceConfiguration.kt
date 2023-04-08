package com.dongholab.batch.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.support.JdbcTransactionManager
import javax.sql.DataSource

@Configuration
class BatchDataSourceConfiguration(private val env: Environment) {

    @Bean
    @ConfigurationProperties("spring.datasource.batch")
    fun batchHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun batchTransactionManager(dataSource: DataSource): JdbcTransactionManager {
        return JdbcTransactionManager(dataSource)
    }

    @Bean
    fun batchDataSource(
        @Qualifier("batchHikariConfig") batchHikariConfig: HikariConfig
    ): DataSource {
        return createDatasource(batchHikariConfig)
    }

    private fun createDatasource(hikariConfig: HikariConfig): HikariDataSource {
        if (env.activeProfiles.contains("local") || env.activeProfiles.contains("test") || env.activeProfiles.isEmpty()) {
            return HikariDataSource(hikariConfig)
        }
        return HikariDataSource(hikariConfig)
    }
}
