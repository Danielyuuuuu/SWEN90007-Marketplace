package MS_Quokka.Mapper;

import MS_Quokka.Domain.AuctionListing;
import MS_Quokka.Domain.Purchase;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class PurchaseMapper extends Mapper<Purchase> {

    private DBConnPool dbConnPool = DBConnPool.getInstance();

    public PurchaseMapper() {}

    private Purchase purchaseFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String title = rs.getString("title");
        int quantity = rs.getInt("quantity");
        double price = rs.getDouble("price");
        String buyerId = rs.getString("buyer");
        String status = rs.getString("purchase_status");
        String ProductId = rs.getString("product");
        String SellerId = rs.getString("seller_group");
        String fixedPriceId = rs.getString("fixed_price_listing");
        String auctionId = rs.getString("auction_listing");
        return new Purchase(id, title, quantity, price, buyerId, status, ProductId, SellerId, fixedPriceId, auctionId);
    }

    @Override
    public ArrayList<Purchase> readList() {
        String sql = "SELECT * FROM purchase";
        Connection conn = dbConnPool.getConnection();
        ArrayList<Purchase> purchases = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    purchases.add(purchaseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnPool.releaseConnection(conn);
        }
        return purchases;
    }

    @Override
    public Purchase readOne(String id) {
        String sql = "SELECT * FROM purchase WHERE id = ?";
        Connection conn = dbConnPool.getConnection();
        Purchase purchase = null;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    purchase = purchaseFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnPool.releaseConnection(conn);
        }
        return purchase;
    }

    @Override
    public void create(Purchase newDocument, Connection conn) {
        String sql = "INSERT INTO purchase (id, title, quantity, price, buyer, purchase_status, product, seller_group, fixed_price_listing, auction_listing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, newDocument.getId());
            statement.setString(2, newDocument.getTitle());
            statement.setInt(3, newDocument.getQuantity());
            statement.setDouble(4, newDocument.getPrice());
            statement.setString(5, newDocument.getBuyer().getId()); // buyer
            statement.setString(6, newDocument.getStatus().toString());
            statement.setString(7, newDocument.getProduct().getId()); // product
            statement.setString(8, newDocument.getSeller().getId()); // sellerGroup
            if (newDocument.getListing() instanceof AuctionListing) {
                statement.setNull(9, Types.VARCHAR);
                statement.setString(10, newDocument.getListing().getId());
            } else {
                statement.setString(9, newDocument.getListing().getId());
                statement.setNull(10, Types.VARCHAR);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Purchase updatedDocument, Connection conn) {
        String sql = "UPDATE purchase SET quantity = ?, purchase_status = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, updatedDocument.getQuantity());
            statement.setString(2, updatedDocument.getStatus().toString());
            statement.setDouble(3, updatedDocument.getPrice());
            statement.setString(4, updatedDocument.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Purchase> readListByBuyerID(String buyerID) {
        String sql = "SELECT * FROM purchase WHERE buyer = ?";
        Connection conn = dbConnPool.getConnection();
        ArrayList<Purchase> purchases = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, buyerID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    purchases.add(purchaseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnPool.releaseConnection(conn);
        }
        return purchases;
    }

    public ArrayList<Purchase> readListBySellerID(String buyerID) {
        String sql = "SELECT * FROM purchase WHERE seller_group = ?";
        Connection conn = dbConnPool.getConnection();
        ArrayList<Purchase> purchases = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, buyerID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    purchases.add(purchaseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnPool.releaseConnection(conn);
        }
        return purchases;
    }
}
