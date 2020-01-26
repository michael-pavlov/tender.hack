package ru.gitpush.tender.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cte_id")
    public Cte cte;

    @Column(name = "count_goods", nullable = false)
    public Integer countGoods;

    @Column(name = "start_time", nullable = false)
    public Long startTime;

    @Column(name = "stop_time", nullable = false)
    public Long stopTime;

    @Column(name = "end_time_order", nullable = false)
    public Long endTimeOrder;

    @Column(name = "cooperative_id")
    private Long cooperativeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
}
