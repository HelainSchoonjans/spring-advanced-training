package com.acme.ex2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.ex2.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}
