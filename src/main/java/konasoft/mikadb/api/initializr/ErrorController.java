package konasoft.mikadb.api.initializr;

import konasoft.mikadb.MikadbApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("version", MikadbApplication.VERSION);
        //
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null) {
            mav.addObject("status", "None !");
            mav.addObject("note", "You brought yourself here !");
            return mav;
        }
        mav.addObject("status", status.toString());
        return mav;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
