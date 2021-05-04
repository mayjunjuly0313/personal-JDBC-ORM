package personal.jdbc.orm.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDriver;
import personal.jdbc.orm.model.Customer;
import personal.jdbc.orm.model.Movie;
import personal.jdbc.orm.model.Review;
import personal.jdbc.orm.model.WishList;

public class DatabaseManager {
    private Driver driver;
    private Connection conn;
    private CustomerDAO customerDAO;
    private MovieDAO movieDAO;
    private ReviewDAO reviewDAO;
    private WishListDAO wishListDAO;

    private final String url = "jdbc:derby:db/drinksReviewApp";

    public DatabaseManager(){
        driver = new EmbeddedDriver();

        Properties prop = new Properties();
        prop.put("create", "false");

        try {
            conn = driver.connect(url, prop);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            try {
                prop.put("create", "true");
                conn = driver.connect(url, prop);
                conn.setAutoCommit(false);
                createTables(conn);
            } catch (SQLException e2) {
                throw new RuntimeException("cannot connect to database", e2);
            }
        }
        this.customerDAO = new CustomerDAO(conn, this);
        this.movieDAO = new MovieDAO(conn, this);
        this.reviewDAO = new ReviewDAO(conn, this);
        this.wishListDAO = new WishListDAO(conn, this);
    }

    private void createTables(Connection conn) throws SQLException {
        CustomerDAO.createTable(conn);
        MovieDAO.createTable(conn);
        ReviewDAO.createTable(conn);
        WishListDAO.createTable(conn);

        conn.commit();
    }

    public Customer addCustomer(int cId, String nickname, String email, int hashedPassword){
        return customerDAO.insert(cId, nickname, email, hashedPassword);
    }

    public Movie addMovie(int mId, String name, String imageUrl, String category){
        return movieDAO.insert(mId, name, imageUrl, category);
    }

    public Review addReview(int rId, int cId, int mId, int rating, String comment){
        Customer customer = customerDAO.findByCustomerId(cId);
        Movie movie = movieDAO.findByMovieId(mId);
        if( customer != null && movie != null){
            return reviewDAO.insert(rId, rating, comment, customer, movie);
        }
        else{
            return null;
        }
    }

    public WishList addWishList(int wId, int cId, int mId){
        Customer customer = customerDAO.findByCustomerId(cId);
        Movie movie = movieDAO.findByMovieId(mId);
        if( customer != null && movie != null){
            return wishListDAO.insert(wId, customer, movie);
        }
        else{
            return null;
        }
    }

    public boolean customerAuthentication(int customerId, int password){
        return customerDAO.authentication(customerId, password);
    }

    public Customer getCustomerById(int cId){
        return customerDAO.findByCustomerId(cId);
    }

    public Movie getMovieById(int mId){
        return movieDAO.findByMovieId(mId);
    }

    public Collection<Customer> getAllCustomer(){
        return customerDAO.getAll();
    }

    public Collection<Movie> getAllMovie(){
        return movieDAO.getAll();
    }

    public Collection<Review> getReviewsByCustomerId(int cId){
        return reviewDAO.getReviewsByCustomerId(cId);
    }

    public Review getReviewsByCIdAndMId(int cId, int mId){
        return reviewDAO.getReviewByCIdAndMId(cId, mId);
    }

    public Collection<WishList> getWishListsByCustomerId(int cId){
        return wishListDAO.getWishListsByCustomerId(cId);
    }

    public WishList getWishListByCIdAndMId(int cId, int mId){
        return wishListDAO.getWishListByCIdAndMId(cId, mId);
    }

    public double getAvgRatingOfMovie(int mId){
        return reviewDAO.getAvgRatingOfMoive(mId);
    }

    public int getNumReviewOfMovie(int mId){
        return reviewDAO.getNumReviewsOfMoive(mId);
    }

    public int getNumWishListOfMovie(int mId){
        return wishListDAO.getNumWishListsOfMoive(mId);
    }

    public void clearTables(){
        try{
            customerDAO.clear();
        }
        catch (SQLException e){
            throw new RuntimeException("cannot clear database", e);
        }
    }

    /**
     * Commit changes since last call to commit
     */
    public void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("cannot commit database", e);
        }
    }

    /**
     * Abort changes since last call to commit, then close connection
     */
    public void cleanup() {
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException e) {
            System.out.println("fatal error: cannot cleanup connection");
        }
    }

    /**
     * Close connection and shutdown database
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("cannot close database connection", e);
        }

        // Now shutdown the embedded database system -- this is Derby-specific
        try {
            Properties prop = new Properties();
            prop.put("shutdown", "true");
            conn = driver.connect(url, prop);
        } catch (SQLException e) {
            // This is supposed to throw an exception...
            System.out.println("Derby has shut down successfully");
        }
    }
}
