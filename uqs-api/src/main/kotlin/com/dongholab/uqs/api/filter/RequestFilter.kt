package com.dongholab.uqs.api.filter

import com.dongholab.uqs.domain.log.CustomServletRequestWrapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class RequestFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val customHttpServletRequestWrapper = CustomServletRequestWrapper(request as HttpServletRequest)
        chain?.doFilter(customHttpServletRequestWrapper, response)
    }
}
