package tacos.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.dao.IngredientRepository;
import tacos.domain.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JdbcIngredientRepository implements IngredientRepository {

    private final JdbcTemplate jdbc;

    @Override
    public Iterable<Ingredient> findAll() {
        return this.jdbc.query("select id, name, type from ingredient", this::mapRowToIngredient);
    }

    @Override
    public Ingredient findOne(final String id) {
        return this.jdbc.queryForObject("select id, name, type from ingredient where id=?",
                this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(final Ingredient ingredient) {
        this.jdbc.update("insert into ingredient (id, name, type) value (?,?,?)",
                ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new Ingredient(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type"))
        );
    }
}
