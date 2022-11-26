package MS_Quokka.Mapper;

import MS_Quokka.Domain.*;
import MS_Quokka.Utils.DBConnPool;

import java.sql.*;
import java.util.ArrayList;

public class AuctionListingMapper extends Mapper<AuctionListing>{

    private DBConnPool dbConnPool = DBConnPool.getInstance();
    private ProductMapper productMapper = new ProductMapper();
    private SellerGroupMapper sellerGroupMapper = new SellerGroupMapper();
    private UserMapper userMapper = new UserMapper();
    private BidMapper bidMapper = new BidMapper();

    private String createStatement(){

        return "INSERT INTO " +
                "auction_listing(id, listing_title, quantity, start_time, end_time, price, description, seller_group, product, archive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "quantity as auctionListing_quantity, " +
                    "start_time as auctionListing_startTime, " +
                    "end_time as auctionListing_endTime, " +
                    "price as auctionListing_price, " +
                    "description as auctionListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as auctionListing_archive";
    }

    private String updateStatement() {
        return  "UPDATE auction_listing " +
                "SET " +
                    "listing_title=?, " +
                    "quantity=?, " +
                    "price=?, " +
                    "description=?, " +
                    "archive =? " +
                "WHERE " +
                    "id = ?" +
                "RETURNING " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "quantity as auctionListing_quantity, " +
                    "start_time as auctionListing_startTime, " +
                    "end_time as auctionListing_endTime, " +
                    "price as auctionListing_price, " +
                    "description as auctionListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as auctionListing_archive";
    }

    private String readStatement() {
        return  "SELECT " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "quantity as auctionListing_quantity, " +
                    "start_time as auctionListing_startTime, " +
                    "end_time as auctionListing_endTime, " +
                    "price as auctionListing_price, " +
                    "description as auctionListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as auctionListing_archive " +
                "FROM auction_listing WHERE id = ?";
    }

    private String readListStatement() {
        return  "SELECT " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "quantity as auctionListing_quantity, " +
                    "start_time as auctionListing_startTime, " +
                    "end_time as auctionListing_endTime, " +
                    "price as auctionListing_price, " +
                    "description as auctionListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as auctionListing_archive " +
                "FROM auction_listing";
    }

    private String searchStatement() {
        return  "SELECT " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "price as auctionListing_price " +
                "FROM auction_listing WHERE listing_title = ? AND end_time > now() AND quantity > 0 AND not archive";
    }

    private String readListSellerStatement() {
        return  "SELECT " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "quantity as auctionListing_quantity, " +
                    "start_time as auctionListing_startTime, " +
                    "end_time as auctionListing_endTime, " +
                    "price as auctionListing_price, " +
                    "description as auctionListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as auctionListing_archive " +
                "FROM auction_listing WHERE seller_group = ? AND not archive";
    }


    private String readListBuyerStatement() {
        return  "SELECT " +
                    "id as auctionListing_id, " +
                    "listing_title as auctionListing_listingTitle, " +
                    "price as auctionListing_price " +
                "FROM auction_listing WHERE end_time > now() AND quantity > 0 AND not archive";
    }

    private String deleteStatement() {
        return  "DELETE FROM auction_listing WHERE id = ?";
    }


    public void create(AuctionListing listing, Connection conn) {

        //product pre setup
        String sql = createStatement();
        //AuctionListing result = null;

        //DB pre setup
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listing.getId());
            sqlStatement.setString(2, listing.getListingTitle());
            sqlStatement.setInt(3, listing.getQuantity());
            sqlStatement.setTimestamp(4, listing.getStartTime());
            sqlStatement.setTimestamp(5, listing.getEndTime());
            sqlStatement.setDouble(6, listing.getPrice());
            sqlStatement.setString(7, listing.getDescription());
            sqlStatement.setString(8, listing.getSellerGroup().getId());
            sqlStatement.setString(9, listing.getProduct().getId());
            sqlStatement.setBoolean(10, listing.getArchive());

            rs = sqlStatement.executeQuery();
            if (rs.next()) {
                //result = load(rs);
                System.out.println("auctionListing id = " + listing.getId()  + " create successfully");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (sqlStatement != null) {
                    sqlStatement.close();
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(AuctionListing listing, Connection conn) {

        //product pre setup
        String sql = updateStatement();
        //AuctionListing result = null;

        //DB pre setup
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listing.getListingTitle());
            sqlStatement.setInt(2, listing.getQuantity());
            sqlStatement.setDouble(3, listing.getPrice());
            sqlStatement.setString(4, listing.getDescription());
            sqlStatement.setBoolean(5, listing.getArchive());
            sqlStatement.setString(6, listing.getId());

            rs = sqlStatement.executeQuery();
            if (rs.next()) {
                //result = load(rs);
                System.out.println("auctionListing id = " + listing.getId()  + " update successfully");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (sqlStatement != null) {
                    sqlStatement.close();
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public AuctionListing readOne(String id) {

        //product pre setup
        String sql = readStatement();
        AuctionListing result = null;

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, id);

            rs = sqlStatement.executeQuery();
            if (rs.next()) {
                result = load(rs, true);
                System.out.println("auctionListing id = " + id  + " readOne successfully");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public ArrayList<AuctionListing> readList() {

        //product pre setup
        String sql = readListStatement();
        ArrayList<AuctionListing> result = new ArrayList<AuctionListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs, true));
            }

            System.out.println("Found this many = " + result.size());

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    public ArrayList<AuctionListing> search(String listingTitle) {

        //product pre setup
        String sql = searchStatement();
        ArrayList<AuctionListing> result = new ArrayList<AuctionListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {

            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listingTitle);


            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs, false));
            }

            System.out.println("Found this many = " + result.size());

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    public ArrayList<AuctionListing> readListSeller(String sellerGroupId) {

        //product pre setup
        String sql = readListSellerStatement();
        ArrayList<AuctionListing> result = new ArrayList<AuctionListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {

            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, sellerGroupId);


            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs, true));
            }

            System.out.println("Found this many = " + result.size());

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    public void delete(String id) {

        //product pre setup
        String sql = deleteStatement();
        //AuctionListing result = null;

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, id);

            if(sqlStatement.executeUpdate() > 0) {
                System.out.println("fixPriceListing id = " + id  + " delete successfully");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //return result;
    }

    public ArrayList<AuctionListing> readListBuyer() {

        //product pre setup
        String sql = readListBuyerStatement();
        ArrayList<AuctionListing> result = new ArrayList<AuctionListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs, false));
            }

            System.out.println("Found this many = " + result.size());

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    private AuctionListing load(ResultSet rs, Boolean loadBids) throws SQLException {

        String id = getString(rs, "auctionListing_id");
        String listingTitle = getString(rs, "auctionListing_listingTitle");
        Integer quantity = getInt(rs, "auctionListing_quantity");
        Timestamp startTime = getTimestamp(rs, "auctionListing_startTime");
        Timestamp endTime = getTimestamp(rs, "auctionListing_endTime");
        Double price = getDouble(rs,"auctionListing_price");
        String description = getString(rs,"auctionListing_description");
        String sellerGroupId = getString(rs,"sellerGroup_id");
        String productId = getString(rs,"product_id");
        Boolean archive = getBoolean(rs,"auctionListing_archive");

        SellerGroup sellerGroup;
        if(sellerGroupId == null) sellerGroup = null;
        else sellerGroup = sellerGroupMapper.readOne(sellerGroupId);

        Product product;
        if(productId == null) product = null;
        else product = productMapper.readOne(productId);

        Integer bidCounter = null;
        User highestBidder = null;

        if(loadBids){
            bidCounter = bidMapper.getBidCounter(id);
            highestBidder = bidMapper.getHighestBidder(id);
        }

        return new AuctionListing(id, listingTitle, quantity, startTime, endTime, price, description, bidCounter, sellerGroup, product, highestBidder, archive);

    }

    private String getString(ResultSet rs, String column){
        try{ return rs.getString(column); }
        catch (SQLException e) { return null; }
    }

    private Integer getInt(ResultSet rs, String column){
        try{ return rs.getInt(column); }
        catch (SQLException e) { return null; }
    }

    private Double getDouble(ResultSet rs, String column){
        try{ return rs.getDouble(column); }
        catch (SQLException e) { return null; }
    }

    private Boolean getBoolean(ResultSet rs, String column){
        try{ return rs.getBoolean(column); }
        catch (SQLException e) { return null; }
    }

    private Timestamp getTimestamp(ResultSet rs, String column){
        try{ return rs.getTimestamp(column); }
        catch (SQLException e) { return null; }
    }

}
