package konasoft.mikadb.decoder;

public class DateDecoder extends Decoder {
    //
    public final String[] MONTH_SHORT = {
            "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec",
    };

    public final String[] MONTH = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December",
    };

    public final String[] SEASON = {
            "Winter", "Spring", "Summer", "Fall",
    };

    @Override
    public String get(String[] arr, int i) {
        switch (i) {
            case 9999: return "Watching";
            case -2: return "Unknown";
            default: return arr[i];
        }
    }

    public String get(int i) {
        switch (i) {
            case 9999: return "Watching";
            case -2: return "Unknown";
        }
        return String.format("%s", i);
    }

    /**
     * brand new getters
     * */
    public String getYear(int i) {
        return get(i);
    }

    public String getMonthShort(int i) {
        return get(MONTH_SHORT, i);
    }

    public String getMonth(int i) {
        return get(MONTH, i);
    }

    public String getSeason(int i) {
        return get(SEASON, i);
    }

    // some instance
    private static DateDecoder instance = new DateDecoder();

    public static DateDecoder getInstance() {
        return instance;
    }

    private DateDecoder() {

    }
}
