package konasoft.mikadb.api.initializr.gallery;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(GalleryController.PATH)
public class GalleryController {
    public static final String PATH = "/gallery";

    private PageDAO pageDAO;

    public GalleryController() throws SQLException {
        pageDAO = new PageDAO();
    }

    @GetMapping("")
    public ModelAndView gallery() throws SQLException {
        ModelAndView mav = new ModelAndView(PATH);
        ThemeAPreparer.prepare(mav, pageDAO.getPage(PATH));
        //
        mav.addObject("childPages", pageDAO.getChildPages(PATH));
        return mav;
    }

    @GetMapping("/{p}")
    private ModelAndView staticPage(@PathVariable String p) {
        ModelAndView mav;
        p = "/gallery/" + p; 
        try {
            mav = new ModelAndView(p);
            ThemeAPreparer.prepare(mav, pageDAO.getPage(p));
        } catch (SQLException e) {
            e.printStackTrace();
            mav = new ModelAndView("error");
        }
        return mav;
    }
}
