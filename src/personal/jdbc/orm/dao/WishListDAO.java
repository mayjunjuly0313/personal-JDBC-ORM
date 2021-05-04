package personal.jdbc.orm.dao;

import personal.jdbc.orm.model.Customer;
import personal.jdbc.orm.model.Movie;
import personal.jdbc.orm.model.Review;
import personal.jdbc.orm.model.WishList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WishListDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, WishList> cache;

    public WishListDAO(Connection conn, DatabaseManager dbm) {
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void createTable(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE WishList (");
        sb.append(" WId int,");
        sb.append(" CustomerId int NOT NULL,");
        sb.append(" MovieId int NOT NULL,");
        sb.append(" primary key (WId),");
        sb.append(" foreign key (CustomerId) references Customer on delete cascade,");
        sb.append(" foreign key (MovieId) references Movie on delete cascade") ;
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public WishList findByWishListId(int wId){
        if(cache.containsKey(wId)){
            return cache.get(wId);
        }

        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT w.WId, w.CustomerId, w.MovieId");
            sb.append(" FROM WishList w");
            sb.append(" WHERE m.WId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, wId);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return null;
            }

            int customerId = rs.getInt("CustomerId");
            int movieId = rs.getInt("MovieId");
            rs.close();

            Customer customer = dbm.getCustomerById(customerId);
            Movie movie = dbm.getMovieById(movieId);

            WishList wishList = new WishList(this, wId, customer, movie);
            return wishList;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error finding wishList", e);
        }
    }

    public WishList insert(int wId, Customer customer, Movie movie){
        try{
            if(this.getWishListByCIdAndMId(customer.getId(), movie.getId()) == null){
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT into WishList(WId, CustomerId, MovieId)");
                sb.append(" values(?, ?, ?)");

                PreparedStatement pstmt = conn.prepareStatement(sb.toString());
                pstmt.setInt(1, wId);
                pstmt.setInt(2, customer.getId());
                pstmt.setInt(3, movie.getId());
                pstmt.executeUpdate();

                WishList wishList = new WishList(this, wId, customer, movie);
                return wishList;
            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error inserting wishList", e);
        }
    }

    public Collection<WishList> getWishListsByCustomerId(int customerId){
        try{
            Collection<WishList> wishListsOfCustomer = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT w.WId, w.MovieId");
            sb.append(" FROM WishList w");
            sb.append(" WHERE w.CustomerId = ?");

            PreparedStatement pstmt  = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int wId = rs.getInt("WId");
                int movieId = rs.getInt("MovieId");

                Customer customer = dbm.getCustomerById(customerId);
                Movie movie = dbm.getMovieById(movieId);

                WishList wishList = new WishList(this, wId, customer, movie);
                wishListsOfCustomer.add(wishList);
            }
            return wishListsOfCustomer;
        }
        catch(SQLException e) {
            dbm.cleanup();
            throw new RuntimeException(String.format("error getting reviews of the custumer {}, msg: ", customerId, e));
        }
    }

    public WishList getWishListByCIdAndMId(int customerId, int movieId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT w.WId");
            sb.append(" FROM WishList w");
            sb.append(" WHERE w.CustomerId = ?, w.MovieId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int wId = rs.getInt("WId");

                Customer customer = dbm.getCustomerById(customerId);
                Movie movie = dbm.getMovieById(movieId);

                WishList wishList = new WishList(this, wId, customer, movie);
                return wishList;
            } else {
                return null;
            }
        } catch (SQLException e) {
            dbm.cleanup();
            throw new RuntimeException(
                    String.format("error getting wishlist of the movie {}, cus {}, msg: ", movieId, customerId, e)
            );
        }
    }

    public int getNumWishListsOfMoive(int movieId){
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(w.WId)");
            sb.append(" FROM WishList w");
            sb.append(" WHERE w.MovieId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, movieId);;
            ResultSet rs = pstmt.executeQuery();

            int numWishLists = rs.getInt("1");
            System.out.println(String.format("numWishLists: {}", numWishLists));
            return numWishLists;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException(
                    String.format("error getting avgRating of the movie {}, msg: {}", movieId, e)
            );
        }
    }
}
