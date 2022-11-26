package MS_Quokka.Mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Mapper<T> {
    public abstract ArrayList<T> readList();
    public abstract T readOne(String id);
    public abstract void create(T newDocument, Connection conn) throws SQLException;
    public abstract void update(T updatedDocument, Connection conn) throws SQLException;
}
