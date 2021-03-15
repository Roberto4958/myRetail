package myRetail.productpricing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping
@ApiIgnore
public class Swagger {
    @GetMapping
    public String swagger() { return "redirect:/swagger-ui.html"; }
}
