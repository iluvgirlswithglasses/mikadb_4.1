package konasoft.mikadb.model.comps;

import konasoft.mikadb.decoder.DateDecoder;

import java.time.YearMonth;

public class SeasonalDate {
    // in SQLite, we portray watching status with the code 9999
    public static final int WATCHING = 9999;
    public static final int UNKNOWN = -2;

    /**
     * @fields
     * */
    private int year = UNKNOWN;
    private int season = UNKNOWN;
    private int month = UNKNOWN;
    private int day = UNKNOWN;
    private int id = -1;


    /**
     * @constructors
     * */
    public SeasonalDate() {

    }

    public SeasonalDate(int year, int month, int day) {
        construct(year, UNKNOWN, month, day, UNKNOWN);
    }

    public SeasonalDate(int year, int season, int month, int day) {
        construct(year, season, month, day, UNKNOWN);
    }

    public SeasonalDate(int year, int season, int month, int day, int id) {
        construct(year, season, month, day, id);
    }

    public void construct(int year, int season, int month, int day, int id) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setSeason(season);
        setId(id);
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%02d:%02d:%s", getYear(), getSeason(), getMonth(), getDay(), getId());
    }


    /**
     * validators
     * */

    public boolean isSpecial(int i) {
        return i == UNKNOWN || i == WATCHING;
    }

    public boolean isValidMonth(int month) {
        if (isSpecial(month)) return true;
        return 0 <= month && month < 12;
    }

    private boolean isValidDay(int d) {
        if (isSpecial(d)) return true;
        int lengthOfMonth = YearMonth.of(this.year, this.month + 1).lengthOfMonth();
        return 0 < d && d <= lengthOfMonth;
    }

    private boolean isValidSeason(int s) {
        if (isSpecial(s)) return true;
        return 0 <= s && s < 4;
    }


    /**
     * setters
     * */
    // year affects every other date fields
    public void setYear(int year) {
        year = Math.max(year, UNKNOWN);
        // not set year case
        if (isSpecial(year)) {
            this.season = year;
            this.month = year;
            this.day = year;
        }
        this.year = year;
    }

    // month affects season and day fields
    public void setMonth(int month) {
        // the maximum value for month is 11 (December)
        if (!isValidMonth(month)) {
            return;
        }
        // month setting is only available if:
        // year is set
        if (!isSpecial(year)) {
            this.month = month;
            // if month is set, it will decide the season
            if (month >= 0) this.season = this.month / 3;
        }
    }

    // day affect no field
    public void setDay(int day) {
        // day is only available if:
        // year is set
        // month is set
        if (isSpecial(this.year) || isSpecial(this.month)) {
            return;
        }
        // day is the only value that can not be 0
        if (isValidDay(day)) this.day = day;
    }

    // season affects no field
    public void setSeason(int season) {
        // the maximum value for season is 3 (Autumn)
        if (isValidSeason(season)) {
            // season setting is only available if:
            // year is set &&
            // month is not set
            if (!isSpecial(year) && isSpecial(month)) {
                this.season = season;
            }
        }
    }

    // id affects no field
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @getters
     * */
    private static DateDecoder dateDecoder = DateDecoder.getInstance();
    public String getString() {
        // basically, if year is a special value, all other fields gonna have the same value as year
        if (isSpecial(year)) return dateDecoder.get(year);
        // if year is set and season is unknown, then year is the only field with a value
        // additionally, if month is set, it decide the season
        // the season field can only decide itself when month is unknown
        if (season == SeasonalDate.UNKNOWN) {
            return String.format("%s", year);
        }
        // if month is not set, furthur details are not available
        // therefore, we only have year and season
        if (month == SeasonalDate.UNKNOWN) {
            return String.format("%s %s", dateDecoder.SEASON[season], year);
        }
        // if month is set, so is day
        return String.format("%s-%s-%s", day, dateDecoder.MONTH_SHORT[month], year);
    }

    public int getYear() {
        return year;
    }

    public int getSeason() {
        return season;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getId() {
        return id;
    }
}
