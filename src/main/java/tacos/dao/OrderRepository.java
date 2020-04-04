package tacos.dao;

import tacos.domain.Order;

public interface OrderRepository {

    Order save(Order order);
}
