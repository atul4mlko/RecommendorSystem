package com.recommendations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Evaluation {

	private long count = 0;
	private long oneUserCount = 0;
	/*
	 * Input - User's rating profile and array of genres for a movies
	 * Output - Predicted rating for that movie
	 * e.g - ratings[] = 3.4285715,3.6,0,0,3.0,0,0,4.0,0,0,0,0,2.0,3.0,5.0,2.75,4.0,5.0
	 * genres[] = {Adventure,Animation,Children,Comedy,Fantasy}
	 * output = (3.6+0+0+3+4)/5 = 2.12
	 */
	public float getPredictedRating(float[] ratings, String[] genres) {
		float predictedRating = 0;
		int count = 0;
		for (String genre : genres) {
			if (genre.equals("Action")) {
				predictedRating += ratings[0];
				count++;
			} else if (genre.equals("Adventure")) {
				predictedRating += ratings[1];
				count++;
			} else if (genre.equals("Animation")) {
				predictedRating += ratings[2];
				count++;
			} else if (genre.equals("Children")) {
				predictedRating += ratings[3];
				count++;
			} else if (genre.equals("Comedy")) {
				predictedRating += ratings[4];
				count++;
			} else if (genre.equals("Crime")) {
				predictedRating += ratings[5];
				count++;
			} else if (genre.equals("Documentary")) {
				predictedRating += ratings[6];
				count++;
			} else if (genre.equals("Drama")) {
				predictedRating += ratings[7];
				count++;
			} else if (genre.equals("Fantasy")) {
				predictedRating += ratings[8];
				count++;
			} else if (genre.equals("Film-Noir")) {
				predictedRating += ratings[9];
				count++;
			} else if (genre.equals("Horror")) {
				predictedRating += ratings[10];
				count++;
			} else if (genre.equals("Musical")) {
				predictedRating += ratings[11];
				count++;
			} else if (genre.equals("Mystery")) {
				predictedRating += ratings[12];
				count++;
			} else if (genre.equals("Romance")) {
				predictedRating += ratings[13];
				count++;
			} else if (genre.equals("Sci-Fi")) {
				predictedRating += ratings[14];
				count++;
			} else if (genre.equals("Thriller")) {
				predictedRating += ratings[15];
				count++;
			} else if (genre.equals("War")) {
				predictedRating += ratings[16];
				count++;
			} else if (genre.equals("Western")) {
				predictedRating += ratings[17];
				count++;
			}
		}
		if (count != 0)
			return predictedRating/count;
		else
			return 0;
	}
	
	/*
	 * Evaluates the erms for a particular user.
	 * erms = sqrt( (sum of (actual rating - predicted rating)^2)/number of ratings )
	 */
	public float evaluatePredictions (int userId) {
		float evaluation = 0;
		oneUserCount = 0;
		File file = new File("Combine.dat"); // 3::110::4.5::1136075500::Action|Drama|War
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split("::");
				int uId = Integer.parseInt(values[0]);
				if (uId == userId) {
					count++;
					oneUserCount++;
					float[] ratings = this.getRatings(uId);
					String[] genres = values[4].split("\\|");
					float prediction = this.getPredictedRating(ratings, genres);
					float actualRating = Float.parseFloat(values[2]);
					//System.out.println(actualRating + " - "+ prediction + " - " + (Math.pow(actualRating - prediction,2)));
					evaluation += Math.pow(actualRating - prediction, 2);
				} else if (uId > userId) {
					return evaluation;
					//return (float) Math.sqrt(evaluation/count);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return evaluation;
		//return (float) Math.sqrt(evaluation/count);
	}
	
	/*
	 * Returns the user profile vector of a particular user
	 * Input - User 1
	 * Output - 5.0,5.0,5.0,0,5.0,5.0,0,5.0,5.0,0,0,5.0,0,5.0,5.0,5.0,5.0,0 
	 */
	public float[] getRatings (int userId) {
		float[] ratings = new float[18];
		File file = new File("userProfiles");
		try {
			Scanner sc = new Scanner(file);
			int uId = 1;
			while (sc.hasNextLine()) {
				if (uId == userId) {
					String[] values = sc.nextLine().split(",");
					for (int i=0; i<values.length;i++)
						ratings[i] = Float.parseFloat(values[i]);
					break;
				} else {
					sc.nextLine();
					uId++;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ratings;
	}
	
	public void evaluateErms() {
		File userProfiles = new File("userProfiles");
		File ratings = new File("Combine.dat");
		File ermsFile = new File("ermsFile");
		int user = 1;
		int totalRatings = 0;
		float[] ratingForUser = new float[18];
		try {
			Scanner userProfileScanner = new Scanner(userProfiles);
			Scanner ratingsScanner = new Scanner(ratings);
			PrintWriter pw = new PrintWriter(ermsFile);
			String[] values = userProfileScanner.nextLine().split(",");
			float predictedRating;
			float diffSquared;
			float sum = 0;
			float overAllSum = 0;
			int oneUserCount = 0;
			for (int i=0; i<values.length;i++)
				ratingForUser[i] = Float.parseFloat(values[i]);
			
			while(ratingsScanner.hasNextLine()) {
				String[] ratingValues = ratingsScanner.nextLine().split("::"); // 3::110::4.5::1136075500::Action|Drama|War
				int uId = Integer.parseInt(ratingValues[0]);
				float actualRating = Float.parseFloat(values[2]);
				if (uId == user) {
					predictedRating = getPredictedRating(ratingForUser,ratingValues[4].split("\\|"));
					diffSquared = (float) Math.pow((actualRating - predictedRating),2);
					sum += diffSquared;
					overAllSum += diffSquared;;
					oneUserCount++;
					totalRatings++;
				}else {
					pw.println(user + ":" + Math.sqrt(sum/oneUserCount));
					pw.flush();
					values = userProfileScanner.nextLine().split(",");
					for (int i=0; i<values.length;i++)
						ratingForUser[i] = Float.parseFloat(values[i]);
					user++;
					oneUserCount = 1;
					predictedRating = getPredictedRating(ratingForUser,ratingValues[4].split("\\|"));
					diffSquared = (float) Math.pow((actualRating - predictedRating),2);
					sum = 0;
					sum += diffSquared;
					overAllSum += diffSquared;;
					totalRatings++;
				}
			}
			pw.println("Final erms = " + overAllSum + ":" +totalRatings + ":" +Math.sqrt(overAllSum/totalRatings));
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Evaluation e = new Evaluation();
		//e.evaluateErms();
		float sum = 0;
		File ermsFile = new File("ermsFile");
		float rating4 = 0, rating4n6 = 0, rating6n8 = 0, rating8 = 0;
		try {
			Scanner sc = new Scanner(ermsFile);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split(":");
				float rating = Float.parseFloat(values[1]);
				if (rating < 0.4)
					rating4 += 1;
				else if (rating > 0.4 && rating <= 0.6)
					rating4n6 += 1;
				else if (rating > 0.6 && rating <= 0.8)
					rating6n8 += 1;
				else if (rating > 0.8)
					rating8 += 1;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		//System.out.println(rating4 + rating4n6 + rating6n8 + rating8);
		System.out.println(rating4/71567 + "\n" + rating4n6/71567 + "\n" + rating6n8/71567 + "\n" + rating8/71567);
	}
}
