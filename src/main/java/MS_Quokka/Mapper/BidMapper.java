package MS_Quokka.Mapper;

import MS_Quokka.Domain.User;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class BidMapper {
    private final DBConnPool dbConnPool;

    public BidMapper() {
        dbConnPool = DBConnPool.getInstance();
    }

    public Double getHighestBidPrice(String listingID) throws SQLException {
        String sql = "SELECT MAX(bid_price) as max_price FROM \"bid\" WHERE auction_listing = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listingID);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                Double maxPrice = Double.parseDouble(rs.getString("max_price"));
                System.out.println("HighestBidPrice: " + maxPrice);
                return maxPrice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (sqlStatement != null) {
                sqlStatement.close();
            }
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return -1.0;
    }

    public void create(Double price, String buyerID, String listingID, Connection conn) throws SQLException {
        String sql = "INSERT INTO bid (id, bid_price, buyer, auction_listing) VALUES (?, ?, ?, ?);";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        UUID id = UUID.randomUUID();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, id.toString());
            sqlStatement.setDouble(2, price);
            sqlStatement.setString(3, buyerID);
            sqlStatement.setString(4, listingID);

            if (sqlStatement.executeUpdate() > 0) {
                System.out.println("Bid inserted successfully");
            } else {
                System.out.println("Failed to insert the bid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (sqlStatement != null) {
                sqlStatement.close();
            }
        }
    }

    public ArrayList<String> getAllUsersThatHasMadeABid(String listingID) throws SQLException {
        String sql = "SELECT DISTINCT buyer FROM bid WHERE auction_listing = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        UserMapper userMapper = new UserMapper();

        ArrayList<String> buyers = new ArrayList<>();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listingID);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            while (rs.next()) {
                String buyerID = rs.getString("buyer");
                System.out.println("buyerID: " + buyerID);

                User user = userMapper.readOne(buyerID);
                buyers.add(user.getFirstname());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (sqlStatement != null) {
                sqlStatement.close();
            }
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return buyers;
    }

    public User getHighestBidder(String listingID) throws SQLException {
        String sql = "SELECT buyer FROM \"bid\" WHERE auction_listing = ? ORDER BY bid_price DESC;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        UserMapper userMapper = new UserMapper();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listingID);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                String highestBidderID = rs.getString("buyer");
                System.out.println("getHighestBidder: " + highestBidderID);

                User highestBidder = userMapper.readOne(highestBidderID);
                return highestBidder;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (sqlStatement != null) {
                sqlStatement.close();
            }
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return null;
    }

    public Integer getBidCounter(String listingID) throws SQLException {
        String sql = "SELECT COUNT(id) as bidCounter FROM \"bid\" WHERE auction_listing = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listingID);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                Integer bidCounter = Integer.parseInt(rs.getString("bidCounter"));
                System.out.println("bidCounter: " + bidCounter);
                return bidCounter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (sqlStatement != null) {
                sqlStatement.close();
            }
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return -1;
    }

}
