package konasoft.mikadb.printer;

import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.decoder.anime.TypeDecoder;
import konasoft.mikadb.model.lists.AnimeModel;

import java.util.Calendar;
import java.util.List;

public class AnimeModelPrinter {
    public static void print(AnimeModel anime) {
        //
        TypeDecoder typeDecoder = TypeDecoder.getInstance();
        StateDecoder stateDecoder = StateDecoder.getInstance();
        RatingDecoder ratingDecoder = RatingDecoder.getInstance();
        //
        print(anime.getId());
        print(anime.getTitle());
        print(anime.getFranchise().getName());
        print(typeDecoder.TYPE[anime.getType()]);
        print(stateDecoder.STATE[anime.getState()]);
        print(ratingDecoder.RATING_FULL[anime.getRating()]);
        print(anime.getCompleteDate().toString());
        print(anime.getEpisodes());
        print(anime.getDuration());

        printCalendar(anime.getAired());
        printList(anime.getEndingThemes());
        printCalendar(anime.getFinishedAiring());
        printList(anime.getLicensors());
        print(anime.getMalId());
        printList(anime.getOpeningThemes());
        print(anime.getPremiered());
        printList(anime.getProducers());
        print(anime.getSource());
        print(anime.getStatus());
        printList(anime.getStudios());
        print(anime.getTitleEnglish());
        print(anime.getTitleJapanese());
        print(anime.getMalURL());
    }

    private static void print(int i) {
        System.out.println(i);
    }

    private static void print(String i) {
        System.out.println(i);
    }

    private static void printList(List<String> lst) {
        StringBuilder s = new StringBuilder("[");
        for (String i: lst) s.append("'").append(i).append("', ");
        s.append("]");
        //
        print(s.toString());
    }

    private static void printCalendar(Calendar calendar) {
        DateDecoder dateDecoder = DateDecoder.getInstance();
        print(String.format("%s-%s-%s",
                calendar.get(Calendar.DAY_OF_MONTH),
                dateDecoder.get(DateDecoder.getInstance().MONTH, calendar.get(Calendar.MONTH)),
                calendar.get(Calendar.YEAR)
        ));
    }
}
