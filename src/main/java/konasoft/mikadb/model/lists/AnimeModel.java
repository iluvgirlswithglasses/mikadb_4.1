package konasoft.mikadb.model.lists;

import konasoft.mikadb.api.DataAccess;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.decoder.anime.TypeDecoder;
import konasoft.mikadb.model.comps.SeasonalDate;
import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnimeModel {

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static StateDecoder stateDecoder = StateDecoder.getInstance();
    private static TypeDecoder typeDecoder = TypeDecoder.getInstance();

    /**
     * @fields
     * traditional fields
     * **/
    private int id;
    private String title;
    private FranchiseModel franchise;   // user based
    private String comment;             // user based
    private int type;
    private int state;                  // user based
    private int seen;                   // user based
    private int rating;                 // user based
    private SeasonalDate startDate;     // user based
    private SeasonalDate completeDate;  // user based
    private int episodes;
    private int duration;
    private String rewatchNotes;        // user based

    // new fields
    // all of them should be auto handled by MAL API
    private Calendar aired;
    private List<String> endingThemes;
    private Calendar finishedAiring;
    private List<String> licensors;
    private int malId;
    private List<String> openingThemes;
    private String premiered;
    private List<String> producers;
    private String source;
    private String status;
    private List<String> studios;
    private List<String> genres;
    private String titleEnglish;
    private String titleJapanese;
    private String malURL;

    @Override
    public String toString() {
        return String.format("[%s] - %s", id, title);
    }

    /**
     * @constructors
     * **/
    // default values
    public AnimeModel() {
        id = -1;
        title = "";
        franchise = new FranchiseModel(title, "anime");
        comment = "";
        type = typeDecoder.indexOf(typeDecoder.TYPE, "ONA");
        state = stateDecoder.indexOf(stateDecoder.STATE, "Plan to Watch");
        seen = 0;
        rating = ratingDecoder.indexOf(ratingDecoder.RATING_SHORT, "N/A");
        startDate = new SeasonalDate();
        completeDate = new SeasonalDate();
        episodes = 0;
        duration = 0;
        rewatchNotes = "";

        aired = Calendar.getInstance();
        endingThemes = new ArrayList<>();
        finishedAiring = Calendar.getInstance();
        licensors = new ArrayList<>();
        malId = -1;
        openingThemes = new ArrayList<>();
        premiered = "";
        producers = new ArrayList<>();
        source = "";
        studios = new ArrayList<>();
        genres = new ArrayList<>();
        titleEnglish = "";
        titleJapanese = "";
        malURL = "";
    }

    // full parse
    public AnimeModel(
            int id,
            String title,
            FranchiseModel franchise,
            String comment,
            int type,
            int state,
            int seen,
            int rating,
            SeasonalDate startDate,
            SeasonalDate completeDate,
            int episodes,
            int duration,
            String rewatchNotes,

            Calendar aired,
            List<String> endingThemes,
            Calendar finishedAiring,
            List<String> licensors,
            int malId,
            List<String> openingThemes,
            String premiered,
            List<String> producers,
            String source,
            String status,
            List<String> studios,
            List<String> genres,
            String titleEnglish,
            String titleJapanese,
            String malURL
    ) {
        this.id = id;
        this.title = title;
        this.franchise = franchise;
        this.comment = comment;
        this.type = type;
        this.state = state;
        this.seen = seen;
        this.rating = rating;
        this.startDate = startDate;
        this.completeDate = completeDate;
        this.episodes = episodes;
        this.duration = duration;
        this.rewatchNotes = rewatchNotes;

        this.aired = aired;
        this.endingThemes = endingThemes;
        this.finishedAiring = finishedAiring;
        this.licensors = licensors;
        this.malId = malId;
        this.openingThemes = openingThemes;
        this.premiered = premiered;
        this.producers = producers;
        this.source = source;
        this.status = status;
        this.studios = studios;
        this.genres = genres;
        this.titleEnglish = titleEnglish;
        this.titleJapanese = titleJapanese;
        this.malURL = malURL;

        //
        this.completeDate.setId(this.id);
    }

    // mal parse
    public AnimeModel(
            String title,
            int type,
            int episodes,
            int duration,

            Calendar aired,
            List<String> endingThemes,
            Calendar finishedAiring,
            List<String> licensors,
            int malId,
            List<String> openingThemes,
            String premiered,
            List<String> producers,
            String source,
            String status,
            List<String> studios,
            List<String> genres,
            String titleEnglish,
            String titleJapanese,
            String malURL
    ) {
        this.id = -1;
        this.title = title;
        this.franchise = new FranchiseModel("", "anime");
        this.comment = "";
        this.type = type;
        this.state = stateDecoder.indexOf(stateDecoder.STATE, "Plan to Watch");
        this.seen = 0;
        this.rating = ratingDecoder.indexOf(ratingDecoder.RATING_SHORT, "N/A");
        this.startDate = new SeasonalDate();
        this.completeDate = new SeasonalDate();
        this.episodes = episodes;
        this.duration = duration;
        this.rewatchNotes = "";

        this.aired = aired;
        this.endingThemes = endingThemes;
        this.finishedAiring = finishedAiring;
        this.licensors = licensors;
        this.malId = malId;
        this.openingThemes = openingThemes;
        this.premiered = premiered;
        this.producers = producers;
        this.source = source;
        this.status = status;
        this.studios = studios;
        this.genres = genres;
        this.titleEnglish = titleEnglish;
        this.titleJapanese = titleJapanese;
        this.malURL = malURL;

        //
        this.completeDate.setId(this.id);
    }

    /**
     * getters
     * **/
    public int getRewatchCount() {
        if (episodes == 0) return 0;
        return (seen / episodes) - 1;
    }

    public int getProgress() {
        // exclude this first
        if (episodes == 0) return 0;
        // no progress yet
        if (seen == 0) return 0;
        // has completed
        if (seen % episodes == 0) {
            return episodes;
        }
        // in progress
        return seen % episodes;
    }

    public String getProfilePic() throws URISyntaxException {
        return new AnimeDAO().getProfilePic(id);
    }

    /**
     * basic getters
     * **/
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public FranchiseModel getFranchise() {
        return franchise;
    }

    public String getComment() {
        return comment;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public int getSeen() {
        return seen;
    }

    public int getRating() {
        return rating;
    }

    public SeasonalDate getStartDate() {
        return startDate;
    }

    public SeasonalDate getCompleteDate() {
        return completeDate;
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getDuration() {
        return duration;
    }

    public String getRewatchNotes() {
        return rewatchNotes;
    }

    public Calendar getAired() {
        return aired;
    }

    public Calendar getFinishedAiring() {
        return finishedAiring;
    }

    public List<String> getEndingThemes() {
        return endingThemes;
    }

    public List<String> getLicensors() {
        return licensors;
    }

    public int getMalId() {
        return malId;
    }

    public List<String> getOpeningThemes() {
        return openingThemes;
    }

    public String getPremiered() {
        return premiered;
    }

    public List<String> getProducers() {
        return producers;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getStudios() {
        return studios;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public String getMalURL() {
        return malURL;
    }

    /**
     * basic setters
     * **/
    public void setId(int id) {
        this.id = id;

        this.completeDate.setId(this.id);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFranchise(FranchiseModel franchise) {
        this.franchise = franchise;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setStartDate(SeasonalDate startDate) {
        this.startDate = startDate;
    }

    public void setCompleteDate(SeasonalDate completeDate) {
        this.completeDate = completeDate;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRewatchNotes(String rewatchNotes) {
        this.rewatchNotes = rewatchNotes;
    }

    public void setAired(Calendar aired) {
        this.aired = aired;
    }

    public void setFinishedAiring(Calendar finishedAiring) {
        this.finishedAiring = finishedAiring;
    }

    public void setEndingThemes(List<String> endingThemes) {
        this.endingThemes = endingThemes;
    }

    public void setLicensors(List<String> licensors) {
        this.licensors = licensors;
    }

    public void setMalId(int malId) {
        this.malId = malId;
    }

    public void setOpeningThemes(List<String> openingThemes) {
        this.openingThemes = openingThemes;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public void setProducers(List<String> producers) {
        this.producers = producers;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStudios(List<String> studios) {
        this.studios = studios;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
    }

    public void setMalURL(String malURL) {
        this.malURL = malURL;
    }
}
