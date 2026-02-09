package my_app.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ModelBase<Dto> {
    public Long id;
    public Long dataCriacao;

    abstract public ModelBase<?> fromResultSet(ResultSet queryResultSet) throws SQLException;
    @Deprecated
    abstract public ModelBase<?> fromIdAndDto(Long id, Dto dto);
    public ModelBase<?> fromIdAndDtoAndMillis(Long id, Dto dto, long millis){
        this.id = id;
        this.dataCriacao = millis;
        return this;
    }
}
