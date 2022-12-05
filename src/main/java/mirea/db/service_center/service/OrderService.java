package mirea.db.service_center.service;

import lombok.RequiredArgsConstructor;
import mirea.db.service_center.model.Order;
import mirea.db.service_center.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAll(Specification<Order> specification, Pageable pageable) {
        return orderRepository.findAll(specification, pageable).toList();
    }

    public Order findById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public void deleteById(int id) {
        orderRepository.deleteById(id);
    }
}
