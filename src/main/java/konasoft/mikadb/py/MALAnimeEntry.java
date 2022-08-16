package konasoft.mikadb.py;

import konasoft.mikadb.api.DataAccess;
import konasoft.mikadb.decoder.anime.TypeDecoder;
import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.parser.CalendarParser;
import konasoft.mikadb.parser.ListParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MALAnimeEntry {
    public static final String PACKAGE_DIR = "/py/mal-anime-entry";

    public List<String> getAttrs() throws URISyntaxException, IOException {
        List<String> attrs = new ArrayList<>();
        //
        BufferedReader reader = new BufferedReader(new FileReader(
                DataAccess.getProjectDir() + PACKAGE_DIR + "/attrs.txt", StandardCharsets.UTF_8
        ));
        // handle result
        String line;
        while ((line = reader.readLine()) != null) {
            attrs.add(line);
        }
        return attrs;
    }

    public AnimeModel getAnimeFromMALId(int id) throws IOException, InterruptedException {
        // result handler
        // see '/py/mal-anime-entry/attrs.txt' to know what attrs is containing
        HashMap<String, String> dct = new HashMap<>();
        CalendarParser calendarParser = new CalendarParser();
        ListParser listParser = new ListParser();

        // execute
        Process pc = Runtime.getRuntime().exec("python3 py/mal-anime-entry/run.py " + id);
        pc.waitFor();

        // getting result
        BufferedReader reader = new BufferedReader(new FileReader("py/mal-anime-entry/out.txt", StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] pair = line.split("\\.\\./>>>");
            dct.put(pair[0], pair[1]);
        }

        // special
        String[] dates = dct.get("aired").split("\\sto\\s");
        Calendar aired = calendarParser.malDateToCalendar(dates[0]);
        Calendar finishedAiring = aired;
        if (dates.length > 1) finishedAiring = calendarParser.malDateToCalendar(dates[1]);

        int episodes = 1000;
        if (!dct.get("episodes").equals("None")) episodes = Integer.parseInt(dct.get("episodes"));

        // returning
        TypeDecoder typeDecoder = TypeDecoder.getInstance();
        return new AnimeModel(
                dct.get("title"),                                                       // title
                typeDecoder.indexOf(typeDecoder.MAL_TYPE, dct.get("type")),             // type
                episodes,                                                               // episodes
                parseDuration(dct.get("duration")),                                     // duration
                aired,                                                                  // aired
                listParser.stringToList(dct.get("ending_themes")),                      // endingThemes
                finishedAiring,                                                         // finishedAiring
                listParser.stringToList(dct.get("licensors")),                          // licensors
                id,                                                                     // malId
                listParser.stringToList(dct.get("opening_themes")),                     // openingThemes
                dct.get("premiered"),                                                   // premiered
                listParser.stringToList(dct.get("producers")),                          // producers
                dct.get("source"),                                                      // source
                dct.get("status"),                                                      // status
                listParser.stringToList(dct.get("studios")),                            // studios
                listParser.stringToList(dct.get("genres")),                             // genres
                dct.get("title_english"),                                               // titleEnglish
                dct.get("title_japanese"),                                              // titleJapanese
                dct.get("url")                                                          // malURL
        );
    }

    private int parseDuration(String str) {
        int mins = 0;
        String[] args = str.split("\\s");
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("min."))
                mins += Integer.parseInt(args[i-1]);
            if (args[i].equals("hr."))
                mins += Integer.parseInt(args[i-1]) * 60;
        }
        return mins;
    }
}
