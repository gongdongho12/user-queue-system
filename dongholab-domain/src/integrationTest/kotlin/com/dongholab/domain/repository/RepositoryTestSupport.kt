package com.dongholab.domain.repository

import com.dongholab.domain.DomainConfigurationLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(classes = [DomainConfigurationLoader::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@Transactional
class RepositoryTestSupport
