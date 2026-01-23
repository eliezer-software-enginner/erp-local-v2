package my_app.db.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ModelBase<Dto> {
    public Long id;
    public Long dataCriacao;

    abstract ModelBase<?> fromResultSet(ResultSet queryResultSet) throws SQLException;
    abstract ModelBase<?> fromIdAndDto(Long id, Dto dto);
}
