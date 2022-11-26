package MS_Quokka.Utils;
import java.util.ArrayList;
import java.sql.*;

public class DBConnPool {

    //static variables
    private static DBConnPool instance;

    //instance variables
    //constance variables
    private final String url = System.getenv().get("POSTGRES_URL");
    private final String user = System.getenv().get("POSTGRES_USER");
    private final String password = System.getenv().get("POSTGRES_PASSWORD");

    //variables
    private int maxPoolSize = 10;
    private ArrayList<Connection> inuseConnectionPool = new ArrayList<Connection>();
    private ArrayList<Connection> freeConnectionPool = new ArrayList<Connection>();

    private DBConnPool(){}

    public static DBConnPool getInstance(){

        // double-checked locking in Singleton pattern
        if(instance == null){
            synchronized (DBConnPool.class) {
                if(instance == null){
                    instance = new DBConnPool();
                }
            }
        }
        return instance;
    }

    private synchronized Connection createConnection(){

        Connection conn = null;

        if (inuseConnectionPool.size() >= maxPoolSize) {
            return null;
        }

        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null){
                System.out.println("Connected to the PostgreSQL server successfully");
            }
            else {
                System.out.println("Failed to make the connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inuseConnectionPool.add(conn);
        return conn;
    }

    private synchronized Connection registerConnection(){

        Connection conn = null;

        if (freeConnectionPool.size() == 0){
            conn = createConnection();
            return conn;
        }

        conn = freeConnectionPool.remove(0);
        inuseConnectionPool.add(conn);

        return conn;
    }

    public synchronized Connection getConnection(){

        Connection conn = registerConnection();

        while (conn == null) {
            try {
                // Wait until notifyAll is called...
                wait();
            } catch (InterruptedException ex) {}
            conn = registerConnection();
        }
        return conn;
    }

    public synchronized void releaseConnection(Connection conn) {
        if (conn != null){
            try {
                conn.setAutoCommit(true);
            } catch (Exception e){
                e.printStackTrace();
            }
            freeConnectionPool.add(inuseConnectionPool.remove(inuseConnectionPool.indexOf(conn)));
        }
        notifyAll();
    }


    //@resource:
    //Singleton
    // https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples
    //Pool:
    // https://objectcomputing.com/resources/publications/sett/march-2002-object-resource-pooling
}
