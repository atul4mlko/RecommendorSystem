package com.recommendations;

import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Scanner;

public class Recommendations {
  // float[][] ratings = new float[4000][4000];

  public void loadData() {
    File file = new File(
        "Combine.dat");
    File profiles = new File(
        "userProfiles.dat");
    // 1::122::5::838985046::Comedy|Romance
    Scanner sc;
    PrintWriter pw;
    try {
      sc = new Scanner(file, "UTF-8");
      pw = new PrintWriter(profiles);
      float[] userProfile = new float[36];
      int prev = 1;
      int uId = -1;
      int userCounter = 1;
      while (sc.hasNext()) {
        String line = sc.nextLine();
        String[] v = line.split("::");
        uId = Integer.parseInt(v[0]);
        int mId = Integer.parseInt(v[1]);
        float rating = Float.parseFloat(v[2]);
        String genreList = v[4];
        String[] genre = genreList.split("\\|");
        if (uId != prev) {
          // pw.print(prev + ": ");
          for (int j = 0; j < userProfile.length; j += 2) {
            if (userProfile[j + 1] != 0)
              pw.print(Float.toString(userProfile[j]
                    / userProfile[j + 1])
                  + ",");
            else
              pw.print("0,");
          }
          pw.println();
          pw.flush();
          Arrays.fill(userProfile, 0);
          while (userCounter != uId - 1) {
            // pw.print(++userCounter + ": ");
            ++userCounter;
            for (int j = 0; j < userProfile.length; j += 2)
              pw.print(Float.toString(userProfile[j]) + ",");
            pw.println();
            // userCounter++;
          }
          userCounter++;
        }
        for (int i = 0; i < genre.length; i++) {
          int index = this.getIndex(genre[i]);
          if (index >= 0) {
            userProfile[index] += rating;
            userProfile[++index] += 1;
          }
        }
        prev = uId;
      }
      for (int j = 0; j < userProfile.length; j += 2) {
        // pw.print(uId + ": ");
        if (userProfile[j + 1] != 0){
          //System.out.println(Float.toString(userProfile[j] / userProfile[j + 1]));
          pw.print(Float.toString(userProfile[j] / userProfile[j + 1])+ ",");
        }
        else
          pw.print("0,");
      }
      pw.println();
      pw.flush();
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    // System.out.print(this.ratings[2][376]);
  }

  /*
   * Action 0 Adventure 1 Animation 2 Children's 3 Comedy 4 Crime 5
   * Documentary 6 Drama 7 Fantasy 8 Film-Noir 9 Horror 10 Musical 11 Mystery
   * 12 Romance 13 Sci-Fi 14 Thriller 15 War 16 Western 17
   */
  public int getIndex(String genre) {
    if (genre.equals("Action")) {
      return 0;
    } else if (genre.equals("Adventure")) {
      return 2;
    } else if (genre.equals("Animation")) {
      return 4;
    } else if (genre.equals("Children")) {
      return 6;
    } else if (genre.equals("Comedy")) {
      return 8;
    } else if (genre.equals("Crime")) {
      return 10;
    } else if (genre.equals("Documentary")) {
      return 12;
    } else if (genre.equals("Drama")) {
      return 14;
    } else if (genre.equals("Fantasy")) {
      return 16;
    } else if (genre.equals("Film-Noir")) {
      return 18;
    } else if (genre.equals("Horror")) {
      return 20;
    } else if (genre.equals("Musical")) {
      return 22;
    } else if (genre.equals("Mystery")) {
      return 24;
    } else if (genre.equals("Romance")) {
      return 26;
    } else if (genre.equals("Sci-Fi")) {
      return 28;
    } else if (genre.equals("Thriller")) {
      return 30;
    } else if (genre.equals("War")) {
      return 32;
    } else if (genre.equals("Western")) {
      return 34;
    } else
      return -1;
  }

  /*
   * //Correlation matrix
   */
  public void CalculatePearsonsCorr() {
    File profiles = new File("userProfiles.dat");
    Scanner sc, sc1,sc2;
    float [] meanVar = new float[71568];
    int usrID = 0; 
    try {
      sc = new Scanner(profiles);
      while (sc.hasNext()){
        String values[] = sc.next().split(",");
        float mean = 0;
        for(int i = 0; i<values.length; i++) {
          if (!values[i].equals("")) {
            //System.out.println(values[i]);
            mean += Float.parseFloat(values[i]);
          }
        }
        mean = mean / 18;
        meanVar[usrID] = mean;
      }//end of while
      //Task: we need to hav only 5 users get written instead of all the users
      int i = 0;
      //float [] intermediateCorr = new float[71568];
      File f = new File("Correlation.txt");
      PrintWriter pw = new PrintWriter(f);	
      sc1 = new Scanner(profiles);
      //This while runs for all the rows.
      while(sc1.hasNext()){
        sc2 = new Scanner(profiles);
        int j = 0;
        String values[] = sc1.next().split(",");
        while(sc2.hasNext()) {	
          double numerator = 0;
          double denominator = 0;       
          double leftDeno = 0;
          double rightDeno = 0;	
          String values1[] = sc2.next().split(",");
          if( i == j) {
            float coorelation = 1;
            pw.print(coorelation+" ");
            //intermediateCorr[j] = coorelation;
            j++;
            continue;
          }
          //genre loop
          for(int k = 0; k < values.length; k++)
          {
            //since on the last row we have every columns mean value.
            float Xdiff = 0, Ydiff = 0;
            if (!values[k].equals("") && !values1[k].equals("")) {
              Xdiff = (Float.parseFloat(values[k]) - meanVar[i]);
              Ydiff = (Float.parseFloat(values1[k]) - meanVar[j]);
            }
            numerator += (Xdiff * Ydiff);
            leftDeno += Math.pow(Xdiff,2);
            rightDeno += Math.pow(Ydiff,2);           
          }
          denominator = (Math.sqrt(leftDeno) * Math.sqrt(rightDeno));
          float coorelation = 0;
          if(denominator != 0)
            coorelation = (float) (numerator / denominator);
          pw.print(coorelation+" "); //printing the correlation value between a single user and the rest
          //intermediateCorr[j] = coorelation;
          j++;
          continue;
        }//end of while sc2
        i++;
        pw.println();
      }//end of sc1 while

      //writing only top five values in the file.
      //instead of correlation values, indexes to be noted
      /*Arrays.sort(intermediateCorr);
        int temp = intermediateCorr.length - 1;
        int tempCounter = 0;
        while(tempCounter < 5){
        pw.print(intermediateCorr[temp--]+" ");
        tempCounter++;
        }	*/

      pw.println();
      pw.flush();
      pw.close();
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    }	

  }

  /*
   * public float calculateCosine(int u1, int u2) { float sum_xy = 0; float
   * squareSum_user1 = 0; float squareSum_user2 = 0; float numerator = 0;
   * float denominator = 0; for (int i=0; i<ratings[u1].length; i++) { sum_xy
   * += ratings[u1][i] * ratings[u2][i]; squareSum_user1 +=
   * Math.pow(ratings[u1][i], 2); squareSum_user2 += Math.pow(ratings[u2][i],
   * 2); } numerator = sum_xy; denominator = (float)
   * Math.sqrt(squareSum_user1) * (float) Math.sqrt(squareSum_user2); if
   * (denominator == 0) return 0; else return numerator/denominator; }
   * 
   * public float[] getNeighbours (int user) { float[] neighbours = new
   * float[4000]; for (int i=0; i<ratings.length; i++) { if (i != user) {
   * neighbours[i] = this.calculateCosine(user,i); } }
   * //Arrays.sort(neighbours); return neighbours; }
   */

  public void topFiveUsers() {
    try	
    {
      File f = new File("Correlation.txt");
      File f1 = new File("TopFiveUsers.dat");
      PrintWriter pw = new PrintWriter(f1);
      Scanner s = new Scanner(f);

      while(s.hasNext()) {
        String line = s.nextLine();
        String coorelationValues[] = line.split(" ");
        float[] topValue = new float[5];
        float[] topIndex = new float[5];
        //initialize
        for(int i = 0; i < 5; i++) {
          topValue[i] = -1;
          topIndex[i] = -1;
        }

        for(int value = 0; value < coorelationValues.length; value++) {
          float corrValue = Float.parseFloat(coorelationValues[value]);	

          for(int j = 0; j < 5; j++) {
            if(corrValue > topValue[j] && j < 4) {
              int temp = (j == 0) ? (j+1) :  j;
              for(int k = 4; k >= temp; k--){
                topValue[k] = topValue[k-1];
                topIndex[k] = topIndex[k-1];
              }
              topValue[j] = corrValue;
              topIndex[j] = value;//index or the user id that needs to be recorded
              break;
            }
            if(corrValue > topValue[j] && j == 4) {
              topValue[j] = corrValue;
              topIndex[j] = value;
              break;
            }
            if(corrValue < topValue[j]) continue;
          }//end of for j -> 5	

        }//end of value for loop

        //write the top Users to the file
        for(int i = 0; i < 5; i++)	
          pw.print(topIndex[i]+" ");
        pw.println();
        pw.flush();
      }//end of while loop
      pw.close();
    }catch(Exception e) {
      e.printStackTrace();
    }		
  }//end of topFiveUsers function

 public void retrieveTopFiveMovies() {
    try {
      File topFive = new File("TopFiveUsers.dat");
	  Scanner s = new Scanner(topFive);
	  String [] users = new String[71567];
	  int z = 30000;
	  while(s.hasNext()){
		String temp = new String(s.nextLine());
		users[z++] = temp;
	  }

	  File f = new File("User_Movie.dat");
      PrintWriter pw = new PrintWriter(f);
	       for(int k = 30000; k<users.length; k++) {
        //UserID
        String [] userIDs = users[k].split(" ");//top five users
        pw.print(k+" ");

        for(int i = 1; i<userIDs.length; i++) {
          //Rating.dat -> userID::MovieId::Rating 
 
          float v = Float.parseFloat(userIDs[i]);
			    int fileNumber = (int) (v/1000);
			    fileNumber++;
		  	  File Rfile = new File(fileNumber+".dat");
			    Scanner sFile = new Scanner(Rfile);
			    while(sFile.hasNext()) {
            //search for similar userID
        		
				      String [] rating = sFile.nextLine().split("::");
	           	float u = Float.parseFloat(rating[0]); 
				      float rate = Float.parseFloat(rating[2]);
   				    if(u!=v){
				     	  continue;
             	}
				      else{
					      if(u == v && rate > 3){
               			pw.print(rating[1]+" ");
            		}
	         	  }
            }//end of while
          }//end of for
        pw.println();
        pw.flush(); 
       }//end of for k
        pw.close();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }//end of retrieveTopFiveMovies
  public void randomAccess() {
	try{
	RandomAccessFile file = new RandomAccessFile("ratings.dat", "r");
	file.seek(23*1);
	System.out.println(file.readLine());
	System.out.println(file.readLine());
	file.close();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
 }  
  public void movieRecommendations() {
    //from the User_Movie.dat file we need to read the movieIDS and retrieve the movie names,
    //for each user and then maybe store them in an array/list or file
    //And whenever a userID is entered we need to return the listing from that
   try{
    File f = new File("User_Movie.dat");
    Scanner s = new Scanner(f);
    File movies = new File("movies.dat");
    File movR = new File("RecommendedMovies.dat");
    PrintWriter pw = new PrintWriter(movR);
    while(s.hasNext()) {
      String line = s.nextLine();//read movieIDs
      String [] info = line.split(" ");
      pw.print(info[0]+ " ");//print user ID
      Scanner movS = new Scanner(movies);//movieID::movieName::Genre
      for(int i = 1; i < info.length; i++) {
        while(movS.hasNext()){
          String m[] = movS.nextLine().split("::");
          if(info[i].equals(m[0])){
            //write in a file or insert into a structure m[1]-> movieName
            pw.print(m[1]+" ");//enter the movie name
          }//end of if
        }//end of while
      }
      pw.println();
      pw.flush();
    }//end of while
    pw.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  } //end of function
public void getTopFive() {
	try{
	File f = new File("TopFiveUsers.dat");
	Scanner s = new Scanner(f);
	String [] users = new String[71568];
	int i = 0;
	while(s.hasNext()){
		String temp = new String(s.nextLine());
	//	System.out.println("Incoming value "+temp);
		users[i++] = temp;
	//	break;
	}
	System.out.println("Printing 71567 value ="+users[71567]);
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
  public static void main(String[] args) {
      Recommendations r = new Recommendations();
      // File fp = new
      // File("/home/atul/ParallelProject/ml-10M100K/splits/ra.train");
      //r.loadData();
      //r.CalculatePearsonsCorr();
      //r.topFiveUsers();
		//r.getTopFive();
      r.retrieveTopFiveMovies();
		//r.randomAccess();
		// r.movieRecommendations();
      // System.out.println(r.calculateCosine(1,2));
      // System.out.println(r.getNeighbours(1)[3]);
    }
  }
