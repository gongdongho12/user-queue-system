package com.dongholab.uqs.api.security.authorize

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention
@PreAuthorize("hasAnyRole('USER')")
annotation class UserAuthorize
