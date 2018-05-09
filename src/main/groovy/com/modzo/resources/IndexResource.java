package com.modzo.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
class IndexResource {

    @GetMapping("/")
    ModelAndView index() {
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
