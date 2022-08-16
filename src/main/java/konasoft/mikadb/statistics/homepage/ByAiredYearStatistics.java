package konasoft.mikadb.statistics.homepage;

import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.model.lists.AnimeModel;

import java.util.*;

public class ByAiredYearStatistics {
    private static final int HIGH_RATED = 8;
    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();

    // fields
    private HashMap<Integer, Integer> titlesWatched = new HashMap<>();
    private HashMap<Integer, Integer> ratingCount = new HashMap<>();
    private HashMap<Integer, Integer> ratingSum = new HashMap<>();
    private HashMap<Integer, Integer> highRatedCount = new HashMap<>();

    // peak values
    private int titlesPeak = 0;
    private double meanPeak = 0;
    private double highRatedPeak = 0;

    private int compare(int a, int b) {
        return a - b;
    }

    /**
     * modifications
     * */
    public void iterationHandler(AnimeModel anime) {
        // 7 == "Plan to Watch", defined in StateDecoder
        // 3 == "Enqueued"
        if (anime.getState() == 8 || anime.getState() == 3) return;
        //
        int year = anime.getAired().get(Calendar.YEAR);
        //
        addTitle(year);
        addRating(year, anime.getRating());
    }

    public void addTitle(int year) {
        if (titlesWatched.containsKey(year)) {
            titlesWatched.put(year, titlesWatched.get(year) + 1);
        } else {
            titlesWatched.put(year, 1);
        }
    }

    public void addRating(int year, int rating) {
        int ratingVal = ratingDecoder.getRatingValue(rating);
        // initialize if year is absent
        if (!ratingCount.containsKey(year)) {
            ratingCount.put(year, 0);
            ratingSum.put(year, 0);
            highRatedCount.put(year, 0);
        }
        //
        if (ratingVal > 0) {
            ratingCount.put(year, ratingCount.get(year) + 1);
            ratingSum.put(year, ratingSum.get(year) + ratingVal);
            //
            if (ratingVal >= HIGH_RATED) highRatedCount.put(year, highRatedCount.get(year) + 1);
        }
    }

    /**
     * getters
     * */
    private LinkedHashMap<Integer, Integer> sortedTitlesWatched;
    public LinkedHashMap<Integer, Integer> getTitlesWatched() {
        if (sortedTitlesWatched == null) {
            sortedTitlesWatched = new LinkedHashMap<>();
            titlesWatched.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                sortedTitlesWatched.put(entry.getKey(), entry.getValue());
                if (entry.getValue() > titlesPeak) titlesPeak = entry.getValue();
            });
        }
        return sortedTitlesWatched;
    }

    // each time getMeanScore() is called:
    // time complexity: O(nlog(n)), with n == ratingCount.size();
    private Map<Integer, String> meanScore;
    public Map<Integer, String> getMeanScore() {
        if (meanScore == null) {
            double[] peak = new double[]{0};
            meanScore = mapDevideMap(ratingSum, ratingCount, peak, 1);
            //
            meanPeak = peak[0];
        }
        return meanScore;
    }

    private Map<Integer, String> highRatedPercentage;
    public Map<Integer, String> getHighRatedPercentage() {
        if (highRatedPercentage == null) {
            double[] peak = new double[]{0};
            highRatedPercentage = mapDevideMap(highRatedCount, titlesWatched, peak, 100);
            //
            highRatedPeak = peak[0];
        }
        return highRatedPercentage;
    }

    // if k == 100 -> result contains percentages
    public Map<Integer, String> mapDevideMap(Map<Integer, Integer> a, Map<Integer, Integer> b, double[] peak, int k) {
        Map<Integer, String> res = new TreeMap<>(
                ByAiredYearStatistics.this::compare
        );
        for (int key: b.keySet()) {
            double val = 0;
            if (b.get(key) != 0) {
                val = ((double) a.get(key) * k) / (double) b.get(key);
            }
            //
            res.put(key, String.format("%.2f", val));
            // store peak value
            if (val > peak[0]) peak[0] = val;
        }
        //
        return res;
    }

    public int getTitlesPeak() {
        return titlesPeak;
    }

    public double getMeanPeak() {
        return meanPeak;
    }

    public double getHighRatedPeak() {
        return highRatedPeak;
    }
}
