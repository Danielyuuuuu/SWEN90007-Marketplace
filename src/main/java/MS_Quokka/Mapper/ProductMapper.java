package MS_Quokka.Mapper;

import MS_Quokka.Domain.Category;
import MS_Quokka.Domain.Product;
import MS_Quokka.Utils.DBConnPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductMapper extends Mapper<Product>{

    private DBConnPool dbConnPool = DBConnPool.getInstance();
    private CategoryMapper categoryMapper = new CategoryMapper();

    private String createStatement(){
        //return "DO $do$ BEGIN IF NOT EXISTS (SELECT * FROM category WHERE category.name = ?) THEN INSERT INTO public.category (id, name) VALUES (?, ?); END IF; END $do$";
        return "INSERT INTO public.product( id, category, brand, name) VALUES (?, ?, ?, ?)";
    }

    private String readOneStatement() {
        return  "SELECT " +
                    "product.id as product_id, " +
                    "product.category as category_id, " +
                    "product.brand as product_brand, " +
                    "product.name as product_name " +
                "FROM product WHERE product.id = ?";
    }

    private String readListStatement() {
        return  "SELECT " +
                    "product.id as product_id, " +
                    "product.category as category_id, " +
                    "product.brand as product_brand, " +
                    "product.name as product_name " +
                "FROM product";
    }

    public void create(Product product, Connection conn) {

        //product pre setup
        String sql = createStatement();
        //Product product = null;

        //DB pre setup
        PreparedStatement sqlStatement = null;
        ResultSet rs = null;

        try {
            sqlStatement = conn.prepareStatement(sql);
            sqlStatement.setString(1, product.getId());
            sqlStatement.setString(2, product.getCategory().getId());
            sqlStatement.setString(3, product.getBrand());
            sqlStatement.setString(4, product.getName());

            if(sqlStatement.executeUpdate() > 0) {
                //System.out.println("new product created successfully");
                //product = load(id.toString(), categoryId, brand, name);
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
        //return product;
    }

    @Override
    public void update(Product updatedDocument, Connection conn) {
        return;
    }

    public void delete(String id) {
        return;
    }


    public Product readOne(String id){

        //Product pre setup
        String sql = readOneStatement();
        Product result = null;

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
                //System.out.println("Found product id = " + id);
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

    public ArrayList<Product> readList(){

        //Category pre setup
        String sql = readListStatement();
        ArrayList<Product> result = new ArrayList<Product>();

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

    //@TODO: should including load to identity map check
    private Product load(ResultSet rs) throws SQLException {

        String id = rs.getString("product_id");
        String categoryId = rs.getString("category_id");
        String name = rs.getString("product_name");
        String brand = rs.getString("product_brand");

        Category category = categoryMapper.readOne(categoryId);

        return new Product(id, category, brand, name);

    }

    private Product load(String id, String categoryId, String brand, String name){

        Category category = categoryMapper.readOne(categoryId);

        return new Product(id, category, brand, name);
    }

}
