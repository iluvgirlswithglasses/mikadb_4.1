package konasoft.mikadb.api.initializr.leaderboards;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.datastructure.Pair;
import konasoft.mikadb.model.leaderboards.LeaderboardEntry;
import konasoft.mikadb.model.leaderboards.LeaderboardModel;
import konasoft.mikadb.model.leaderboards.lists.LeaderboardEntryModel;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.leaderboards.LeaderboardDAO;
import konasoft.mikadb.sqlite.dao.leaderboards.LeaderboardEntryDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
public class LeaderboardsController {
    public static final String PATH = "/leaderboards";

    private PageModel page;

    public LeaderboardsController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    /**
     * browsing
     * */
    @RequestMapping(PATH)
    public ModelAndView leaderboards() {
        ModelAndView mav = new ModelAndView(PATH);
        ThemeAPreparer.prepare(mav, page);
        // load all leaderboards
        // sort by category
        // temp code
        List<Pair<String, List<LeaderboardModel>>> m = new ArrayList<>();
        m.add(new Pair<>("anime", new LeaderboardDAO().all()));
        //
        mav.addObject("leaderboards", m);
        return mav;
    }

    @RequestMapping(PATH + "/{id}")
    public ModelAndView leaderboard(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("leaderboards/view");
        // get leaderboard's data
        LeaderboardDAO dao = new LeaderboardDAO();
        LeaderboardModel model;
        List<LeaderboardEntry<LeaderboardEntryModel>> entries;
        try {
            model = dao.get(Integer.parseInt(id));
            entries = new LeaderboardEntryDAO().loadLeaderboard(model.getId());
        } catch (Exception e) {
            return new ModelAndView("redirect:" + PATH);
        }
        // generate page model
        String r = "Ranked";
        if (!model.isRanked()) r = "Unranked";
        PageModel page = new PageModel(
                this.page.getStringPath() + "/" + id,
                String.format("%s <span class=\"tab\"></span><span class=\"sub\">(%s entries | %s)</span>", model.getName(), entries.size(), r),
                model.getDescription(),
                ""
        );
        // add object attrs
        ThemeAPreparer.prepare(mav, page);
        mav.addObject("leaderboard", model);
        mav.addObject("entries", entries);
        //
        return mav;
    }
}
