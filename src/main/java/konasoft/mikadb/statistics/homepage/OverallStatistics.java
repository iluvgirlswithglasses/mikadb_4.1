package konasoft.mikadb.statistics.homepage;

import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.model.lists.AnimeModel;

public class OverallStatistics {
    // overrall-section infomation
    private int completedTitlesCount = 0;
    private int franchisesCount = 0;
    private int minutesWatched = 0;
    private int episodesWatched = 0;
    private int episodesRewatched = 0;
    private int ratedCount = 0;
    private int ratedSum = 0;
    private double balancedRatedSum = 0.0;

    // @getters
    public int getCompletedTitlesCount() {
        return completedTitlesCount;
    }

    public int getFranchisesCount() {
        return franchisesCount;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }

    public int getEpisodesRewatched() {
        return episodesRewatched;
    }

    public String getHoursWatched() {
        return String.format("%.2f", (double) minutesWatched / 60.0 );
    }

    public int getRatedCount() {
        return ratedCount;
    }

    public String getMeanScore() {
        return String.format("%.2f", (double) ratedSum / (double) ratedCount);
    }

    public String getBalancedMeanScore() {
        double res = balancedRatedSum / ratedCount;
        String str = String.format("%.2f", res);
        if (res >= 0) return "+" + str;
        return str;
    }

    // @that hell iteration handler
    public void iterationHandler(AnimeModel anime) {
        // 7 == "Plan to Watch", defined in StateDecoder
        // 3 == "Enqueued"
        if (anime.getState() == 8 || anime.getState() == 3) return;
        
        // completed title count
        if (anime.getSeen() >= anime.getEpisodes()) {
            completedTitlesCount += 1;
        }

        // minutes watched
        // seen: episodes watched; duration: dur/ep;
        minutesWatched += anime.getSeen() * anime.getDuration();

        // episodes watched
        episodesWatched += anime.getSeen();
        // episodes rewatched
        if (anime.getSeen() > anime.getEpisodes()) {
            episodesRewatched += anime.getSeen() - anime.getEpisodes();
        }

        // ratings
        addRating(anime.getRating());
    }

    // @setters
    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    public void addRating(int rating) {
        int ratingVal = ratingDecoder.getRatingValue(rating);
        if (ratingVal > 0) {
            ratedCount += 1;
            ratedSum += ratingVal;
            balancedRatedSum += Double.parseDouble(ratingDecoder.RATING_BALANCED[rating]);
        }
    }

    public void setFranchisesCount(int i) {
        franchisesCount = i;
    }
}
