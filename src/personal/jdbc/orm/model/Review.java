package personal.jdbc.orm.model;

import personal.jdbc.orm.dao.ReviewDAO;

public class Review {
    private ReviewDAO dao;
    private int rId;
    private int rating;
    private String comment;

    private Customer customer;
    private Movie movie;

    public Review(ReviewDAO dao, int rId, int rating, String comment, Customer customer, Movie movie){
        this.dao = dao;
        this.rId = rId;
        this.rating = rating;
        this.comment = comment;
        this.customer = customer;
        this.movie = movie;
    }
}
