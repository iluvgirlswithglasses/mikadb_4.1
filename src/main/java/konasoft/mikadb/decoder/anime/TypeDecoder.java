package konasoft.mikadb.decoder.anime;

import konasoft.mikadb.decoder.Decoder;

public class TypeDecoder extends Decoder {
    public final String[] TYPE = {
            "TV-Series",    // 0
            "Movie",        // 1
            "Special",      // 2
            "OVA",          // 3
            "ONA"           // 4
    };

    public final String[] MAL_TYPE = {
            "TV",
            "Movie",
            "Special",
            "OVA",
            "ONA"
    };

    /**
     * instance
     * */
    private static TypeDecoder instance = new TypeDecoder();
    public static TypeDecoder getInstance() {
        return instance;
    }
    private TypeDecoder() {

    }
}
