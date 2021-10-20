package com.acme.ex1.repository;

import com.acme.ex1.model.Author;
import com.acme.ex1.model.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}
