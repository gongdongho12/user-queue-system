package com.dongholab.api.hello

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery

@DgsComponent
class HelloResolver {
    @DgsQuery
    fun helloWorld(): String {
        return "Hello Dongholab!"
    }
}
