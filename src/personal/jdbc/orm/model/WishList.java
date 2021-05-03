package personal.jdbc.orm.model;

import personal.jdbc.orm.dao.WishListDAO;

public class WishList {
    private WishListDAO dao;
    private int wId;

    private Customer customer;
    private Movie movie;

    public WishList(WishListDAO dao, int wId, Customer customer, Movie movie){
        this.dao = dao;
        this.wId = wId;
        this.customer = customer;
        this.movie = movie;
    }
}
