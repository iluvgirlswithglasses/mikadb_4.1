package konasoft.mikadb.model.lists;

import konasoft.mikadb.api.DataAccess;
import konasoft.mikadb.decoder.game.RatingDecoder;
import konasoft.mikadb.model.comps.SeasonalDate;
import konasoft.mikadb.sqlite.dao.lists.GameDAO;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameModel {
	private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();

	/**
	 * @fields
	 * */
	private int id;
	private String title;
	private FranchiseModel franchise;
	private int rating;
	private SeasonalDate startDate;
	private SeasonalDate completeDate;
	private String comment;

	@Override
	public String toString() {
		return String.format("[%s] - %s", id, title);
	}

	/**
	 * @contructors
	 * */
	public GameModel() {
		id = -1;
		title = "";
		franchise = new FranchiseModel(title, "game");
		rating = ratingDecoder.indexOf(ratingDecoder.RATING_SHORT, "N/A");
		startDate = new SeasonalDate();
		completeDate = new SeasonalDate();
		comment = "";
	}

	public GameModel(
		int _id,
		String _title,
		FranchiseModel _franchise,
		int _rating,
		SeasonalDate _startDate,
		SeasonalDate _completeDate,
		String _comment
	) {
		id = _id;
		title = _title;
		franchise = _franchise;
		rating = _rating;
		startDate = _startDate;
		completeDate = _completeDate;
		comment = _comment;
	}

	/**
	 * @getters
	 * */
	public String getProfilePic() throws URISyntaxException {
        return new GameDAO().getProfilePic(id);
    }

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

    public int getRating() {
        return rating;
    }

    public SeasonalDate getStartDate() {
        return startDate;
    }

    public SeasonalDate getCompleteDate() {
        return completeDate;
    }

    /**
     * @setters
     * */
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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setStartDate(SeasonalDate startDate) {
        this.startDate = startDate;
    }

    public void setCompleteDate(SeasonalDate completeDate) {
        this.completeDate = completeDate;
    }
}
