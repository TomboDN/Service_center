package mirea.db.service_center.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    private int total_cost;
    @Column(nullable = false)
    private String item;
    @Column(nullable = false)
    private String reason;
    private boolean finished;
    private LocalDateTime creation_date;
    private boolean paid;
    private LocalDateTime finished_date;
}
