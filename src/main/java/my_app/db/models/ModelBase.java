package my_app.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ModelBase<Dto> {
    ModelBase fromResultSet(ResultSet queryResultSet) throws SQLException;
    ModelBase fromIdAndDto(Long id, Dto dto);
}
