package konasoft.mikadb.api.initializr.lists.anime;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.decoder.anime.TypeDecoder;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(AnimeController.PATH + "/view")
public class AnimeViewController {
    public static final String PATH = AnimeController.PATH;

    private PageModel page;

    public AnimeViewController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable String id) {
        ModelAndView mav = new ModelAndView(PATH + "/view");
        try {
            mav.addObject("entry", new AnimeDAO().get(Integer.parseInt(id)));
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }

        // prepare
        mav.addObject("version", MikadbApplication.VERSION);
        mav.addObject("page", page);

        // for thymeleaf
        mav.addObject("dateDecoder", DateDecoder.getInstance());
        mav.addObject("typeDecoder", TypeDecoder.getInstance());
        mav.addObject("stateDecoder", StateDecoder.getInstance());
        mav.addObject("ratingDecoder", RatingDecoder.getInstance());

        return mav;
    }
}
