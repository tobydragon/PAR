package edu.ithaca.dragon.par.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Controller
public class WebController implements WebMvcConfigurer {

    @GetMapping("/")
    public String base(){
        return "LoginPage";
    }

    @GetMapping("/login")
    public String login(){
        return "LoginPage";
    }

    @GetMapping(value = "/imageTaskView")
    public String redirect(@RequestParam String userId, Model model) {
        model.addAttribute("User", userId);
        return "old/ImageTaskTemplate";
    }

    @GetMapping("/studentView")
    public String studentView(@RequestParam String userId, Model model){
        model.addAttribute("userId", userId);
        return "StudentView";
    }

    @GetMapping("/authorFromTemplateView")
    public String authorFromTemplateView(@RequestParam String userId, Model model){
        model.addAttribute("userId", userId);
        return "AuthorFromTemplateView";
    }

    @GetMapping("/authorReview")
    public String authorReview(@RequestParam String userId, Model model){
        model.addAttribute("userId", userId);
        return "AuthorReview";
    }


}
