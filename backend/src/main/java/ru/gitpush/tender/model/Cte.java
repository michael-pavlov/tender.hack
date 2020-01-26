package ru.gitpush.tender.model;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
@Table(name="cte")
public class Cte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cte_id")
    private Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "cte_type", nullable = false)
    public String cteType;

    @Column(name = "ram", nullable = false)
    public Integer ram;

    @Column(name = "hdd", nullable = false)
    public Integer hdd;

    @Column(name = "resolution", nullable = false)
    public String resolution;

    @Column(name = "generation", nullable = false)
    public Integer generation;
}
