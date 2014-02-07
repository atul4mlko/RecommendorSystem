package com.recommendations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

public class MovieBasedRecommendation {
	private final int numOfMovies = 65133;
	private final int numOfGenres = 18;
	private boolean[][] movies = new boolean[numOfMovies][numOfGenres];
	private HashMap<Integer,HashMap<Integer,Integer>> rMovies = new HashMap<Integer,HashMap<Integer,Integer>>();
	
	/*
	 * Action 0 Adventure 1 Animation 2 Children's 3 Comedy 4 Crime 5
	 * Documentary 6 Drama 7 Fantasy 8 Film-Noir 9 Horror 10 Musical 11 Mystery
	 * 12 Romance 13 Sci-Fi 14 Thriller 15 War 16 Western 17
	 */
	public void generateMovieProfile() {
		// 1::Toy Story (1995)::Adventure|Animation|Children|Comedy|Fantasy
		File file = new File("movies.dat");
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split("::");
				String[] genre = values[2].split("\\|");
				int i = Integer.parseInt(values[0]) - 1;
				for (int j=0; j<genre.length; j++) {
					if (genre[j].equals("Action")) {
						movies[i][0] = true;
					} else if (genre[j].equals("Adventure")) {
						movies[i][1] = true;
					} else if (genre[j].equals("Animation")) {
						movies[i][2] = true;
					} else if (genre[j].equals("Children")) {
						movies[i][3] = true;
					} else if (genre[j].equals("Comedy")) {
						movies[i][4] = true;
					} else if (genre[j].equals("Crime")) {
						movies[i][5] = true;
					} else if (genre[j].equals("Documentary")) {
						movies[i][6] = true;
					} else if (genre[j].equals("Drama")) {
						movies[i][7] = true;
					} else if (genre[j].equals("Fantasy")) {
						movies[i][8] = true;
					} else if (genre[j].equals("Film-Noir")) {
						movies[i][9] = true;
					} else if (genre[j].equals("Horror")) {
						movies[i][10] = true;
					} else if (genre[j].equals("Musical")) {
						movies[i][11] = true;
					} else if (genre[j].equals("Mystery")) {
						movies[i][12] = true;
					} else if (genre[j].equals("Romance")) {
						movies[i][13] = true;
					} else if (genre[j].equals("Sci-Fi")) {
						movies[i][14] = true;
					} else if (genre[j].equals("Thriller")) {
						movies[i][15] = true;
					} else if (genre[j].equals("War")) {
						movies[i][16] = true;
					} else if (genre[j].equals("Western")) {
						movies[i][17] = true;
					}
				}
				i++;
			} // end of while
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getRelationNumber(int movId1, int movId2) {
		int num = 0;
		for (int i=0; i<numOfGenres; i++) {
			if (this.movies[movId1][i] && this.movies[movId2][i])
				num++;
		}
		return num;
	}
	
	public void generateMoviesRelationsData() {
		File file = new File("similarMovies");
		int[] relationNumbers = new int[numOfMovies];
		try {
			PrintWriter pw = new PrintWriter(file);
			for (int i=0; i<numOfMovies; i++) {
				for (int j=0; j<numOfMovies; j++) {
					if (i != j)
						relationNumbers[j] = this.getRelationNumber(i, j);
					else
						relationNumbers[j] = 0;
					//pw.print(relationNumbers[i] + " ");
				}
				pw.print((i+1) + ":");
				for (int k=0; k<numOfMovies; k++) {
					if (relationNumbers[k] >=3 )
						pw.print((k+1) + "-" + relationNumbers[k] + " ");
				}
				pw.println();
				pw.flush();
				Arrays.fill(relationNumbers, 0);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printMovieVector(int movId) {
		for (int i=0; i< numOfGenres; i++)
			System.out.print(this.movies[movId][i] + " ");
	}
	
	public HashMap<Integer,Integer> getSimilarMovies(int movId) {
		File file = new File("similarMovies"); // 65091:81-3 247-3 293-3 459-3
		HashMap<Integer,Integer> recommendedMovies = new HashMap<Integer,Integer>();
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split(":");
				int mId = Integer.parseInt(values[0]);
				if (mId == movId && values.length > 1) {
					String[] movies = values[1].split(" ");
					for (int i=0; i<movies.length; i++) {
						String[] movie = movies[i].split("-");
						recommendedMovies.put(Integer.parseInt(movie[0]), Integer.parseInt(movie[1]));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (recommendedMovies.isEmpty())
			return null;
		else
			return recommendedMovies;
	}
	
	public String getTopFiveRelatedMovies(int mId) {
		HashMap<Integer,Integer> recommendedMovies;
		if (rMovies.containsKey(mId)) {
			recommendedMovies = rMovies.get(mId);
		} else {
			recommendedMovies = getSimilarMovies(mId);
			rMovies.put(mId, recommendedMovies);
		}
		ArrayList<Integer> topFive = new ArrayList<Integer>(3);
		if (recommendedMovies == null)
			return "";
		for (int i=6; i>=2; i--) {
			for (Entry<Integer, Integer> mov : recommendedMovies.entrySet()) {
				if (mov.getValue() == i && topFive.size() < 4){
					topFive.add(mov.getKey());
				}
			}
		}
		return topFive.toString();
	}
	
	public void generateUserMoviesList() {
		File file = new File("Combine.dat");
		File seenMovies = new File("MoviesAlreadySeenByUser");
		try {
			Scanner sc = new Scanner(file);
			ArrayList<Integer> rating05 = new ArrayList<Integer>();
			ArrayList<Integer> rating1 = new ArrayList<Integer>();
			ArrayList<Integer> rating15 = new ArrayList<Integer>();
			ArrayList<Integer> rating2 = new ArrayList<Integer>();
			ArrayList<Integer> rating25 = new ArrayList<Integer>();
			ArrayList<Integer> rating3 = new ArrayList<Integer>();
			ArrayList<Integer> rating35 = new ArrayList<Integer>();
			ArrayList<Integer> rating4 = new ArrayList<Integer>();
			ArrayList<Integer> rating45 = new ArrayList<Integer>();
			ArrayList<Integer> rating5 = new ArrayList<Integer>();
			ArrayList<Integer> rating0 = new ArrayList<Integer>();
			int prev = 1;
			PrintWriter pw = new PrintWriter(seenMovies);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split("::"); // 1::122::5::838985046::Comedy|Romance
				int uId = Integer.parseInt(values[0]);
				int movId = Integer.parseInt(values[1]);
				if (uId != prev) {
					pw.print(prev + ":");
					for (int i=0;i<rating5.size();i++) {
						String s = getTopFiveRelatedMovies(rating5.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating5.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating45.size();i++) {
						String s = getTopFiveRelatedMovies(rating45.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating45.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating4.size();i++) {
						String s = getTopFiveRelatedMovies(rating4.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating4.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating35.size();i++) {
						String s = getTopFiveRelatedMovies(rating35.get(i)).toString();
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating35.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating3.size();i++) {
						String s = getTopFiveRelatedMovies(rating3.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating3.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating25.size();i++) {
						String s = getTopFiveRelatedMovies(rating25.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating25.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating2.size();i++) {
						String s = getTopFiveRelatedMovies(rating2.get(i));
						if (! s.isEmpty()) {
							//String s = getTopFiveRelatedMovies(rating2.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					/*for (int i=0;i<rating15.size();i++) {
						if (getTopFiveRelatedMovies(rating15.get(i)) != null) {
							String s = getTopFiveRelatedMovies(rating15.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating1.size();i++) {
						if (getTopFiveRelatedMovies(rating1.get(i)) != null) {
							String s = getTopFiveRelatedMovies(rating1.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}
					for (int i=0;i<rating05.size();i++) {
						if (getTopFiveRelatedMovies(rating05.get(i)) != null) {
							String s = getTopFiveRelatedMovies(rating05.get(i)).toString();
							pw.print(s.substring(1, s.length()-1) + ",");
						}
					}*/
					
					pw.println();
					pw.flush();
					rating5.clear();
					rating45.clear();
					rating4.clear();
					rating35.clear();
					rating3.clear();
					rating25.clear();
					rating2.clear();
					rating15.clear();
					rating1.clear();
					rating05.clear();
					rating0.clear();
				}
				float rating = Float.parseFloat(values[2]);
				if (rating == 0.5)
					rating05.add(movId);
				else if (rating == 0.5)
					rating05.add(movId);
				else if (rating == 1.0)
					rating1.add(movId);
				else if (rating == 1.5)
					rating15.add(movId);
				else if (rating == 2.0)
					rating2.add(movId);
				else if (rating == 2.5)
					rating25.add(movId);
				else if (rating == 3.0)
					rating3.add(movId);
				else if (rating == 3.5)
					rating35.add(movId);
				else if (rating == 4.0)
					rating4.add(movId);
				else if (rating == 4.5)
					rating45.add(movId);
				else if (rating == 5.0)
					rating5.add(movId);
				else
					rating0.add(movId);
				prev = uId;
			}
			pw.print(prev + ":");
			for (int i=0;i<rating5.size();i++) {
				String s = getTopFiveRelatedMovies(rating5.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating5.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating45.size();i++) {
				String s = getTopFiveRelatedMovies(rating45.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating45.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating4.size();i++) {
				String s = getTopFiveRelatedMovies(rating4.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating4.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating35.size();i++) {
				String s = getTopFiveRelatedMovies(rating35.get(i)).toString();
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating35.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating3.size();i++) {
				String s = getTopFiveRelatedMovies(rating3.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating3.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating25.size();i++) {
				String s = getTopFiveRelatedMovies(rating25.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating25.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating2.size();i++) {
				String s = getTopFiveRelatedMovies(rating2.get(i));
				if (! s.isEmpty()) {
					//String s = getTopFiveRelatedMovies(rating2.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			/*for (int i=0;i<rating15.size();i++) {
				if (getTopFiveRelatedMovies(rating15.get(i)) != null) {
					String s = getTopFiveRelatedMovies(rating15.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating1.size();i++) {
				if (getTopFiveRelatedMovies(rating1.get(i)) != null) {
					String s = getTopFiveRelatedMovies(rating1.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}
			for (int i=0;i<rating05.size();i++) {
				if (getTopFiveRelatedMovies(rating05.get(i)) != null) {
					String s = getTopFiveRelatedMovies(rating05.get(i)).toString();
					pw.print(s.substring(1, s.length()-1) + ",");
				}
			}*/
			
			pw.println();
			pw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void generateFinalRecommendations() {
		File file = new File("MoviesAlreadySeenByUser");
		File movieRec = new File("FinalMovieBasedRecommendation");
		Evaluation eval = new Evaluation();
		try {
			Scanner sc = new Scanner(file);
			PrintWriter pw = new PrintWriter(movieRec);
			while (sc.hasNextLine()) {
				String[] values = sc.nextLine().split(":");
				if (values.length > 1) {
					int uId = Integer.parseInt(values[0]);
					String[] movies = values[1].trim().split(",");
					int length = movies.length;
					int[] m = new int[length]; 
					for (int i=0; i<movies.length;i++) {
						m[i] = Integer.parseInt(movies[i].trim());
					}
					Arrays.sort(m);
					int newArray[] = new int[m.length];
				    newArray[0] = m[0];
				    int k = 1;
				    for (int i = 0; i < m.length - 1; i++) {
				        if(m[i+1] > m[i]) {
				            newArray[k] = m[i + 1];
				            k++;
				        }
				    }
				    newArray = Arrays.copyOf(newArray, k);
				    m = newArray;
					
					
					int i = 0;
					float[] profile = eval.getRatings(uId);
					File movieFile = new File("movies.dat");
					Scanner movieScanner = new Scanner(movieFile);
					pw.print(uId + "==");
					HashMap<Integer, Float> recMovies = new HashMap<Integer, Float>();
					HashMap<Integer,Movie> movieList = new HashMap<Integer,Movie>();
					while (movieScanner.hasNextLine() && i < m.length) {
						String[] vals = movieScanner.nextLine().split("::");
						int mId = Integer.parseInt(vals[0]);
						if (mId == m[i]) {
							String[] genres = vals[2].split("\\|");
							float predictedRating = eval.getPredictedRating(profile, genres);
							Movie movie = new Movie(mId,vals[1],vals[2],predictedRating);
							movieList.put(mId,movie);
							recMovies.put(mId, predictedRating);
							//pw.print(mId + "=" + predictedRating + ",");
							i++;
						}
					}
					ValueComparator bvc =  new ValueComparator(recMovies);
					TreeMap<Integer, Float> sorted_map = new TreeMap<Integer, Float>(bvc);
					sorted_map.putAll(recMovies);
					
					Iterator<Integer> it = sorted_map.keySet().iterator();
				    while (it.hasNext()) {
				    	pw.print(movieList.get((Integer) it.next()) + "#");
				    }
				    
					//pw.println(sorted_map);
					pw.println();
					pw.flush();
					recMovies.clear();
					movieList.clear();
					movieScanner.close();
				}
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MovieBasedRecommendation r = new MovieBasedRecommendation();
		//System.out.println(r.getTopFiveRelatedMovies(122));
		//r.printMovieVector(122);
		//r.generateUserMoviesList();
		r.generateFinalRecommendations();
	}
}
