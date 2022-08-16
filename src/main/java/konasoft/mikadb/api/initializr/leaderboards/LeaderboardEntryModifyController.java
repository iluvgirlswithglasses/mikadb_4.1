package konasoft.mikadb.api.initializr.leaderboards;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.model.leaderboards.LeaderboardEntry;
import konasoft.mikadb.model.leaderboards.lists.LeaderboardAnimeModel;
import konasoft.mikadb.model.leaderboards.lists.LeaderboardEntryModel;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.leaderboards.LeaderboardDAO;
import konasoft.mikadb.sqlite.dao.leaderboards.LeaderboardEntryDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping(LeaderboardEntryModifyController.PATH)
public class LeaderboardEntryModifyController {
    /**
     *
     * */
    public static final String PATH = LeaderboardsController.PATH + "/entry";
    private PageModel page;

    public LeaderboardEntryModifyController() throws SQLException {
        page = new PageDAO().getPage(LeaderboardsController.PATH);
    }

    /**
     * shortcuts
     * */
    // redirect to the leaderboard where the entry belongs
    private ModelAndView toLeaderboard(HttpServletRequest request) {
        return new ModelAndView("redirect:" + LeaderboardsController.PATH + "/" + request.getParameter("leaderboard-id"));
    }

    /**
     * add
     * */
    @RequestMapping("/add")
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("leaderboards/mods/add-entry");
        ThemeAPreparer.prepare(mav, page);
        //
        LeaderboardDAO dao = new LeaderboardDAO();
        mav.addObject("leaderboards", dao.all());
        LeaderboardEntry<LeaderboardEntryModel> entry = new LeaderboardEntry<>();
        entry.setModel(new LeaderboardEntryModel());
        mav.addObject("entry", entry);
        //
        return mav;
    }

    @RequestMapping("/add/submit")
    public ModelAndView addSubmit(HttpServletRequest request) {
        LeaderboardEntryDAO dao = new LeaderboardEntryDAO();
        try {
            dao.add(dao.entryConstructor(request));
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH + "/add");
        }
        return toLeaderboard(request);
    }

    /**
     * add shortcut
     * */
    @RequestMapping("/add/table={table},id={id}")
    public ModelAndView add(@PathVariable String table, @PathVariable String id) {
        ModelAndView mav = new ModelAndView("leaderboards/mods/add-entry");
        ThemeAPreparer.prepare(mav, page);
        //
        LeaderboardEntry<LeaderboardEntryModel> entry = new LeaderboardEntry<>();
        entry.setModel(new LeaderboardEntryModel());
        entry.getModel().setEntryTable(table);
        entry.getModel().setEntryId(id);
        //
        mav.addObject("leaderboards", new LeaderboardDAO().all());
        mav.addObject("entry", entry);
        return mav;
    }

    /**
     * edit
     * */
    @RequestMapping("/view/{id}")
    public ModelAndView view(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("leaderboards/mods/view-entry");
        ThemeAPreparer.prepare(mav, page);
        LeaderboardEntry<LeaderboardEntryModel> entry;
        //
        try {
            entry = new LeaderboardEntryDAO().get(Integer.parseInt(id));
            mav.addObject("entry", entry);
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH + "/add");
        }
        mav.addObject("leaderboard", new LeaderboardDAO().get(entry.getLeaderboardId()));
        return mav;
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("leaderboards/mods/edit-entry");
        ThemeAPreparer.prepare(mav, page);
        //
        mav.addObject("leaderboards", new LeaderboardDAO().all());
        try {
            mav.addObject("entry", new LeaderboardEntryDAO().get(Integer.parseInt(id)));
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH + "/add");
        }
        //
        return mav;
    }

    @RequestMapping("/edit/submit")
    public ModelAndView editSubmit(HttpServletRequest request) {
        LeaderboardEntryDAO dao = new LeaderboardEntryDAO();
        try {
            dao.update(dao.entryConstructor(request));
        } catch (Exception e0) {
            try {
                // something may corrupted
                return new ModelAndView("redirect:" + PATH + "/edit" + request.getParameter("id"));
            } catch (Exception e1) {
                // even the freaking id is corrupted
                return new ModelAndView("redirect:" + PATH + "/add");
            }
        }
        //
        return toLeaderboard(request);
    }

    /**
     * delete
     * */
    @RequestMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable String id) {
        try {
            LeaderboardEntryDAO dao = new LeaderboardEntryDAO();
            dao.delete(Integer.parseInt(id));
        } catch (Exception e) {
            System.out.println("invalid id");
        }
        return new ModelAndView("redirect:" + LeaderboardsController.PATH);
    }
}
