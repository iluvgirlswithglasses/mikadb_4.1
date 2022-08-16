package konasoft.mikadb.api.initializr.lists.anime;

import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.decoder.anime.TypeDecoder;
import konasoft.mikadb.sqlite.dao.lists.FranchiseDAO;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Calendar;


public class AnimeFormPreparer {

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static StateDecoder stateDecoder = StateDecoder.getInstance();
    private static TypeDecoder typeDecoder = TypeDecoder.getInstance();
    private static DateDecoder dateDecoder = DateDecoder.getInstance();

    public static void prepare(ModelAndView mav) {
        try {
            mav.addObject("today", Calendar.getInstance());
            mav.addObject("franchises", new FranchiseDAO().all("anime"));
            mav.addObject("typeDecoder", typeDecoder);
            mav.addObject("stateDecoder", stateDecoder);
            mav.addObject("ratingDecoder", ratingDecoder);
            mav.addObject("dateDecoder", dateDecoder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
