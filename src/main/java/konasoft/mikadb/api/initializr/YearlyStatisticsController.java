package konasoft.mikadb.api.initializr;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.statistics.yearly.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RestController
@RequestMapping(YearlyStatisticsController.PATH)
public class YearlyStatisticsController {
    public static final String PATH = "/lists/stats/cmpl-yr";

    private PageModel page;

    public YearlyStatisticsController() throws SQLException {
        page = new PageModel(PATH, "Yearly Watching Statistics", "Your stats by complete year", "");
    }

    @GetMapping("")
    public ModelAndView index() throws SQLException {
        ModelAndView mav = new ModelAndView(PATH);
        ThemeAPreparer.prepare(mav, page);
        //
        mav.addObject("ratingDecoder", RatingDecoder.getInstance());
        mav.addObject("dateDecoder", DateDecoder.getInstance());
        //
        Statistics stats = new Statistics();
        stats.load();
        mav.addObject("stats", stats.getStats());
        mav.addObject("overallStats", stats.getOverallStats());
        return mav;
    }
}
