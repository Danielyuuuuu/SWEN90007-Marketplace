package MS_Quokka.Mapper;

import MS_Quokka.Domain.SellerGroup;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellerGroupMapper extends Mapper<SellerGroup>{

    private DBConnPool dbConnPool = DBConnPool.getInstance();

    @Override
    public void create(SellerGroup sellerGroup, Connection conn) throws SQLException  {

        //String sql = "INSERT INTO seller_group VALUES ('" + sellerGroup.id + "', " + "'" + sellerGroup.name+ "')";
        String sql = "INSERT INTO seller_group VALUES (?, ?)";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, sellerGroup.getId());
            sqlStatement.setString(2, sellerGroup.getName());
            int createdRows = sqlStatement.executeUpdate();
            if (createdRows == 1) {
                System.out.println("Successfully created the SellerGroup");
            } else {
                System.out.println("Failed to create the SellerGroup!");
            }
        } catch(SQLException e) {
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


    @Override
    public ArrayList<SellerGroup> readList() {
        System.out.println("Executing inside readList for SellerGroup");
        Connection conn = dbConnPool.getConnection();

        String sql = "SELECT * FROM seller_group";

        ArrayList<SellerGroup> allSellerGroups = new ArrayList<SellerGroup>();

        try (PreparedStatement sqlStatement = conn.prepareStatement(sql)){
            try (ResultSet rs = sqlStatement.executeQuery()) {
                while (rs.next()) {
                    String sellerGroupName = rs.getString("name");
                    String sellerGroupId = rs.getString("id");
                    allSellerGroups.add(new SellerGroup(sellerGroupId, sellerGroupName));
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }

        return allSellerGroups;

    }

    @Override
    public void update(SellerGroup sellerGroup, Connection conn) throws SQLException {
        String sql = "UPDATE seller_group SET name = ?, id = ?";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        sqlStatement = conn.prepareStatement(sql);
        sqlStatement.setString(1, sellerGroup.getName());
        sqlStatement.setString(2, sellerGroup.getId());
        int updatedRows = sqlStatement.executeUpdate();
        if (updatedRows == 1) {
            System.out.println("Successfully updated the SellerGroup");
        } else {
            System.out.println("Failed to update the SellerGroup!");
        }
    }

    @Override
    public SellerGroup readOne(String sellerGroupId) {
        System.out.println("Executing readOne in SellerGroupMapper");

        Connection conn = dbConnPool.getConnection();

        //String sql = "SELECT * FROM seller_group WHERE id='" + sellerGroupId + "'";
        String sql = "SELECT * FROM seller_group WHERE id = ?";

        SellerGroup sellerGroup = null;
        try (PreparedStatement sqlStatement = conn.prepareStatement(sql)) {
            sqlStatement.setString(1, sellerGroupId);
            try (ResultSet rs = sqlStatement.executeQuery()) {
                while (rs.next()) {
                    String sellerGroupName = rs.getString("name");
                    sellerGroup = new SellerGroup(sellerGroupId, sellerGroupName);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }

        return sellerGroup;
    }

}
