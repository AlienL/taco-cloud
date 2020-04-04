package tacos.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.dao.OrderRepository;
import tacos.domain.Order;
import tacos.domain.Taco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final SimpleJdbcInsert orderInserter;
    private final SimpleJdbcInsert orderTacoInserter;
    private final ObjectMapper objectMapper;

    public JdbcOrderRepository(final JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc).withTableName("taco_order").usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc).withTableName("taco_order_tacos");
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public Order save(final Order order) {
        order.setPlacedAt(new Date());
        final long orderId = saveOrderDetails(order);
        order.setId(orderId);
        order.getTacos().forEach(taco -> saveTacoToOrder(orderId, taco));
        return order;
    }

    private long saveOrderDetails(final Order order) {
        final Map<String, Object> values = this.objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());
        return this.orderInserter.executeAndReturnKey(values).longValue();
    }

    private void saveTacoToOrder(final long orderId, final Taco taco) {
        final Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        this.orderTacoInserter.execute(values);
    }
}
