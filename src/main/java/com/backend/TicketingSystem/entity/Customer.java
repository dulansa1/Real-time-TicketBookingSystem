package com.backend.TicketingSystem.entity;
import jakarta.persistence.*;

@Entity
//@Table(name="customer")

public class Customer {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="customer_id") //column in the database (not necessary)
    private int customerId;

}
