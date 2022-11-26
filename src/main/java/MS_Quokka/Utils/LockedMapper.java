package MS_Quokka.Utils;

import MS_Quokka.Domain.DomainObject;
import MS_Quokka.Mapper.DataMapper;
import MS_Quokka.Mapper.Mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class LockedMapper<T extends DomainObject> extends Mapper<T> implements AutoCloseable {

    private final String id;
    private final Mapper<T> mapper;
    private final ExclusiveWriteManager lockManager = ExclusiveWriteManager.getInstance();

    public LockedMapper(String id, Class<T> c) {
        this.id = id;
        lockManager.acquireLock(id, Thread.currentThread().getName());
        mapper = DataMapper.getMapper(c);
    }

    @Override
    public ArrayList<T> readList() {
        return null;
    }

    @Override
    public T readOne(String id) {
        if (id.equals(this.id)) {
            return mapper.readOne(id);
        }
        return null;
    }

    @Override
    public void create(T newDocument, Connection conn) throws SQLException {
        mapper.create(newDocument, conn);
    }

    @Override
    public void update(T updatedDocument, Connection conn) throws SQLException {
        if (id.equals(updatedDocument.getId())) {
            mapper.update(updatedDocument, conn);
        }
    }

    @Override
    public void close() throws Exception {
        lockManager.releaseLock(id, Thread.currentThread().getName());
    }
}
