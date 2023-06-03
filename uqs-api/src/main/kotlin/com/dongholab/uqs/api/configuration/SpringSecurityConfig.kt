package com.dongholab.uqs.api.configuration

import com.dongholab.uqs.api.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler


@Configuration
@EnableMethodSecurity
class SpringSecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val logoutHandler: LogoutHandler,
    private val memberLogoutSuccessHandler: LogoutSuccessHandler
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.run {
            csrf().disable().cors().disable()
                .authorizeHttpRequests { request ->
                    request
                        .requestMatchers("/hello", "/eks-hello", "/images/**", "/auth/*").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
                        .logout()
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(memberLogoutSuccessHandler)
                }
        }.build()
    }
}
