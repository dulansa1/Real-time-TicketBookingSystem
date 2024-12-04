package com.backend.TicketingSystem.entity;
import jakarta.persistence.*;

@Entity
//@Table(name="ticket")

public class Ticket {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="ticket_id") //column in the database (not necessary)
    private int ticketId;

}
