package konasoft.mikadb.api.initializr.tools;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.model.sys.PageModel;
import org.springframework.web.servlet.ModelAndView;

public class ThemeAPreparer {
    public static void prepare(ModelAndView mav, PageModel page) {
        mav.addObject("version", MikadbApplication.VERSION);
        mav.addObject("page", page);
    }
}
