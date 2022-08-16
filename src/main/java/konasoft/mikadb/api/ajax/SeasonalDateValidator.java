package konasoft.mikadb.api.ajax;

import konasoft.mikadb.model.comps.SeasonalDate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SeasonalDateValidator {
    @RequestMapping("/ajax/SeasonalDateValidator/")
    public void validate(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SeasonalDate date = new SeasonalDate(
                Integer.parseInt(request.getParameter("year")),
                Integer.parseInt(request.getParameter("season")),
                Integer.parseInt(request.getParameter("month")),
                Integer.parseInt(request.getParameter("day"))
        );

        // and then the date instance will handle everything itself
        try {
            response.getWriter().write(
                    String.format("%s:%s:%s:%s", date.getYear(), date.getSeason(), date.getMonth(), date.getDay())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
