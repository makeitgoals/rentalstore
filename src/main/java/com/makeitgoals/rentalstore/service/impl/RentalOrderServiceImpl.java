package com.makeitgoals.rentalstore.service.impl;

import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.RentalOrderService;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import com.makeitgoals.rentalstore.service.mapper.RentalOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RentalOrder}.
 */
@Service
@Transactional
public class RentalOrderServiceImpl implements RentalOrderService {

    private final Logger log = LoggerFactory.getLogger(RentalOrderServiceImpl.class);

    private final RentalOrderRepository rentalOrderRepository;

    private final RentalOrderMapper rentalOrderMapper;

    public RentalOrderServiceImpl(RentalOrderRepository rentalOrderRepository, RentalOrderMapper rentalOrderMapper) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    @Override
    public RentalOrderDTO save(RentalOrderDTO rentalOrderDTO) {
        log.debug("Request to save RentalOrder : {}", rentalOrderDTO);
        RentalOrder rentalOrder = rentalOrderMapper.toEntity(rentalOrderDTO);
        rentalOrder = rentalOrderRepository.save(rentalOrder);
        return rentalOrderMapper.toDto(rentalOrder);
    }

    @Override
    public Optional<RentalOrderDTO> partialUpdate(RentalOrderDTO rentalOrderDTO) {
        log.debug("Request to partially update RentalOrder : {}", rentalOrderDTO);

        return rentalOrderRepository
            .findById(rentalOrderDTO.getId())
            .map(
                existingRentalOrder -> {
                    rentalOrderMapper.partialUpdate(existingRentalOrder, rentalOrderDTO);

                    return existingRentalOrder;
                }
            )
            .map(rentalOrderRepository::save)
            .map(rentalOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RentalOrders");
        return rentalOrderRepository.findAll(pageable).map(rentalOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentalOrderDTO> findOne(Long id) {
        log.debug("Request to get RentalOrder : {}", id);
        return rentalOrderRepository.findById(id).map(rentalOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RentalOrder : {}", id);
        rentalOrderRepository.deleteById(id);
    }
}
