package konasoft.mikadb.statistics.yearly;

import konasoft.mikadb.decoder.anime.RatingDecoder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class YearStat {
    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();

    /**
     * fields and contructors
     * */
    private int titleCount;
    private int titleCompleted;
    private int[] ratingDist;
    private int ratedCount;
    private int unratedCount;
    private int ratingSum;
    private double balancedRatingSum;
    private double enjoymentSum;

    public YearStat() {
        ratingDist = new int[ratingDecoder.RATING_FULL.length];
    }

    /**
     * api to statistics.yearly.Statistics
     * */
    public void addEntry(ResultSet rs) throws SQLException {
        int state = rs.getInt("state");
        // 8: PTW
        // 3: Enqueued
        if (state == 8 || state == 3) return;
        // 2: rewatching, 4: completed; defined in StateDecoder;
        if (state == 2 || state == 4) titleCompleted++;
        // titleCount count & score distribution
        titleCount++;
        int r = rs.getInt("rating");
        ratingDist[r]++;
        // anything related to mean score
        if (ratingDecoder.getRatingValue(r) > 0) {
            try {
                // if this first line happens, so does any other
                ratingSum += Integer.parseInt(ratingDecoder.RATING_SHORT[r]);
                double balancedRating = Double.parseDouble(ratingDecoder.RATING_BALANCED[r]);
                balancedRatingSum += balancedRating;
                ratedCount++;
                //
                if (balancedRating >= 0) 
                    enjoymentSum += balancedRating;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            unratedCount++;
        }
    }
    
    /**
     * getters
     * */
    public int getTitleCompleted() {
        return titleCompleted;
    }

    public int[] getRatingDist() {
        return ratingDist;
    }
    
    public int getRatedCount() {
        return ratedCount;
    }

    public int getUnratedCount() {
        return unratedCount;
    }

    public String getMeanRating() {
        return String.format("%.2f", (double) ratingSum / ratedCount);
    }

    public String getBalancedMeanRating() {
        double res = balancedRatingSum / ratedCount;
        String str = String.format("%.2f", res);
        if (res >= 0) return "+" + str;
        return str;
    }

    public String getEnjoyment() {
        return String.format("+%.2f", enjoymentSum / ratedCount);
    }

    /**
     * peak handler
     * */
    private int ratingDistPeak = -1;
    public int getRatingDistPeak() {
        if (ratingDistPeak == -1) {
            for (int i: ratingDist) ratingDistPeak = Math.max(ratingDistPeak, i);
        }
        //
        return ratingDistPeak;
    }

    /**
     * top & percentage
     * */
    private String[] ratingDistPercentage;
    public String[] getRatingDistPercentage() {
        if (ratingDistPercentage == null) {
            ratingDistPercentage = new String[ratingDist.length];
            for (int i = 0; i < ratingDist.length; i++) {
                ratingDistPercentage[i] = String.format(
                        "%.2f", (double) ratingDist[i] * 100 / titleCount
                );
            }
        }
        return ratingDistPercentage;
    }

    private String[] ratingDistTop;
    public String[] getRatingDistTop() {
        if (ratingDistTop == null) {
            ratingDistTop = new String[ratingDist.length];
            //
            int s = 0;
            for (int i = 0; i < 10; i++) {
                s += ratingDist[i];
                ratingDistTop[i] = String.format(
                        "%.2f", (double) s * 100 / ratedCount
                );
            }
            for (int i = 10; i < ratingDist.length; i++) {
                ratingDistTop[i] = "0.00";
            }
        }
        return ratingDistTop;
    }
}
