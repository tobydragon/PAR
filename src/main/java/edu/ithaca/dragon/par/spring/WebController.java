package edu.ithaca.dragon.par.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Controller
public class WebController implements WebMvcConfigurer {

    @RequestMapping("/login")
    public String login(){
        return "LoginPage";
    }
    @GetMapping(value = "/imageTaskView")
    public String redirect(@RequestParam String userId, Model model) {
        model.addAttribute("User", userId);
        return "ImageTaskTemplate";
    }

//    @RequestMapping("/start")
//    public String finalPage() {
//        return "ImageTaskTemplate";
//    }
//    @RequestMapping("/login")
//    public String loginPage() {return "LoginPage";}

//    @RequestMapping("/error")
//    public String errorPage() {
//        return "ErrorPage";
//    }
    @RequestMapping("/badUrl")
    public String errorPageURl() {
        return "ErrorPageBadUrl";
    }

    @RequestMapping("/serverError")
    public String errorPageServer() {
        return "ErrorPageServer";
    }

}
