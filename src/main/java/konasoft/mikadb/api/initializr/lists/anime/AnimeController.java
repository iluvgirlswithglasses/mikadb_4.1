package konasoft.mikadb.api.initializr.lists.anime;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping(AnimeController.PATH)
public class AnimeController {
    public static final String PATH = "/lists/anime";

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static StateDecoder stateDecoder = StateDecoder.getInstance();

    private PageModel page;

    public AnimeController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }


    /**
     * views
     * */
    @GetMapping("")
    public ModelAndView anime(HttpServletRequest request) {
        // init utilsbar's request attrs if null
        noneNullOrDefault(request, "filter", "");
        noneNullOrDefault(request, "sortby", "date");
        noneNullOrDefault(request, "statefilt", "all");
        noneNullOrDefault(request, "ratingfilt", "all");
        // model and view
        ModelAndView mav = new ModelAndView(PATH);
        prepareModelAndView(mav, request);
        // deploy
        return mav;
    }

    /**
     * simple query
     * */
    @GetMapping(":{s}")
    public ModelAndView searchAnime(HttpServletRequest request, @PathVariable String s) {
        request.setAttribute("filter", s);
        request.setAttribute("sortby", "title");
        return anime(request);
    }

    @GetMapping("/{t}={s}")
    public ModelAndView searchAnime(HttpServletRequest request, @PathVariable String t, @PathVariable String s) {
        if (t.equals("franchise"))
            return searchFranchise(request, s);
        request.setAttribute(t, s);
        return anime(request);
    }

    public ModelAndView searchFranchise(HttpServletRequest request, @PathVariable String f) {
        // set utilsbar's request attrs if null
        request.setAttribute("filter", "");
        request.setAttribute("sortby", "aired");
        request.setAttribute("statefilt", "all");
        request.setAttribute("ratingfilt", "all");
        //
        ModelAndView mav = new ModelAndView(PATH);
        ThemeAPreparer.prepare(mav, page);
        mav.addObject("ratingDecoder", ratingDecoder);
        mav.addObject("stateDecoder", stateDecoder);
        //
        try {
            mav.addObject("entriesList", new AnimeDAO().loadFromFranchise(f));
            return mav;
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }
    }

    /**
     * system generated queries
     * */
    @GetMapping("/query/:{filter}/{sortby}/{statefilt}/{ratingfilt}")
    public ModelAndView anime(
            HttpServletRequest request,
            @PathVariable String filter,
            @PathVariable String sortby,
            @PathVariable String statefilt,
            @PathVariable String ratingfilt
    ) {
        // system
        request.setAttribute("filter", filter);
        request.setAttribute("sortby", sortby);
        request.setAttribute("statefilt", statefilt);
        request.setAttribute("ratingfilt", ratingfilt);
        //
        return anime(request);
    }


    /**
     * utils
     * */
    // preparation
    private void prepareModelAndView(ModelAndView mav, HttpServletRequest request) {
        // some vars
        String filter = (String) request.getAttribute("filter");
        String sortby = (String) request.getAttribute("sortby");
        String statefilt = (String) request.getAttribute("statefilt");
        String ratingfilt = (String) request.getAttribute("ratingfilt");

        // the website info
        ThemeAPreparer.prepare(mav, page);
        // the data
        mav.addObject("ratingDecoder", ratingDecoder);
        mav.addObject("stateDecoder", stateDecoder);
        // options feedback
        mav.addObject("filter", filter);
        mav.addObject("sortby", sortby);
        mav.addObject("statefilt", statefilt);
        mav.addObject("ratingfilt", ratingfilt);
        // filter query
        String filterQuery = getFilterQuery(filter, statefilt, ratingfilt);
        // entries
        getEntries(mav, sortby, filterQuery);
    }

    // query entries with sortby parameter and filter query
    private void getEntries(ModelAndView mav, String sortby, String filterQuery) {
        try {
            // switching sort mode
            switch (sortby) {
                case "date":
                    mav.addObject("entriesList", new AnimeDAO().sortByCompleteDate(filterQuery));
                    break;
                case "title":
                    mav.addObject("entriesList", new AnimeDAO().sortByTitle(filterQuery));
                    break;
                case "rating":
                    mav.addObject("entriesList", new AnimeDAO().sortByRating(filterQuery));
                    break;
                case "aired":
                    mav.addObject("entriesList", new AnimeDAO().sortByAired(filterQuery));
                    break;
                case "franchise":
                    mav.addObject("entriesList", new AnimeDAO().sortByFranchise(filterQuery));
                    break;
                case "state":
                default:
                    mav.addObject("entriesList", new AnimeDAO().sortByState(filterQuery));
                    break;
            }
        } catch (SQLException e) {
            // the only line in method that throws exception
            try {
                mav.addObject("entriesList", new AnimeDAO().sortByState(""));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //
    private void noneNullOrDefault(HttpServletRequest request, String param, String def) {
        if (request.getAttribute(param) == null) {
            request.setAttribute(param, def);
        }
    }


    /**
     * urls query
     * */
    //
    private String getFilterQuery(String filter, String statefilt, String ratingfilt) {
        //
        if (statefilt.equals("all")) statefilt = "";
        if (ratingfilt.equals("all")) ratingfilt = "";
        //
        StringBuilder b = new StringBuilder();
        // title filter
        if (!filter.isBlank()) {
            filter = filter.replace("'", "''");
            b.append(
                String.format(
                        "(title LIKE '%%%s%%' or title_english LIKE '%%%s%%' or franchise LIKE '%%%s%%') and ",
                        filter, filter, filter
                )
            );
        }
        // state filter
        if (!statefilt.isBlank()) {
            b.append("(")
             .append(stateQuery(statefilt))
             .append(") and ");
        }
        // rating filter
        if (!ratingfilt.isBlank()) {
            b.append("rating=").append(ratingfilt).append(" and ");
        }
        // return
        if (b.length() > 0) {
            return "where " + b.toString().substring(0, b.length() - " and ".length());
        }
        return "";
    }

    private String stateQuery(String statefilt) {
        StringBuilder b = new StringBuilder();
        for (int i: stateDecoder.STATE_FILT.get(statefilt)) {
            b.append("state=").append(i).append(" or ");
        }
        return b.toString().substring(0, b.length() - " or ".length());
    }
}
