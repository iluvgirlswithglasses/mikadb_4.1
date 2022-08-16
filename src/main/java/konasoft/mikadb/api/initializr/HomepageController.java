package konasoft.mikadb.api.initializr;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.statistics.homepage.Statistics;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import konasoft.mikadb.tester.Shell;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
public class HomepageController {
    public static final String PATH = "/homepage";

    private PageModel page;

    public HomepageController() throws SQLException {
        page = new PageDAO().getPage(PATH);
        // debug
        Shell.execute();
    }

    @RequestMapping("/")
    public ModelAndView index() {
        return homepage();
    }

    @RequestMapping(PATH)
    public ModelAndView homepage() {
        ModelAndView mav = new ModelAndView(PATH);
        ThemeAPreparer.prepare(mav, page);
        // homepage is actually a statistics page
        new Statistics().load(mav);
        //
        return mav;
    }
}
