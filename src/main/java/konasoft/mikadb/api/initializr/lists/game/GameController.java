package konasoft.mikadb.api.initializr.lists.game;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.decoder.game.RatingDecoder;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.lists.GameDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

//TODO: The utils bar is not yet implemented

@RestController
@RequestMapping(GameController.PATH)
public class GameController {
    public static final String PATH = "/lists/game";

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();

    private PageModel page;

    public GameController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    @GetMapping("")
    public ModelAndView game(HttpServletRequest request) throws SQLException {
        noneNullOrDefault(request, "filter", "");
        noneNullOrDefault(request, "statefilt", "all");
        noneNullOrDefault(request, "ratingfilt", "all");
        //
        ModelAndView mav = new ModelAndView(PATH);
        prepareModelAndView(mav, request);
        return mav;
    }


    /**
     * utils
     * */
    // preparation
    private void prepareModelAndView(ModelAndView mav, HttpServletRequest request) {
        // some vars
        String filter = (String) request.getAttribute("filter");
        String sortby = (String) request.getAttribute("sortby");
        String ratingfilt = (String) request.getAttribute("ratingfilt");

        // the website info
        ThemeAPreparer.prepare(mav, page);
        // the data
        mav.addObject("ratingDecoder", ratingDecoder);
        // options feedback
        mav.addObject("filter", filter);
        mav.addObject("sortby", sortby);
        mav.addObject("ratingfilt", ratingfilt);
        // filter query
        // String filterQuery = getFilterQuery(filter, statefilt, ratingfilt);
        // entries
        // getEntries(mav, sortby, filterQuery);
        getEntries(mav);
    }

    private void noneNullOrDefault(HttpServletRequest request, String param, String def) {
        if (request.getAttribute(param) == null) {
            request.setAttribute(param, def);
        }
    }

    /**
     * queries
     * */
    private void getEntries(ModelAndView mav) {
        try {
            mav.addObject("entriesList", new GameDAO().sortByCompleteDate(""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
