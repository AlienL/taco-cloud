package tacos.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.dao.TacoRepository;
import tacos.domain.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JdbcTacoRepository implements TacoRepository {

    private final JdbcTemplate jdbc;

    @Override
    public Taco save(final Taco taco) {
        final long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        taco.getIngredients().forEach(ingredient -> saveIngredientToTaco(ingredient, tacoId));
        return taco;
    }

    private long saveTacoInfo(final Taco taco) {
        final PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory("insert into taco (name, createdAt) values (?,?)", Types.VARCHAR, Types.TIMESTAMP);
        // By default, returnGeneratedKeys = false so change it to true
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        taco.setCreatedAt(new Date());
        final PreparedStatementCreator preparedStatementCreator =
                preparedStatementCreatorFactory.newPreparedStatementCreator(
                        Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbc.update(preparedStatementCreator, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void saveIngredientToTaco(final String ingredient, final long tacoId) {
        this.jdbc.update("insert into taco_ingredients (taco, ingredient) values (?,?)", tacoId, ingredient);
    }
}
