package com.makeitgoals.rentalstore.service.impl;

import com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer;
import com.makeitgoals.rentalstore.repository.ItemBalanceByCustomerRepository;
import com.makeitgoals.rentalstore.service.ItemBalanceByCustomerService;
import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import com.makeitgoals.rentalstore.service.mapper.ItemBalanceByCustomerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemBalanceByCustomer}.
 */
@Service
@Transactional
public class ItemBalanceByCustomerServiceImpl implements ItemBalanceByCustomerService {

    private final Logger log = LoggerFactory.getLogger(ItemBalanceByCustomerServiceImpl.class);

    private final ItemBalanceByCustomerRepository itemBalanceByCustomerRepository;

    private final ItemBalanceByCustomerMapper itemBalanceByCustomerMapper;

    public ItemBalanceByCustomerServiceImpl(
        ItemBalanceByCustomerRepository itemBalanceByCustomerRepository,
        ItemBalanceByCustomerMapper itemBalanceByCustomerMapper
    ) {
        this.itemBalanceByCustomerRepository = itemBalanceByCustomerRepository;
        this.itemBalanceByCustomerMapper = itemBalanceByCustomerMapper;
    }

    @Override
    public ItemBalanceByCustomerDTO save(ItemBalanceByCustomerDTO itemBalanceByCustomerDTO) {
        log.debug("Request to save ItemBalanceByCustomer : {}", itemBalanceByCustomerDTO);
        ItemBalanceByCustomer itemBalanceByCustomer = itemBalanceByCustomerMapper.toEntity(itemBalanceByCustomerDTO);
        itemBalanceByCustomer = itemBalanceByCustomerRepository.save(itemBalanceByCustomer);
        return itemBalanceByCustomerMapper.toDto(itemBalanceByCustomer);
    }

    @Override
    public Optional<ItemBalanceByCustomerDTO> partialUpdate(ItemBalanceByCustomerDTO itemBalanceByCustomerDTO) {
        log.debug("Request to partially update ItemBalanceByCustomer : {}", itemBalanceByCustomerDTO);

        return itemBalanceByCustomerRepository
            .findById(itemBalanceByCustomerDTO.getId())
            .map(
                existingItemBalanceByCustomer -> {
                    itemBalanceByCustomerMapper.partialUpdate(existingItemBalanceByCustomer, itemBalanceByCustomerDTO);

                    return existingItemBalanceByCustomer;
                }
            )
            .map(itemBalanceByCustomerRepository::save)
            .map(itemBalanceByCustomerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemBalanceByCustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemBalanceByCustomers");
        return itemBalanceByCustomerRepository.findAll(pageable).map(itemBalanceByCustomerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemBalanceByCustomerDTO> findOne(Long id) {
        log.debug("Request to get ItemBalanceByCustomer : {}", id);
        return itemBalanceByCustomerRepository.findById(id).map(itemBalanceByCustomerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemBalanceByCustomer : {}", id);
        itemBalanceByCustomerRepository.deleteById(id);
    }
}
