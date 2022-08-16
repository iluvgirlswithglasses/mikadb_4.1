package konasoft.mikadb.decoder.anime;

import konasoft.mikadb.decoder.Decoder;

public class RatingDecoder extends Decoder {
    public String[] RATING_FULL = {
            "10 - Masterpiece",     // 0
            "9 - Top-notch",        // 1
            "8 - Astonishing",      // 2
            "7 - Memorable",        // 3
            "6 - Worthy",           // 4
            "5 - Above-average",    // 5
            "4 - Plain",            // 6
            "3 - Substandard",      // 7
            "2 - Time-wasting",     // 8
            "1 - Abomination",      // 9
            "Inherit",              // 10
            "Currently Rating",     // 11
            "Not Available",        // 12
    };

    public String[] RATING_SHORT = {
            "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "Inh", "C/R", "N/A",
    };

    public String[] RATING_BALANCED = {
            "+4.0", "+3.0", "+2.5", "+1.5", "+1.0", "+0.5", "-0.5", "-1.5", "-3.0", "-4.0",
            "Inh", "C/R", "N/A",
    };

    // 0 == positive
    // 1 == neutral
    // 2 == negative
    public int[] RATING_BALANCED_AXIS = {
            0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 1,
    };

    public String[] RATING_BALANCED_AXIS_COLOR = {
            "12a026", "9f9f9f", "eb4949"
    };

    public int getRatingValue(String[] arr, String score) {
        for (int i = 0; i < arr.length; i++) {
            if (score.equals(arr[i])) return getRatingValue(i);
        }
        return -1;
    }

    public int getRatingValue(String i) {
        return getRatingValue(Integer.parseInt(i));
    }

    public int getRatingValue(int i) {
        if (i >= 10) return -1;
        return 10 - i;
    }

    /**
     * new getters
     * */
    public String getRatingFull(int i) {
        return get(RATING_FULL, i);
    }

    public String getRatingShort(int i) {
        return get(RATING_SHORT, i);
    }

    public String getRatingBalanced(int i) {
        return get(RATING_BALANCED, i);
    }

    private static final String FULL_HTML_STYLE = "font-family: Consolas;";
    public String getListUtilsOptionHtml(int i) {
        String cls = "\"option option-" + i + "\"";
        if (i >= 10) {
            return String.format(
                "<p class=%s value=\"%s\">%s</p>",
                cls, i,
                RATING_FULL[i]
            );
        }
        return String.format(
                "<p class=%s value=\"%s\">%s <span class=%s value=\"%s\" style=\"color: #%s; %s\">(%s)</span></p>",
                cls, i,
                RATING_FULL[i],
                cls, i,
                RATING_BALANCED_AXIS_COLOR[RATING_BALANCED_AXIS[i]],
                FULL_HTML_STYLE,
                RATING_BALANCED[i]
        );
    }
    public String getTextHtml(int i) {
        if (i >= 10) return RATING_FULL[i];
        return String.format(
                "%s <span style=\"color: #%s; %s\">(%s)</span>",
                RATING_FULL[i],
                RATING_BALANCED_AXIS_COLOR[RATING_BALANCED_AXIS[i]],
                FULL_HTML_STYLE,
                RATING_BALANCED[i]
        );
    }

    /**
     * instance
     * */
    private static RatingDecoder instance = new RatingDecoder();
    public static RatingDecoder getInstance() {
        return instance;
    }
    private RatingDecoder() {

    }
}
