package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Page<Ship> getAllShips(Specification<Ship> shipSpecification, Pageable sortedByName) {
        return shipRepository.findAll(shipSpecification,sortedByName);
    }

    @Override
    public List<Ship> getAllShips(Specification<Ship> shipSpecification) {
        return shipRepository.findAll(shipSpecification);
    }

    @Override
    public Ship createShip(Ship ship) {
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Ship getShip(Long id) {
        return shipRepository.findById(id).get();
    }

    @Override
    public Ship editShip(Long id, Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public void deleteById(Long id) {
        if (shipRepository.existsById(id)) shipRepository.deleteById(id);
    }

    @Override
    public Long checkAndParseId(String id) {
        return null;
    }

    private Double calcRating(Ship ship){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        //вычисляем рейтинг по заданной формуле
        /*
R=(80·v·k)/y 0 −y 1 +1,
где:
v — скорость корабля;
k — коэффициент, который равен 1 для нового корабля и 0,5 для
использованного;
y 0 — текущий год (не забудь, что «сейчас» 3019 год);
y 1 — год выпуска корабля.
         */
        BigDecimal rating = new BigDecimal((80*ship.getSpeed()*(ship.getUsed() ? 0.5 : 1)) / (3019 - year +1));
        // округляем до сотых
        rating = rating.setScale(2, RoundingMode.HALF_UP);

        return rating.doubleValue();
    }
}
