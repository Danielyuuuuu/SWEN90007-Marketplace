package MS_Quokka.Mapper;

import MS_Quokka.Domain.Category;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryMapper extends Mapper<Category> {

    private DBConnPool dbConnPool = DBConnPool.getInstance();

    private String createStatement(){
        //return "DO $do$ BEGIN IF NOT EXISTS (SELECT * FROM category WHERE category.name = ?) THEN INSERT INTO public.category (id, name) VALUES (?, ?); END IF; END $do$";
        return "INSERT INTO category (id, name) VALUES (?, ?)";
    }

    private String readOneStatement() {
        return  "SELECT id as category_id, name as category_name FROM category WHERE  id = ?";
    }

    private String readListStatement() {
        return  "SELECT id as category_id, name as category_name FROM category";
    }

    private String checkNameExistStatement(){
        return "SELECT COUNT(1) FROM category WHERE category.name = ?";
    }

    public Category readOne(String id){

        //Category pre setup
        String sql = readOneStatement();
        Category result = null;

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, id);
            sqlStatement.execute();

            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                result = load(rs);
                //System.out.println("Found Category id = " + id);
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


    public ArrayList<Category> readList(){

        //Category pre setup
        String sql = readListStatement();
        ArrayList<Category> result = new ArrayList<Category>();

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

            //System.out.println("Found this many = " + result.size());

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


    private Boolean checkCategoryExist(String name){

        String sql = checkNameExistStatement();

        //DB pre setup
        Connection conn = dbConnPool.getConnection();
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;
        int count = 0;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, name);
            sqlStatement.execute();

            rs = sqlStatement.getResultSet();
            if (rs.next()) {
                count = rs.getInt(1);
                //System.out.println("Found this many duplicate: " + count);
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

        if (count == 0) return false;

        return true;
    }

    public void create(Category category, Connection conn) {

        //category pre setup
        String sql = createStatement();
        //Category category = null;

        //DB pre setup
        PreparedStatement insertStatement = null;
        ResultSet rs = null;

        try {
            insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, category.getId());
            insertStatement.setString(2, category.getName());

            if(insertStatement.executeUpdate() > 0) {
                //System.out.println("new category created successfully");
                //category = load(id.toString(), name);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //return category;
    }

    @Override
    public void update(Category updatedDocument, Connection conn) {
        return;
    }

    public void delete(String id) {
        return;
    }



    //@TODO: should including load to identity map check
    public Category load(ResultSet rs) throws SQLException{
        String id = rs.getString("category_id");
        String name = rs.getString("category_name");

        return new Category(id, name);
    }

    public Category load(String id, String name){
        return new Category(id, name);
    }

}
