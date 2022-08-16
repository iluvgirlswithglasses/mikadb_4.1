package konasoft.mikadb.api.initializr.lists.game;

import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.game.RatingDecoder;
import konasoft.mikadb.sqlite.dao.lists.FranchiseDAO;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Calendar;


public class GameFormPreparer {

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static DateDecoder dateDecoder = DateDecoder.getInstance();

    public static void prepare(ModelAndView mav) {
        try {
            mav.addObject("today", Calendar.getInstance());
            mav.addObject("franchises", new FranchiseDAO().all("game"));
            mav.addObject("ratingDecoder", ratingDecoder);
            mav.addObject("dateDecoder", dateDecoder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
