package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

}
