package konasoft.mikadb.api.initializr.lists.game;

import konasoft.mikadb.MikadbApplication;
import konasoft.mikadb.model.lists.GameModel;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.lists.GameDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@RestController
@RequestMapping(GameController.PATH)
public class GameModificationController {
    public static final String PATH = GameController.PATH;

    private PageModel page;

    public GameModificationController() throws SQLException {
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
            mav.addObject("entry", new GameModel());
            // prepare page infomation
            mav.addObject("version", MikadbApplication.VERSION);
            mav.addObject("page", page);
            GameFormPreparer.prepare(mav);
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
            GameDAO dao = new GameDAO();
            GameModel game = new GameModel();
            // get model
            dao.updateModelFromServletRequest(game, request);
            // sqlite
            dao.add(game);
            // return
            return new ModelAndView("redirect:" + PATH);
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("redirect:" + PATH + "/add");
            mav.addObject("error", "Error occured, You messed something up didn't you :)");
            return mav;
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
            GameModel game = new GameDAO().get(Integer.parseInt(id));
            mav.addObject("entry", game);
            // prepare
            mav.addObject("version", MikadbApplication.VERSION);
            mav.addObject("page", page);
            GameFormPreparer.prepare(mav);
            //
            return mav;
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }
    }

    @RequestMapping("/edit/{id}/submit")
    public ModelAndView editSubmit(HttpServletRequest request, @PathVariable(value="id") String id) {
        try {
            GameDAO dao = new GameDAO();
            GameModel game = new GameDAO().get(Integer.parseInt(id));
            dao.updateModelFromServletRequest(game, request);
            dao.update(game);
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
