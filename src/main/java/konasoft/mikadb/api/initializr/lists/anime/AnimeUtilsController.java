package konasoft.mikadb.api.initializr.lists.anime;

import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.py.MALAnimeEntry;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;
import konasoft.mikadb.sqlite.dao.lists.FranchiseDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * special utils for /lists/anime
 * */

@RestController
@RequestMapping(AnimeUtilsController.PATH)
public class AnimeUtilsController {
    public static final String PATH = AnimeController.PATH;

    // override information of MAL entry
    @GetMapping("/replace/{id}/{malId}")
    public ModelAndView replace(@PathVariable String id, @PathVariable String malId) {
        try {
            AnimeDAO dao = new AnimeDAO();
            AnimeModel model = new MALAnimeEntry().getAnimeFromMALId(Integer.parseInt(malId));
            model.setId(Integer.parseInt(id));
            dao.update(model);
        } catch (Exception e) {
            return new ModelAndView("redirect:" + AnimeController.PATH);
        }
        //
        return new ModelAndView("redirect:" + AnimeController.PATH + "/edit/" + id);
    }

    // delete a franchise
    @GetMapping("/deletef/{f}")
    public ModelAndView deleteFranchise(@PathVariable String f) {
        try {
            new FranchiseDAO().delete(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:" + AnimeController.PATH);
    }

    // rename a franchise
    @GetMapping("/renamef/{f}/{t}")
    public ModelAndView renameFranchise(@PathVariable String f, @PathVariable String t) {
        try {
            new FranchiseDAO().rename(f, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:" + AnimeController.PATH + "/franchise=" + t);
    }

    // clear session & attrs
    @GetMapping("/clear")
    public ModelAndView clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
        //
        return new ModelAndView("redirect:" + AnimeController.PATH);
    }
}
