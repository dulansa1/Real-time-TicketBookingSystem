package com.backend.TicketingSystem.entity;
import jakarta.persistence.*;

@Entity
@Table(name="vendor")

public class Vendor {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="vendor_id") //column in the database (not necessary)
    private int vendorId;

}
