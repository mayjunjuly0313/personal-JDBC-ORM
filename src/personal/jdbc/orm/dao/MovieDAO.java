package personal.jdbc.orm.dao;

import java.sql.Connection;
import java.util.HashMap;

public class MovieDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Movie> hashMap;

    public  MovieDAO(Connection conn, DatabaseManager dbm){
        this.conn = conn;
        this.dbm = dbm;
        this.hashMap = new HashMap<>();
    }

    public createTable()
}
