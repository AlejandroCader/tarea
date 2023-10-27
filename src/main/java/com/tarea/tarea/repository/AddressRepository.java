package com.tarea.tarea.repository;

import com.tarea.tarea.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
