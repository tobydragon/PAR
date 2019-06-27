package edu.ithaca.dragon.par.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String PATH = "/error";

    public ErrorController() {
        super();
    }

    @RequestMapping(PATH)
    public String redirect404() {
        return "Error";
    }

    @RequestMapping("")
    public String redirectLanding() {
        return "Login";
    }

    public String getErrorPath() {
        return PATH;
    }
}
