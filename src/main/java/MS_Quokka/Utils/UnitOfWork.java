package MS_Quokka.Utils;

import MS_Quokka.Domain.DomainObject;
import MS_Quokka.Mapper.DataMapper;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private static ThreadLocal current = new ThreadLocal();
    private List<DomainObject> newObjects = new ArrayList<DomainObject>();
    private List<DomainObject> dirtyObjects = new ArrayList<DomainObject>();
//    private List<DomainObject> deletedObjects = new ArrayList<DomainObject>();

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return (UnitOfWork) current.get();
    }

    public void registerNew(DomainObject obj) {
        Assert.notNull(obj.getId(), "id is null");
        Assert.isTrue(!dirtyObjects.contains(obj), "object is dirty");
//        Assert.isTrue(!deletedObjects.contains(obj), "object is deleted");
        Assert.isTrue(!newObjects.contains(obj), "object is new");
        newObjects.add(obj);
    }

    public void registerDirty(DomainObject obj) {
        Assert.notNull(obj.getId(), "id is null");
//        Assert.isTrue(!deletedObjects.contains(obj), "object is deleted");
        if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
            dirtyObjects.add(obj);
        }
    }


//    public void registerDeleted(DomainObject obj) {
//        Assert.notNull(obj.getId(), "id is null");
//        if (newObjects.remove(obj)) return;
//        dirtyObjects.remove(obj);
//        if (!deletedObjects.contains(obj)) {
//            deletedObjects.add(obj);
//        }
//    }

//    public void registerClean(DomainObject obj) {
//        Assert.notNull(obj.getId(), "id is null");
//    }

    public void commit() throws SQLException {
        Connection conn = DBConnPool.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            for (DomainObject obj : newObjects) {
                DataMapper.create(obj, conn);
            }
            for (DomainObject obj : dirtyObjects) {
                DataMapper.update(obj, conn);
            }
//            for (DomainObject obj : deletedObjects) {
//                DataMapper.delete(obj);
//            }
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
                System.out.println("Rolling back the transaction");
                e.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } finally {
            // Set auto-commit back to true, release the connection back to the connection pool
            DBConnPool.getInstance().releaseConnection(conn);
        }
    }
}
