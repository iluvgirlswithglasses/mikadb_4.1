package konasoft.mikadb.statistics.homepage;

import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.model.comps.SeasonalDate;

import java.util.Calendar;

public class ByCompletedYearStatistics extends ByAiredYearStatistics {

    @Override
    public void iterationHandler(AnimeModel anime) {
        // 7 == "Plan to Watch", defined in StateDecoder
        // 3 == "Enqueued"
        if (anime.getState() == 8 || anime.getState() == 3) return;
        //
        int year = anime.getCompleteDate().getYear();
        // "watching" take part in current year
        if (year == SeasonalDate.WATCHING) year = Calendar.getInstance().get(Calendar.YEAR);
        //
        super.addTitle(year);
        super.addRating(year, anime.getRating());
    }
}
