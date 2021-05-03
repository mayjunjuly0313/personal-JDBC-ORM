package personal.jdbc.orm.model;

import personal.jdbc.orm.dao.MovieDAO;

public class Movie {
    private MovieDAO dao;
    private int mId;
    private String  name;
    private String imageUrl;
    private String category;

    public Movie(MovieDAO dao, int mId, String name, String imageUrl, String category){
        this.dao = dao;
        this.mId = mId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public int getId(){
        return this.mId;
    }
}
