package mirea.db.service_center.repository;

import mirea.db.service_center.model.OrdersService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersServiceRepository extends JpaRepository<OrdersService, Integer> {
}