package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.TicketUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketUpdateRepository extends JpaRepository<TicketUpdate, Integer> {

}
