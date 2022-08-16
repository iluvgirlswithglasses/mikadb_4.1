package konasoft.mikadb.api.initializr.leaderboards;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.model.leaderboards.LeaderboardModel;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.leaderboards.LeaderboardDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
public class LeaderboardModifyController {
    /**
     *
     * */
    public static final String PATH = LeaderboardsController.PATH;
    private PageModel page;

    public LeaderboardModifyController() throws SQLException {
        page = new PageDAO().getPage(LeaderboardsController.PATH);
    }

    /**
     * add
     * */
    @RequestMapping(PATH + "/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("/leaderboards/mods/add-leaderboard");
        ThemeAPreparer.prepare(mav, page);
        //
        LeaderboardModel model = new LeaderboardModel();
        mav.addObject("leaderboard", model);
        //
        return mav;
    }

    @RequestMapping(PATH + "/add/submit")
    public ModelAndView addSubmit(HttpServletRequest request) {
        LeaderboardDAO dao = new LeaderboardDAO();
        try {
            dao.add(dao.leaderboardConstructor(request));
            //
            return new ModelAndView("redirect:" + PATH);
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH + "/add");
        }
    }

    /**
     * modification
     * */
    @RequestMapping(PATH + "/edit/{id}")
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("leaderboards/mods/edit-leaderboard");
        ThemeAPreparer.prepare(mav, page);
        //
        try {
            LeaderboardModel model = new LeaderboardDAO().get(Integer.parseInt(id));
            mav.addObject("leaderboard", model);
        } catch (Exception e) {
            return new ModelAndView("redirect:" + LeaderboardsController.PATH);
        }
        //
        return mav;
    }

    @RequestMapping(PATH + "/edit/submit")
    public ModelAndView editSubmit(HttpServletRequest request) {
        LeaderboardDAO dao = new LeaderboardDAO();
        try {
            dao.update(dao.leaderboardConstructor(request));
            return new ModelAndView("redirect:" + LeaderboardsController.PATH + "/" + request.getParameter("id"));
        } catch (Exception e0) {
            try {
                // something is corrupted
                return new ModelAndView("redirect:" + PATH + "/edit/" + request.getParameter("id"));
            } catch (Exception e1) {
                // even id is corrupted
                return new ModelAndView("redirect:" + PATH + "/add");
            }
        }
    }

    /**
     * delete
     * */
    @RequestMapping(PATH + "/delete/{id}")
    public ModelAndView delete(@PathVariable String id) {
        try {
            new LeaderboardDAO().delete(Integer.parseInt(id));
        } catch (Exception e) {
            System.out.print("invalid id");
        }
        //
        return new ModelAndView("redirect:" + LeaderboardsController.PATH);
    }
}
