package edu.ithaca.dragon.par.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Controller
public class WebController implements WebMvcConfigurer {

//    @RequestMapping("/chooseMode")
//    public String chooseMode() {
//        return "ModeSelectPage";
//    }
//    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
//    public String redirect() {
//        return "redirect:finalPage";
//    }
//
//    @RequestMapping(value = "/finalPage", method = RequestMethod.GET)
////    public String finalPage() {
////        return "ImageTaskTemplate";
////    }

    @RequestMapping("/start")
    public String finalPage() {
        return "ImageTaskTemplate";
    }

//    @RequestMapping("/error")
//    public String errorPage() {
//        return "ErrorPage";
//    }

}
