package com.recommendations;

public class Movie {
	private int movieId;
	private String movieName;
	private String genres;
	private float predictedRating;
	
	public Movie (int mId, String name, String genres, float pRating) {
		this.movieId = mId;
		this.movieName = name;
		this.genres = genres;
		this.predictedRating = pRating;
	}

	public String toString() {
		return this.movieId + "::" + this.movieName + "::" + this.genres + "::" + this.predictedRating;
	}
	
	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public float getPredictedRating() {
		return predictedRating;
	}

	public void setPredictedRating(float predictedRating) {
		this.predictedRating = predictedRating;
	}
}
