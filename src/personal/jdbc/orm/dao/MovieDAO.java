package personal.jdbc.orm.dao;

import personal.jdbc.orm.model.Movie;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MovieDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Movie> cache;

    public  MovieDAO(Connection conn, DatabaseManager dbm){
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public void createTable(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE Movie (");
        sb.append(" MId int,");
        sb.append(" Name varchar(255) NOT NULL,");
        sb.append(" ImageUrl varchar(255) NOT NULL,");
        sb.append(" Category varchar(255) NOT NULL,");
        sb.append(" private key (MId)");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Movie findByMovieId(int MId){
        if(cache.containsKey(MId)){
            return cache.get(MId);
        }

        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT m.MId, m.Name, m.ImageUrl, m.Category");
            sb.append(" FROM Movie m");
            sb.append(" WHERE m.MId = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, MId);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return null;
            }

            String name = rs.getString("Name");
            String imageUrl = rs.getString("ImageUrl");
            String category = rs.getString("Category");
            rs.close();

            Movie movie = new Movie(this, MId, name, imageUrl, category);
            return movie;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error finding movie", e);
        }
    }

    public Movie insert(int MId, String name, String imageUrl, String category){
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT into Movie(MId, Name, ImageUrl, Category)");
            sb.append(" values(?, ?, ?, ?)");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, MId);
            pstmt.setString(2, name);
            pstmt.setString(3, imageUrl);
            pstmt.setString(4, category);
            pstmt.executeUpdate();

            Movie movie = new Movie(this, MId, name, imageUrl, category);
            return movie;
        }
        catch (SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error inserting movie", e);
        }

    }
}
