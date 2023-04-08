package com.dongholab.api.configuration

import com.dongholab.domain.log.CustomServletRequestWrapper
import org.springframework.stereotype.Component
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

@Component
class RequestFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val customHttpServletRequestWrapper = CustomServletRequestWrapper(request as HttpServletRequest)
        chain?.doFilter(customHttpServletRequestWrapper, response)
    }
}
