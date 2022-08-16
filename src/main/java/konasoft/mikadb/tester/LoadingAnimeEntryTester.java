package konasoft.mikadb.tester;

import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.printer.AnimeModelPrinter;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;

public class LoadingAnimeEntryTester {
    public static void execute() {
        try {
            for (AnimeModel i: new AnimeDAO().all()) {
                System.out.println("-".repeat(64));
                System.out.println(i);
                AnimeModelPrinter.print(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
