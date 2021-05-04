package personal.jdbc.orm.dao;

import personal.jdbc.orm.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerDAO {
    private Connection conn;
    private DatabaseManager dbm;
    private Map<Integer, Customer> cache;

    public CustomerDAO(Connection conn, DatabaseManager dbm){
        this.conn = conn;
        this.dbm = dbm;
        this.cache = new HashMap<>();
    }

    public static void createTable(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE Customer (");
        sb.append(" CId int,");
        sb.append(" Nickname varchar(255) NOT NULL,");
        sb.append(" Email varchar(255) NOT NULL,");
        sb.append(" HashedPassword int NOT NULL,");
        sb.append(" Description varchar(1000) DEFAULT '' NOT NULL,");
        sb.append(" ImageUrl varchar(255) DEFAULT 'defaultImage.url' NOT NULL,");
        sb.append(" primary key (CId)");
        sb.append(")");

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sb.toString());
    }

    public Customer findByCustomerId(int cId){
        if(cache.containsKey(cId)){
            return cache.get(cId);
        }
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.Nickname, c.Email, c.HashedPassword, c.Description, c.ImageUrl");
            sb.append(" FROM Customer c");
            sb.append(" WHERE c.CId = ? ");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, cId);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return null;
            }

            String nickname = rs.getString("Nickname");
            String email = rs.getString("Email");
            int hashedPassword = rs.getInt("HashedPassword");
            String description = rs.getString("Description");
            String imageUrl = rs.getString("ImageUrl");
            rs.close();

            Customer customer = new Customer (this, cId, nickname, email, hashedPassword, description, imageUrl);
            cache.put(cId, customer);
            return customer;
        }
        catch(SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error finding customer", e);
        }
    }

    public Customer insert(int cId, String nickname, String email, int hashedPassword) {
        try{
            String description = "default description";
            String imageUrl = "default customer image";
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT into Customer(CId, Nickname, Email, HashedPassword, Description, ImageUrl)");
            sb.append(" values(?, ?, ?, ?, ?, ?)");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, cId);
            pstmt.setString(2, nickname);
            pstmt.setString(3, email);
            pstmt.setInt(4, hashedPassword);
            pstmt.setString(5, description);
            pstmt.setString(6, imageUrl);
            pstmt.executeUpdate();

            Customer customer = new Customer(this, cId, nickname, email, hashedPassword, description, imageUrl);
            cache.put(cId, customer);
            return customer;
        }
        catch(SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error inserting customer", e);
        }
    }

    public Collection<Customer> getAll(){
        try{
            Collection<Customer> customers = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.CId, c.Nickname, c.Email, c.HashedPassword, c.Description, c.ImageUrl");
            sb.append(" FROM Customer c");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int cId = rs.getInt("CId");
                String nickname = rs.getString("Nickname");
                String email = rs.getString("Email");
                int hashedPassword = rs.getInt("HashedPassword");
                String description = rs.getString("Description");
                String imageUrl = rs.getString("ImageUrl");

                Customer customer = new Customer(this, cId, nickname, email, hashedPassword, description, imageUrl);
                customers.add(customer);
            }
            return customers;
        }
        catch(SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error getting all customers", e);
        }
    }

    public boolean authentication(int customerId, int password){
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c.CId, c.Nickname, c.Email, c.HashedPassword, c.Description, c.ImageUrl");
            sb.append(" FROM Customer c");
            sb.append(" WHERE c.CId = ? AND c.HashedPassword = ?");

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, password);
            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return false;
            }
            else{
                return true;
            }
        }
        catch(SQLException e){
            dbm.cleanup();
            throw new RuntimeException("error authenticating customer", e);
        }
    }

    public void clear() throws SQLException {
        Statement stmt = conn.createStatement();
        String s = "delete from Customer";
        stmt.executeUpdate(s);
        cache.clear();
    }
}
