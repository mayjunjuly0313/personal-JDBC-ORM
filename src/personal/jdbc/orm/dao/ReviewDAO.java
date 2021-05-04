package personal.jdbc.orm.dao;

import personal.jdbc.orm.model.Customer;
import personal.jdbc.orm.model.Movie;
import personal.jdbc.orm.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReviewDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Review> cache;

    public ReviewDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void createTable(Connection conn) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE Review (");
        sb.append(" RId int,");
        sb.append(" Rating int NOT NULL,");
        sb.append(" Comment varchar(1000) NOT NULL,");
        sb.append(" CustomerId int NOT NULL,");
        sb.append(" MovieId int NOT NULL,");
        sb.append(" primary key (RId),");
        sb.append(" foreign key (CustomerId) references Customer on delete cascade,");
        sb.append(" foreign key (MovieId) references Movie on delete cascade") ;
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Review findByReviewId(int rId){
        if(cache.containsKey(rId)){
            return cache.get(rId);
        }

        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT r.RId, r.Rating, r.Comment, r.CustomerId, r.MovieId");
            sb.append(" FROM Review r");
            sb.append(" WHERE m.RId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, rId);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return null;
            }

            int rating = rs.getInt("Rating");
            String comment = rs.getString("Comment");
            int customerId = rs.getInt("CustomerId");
            int movieId = rs.getInt("MovieId");
            rs.close();

            Customer customer = dbm.getCustomerById(customerId);
            Movie movie = dbm.getMovieById(movieId);

            Review review = new Review(this, rId, rating, comment, customer, movie);
            return review;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error finding movie", e);
        }
    }

    public Review insert(int rId, int rating, String comment, Customer customer, Movie movie){
        try{
            if(getReviewByCIdAndMId(customer.getId(), movie.getId()) == null){
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT into Review(RId, Rating, Comment, CustomerId, MovieId)");
                sb.append(" values(?, ?, ?, ?, ?)");

                PreparedStatement pstmt = conn.prepareStatement(sb.toString());
                pstmt.setInt(1, rId);
                pstmt.setInt(2, rating);
                pstmt.setString(3, comment);
                pstmt.setInt(4, customer.getId());
                pstmt.setInt(5, movie.getId());
                pstmt.executeUpdate();

                Review review = new Review(this, rId, rating, comment, customer, movie);
                return review;
            }
            else{
                return null;
            }
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error inserting movie", e);
        }
    }

    public Collection<Review> getReviewsByCustomerId(int customerId){
        try{
            Collection<Review> reviewsOfCustomer = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT r.RId, r.Rating, r.Comment, r.MovieId");
            sb.append(" FROM Review r");
            sb.append(" WHERE r.CustomerId = ?");

            PreparedStatement pstmt  = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int rId = rs.getInt("RId");
                int rating = rs.getInt("Rating");
                String comment = rs.getString("Comment");
                int movieId = rs.getInt("MovieId");

                Customer customer = dbm.getCustomerById(customerId);
                Movie movie = dbm.getMovieById(movieId);

                Review review = new Review(this, rId, rating, comment, customer, movie);
                reviewsOfCustomer.add(review);
            }
            return reviewsOfCustomer;
        }
        catch(SQLException e) {
            dbm.cleanup();
            throw new RuntimeException(String.format("error getting reviews of the custumer {}, msg: ", customerId, e));
        }
    }

    public Review getReviewByCIdAndMId(int customerId, int movieId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT r.RId, r.Rating, r.Comment");
            sb.append(" FROM Review r");
            sb.append(" WHERE r.CustomerId = ?, r.MovieId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int rId = rs.getInt("RId");
                int rating = rs.getInt("Rating");
                String comment = rs.getString("Comment");

                Customer customer = dbm.getCustomerById(customerId);
                Movie movie = dbm.getMovieById(movieId);

                Review review = new Review(this, rId, rating, comment, customer, movie);
                return review;
            } else {
                return null;
            }
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException(
                    String.format("error getting review of the movie {}, cus {}, msg: {}", movieId, customerId, e)
            );
        }
    }

    public double getAvgRatingOfMoive(int movieId){
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT AVG(CAST (r.Rating AS DOUBLE PRECISION))");
            sb.append(" FROM Review r");
            sb.append(" WHERE r.MovieId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, movieId);;
            ResultSet rs = pstmt.executeQuery();

            double avgRating = rs.getDouble("1");
            System.out.println(String.format("avgRating: {}", avgRating));
            return avgRating;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException(
                    String.format("error getting avgRating of the movie {}, msg: {}", movieId, e)
            );
        }
    }

    public int getNumReviewsOfMoive(int movieId){
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(r.RId)");
            sb.append(" FROM Review r");
            sb.append(" WHERE r.MovieId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, movieId);;
            ResultSet rs = pstmt.executeQuery();

            int numReviews = rs.getInt("1");
            System.out.println(String.format("numReviews: {}", numReviews));
            return numReviews;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException(
                    String.format("error getting avgRating of the movie {}, msg: {}", movieId, e)
            );
        }
    }
}
