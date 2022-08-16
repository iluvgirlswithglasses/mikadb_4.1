package konasoft.mikadb.decoder.anime;

import konasoft.mikadb.decoder.Decoder;

import java.util.HashMap;

public class StateDecoder extends Decoder {
    public String[] STATE = {
            "Roaming",      // 0
            "Watching",     // 1
            "Re-watching",  // 2
            "Enqueued",     // 3
            "Completed",    // 4
            "Low-priority", // 5
            "Endless",      // 6
            "Dropped",      // 7
            "Plan to Watch",// 8
    };

    public String[] STATE_COLOR = {
            "3ac009",       // 0
            "0b703a",       // 1
            "0b9180",       // 2
            "1f3033",       // 3
            "17397d",       // 4
            "82630d",       // 5
            "650549",       // 6
            "6f1a1a",       // 7
            "525458",       // 8
    };

    public HashMap<String, int[]> STATE_FILT = new HashMap<>(){{
        put("completed", new int[]{2, 4, });
        put("watching", new int[]{0, 1, 2, 5, });
        put("enqueued", new int[]{3, });
        put("dropped", new int[]{5, 6, 7, });
        put("ptw", new int[]{8, });
    }};

    /**
     * instance
     * */
    private static StateDecoder instance = new StateDecoder();
    public static StateDecoder getInstance() {
        return instance;
    }
    private StateDecoder() {

    }
}
