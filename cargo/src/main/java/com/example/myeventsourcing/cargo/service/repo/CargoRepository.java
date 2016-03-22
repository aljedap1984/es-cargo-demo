package com.example.myeventsourcing.cargo.service.repo;

import com.example.myeventsourcing.cargo.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrador on 14/03/2016.
 */
public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
