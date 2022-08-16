package konasoft.mikadb.statistics.homepage;

import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.model.lists.AnimeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * note: ALWAYS call getTopPercentage() before getPercentage()
 * or getPercentage() won't work
 * */

@SuppressWarnings("UnusedReturnValue")
public class ScoreDistributionStatistics {
    private List<Integer> dists = new ArrayList<>();
    private int len = RatingDecoder.getInstance().RATING_FULL.length;

    // for the percentage
    private List<Integer> cumulativeDist = new ArrayList<>();
    private int ratedCount = 0; // N/A, C/R & Inherit are not counted as rated
    private int titleCount = 0;

    public ScoreDistributionStatistics() {
        for (int i = 0; i < len; i++) {
            dists.add(0);
            cumulativeDist.add(0);
        }
    }

    // @
    public void iterationHandler(AnimeModel anime) {
        // 8 == "Plan to Watch", defined in StateDecoder
        // 3 == "Enqueued"
        if (anime.getState() == 8 || anime.getState() == 3) return;
        //
        add(anime.getRating());
        titleCount++;
    }

    // @getters
    public List<Integer> getDists() {
        return dists;
    }

    public int getPeak() {
        int peak = 0;
        for (int i: dists) {
            if (i > peak) peak = i;
        }
        return peak;
    }

    private List<String> topPercentage;
    public List<String> getTopPercentage() {
        if (topPercentage == null) {
            // init
            cumulativeDist.set(len - 1, dists.get(len - 1));
            ratedCount += dists.get(len - 1);
            //
            for (int i = 1; i < 10; i++) {
                int index = len - i - 1;
                cumulativeDist.set(
                        index, cumulativeDist.get(index + 1) + dists.get(index)
                );
                ratedCount += dists.get(index);
            }
            // handler
            topPercentage = new ArrayList<>();
            for (int i: cumulativeDist) {
                topPercentage.add(String.format("%.2f", (double) i * 100 / ratedCount));
            }
        }
        return topPercentage;
    }

    private List<String> percentage;
    public List<String> getPercentage() {
        //
        if (topPercentage == null) getTopPercentage();
        //
        if (percentage == null) {
            // handler
            percentage = new ArrayList<>();
            for (int i: dists) {
                percentage.add(String.format("%.2f", (double) i * 100 / titleCount));
            }
        }
        return percentage;
    }

    // @setters
    public void add(int i) {
        // add in reverse order
        int index = len - i - 1;
        dists.set(index, dists.get(index) + 1);
    }
}
