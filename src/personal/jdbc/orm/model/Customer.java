package personal.jdbc.orm.model;

import personal.jdbc.orm.dao.CustomerDAO;

public class Customer {
    private CustomerDAO dao;
    private int cid;
    private String nickname;
    private String email;
    private int hashedPassword;
    private String desciption;
    private String imageUrl;

    public Customer(CustomerDAO dao, int cid, String nickname, String email, int hashedPassword, String desciption, String imageUrl){
        this.dao = dao;
        this.cid = cid;
        this.nickname = nickname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.desciption = desciption;
        this.imageUrl = imageUrl;
    }
}
