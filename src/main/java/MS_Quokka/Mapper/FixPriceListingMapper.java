package MS_Quokka.Mapper;

import MS_Quokka.Domain.FixPriceListing;
import MS_Quokka.Domain.Product;
import MS_Quokka.Domain.SellerGroup;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FixPriceListingMapper extends Mapper<FixPriceListing> {

    private DBConnPool dbConnPool = DBConnPool.getInstance();
    private ProductMapper productMapper = new ProductMapper();
    private SellerGroupMapper sellerGroupMapper = new SellerGroupMapper();

    private String createStatement(){
        //return "DO $do$ BEGIN IF NOT EXISTS (SELECT * FROM category WHERE category.name = ?) THEN INSERT INTO public.category (id, name) VALUES (?, ?); END IF; END $do$";
        return "INSERT INTO " +
                    "fixed_price_listing(id, listing_title,  quantity, price, description, seller_group, product, archive) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "quantity as fixPriceListing_quantity, " +
                    "price as fixPriceListing_price, " +
                    "description as fixPriceListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as fixPriceListing_archive";
    }

    private String updateStatement() {
        return  "UPDATE fixed_price_listing " +
                "SET " +
                    "listing_title=?, " +
                    "quantity=?, " +
                    "price=?, " +
                    "description=?, " +
                    "archive=? " +
                "WHERE " +
                    "id = ?" +
                "RETURNING " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "quantity as fixPriceListing_quantity, " +
                    "price as fixPriceListing_price, " +
                    "description as fixPriceListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as fixPriceListing_archive";
    }

    private String deleteStatement() {
        return  "DELETE FROM fixed_price_listing WHERE id = ?";
    }


    private String readOneStatement() {
        return  "SELECT " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "quantity as fixPriceListing_quantity, " +
                    "price as fixPriceListing_price, " +
                    "description as fixPriceListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as fixPriceListing_archive " +
                "FROM fixed_price_listing WHERE id = ?";
    }

    private String readListStatement() {
        return  "SELECT " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "quantity as fixPriceListing_quantity, " +
                    "price as fixPriceListing_price, " +
                    "description as fixPriceListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as fixPriceListing_archive " +
                "FROM fixed_price_listing";
    }

    private String readListSellerStatement() {
        return  "SELECT " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "quantity as fixPriceListing_quantity, " +
                    "price as fixPriceListing_price, " +
                    "description as fixPriceListing_description, " +
                    "seller_group as sellerGroup_id, " +
                    "product as product_id, " +
                    "archive as fixPriceListing_archive " +
                "FROM fixed_price_listing WHERE seller_group = ? AND not archive";
    }

    private String readListBuyerStatement() {
        return  "SELECT " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "price as fixPriceListing_price " +
                "FROM fixed_price_listing WHERE quantity > 0 AND not archive";
    }

    private String searchStatement() {
        return  "SELECT " +
                    "id as fixPriceListing_id, " +
                    "listing_title as fixPriceListing_listingTitle, " +
                    "price as fixPriceListing_price " +
                "FROM fixed_price_listing WHERE listing_title = ? AND quantity > 0 AND not archive";
    }


    public void create(FixPriceListing listing, Connection conn) {

        //product pre setup
        String sql = createStatement();
        //FixPriceListing result = null;

        //DB pre setup
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, listing.getId());
            sqlStatement.setString(2, listing.getListingTitle());
            sqlStatement.setInt(3, listing.getQuantity());
            sqlStatement.setDouble(4, listing.getPrice());
            sqlStatement.setString(5, listing.getDescription());
            sqlStatement.setString(6, listing.getSellerGroup().getId());
            sqlStatement.setString(7, listing.getProduct().getId());
            sqlStatement.setBoolean(8, listing.getArchive());

            rs = sqlStatement.executeQuery();
            if (rs.next()) {
                //result = load(rs);
                System.out.println("fixPriceListing id = " + listing.getId()  + " create successfully");
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
        //return result;
    }

    public void update(FixPriceListing listing, Connection conn) {

        //product pre setup
        String sql = updateStatement();
        //FixPriceListing result = null;

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
                System.out.println("fixPriceListing id = " + listing.getId()  + " update successfully");
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
        //return result;
    }

    public void delete(String id) {

        //product pre setup
        String sql = deleteStatement();
        //FixPriceListing result = null;

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

    public FixPriceListing readOne(String id) {

        //product pre setup
        String sql = readOneStatement();
        FixPriceListing result = null;

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, id);

            rs = sqlStatement.executeQuery();
            if (rs.next()) {
                result = load(rs);
                System.out.println("fixPriceListing id = " + id  + " find successfully");
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

    public ArrayList<FixPriceListing> readList() {

        //product pre setup
        String sql = readListStatement();
        ArrayList<FixPriceListing> result = new ArrayList<FixPriceListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs));
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

    public ArrayList<FixPriceListing> search(String listingTitle) {

        //product pre setup
        String sql = searchStatement();
        ArrayList<FixPriceListing> result = new ArrayList<FixPriceListing>();

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
                result.add(load(rs));
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

    public ArrayList<FixPriceListing> readListSeller(String sellerGroupId) {

        //product pre setup
        String sql = readListSellerStatement();
        ArrayList<FixPriceListing> result = new ArrayList<FixPriceListing>();

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
                result.add(load(rs));
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

    public ArrayList<FixPriceListing> readListBuyer() {

        //product pre setup
        String sql = readListBuyerStatement();
        ArrayList<FixPriceListing> result = new ArrayList<FixPriceListing>();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();

            while(rs.next()) {
                result.add(load(rs));
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


    private FixPriceListing load(ResultSet rs) throws SQLException {

        String id = getString(rs, "fixPriceListing_id");
        String listingTitle = getString(rs, "fixPriceListing_listingTitle");
        Integer quantity = getInt(rs,"fixPriceListing_quantity");
        Double price = getDouble(rs,"fixPriceListing_price");
        String description = getString(rs,"fixPriceListing_description");
        String sellerGroupId = getString(rs,"sellerGroup_id");
        String productId = getString(rs,"product_id");
        Boolean archive = getBoolean(rs,"fixPriceListing_archive");

        SellerGroup sellerGroup;
        if(sellerGroupId == null) sellerGroup = null;
        else sellerGroup = sellerGroupMapper.readOne(sellerGroupId);

        Product product;
        if(productId == null) product = null;
        else product = productMapper.readOne(productId);

        return new FixPriceListing(id, listingTitle, quantity, price, description, sellerGroup, product, archive);

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


    private FixPriceListing load(String id, String listingTitle, Integer quantity, Double price, String description, String sellerGroupId, String productId, Boolean archive) throws SQLException {

        SellerGroup sellerGroup = sellerGroupMapper.readOne(sellerGroupId);
        Product product = productMapper.readOne(productId);

        return new FixPriceListing(id, listingTitle, quantity, price, description, sellerGroup, product, archive);
    }

}
