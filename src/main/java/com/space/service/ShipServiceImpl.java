package com.space.service;

import com.space.exception.BadRequestException;
import com.space.exception.ShipNotFoundException;
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
        if (ship.getName() == null ||
                ship.getPlanet() == null ||
                ship.getShipType() == null ||
                ship.getProdDate() == null ||
                ship.getSpeed() == null ||
                ship.getCrewSize() == null
        ) throw new BadRequestException("Один из парамметров не определен");

        shipParams(ship);

        if (ship.getUsed() == null) ship.setUsed(false);

        ship.setRating(calcRating(ship));

        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Ship getShip(Long id) {
        if(!shipRepository.existsById(id)) throw new ShipNotFoundException("Корабль не найден!");
        return shipRepository.findById(id).get();
    }

    @Override
    public Ship editShip(Long id, Ship ship) {

        shipParams(ship);

        Ship editedShip = shipRepository.findById(id).get();

        if (ship.getName() != null)
            editedShip.setName(ship.getName());

        if (ship.getPlanet() != null)
            editedShip.setPlanet(ship.getPlanet());

        if (ship.getShipType() != null)
            editedShip.setShipType(ship.getShipType());

        if (ship.getProdDate() != null)
            editedShip.setProdDate(ship.getProdDate());

        if (ship.getSpeed() != null)
            editedShip.setSpeed(ship.getSpeed());

        if (ship.getUsed() != null)
            editedShip.setUsed(ship.getUsed());

        if (ship.getCrewSize() != null)
            editedShip.setCrewSize(ship.getCrewSize());

        Double rating = calcRating(editedShip);
        editedShip.setRating(rating);


        return shipRepository.save(editedShip);
    }

    @Override
    public void deleteById(Long id) {
        if (shipRepository.existsById(id)) shipRepository.deleteById(id);
        else throw new ShipNotFoundException("Корабль не найден");
    }

    //проверка id
    @Override
    public Long checkAndParseId(String id) {
        if (id == null || id.equals("") || id.equals("0"))
            throw new BadRequestException("Некорректный ID");

        try {
            Long longId = Long.parseLong(id);
            return longId;
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID не является числом", e);
        }
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

    //определяем заданные параметры
    /*
  определяем для корабля параметры и их диапазон и допустимые границы значений
     */
    private void shipParams(Ship ship) {

        if (ship.getName() != null && (ship.getName().length() < 1 || ship.getName().length() > 50))
            throw new BadRequestException("Incorrect name");

        if (ship.getPlanet() != null && (ship.getPlanet().length() < 1 || ship.getPlanet().length() > 50))
            throw new BadRequestException("Incorrect planet");

        if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            throw new BadRequestException("Incorrect crewSize");

        if (ship.getSpeed() != null && (ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D))
            throw new BadRequestException("Incorrect speed");

        if (ship.getProdDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ship.getProdDate());
            if (cal.get(Calendar.YEAR) < 2800 || cal.get(Calendar.YEAR) > 3019)
                throw new BadRequestException("Incorrect date");
        }
    }

}
