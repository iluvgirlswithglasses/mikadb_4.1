package konasoft.mikadb.statistics.yearly;

import konasoft.mikadb.parser.CalendarParser;
import konasoft.mikadb.sqlite.DatabaseAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class Statistics {
    // the constructor won't do much thing here
    private LinkedHashMap<Integer, YearStat> stats = new LinkedHashMap<>();
    private YearStat overallStats = new YearStat();

    public Statistics() {

    }

    /**
     *
     * */
    public void load() throws SQLException {
        //
        CalendarParser parser = new CalendarParser();
        //
        ResultSet rs = DatabaseAccess.getInstance().getConn().createStatement().executeQuery(
                "select rating, complete_date, state from anime order by complete_date asc"
        );
        while (rs.next()) {
            int year = parser.sqliteStringToSeasonalDate(rs.getString("complete_date")).getYear();
            if (year == 9999) year = Calendar.getInstance().get(Calendar.YEAR);
            overallStats.addEntry(rs);
            //
            if (!stats.containsKey(year)) {
                stats.put(year, new YearStat());
            }
            stats.get(year).addEntry(rs);
        }
    }

    /**
     * getters
     * */
    public LinkedHashMap<Integer, YearStat> getStats() {
        return stats;
    }

    public YearStat getOverallStats() {
        return overallStats;
    }
}
