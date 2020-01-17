package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShipService {
    Page<Ship> getAllShips(Specification<Ship> shipSpecification, Pageable sortedByName);
    List<Ship> getAllShips(Specification<Ship> shipSpecification);
    Ship createShip(Ship ship);
    Ship getShip(Long id);
    Ship editShip(Long id, Ship ship);
    void deleteById(Long id);
    Long checkAndParseId(String id);
}
