package konasoft.mikadb.api.initializr.lists.game;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.game.RatingDecoder;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.lists.GameDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(GameController.PATH + "/view")
public class GameViewController {
    public static final String PATH = GameController.PATH;

    private PageModel page;

    public GameViewController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable String id) {
        ModelAndView mav = new ModelAndView(PATH + "/view");
        try {
            mav.addObject("entry", new GameDAO().get(Integer.parseInt(id)));
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }

        // prepare
        mav.addObject("version", MikadbApplication.VERSION);
        mav.addObject("page", page);

        // for thymeleaf
        mav.addObject("dateDecoder", DateDecoder.getInstance());
        mav.addObject("ratingDecoder", RatingDecoder.getInstance());

        return mav;
    }
}
