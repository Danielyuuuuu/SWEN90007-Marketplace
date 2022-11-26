package MS_Quokka.Mapper;

import MS_Quokka.Domain.SellerGroup;
import MS_Quokka.Domain.User;
import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class UserMapper extends Mapper<User> {
    private final DBConnPool dbConnPool;

    public UserMapper() {
        dbConnPool = DBConnPool.getInstance();
    }

    public Boolean checkIfEmailExist(String email) throws SQLException {
        String sql = "SELECT * FROM \"account\" WHERE email = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, email);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                return true;
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
        return false;
    }

    @Override
    public ArrayList<User> readList() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM \"account\" WHERE role in ('USER', 'SELLER');";
        Connection conn = dbConnPool.getConnection();
        User user = null;
        try (PreparedStatement sqlStatement = conn.prepareStatement(sql)) {
            try (ResultSet rs = sqlStatement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String email = rs.getString("email");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String shippingAddress = rs.getString("shipping_address");
                    Role role = Role.valueOf(rs.getString("role"));
                    String sellerGroupId = rs.getString("seller_group");
                    SellerGroup sellerGroup = new SellerGroup(sellerGroupId, null);
                    user = new User(id, email, firstname, lastname, null, shippingAddress, role, sellerGroup);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return users;

    }

    @Override
    public User readOne(String userId) {
        String sql = "SELECT * FROM \"account\" WHERE id = ?;";
        Connection conn = dbConnPool.getConnection();
        User user = null;
        try (PreparedStatement sqlStatement = conn.prepareStatement(sql)) {
            sqlStatement.setString(1, userId);
            try (ResultSet rs = sqlStatement.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String password = rs.getString("password");
                    String shippingAddress = rs.getString("shipping_address");
                    Role role = Role.valueOf(rs.getString("role"));
                    String sellerGroupId = rs.getString("seller_group");
                    SellerGroup sellerGroup = new SellerGroup(sellerGroupId, null);
                    user = new User(userId, email, firstname, lastname, password, shippingAddress, role, sellerGroup);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return user;
    }

    @Override
    public void create(User user, Connection conn) throws SQLException {
        UUID id = UUID.randomUUID();
        String sql = "INSERT INTO \"account\" (id, email, firstname, lastname, password, shipping_address, role) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, user.getId());
            sqlStatement.setString(2, user.getEmail());
            sqlStatement.setString(3, user.getFirstname());
            sqlStatement.setString(4, user.getLastname());
            sqlStatement.setString(5, user.getPassword());
            sqlStatement.setString(6, user.getShippingAddress());
            sqlStatement.setString(7, user.getRole().toString());
            if (sqlStatement.executeUpdate() > 0) {
                System.out.println("User inserted successfully");
            } else {
                System.out.println("Failed to insert the user");
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

    @Override
    public void update(User userToUpdate, Connection conn) throws SQLException {
        String sql = "UPDATE \"account\" SET firstname = ?, lastname = ?, password = ?, shipping_address = ?, seller_group = ?, role = ? WHERE id = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, userToUpdate.getFirstname());
            sqlStatement.setString(2, userToUpdate.getLastname());
            sqlStatement.setString(3, userToUpdate.getPassword());
            sqlStatement.setString(4, userToUpdate.getShippingAddress());
            if (userToUpdate.getSellerGroup() == null){
                sqlStatement.setString(5, null);
            } else{
                sqlStatement.setString(5, userToUpdate.getSellerGroup().getId());
            }
            sqlStatement.setString(6, userToUpdate.getRole().toString());
            sqlStatement.setString(7, userToUpdate.getId());
            if (sqlStatement.executeUpdate() > 0) {
                System.out.println("User updated successfully");
            } else {
                System.out.println("Failed to update the user");
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

    public void delete(String userId) throws SQLException {
        String sql = "DELETE FROM \"account\" WHERE id = ?;";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, userId);
            if (sqlStatement.executeUpdate() > 0) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("Failed to delete the user");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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
    }


    public ArrayList<User> findUsersInSellerGroup (String sellerGroupId) throws SQLException{
        String sql = "SELECT id, email, firstname, lastname FROM account WHERE seller_group= ?";
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        Connection conn = dbConnPool.getConnection();
        ArrayList<User> usersInSellerGroup = new ArrayList<User>();
        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, sellerGroupId);
            sqlStatement.execute();
            rs = sqlStatement.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String email = rs.getString("email");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                SellerGroup sellerGroup = new SellerGroup(sellerGroupId, null);
                usersInSellerGroup.add(new User(id, email, firstname, lastname, null, null, null, sellerGroup));
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
        return usersInSellerGroup;
    }

    public User readOneByEmail(String userEmail) {
        String sql = "SELECT * FROM \"account\" WHERE email = ?;";
        Connection conn = dbConnPool.getConnection();
        User user = null;
        try (PreparedStatement sqlStatement = conn.prepareStatement(sql)) {
            sqlStatement.setString(1, userEmail);
            try (ResultSet rs = sqlStatement.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("id");
                    String email = rs.getString("email");
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String password = rs.getString("password");
                    String shippingAddress = rs.getString("shipping_address");
                    Role role = Role.valueOf(rs.getString("role"));
                    String sellerGroupId = rs.getString("seller_group");
                    SellerGroup sellerGroup = new SellerGroup(sellerGroupId, null);
                    user = new User(userId, email, firstname, lastname, password, shippingAddress, role, sellerGroup);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                dbConnPool.releaseConnection(conn);
            }
        }
        return user;
    }

}



