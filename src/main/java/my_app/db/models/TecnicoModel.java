package my_app.db.models;

import my_app.db.dto.TecnicoDto;
import my_app.domain.ModelBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TecnicoModel extends ModelBase<TecnicoDto> {
    //TODO: id de quem criar esse tecnico
    public String nome;

    public TecnicoModel fromResultSet(ResultSet rs) throws SQLException {
        var model  = new TecnicoModel();
        model.id = rs.getLong("id");
        model.nome = rs.getString("nome");
        model.dataCriacao = rs.getLong("data_criacao");
       return model;
    }

    @Override
    public TecnicoModel fromIdAndDto(Long id, TecnicoDto tecnicoDto) {
       var model = new TecnicoModel();
       model.id = id;
       model.nome = tecnicoDto.nome();
      // model.dataCriacao = tecnicoDto.
       return model;
    }

    @Override
    public TecnicoModel fromIdAndDtoAndMillis(Long id, TecnicoDto tecnicoDto, long millis) {
        var model = (TecnicoModel) super.fromIdAndDtoAndMillis(id, tecnicoDto, millis);
        model.nome = tecnicoDto.nome();
        return model;
    }
}



