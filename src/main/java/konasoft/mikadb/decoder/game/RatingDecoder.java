package konasoft.mikadb.decoder.game;

import konasoft.mikadb.decoder.Decoder;

public class RatingDecoder extends Decoder {
    public String[] RATING_FULL = {
        "5 - Piece of My Heart",    // 0
        "4 - Top Tier",             // 1
        "3 - Worthy",               // 2
        "2 - Substandard",          // 3
        "1 - Awful",                // 4
        "Inherit",                  // 5
        "Not Available",            // 6
    };

    public String[] RATING_SHORT = {
        "5", "4", "3", "2", "1", "Inh", "N/A", 
    };

    /**
     * instance
     * */
    private static RatingDecoder instance = new RatingDecoder();
    public static RatingDecoder getInstance() {
        return instance;
    }
    private RatingDecoder() {

    }

    /**
     * html
     * */
    public String getTextHtml(int i) {
        String cls = "\"option option-" + i + "\"";
        return String.format(
                "<p class=%s value=\"%s\">%s</p>",
                cls, i,
                RATING_FULL[i]
        );
    }
}
