package mirea.db.service_center.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders_services")
@Getter
@Setter
public class OrdersService implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private Service service;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spare_parts_id", referencedColumnName = "id")
    private SparePart sparePart;
    private boolean finished;
}