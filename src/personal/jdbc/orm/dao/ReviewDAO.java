package personal.jdbc.orm.dao;

import personal.jdbc.orm.model.Customer;
import personal.jdbc.orm.model.Movie;
import personal.jdbc.orm.model.Review;

import java.sql.*;
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
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error inserting movie", e);
        }
    }
}
