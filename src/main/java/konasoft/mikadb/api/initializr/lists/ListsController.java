package konasoft.mikadb.api.initializr.lists;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(ListsController.PATH)
public class ListsController {
    public static final String PATH = "/lists";

    private PageDAO pageDAO;

    public ListsController() {
        pageDAO = new PageDAO();
    }

    @GetMapping("")
    public ModelAndView lists() throws SQLException {
        ModelAndView mav = new ModelAndView(PATH);
        mav.addObject("version", MikadbApplication.VERSION);
        mav.addObject("page", pageDAO.getPage(PATH));
        mav.addObject("childPages", pageDAO.getChildPages(PATH));
        return mav;
    }
}
