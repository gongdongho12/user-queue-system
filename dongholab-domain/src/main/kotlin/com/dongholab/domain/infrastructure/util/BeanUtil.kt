package com.dongholab.domain.infrastructure.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class BeanUtil {
    companion object {
        lateinit var applicationContext: ApplicationContext
    }

    @Autowired
    fun init(applicationContext: ApplicationContext) {
        BeanUtil.applicationContext = applicationContext
    }
}
