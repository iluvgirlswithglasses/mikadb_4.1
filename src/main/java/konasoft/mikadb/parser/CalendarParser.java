package konasoft.mikadb.parser;

import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.model.comps.SeasonalDate;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarParser {
    /**
     * SQLite String <-> Calendar
     * note: SQLiteString use computer month
     * **/
    public static final Pattern SQLITE_DATE = Pattern.compile(
            "^([0-9]+):([0-9]+):([0-9]+)"
    );
    public Calendar sqliteStringToCalendar(String str) {
        // str: YYYY:MM:DD
        //      0123456789
        Calendar instance = Calendar.getInstance();
        Matcher m = SQLITE_DATE.matcher(str);
        //
        if (m.find()) {
            instance.set(Calendar.YEAR, Integer.parseInt(m.group(1)));
            instance.set(Calendar.MONTH, Integer.parseInt(m.group(2)));
            instance.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group(3)));
        } else {
            instance.set(1960, Calendar.JANUARY, 1);
        }
        //
        return instance;
    }

    public String calendarToSqliteString(Calendar instance) {
        return String.format(
                "%s:%02d:%02d",
                instance.get(Calendar.YEAR),
                instance.get(Calendar.MONTH),
                instance.get(Calendar.DAY_OF_MONTH)
        );
    }

    /**
     * SQLite String <-> SeasonalDate
     * note: SQLiteString use computer month
     * note: SQLiteSeasonalDateString will NOT store ID attribute
     * **/
    public static final Pattern SQLITE_SEASONAL_DATE = Pattern.compile(
            "^(.*):(.*):(.*):(.*)"
    );
    public SeasonalDate sqliteStringToSeasonalDate(String str) {
        // str: YYYY:SS:MM:DD:ID
        SeasonalDate instance = new SeasonalDate();
        Matcher m = SQLITE_SEASONAL_DATE.matcher(str);
        //
        if (m.find()) {
            instance.construct(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    -1
            );
        }
        //
        return instance;
    }

    public String seasonalDateToSQLiteString(SeasonalDate d) {
        int year = d.getYear();
        return String.format(
                "%s:%s:%02d:%02d",
                year, d.getSeason(), d.getMonth(), d.getDay()
        );
    }

    /**
     * MAL Date -> Calendar
     * **/
    // format: "MMM DD, YYYY"
    public static final Pattern MAL_DATE = Pattern.compile(
            "^([a-zA-Z]+)\\s([0-9]+),\\s([0-9]+)"
    );
    public Calendar malDateToCalendar(String str) {
        if (str.equals("?")) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 4000);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar;
        }
        // regex
        Matcher m = MAL_DATE.matcher(str);
        // assignments
        Calendar calendar = Calendar.getInstance();
        if (m.find()) {
            calendar.set(Calendar.MONTH, DateDecoder.getInstance().indexOf(
                    DateDecoder.getInstance().MONTH_SHORT, m.group(1)     // index of return computer month, not human month
            ));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group(2)));
            calendar.set(Calendar.YEAR, Integer.parseInt(m.group(3)));
        } else {
            calendar.set(1960, Calendar.JANUARY, 1);
        }
        // return
        return calendar;
    }
}
