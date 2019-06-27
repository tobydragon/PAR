package edu.ithaca.dragon.par.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String PATH = "/error";

    public ErrorController() {
        super();
    }

    @RequestMapping("")
    public String redirectLanding() {
        return "Login";
    }

    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(PATH)
    public String renderErrorPage(HttpServletRequest httpRequest, Model model, String message) {

        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request \n";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized \n";
                break;
            }
            case 404: {
                return "Error";
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error ";
                break;
            }
        }
        model.addAttribute("errorNum", errorMsg);
        model.addAttribute("errorMsg", message);
        return "ServerError";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    private String getErrorMessage(HttpServletRequest httpRequest) {
        return (String) httpRequest.getAttribute("javax.servlet.error.message");
    }
}
