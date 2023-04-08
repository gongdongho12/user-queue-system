package com.dongholab.uqs.domain.infrastructure.util

import io.mockk.junit5.MockKExtension
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class DateUtilTest {

    @Test
    fun toLocalDate() {
        var localDate: LocalDate = toLocalDate("20210506")
        assertThat(localDate.month.toString()).isEqualTo("MAY")
    }
}
