package com.dongholab.api.controller

import com.dongholab.api.configuration.DongholabComponent
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class IndexController(
    private val env: Environment,
    private val dongholabComponent: DongholabComponent
) {
    @GetMapping
    fun index(model: Model): String {
        val profile = env?.getProperty("spring.profiles.active")
        val (author, bookmark) = dongholabComponent
        model.addAttribute("email", author)
        model.addAttribute("profile", profile)
        model.addAttribute("bookmark", bookmark)
        return "index"
    }
}
