package com.acme.ex3.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.acme.ex3.model.Reservation;

public interface ReservationRepository extends R2dbcRepository<Reservation, Integer> {

}
