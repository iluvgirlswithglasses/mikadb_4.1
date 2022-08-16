package konasoft.mikadb.statistics.homepage;

import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.model.homepage.LineChartCounterModel;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;
import konasoft.mikadb.sqlite.dao.lists.FranchiseDAO;
import org.springframework.web.servlet.ModelAndView;

public class Statistics {
    public void load(ModelAndView mav) {
        /* @prepare stats objects */
        OverallStatistics overallStat = new OverallStatistics();
        ScoreDistributionStatistics distStat = new ScoreDistributionStatistics();
        ByCompletedYearStatistics cmplStat = new ByCompletedYearStatistics();
        ByAiredYearStatistics airedStat = new ByAiredYearStatistics();

        /* @prepare dao */
        AnimeDAO dao = new AnimeDAO();

        /* @get data */
        try {
            // franchise firsts
            overallStat.setFranchisesCount(new FranchiseDAO().rowsCount());
            // then the animes
            for (AnimeModel anime: dao.all()) {
                // @overall
                overallStat.iterationHandler(anime);
                // @distributions
                distStat.iterationHandler(anime);
                // @complete year
                cmplStat.iterationHandler(anime);
                // @aired
                airedStat.iterationHandler(anime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* @export */
        exportToMAV(mav, overallStat, distStat, cmplStat, airedStat);
    }

    public void exportToMAV
    (
            ModelAndView mav,
            OverallStatistics overall,
            ScoreDistributionStatistics dist,
            ByCompletedYearStatistics cmpl,
            ByAiredYearStatistics aired
    ) {
        // tools
        mav.addObject("lineChartCounter", new LineChartCounterModel());

        // @overall
        mav.addObject("overallStat", overall);

        // @score distribution
        mav.addObject("ratingsShort", RatingDecoder.getInstance().RATING_SHORT);
        mav.addObject("scoreDistStat", dist);

        // @completed
        mav.addObject("dateDecoder", DateDecoder.getInstance());
        mav.addObject("cmplDateStat", cmpl);

        // @aired
        mav.addObject("airedStat", aired);
    }
}
