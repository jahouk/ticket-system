package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Status;
import com.houkstead.ticketsystem.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByStatusNot(Status status);
}
