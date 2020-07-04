package domain;

public class Film {
	
	private String numberOrHDD;
	private String title;
	private String year;
	private String yearSymbolChoice;
	private String description;
	private String genre1;
	private String genre2;
	private String genre3;
	private String imdbRating;
	private String actor1;
	private String actor2;
	private String actor3;
	private String director;
	private String filmDuration;
	private String tvOrDvd;
	private String certificate;
	private String hddDirectory;
	private String toWatch;
	private String myRating;

	
	public String getNumberOrHDD() {
		return numberOrHDD;
	}

	public void setNumberOrHDD(String numberOrHDD) {
		this.numberOrHDD = numberOrHDD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getYearSymbolChoice() {
		return yearSymbolChoice;
	}

	public void setYearSymbolChoice(String yearSymbolChoice) {
		this.yearSymbolChoice = yearSymbolChoice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getGenre2() {
		return genre2;
	}

	public void setGenre2(String genre2) {
		this.genre2 = genre2;
	}

	public String getGenre3() {
		return genre3;
	}

	public void setGenre3(String genre3) {
		this.genre3 = genre3;
	}

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getActor1() {
		return actor1;
	}

	public void setActor1(String actor1) {
		this.actor1 = actor1;
	}

	public String getActor2() {
		return actor2;
	}

	public void setActor2(String actor2) {
		this.actor2 = actor2;
	}

	public String getActor3() {
		return actor3;
	}

	public void setActor3(String actor3) {
		this.actor3 = actor3;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getFilmDuration() {return filmDuration;}

	public void setFilmDuration(String filmDuration) {this.filmDuration = filmDuration;}

	public String getTvOrDvd() {
		return tvOrDvd;
	}

	public void setTvOrDvd(String tvOrDvd) {
		this.tvOrDvd = tvOrDvd;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getHddDirectory() {
		return hddDirectory;
	}

	public void setHddDirectory(String hddDirectory) {
		this.hddDirectory = hddDirectory;
	}

	public String getToWatch() {
		return toWatch;
	}

	public void setToWatch(String toWatch) {
		this.toWatch = toWatch;
	}

	public String getMyRating() {
		return myRating;
	}

	public void setMyRating(String myRating) { this.myRating = myRating; }

	public Film() {
	}

	public String toString() {
		return String.format("%s", title);
	}
	
}


