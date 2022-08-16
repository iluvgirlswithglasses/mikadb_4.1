package konasoft.mikadb.api.initializr.lists.anime;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.py.MALAnimeEntry;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@RestController
@RequestMapping(AnimeController.PATH)
public class AnimeModificationController {
    public static final String PATH = AnimeController.PATH;

    private PageModel page;

    public AnimeModificationController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    /**
     * /add
     * */
    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request) {
        //
        try {
            ModelAndView mav = new ModelAndView(PATH + "/add");
            // prepare model & section
            AnimeModel anime = (AnimeModel) request.getSession().getAttribute("mal_entry");
            request.getSession().removeAttribute("mal_entry");
            if (anime == null) anime = new AnimeModel();
            request.getSession().setAttribute("entry", anime);
            mav.addObject("entry", anime);
            // prepare page infomation
            mav.addObject("version", MikadbApplication.VERSION);
            mav.addObject("page", page);
            AnimeFormPreparer.prepare(mav);
            //
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("redirect:" + PATH);
        }
    }

    @RequestMapping("/add/submit")
    public ModelAndView addSubmit(HttpServletRequest request) {
        try {
            AnimeDAO dao = new AnimeDAO();
            // get model
            AnimeModel anime = (AnimeModel) request.getSession().getAttribute("entry");
            if (anime == null) return new ModelAndView("redirect:" + PATH + "/add");
            dao.updateModelFromServletRequest(anime, request);
            // sqlite
            dao.add(anime);
            // return
            request.getSession().removeAttribute("entry");
            return new ModelAndView("redirect:" + PATH);
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("redirect:" + PATH + "/add");
            mav.addObject("error", "Error occured, You messed something up didn't you :)");
            return mav;
        }
    }


    /**
     * /add/mal
     *
     * */
    @RequestMapping("/add/mal")
    public ModelAndView addMAL(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView(PATH + "/add-mal");
        // error
        String error = (String) request.getAttribute("error");
        if (error == null) error = "";
        mav.addObject("error", error);
        // prepare page infomation
        mav.addObject("version", MikadbApplication.VERSION);
        mav.addObject("page", page);
        //
        return mav;
    }

    @RequestMapping("/add/mal/submit")
    public ModelAndView submitAddMAL(HttpServletRequest request, HttpServletResponse response) {
        try {
            // import
            AnimeModel anime = new MALAnimeEntry().getAnimeFromMALId(
                    Integer.parseInt(request.getParameter("id"))
            );
            request.getSession().setAttribute("mal_entry", anime);
            return add(request);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Couldn't import entry from MAL. Make sure your ID is valid.");
            return addMAL(request);
        }
    }

    /**
     * /edit
     * create session when user want to edit
     * then invalidate it immediately once submitted and updated
     * */
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable(value="id") String id) {
        //
        ModelAndView mav = new ModelAndView(PATH + "/edit");
        try {
            // get model
            AnimeModel anime = new AnimeDAO().get(Integer.parseInt(id));
            mav.addObject("entry", anime);
            // prepare
            mav.addObject("version", MikadbApplication.VERSION);
            mav.addObject("page", page);
            AnimeFormPreparer.prepare(mav);
            //
            return mav;
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }
    }

    @RequestMapping("/edit/{id}/submit")
    public ModelAndView editSubmit(HttpServletRequest request, @PathVariable(value="id") String id) {
        try {
            AnimeDAO dao = new AnimeDAO();
            AnimeModel anime = new AnimeDAO().get(Integer.parseInt(id));
            dao.updateModelFromServletRequest(anime, request);
            dao.update(anime);
            return new ModelAndView("redirect:" + PATH + "/view/" + id);
        } catch (Exception e) {
            // e.printStackTrace();
            // error
            ModelAndView mav = new ModelAndView("redirect:" + PATH + "/edit/" + id);
            mav.addObject("error", "Some error occured. You messed something up didn't you :)");
            return mav;
        }
    }
}
