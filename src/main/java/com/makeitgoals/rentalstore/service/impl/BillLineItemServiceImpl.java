package com.makeitgoals.rentalstore.service.impl;

import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.repository.BillLineItemRepository;
import com.makeitgoals.rentalstore.service.BillLineItemService;
import com.makeitgoals.rentalstore.service.dto.BillLineItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BillLineItem}.
 */
@Service
@Transactional
public class BillLineItemServiceImpl implements BillLineItemService {

    private final Logger log = LoggerFactory.getLogger(BillLineItemServiceImpl.class);

    private final BillLineItemRepository billLineItemRepository;

    private final BillLineItemMapper billLineItemMapper;

    public BillLineItemServiceImpl(BillLineItemRepository billLineItemRepository, BillLineItemMapper billLineItemMapper) {
        this.billLineItemRepository = billLineItemRepository;
        this.billLineItemMapper = billLineItemMapper;
    }

    @Override
    public BillLineItemDTO save(BillLineItemDTO billLineItemDTO) {
        log.debug("Request to save BillLineItem : {}", billLineItemDTO);
        BillLineItem billLineItem = billLineItemMapper.toEntity(billLineItemDTO);
        billLineItem = billLineItemRepository.save(billLineItem);
        return billLineItemMapper.toDto(billLineItem);
    }

    @Override
    public Optional<BillLineItemDTO> partialUpdate(BillLineItemDTO billLineItemDTO) {
        log.debug("Request to partially update BillLineItem : {}", billLineItemDTO);

        return billLineItemRepository
            .findById(billLineItemDTO.getId())
            .map(
                existingBillLineItem -> {
                    billLineItemMapper.partialUpdate(existingBillLineItem, billLineItemDTO);

                    return existingBillLineItem;
                }
            )
            .map(billLineItemRepository::save)
            .map(billLineItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillLineItemDTO> findAll() {
        log.debug("Request to get all BillLineItems");
        return billLineItemRepository.findAll().stream().map(billLineItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillLineItemDTO> findOne(Long id) {
        log.debug("Request to get BillLineItem : {}", id);
        return billLineItemRepository.findById(id).map(billLineItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillLineItem : {}", id);
        billLineItemRepository.deleteById(id);
    }
}
