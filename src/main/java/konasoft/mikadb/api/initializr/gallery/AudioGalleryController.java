package konasoft.mikadb.api.initializr.gallery;

import konasoft.mikadb.api.initializr.tools.ThemeAPreparer;
import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.dao.gallery.AudioDAO;
import konasoft.mikadb.sqlite.dao.sys.PageDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping(AudioGalleryController.PATH)
public class AudioGalleryController {
    public static final String PATH = "/gallery/audio-gallery";

    private PageModel page;

    public AudioGalleryController() throws SQLException {
        page = new PageDAO().getPage(PATH);
    }

    @GetMapping("")
    public ModelAndView main() {
        // temporarily, we haven't had anime category other than anime just yet
        return new ModelAndView("redirect:" + PATH + "/anime");
    }

    @GetMapping("/{cat}")
    public ModelAndView main(@PathVariable String cat) {
        // objs
        AudioDAO dao = new AudioDAO();
        // mav
        ModelAndView mav = new ModelAndView(PATH);
        try {
            mav.addObject("audioList", dao.all(cat));
        } catch (SQLException e) {
            mav.addObject("audioList", new ArrayList<>());
        }
        ThemeAPreparer.prepare(mav, page);
        return mav;
    }
}
