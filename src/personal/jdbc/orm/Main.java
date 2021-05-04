package personal.jdbc.orm;

import personal.jdbc.orm.dao.DatabaseManager;
import personal.jdbc.orm.model.Customer;
import personal.jdbc.orm.model.Movie;
import personal.jdbc.orm.model.Review;
import personal.jdbc.orm.model.WishList;

import javax.xml.crypto.Data;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;


public class Main {
	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;
	// for convenience
	private static int reviewId = 0;
	private static int wishListId = 0;

	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		// must reset because of reviewId and wishListId
		resetTables(dbm);
		showDefaultMenu();
		loop: while(true){
			switch (requestString("Selection (0 to quit, 9 for menu)? ")) {
				case "0": // Quit
					break loop;

				case "1":
					resetTables(dbm);
					break;

				case "2":
					listMovies(dbm);
					break;

				case "3":
					checkMovieInfo(dbm);
					break;

				case "4":
					listCustomers(dbm);
					break;

				case "5":
					int customerId = login(dbm);
					if(customerId != -1){
						customerExperience(dbm, customerId);
					}
					showDefaultMenu();
					break;
				default:
					showDefaultMenu();
					break;
				}
		}
	}

	private static void showDefaultMenu(){
		out.println("0: Quit");
		out.println("1: Reset Tables");
		out.println("2: List of movies");
		out.println("3: Check Movie info");
		out.println("4: List of Customers");
		out.println("5: LogIn");
	}

	private static void resetTables(DatabaseManager dbm) {
		dbm.clearTables();
		dbm.commit();
		reviewId = 0;
		wishListId = 0;

		Customer brianHoward = dbm.addCustomer(1, "Brian Howard", "bhoward@depauw.edu", 1111);
		Customer seok = dbm.addCustomer(2, "Seok", "seok@depauw.edu", 1111);
		Customer jun = dbm.addCustomer(3, "Jun", "jun@depauw.edu", 1111);
		Customer hong = dbm.addCustomer(4, "Hong", "hong@depauw.edu", 1111);

		Movie ironMan = dbm.addMovie(1, "Iron Man", "image1", "Hero");
		Movie godfather = dbm.addMovie(2, "God Father", "image2", "Crime");
		Movie shawshankRedemption = dbm.addMovie(3, "The Shawshank Redemption", "image3", "Drama");
		Movie darkNight = dbm.addMovie(4, "The Dark Night", "image3", "Crime");
		Movie lordOfRheRing = dbm.addMovie(5, "The Lord of the Rings", "image4", "Adventure");
		Movie fightClub = dbm.addMovie(6, "Fight Club", "image5", "Crime");
		Movie forrestGump = dbm.addMovie(7, "Forrest Gump", "image6", "Drama");
		Movie inception = dbm.addMovie(8, "Inception", "image7", "Sci-fi");
		Movie starWars = dbm.addMovie(9, "Star Wars", "image7", "Fantasy");

		Review review1 = dbm.addReview(reviewId++, brianHoward.getId(), godfather.getId(), 4, "Great Movie");
		Review review2 = dbm.addReview(reviewId++, brianHoward.getId(), fightClub.getId(), 5, "Best Movie in my life");
		Review review3 = dbm.addReview(reviewId++, brianHoward.getId(), inception.getId(), 5, "Nolan is genius");

		Review review4 = dbm.addReview(reviewId++, seok.getId(), ironMan.getId(), 1, "a");
		Review review5 = dbm.addReview(reviewId++, seok.getId(), godfather.getId(), 2, "a");
		Review review6 = dbm.addReview(reviewId++, seok.getId(), shawshankRedemption.getId(), 3, "a");
		Review review7 = dbm.addReview(reviewId++, seok.getId(), darkNight.getId(), 4, "a");
		Review review8 = dbm.addReview(reviewId++, seok.getId(), lordOfRheRing.getId(), 5, "a");
		Review review9 = dbm.addReview(reviewId++, seok.getId(), fightClub.getId(), 2, "a");
		Review review10 = dbm.addReview(reviewId++, seok.getId(), forrestGump.getId(), 3, "a");
		Review review11 = dbm.addReview(reviewId++, seok.getId(), starWars.getId(), 4, "a");
		Review review12 = dbm.addReview(reviewId++, seok.getId(), inception.getId(), 5, "a");

		Review review13 = dbm.addReview(reviewId++, jun.getId(), ironMan.getId(), 4, "a");
		Review review14 = dbm.addReview(reviewId++, jun.getId(), godfather.getId(), 3, "a");
		Review review15 = dbm.addReview(reviewId++, jun.getId(), shawshankRedemption.getId(), 2, "a");
		Review review16 = dbm.addReview(reviewId++, jun.getId(), darkNight.getId(), 5, "a");
		Review review17 = dbm.addReview(reviewId++, jun.getId(), lordOfRheRing.getId(), 2, "a");
		Review review18 = dbm.addReview(reviewId++, jun.getId(), fightClub.getId(), 3, "a");
		Review review19 = dbm.addReview(reviewId++, jun.getId(), forrestGump.getId(), 1, "a");
		Review review20 = dbm.addReview(reviewId++, jun.getId(), starWars.getId(), 2, "a");
		Review review21 = dbm.addReview(reviewId++, jun.getId(), inception.getId(), 4, "a");

		Review review22 = dbm.addReview(reviewId++, hong.getId(), ironMan.getId(), 2, "a");
		Review review23 = dbm.addReview(reviewId++, hong.getId(), godfather.getId(), 1, "a");
		Review review24 = dbm.addReview(reviewId++, hong.getId(), shawshankRedemption.getId(), 4, "a");
		Review review25 = dbm.addReview(reviewId++, hong.getId(), darkNight.getId(), 3, "a");
		Review review26 = dbm.addReview(reviewId++, hong.getId(), lordOfRheRing.getId(), 2, "a");
		Review review27 = dbm.addReview(reviewId++, hong.getId(), fightClub.getId(), 5, "a");
		Review review28 = dbm.addReview(reviewId++, hong.getId(), forrestGump.getId(), 2, "a");
		Review review29 = dbm.addReview(reviewId++, hong.getId(), starWars.getId(), 5, "a");
		Review review30 = dbm.addReview(reviewId++, hong.getId(), inception.getId(), 3, "a");
		out.println("reviewId:" + reviewId);
		WishList wish1 = dbm.addWishList(wishListId++, brianHoward.getId(), lordOfRheRing.getId());
		WishList wish2 = dbm.addWishList(wishListId++, brianHoward.getId(), forrestGump.getId());
		WishList wish3 = dbm.addWishList(wishListId++, brianHoward.getId(), starWars.getId());

		dbm.commit();
	}

	private static int login(DatabaseManager dbm){
		int id = requestInt("select your id number");
		int pw = requestInt("type in your password (default is 1111)");
		if(dbm.customerAuthentication(id, pw)){
			out.println("Login Success");
			return id;
		}
		else{
			out.println("Wrong ID or PW. Try again.");
			return -1;
		}
	}

	private static void customerExperience(DatabaseManager dbm, int customerId){
		showCustomerMenu();
		loop: while(true){
			switch (requestString("Selection (0 to Sign out, 9 for menu)? ")) {
				case "0":
					break loop;

				case "1":
					listMovies(dbm);
					break;

				case "2":
					checkMovieInfo(dbm);
					break;

				case "3":
					showCustomerReviews(dbm, customerId);
					break;

				case "4":
					showCustomerWishLists(dbm, customerId);
					break;

				case "5":
					showCustomerReviewOnMovie(dbm, customerId);
					break;

				case "6":
					addMovieReview(dbm, customerId);
					break;

				case "7":
					addMovieToWishList(dbm, customerId);
					break;

				default:
					showCustomerMenu();
					break;
			}
		}
	}

	private static void showCustomerMenu(){
		out.println("0: Sign out");
		out.println("1: List of movies");
		out.println("2: Check Movie info");
		out.println("3: My Review List");
		out.println("4: My Wish list");
		out.println("5: Check my review on a movie");
		out.println("6: Add review to movie");
		out.println("7: Add movie to wish list");
	}

	private static void listMovies(DatabaseManager dbm){
		out.printf("%-3s %-30s %-10s %-6s\n", "Id", "Name", "Catergory", "Image");
		out.println("----------------------------");

		for(Movie m : dbm.getAllMovie()){
			out.printf("%-3d %-30s %-10s %-6s\n", m.getId(), m.getName(), m.getCategory(), m.getImageUrl());
		}

		dbm.commit();
	}

	private static void listCustomers(DatabaseManager dbm){
		out.printf("%-3s %-10s\n", "Id", "Nickname");
		out.println("----------------------------");

		for(Customer c : dbm.getAllCustomer()){
			out.printf("%-3d %-10s\n", c.getId(), c.getNickname());
		}

		dbm.commit();
	}

	private static void checkMovieInfo(DatabaseManager dbm){
		int movieId = requestInt("select movie Id");
		out.printf("%-3s %-30s %-10s %-10s %-20s %-20s %-20s\n",
				"Id", "Name", "Catergory", "Image", "Average Rating", "Number of Reviews", "Number of Wishlists");
		out.println("----------------------------");
		Movie movie = dbm.getMovieById(movieId);
		if(movie != null){
			double avgRating = dbm.getAvgRatingOfMovie(movieId);
			int numReviews = dbm.getNumReviewOfMovie(movieId);
			int numWishLists = dbm.getNumWishListOfMovie(movieId);
			out.printf("%-3d %-30s %-10s %-10s %-20f %-20d %-20d\n",
					movie.getId(), movie.getName(), movie.getCategory(), movie.getImageUrl(),
					avgRating, numReviews,numWishLists);
		}
		else{
			out.println("Wrong movie Id");
		}
		dbm.commit();
	}

	private static void showCustomerReviews(DatabaseManager dbm, int customerId){
		out.printf("%-10s %-10s %-30s %-10s %-20s\n",
				"ReviewId", "MovieId", "Movie Name", "My Rating", "My Comment");
		out.println("----------------------------");

		for(Review r : dbm.getReviewsByCustomerId(customerId)){
			Movie movie = r.getMovie();
			out.printf("%-10d %-10d %-30s %-10d %-20s\n",
					r.getId(), movie.getId(), movie.getName(), r.getRating(), r.getComment());
		}

		dbm.commit();
	}

	private static void showCustomerWishLists(DatabaseManager dbm, int customerId){
		out.printf("%-10s %-10s %-30s\n",
				"WishId", "MovieId", "Movie Name");
		out.println("----------------------------");

		for(WishList w : dbm.getWishListsByCustomerId(customerId)){
			Movie movie = w.getMovie();
			out.printf("%-10d %-10d %-300s\n",
					w.getId(), movie.getId(), movie.getName());
		}

		dbm.commit();
	}

	private static void showCustomerReviewOnMovie(DatabaseManager dbm, int customerId){
		int movieId = requestInt("select movie Id");
		out.printf("%-10s %-10s %-30s %-10s %-20s\n",
				"ReviewId", "MovieId", "Movie Name", "My Rating", "My Comment");
		out.println("----------------------------");

		Review review = dbm.getReviewsByCIdAndMId(customerId, movieId);
		if(review != null){
			Movie movie = review.getMovie();
			out.printf("%-10d %-10d %-30s %-10d %-20s\n",
					review.getId(), movie.getId(), movie.getName(), review.getRating(), review.getComment());
		}
		else{
			out.println("No review found.");
		}
		dbm.commit();
	}

	private static void addMovieReview(DatabaseManager dbm, int customerId){
		int movieId = requestInt("type movie Id to write review");
		int rating = requestInt("type your rating (1 - 5 integer)");
		String comment = requestString("type your comment");

		Review review = dbm.addReview(reviewId, customerId, movieId, rating, comment);
		if(review != null){
			out.println("successfully added review");
			reviewId++;
		}
		else{
			out.println("wrong movie Id OR you have already wrote review for this movie");
		}
		dbm.commit();
	}

	private static void addMovieToWishList(DatabaseManager dbm, int customerId){
		int movieId = requestInt("type movie Id to add in wish list");

		WishList wishlist = dbm.addWishList(wishListId, customerId, movieId);
		if(wishlist != null){
			out.println("successfully added the movie to your list");
			wishListId++;
		}
		else{
			out.println("wrong movie Id OR the movie is already in your list");
		}
		dbm.commit();
	}

	private static String requestString(String prompt) {
		out.print(prompt);
		out.flush();
		return in.nextLine();
	}

	private static int requestInt(String prompt) {
		out.print(prompt);
		out.flush();
		int result = in.nextInt();
		in.nextLine();
		return result;
	}


}


