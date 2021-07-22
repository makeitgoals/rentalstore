package com.makeitgoals.rentalstore.service.impl;

import com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem;
import com.makeitgoals.rentalstore.repository.BillLineItemToOrderItemRepository;
import com.makeitgoals.rentalstore.service.BillLineItemToOrderItemService;
import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemToOrderItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BillLineItemToOrderItem}.
 */
@Service
@Transactional
public class BillLineItemToOrderItemServiceImpl implements BillLineItemToOrderItemService {

    private final Logger log = LoggerFactory.getLogger(BillLineItemToOrderItemServiceImpl.class);

    private final BillLineItemToOrderItemRepository billLineItemToOrderItemRepository;

    private final BillLineItemToOrderItemMapper billLineItemToOrderItemMapper;

    public BillLineItemToOrderItemServiceImpl(
        BillLineItemToOrderItemRepository billLineItemToOrderItemRepository,
        BillLineItemToOrderItemMapper billLineItemToOrderItemMapper
    ) {
        this.billLineItemToOrderItemRepository = billLineItemToOrderItemRepository;
        this.billLineItemToOrderItemMapper = billLineItemToOrderItemMapper;
    }

    @Override
    public BillLineItemToOrderItemDTO save(BillLineItemToOrderItemDTO billLineItemToOrderItemDTO) {
        log.debug("Request to save BillLineItemToOrderItem : {}", billLineItemToOrderItemDTO);
        BillLineItemToOrderItem billLineItemToOrderItem = billLineItemToOrderItemMapper.toEntity(billLineItemToOrderItemDTO);
        billLineItemToOrderItem = billLineItemToOrderItemRepository.save(billLineItemToOrderItem);
        return billLineItemToOrderItemMapper.toDto(billLineItemToOrderItem);
    }

    @Override
    public Optional<BillLineItemToOrderItemDTO> partialUpdate(BillLineItemToOrderItemDTO billLineItemToOrderItemDTO) {
        log.debug("Request to partially update BillLineItemToOrderItem : {}", billLineItemToOrderItemDTO);

        return billLineItemToOrderItemRepository
            .findById(billLineItemToOrderItemDTO.getId())
            .map(
                existingBillLineItemToOrderItem -> {
                    billLineItemToOrderItemMapper.partialUpdate(existingBillLineItemToOrderItem, billLineItemToOrderItemDTO);

                    return existingBillLineItemToOrderItem;
                }
            )
            .map(billLineItemToOrderItemRepository::save)
            .map(billLineItemToOrderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillLineItemToOrderItemDTO> findAll() {
        log.debug("Request to get all BillLineItemToOrderItems");
        return billLineItemToOrderItemRepository
            .findAll()
            .stream()
            .map(billLineItemToOrderItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillLineItemToOrderItemDTO> findOne(Long id) {
        log.debug("Request to get BillLineItemToOrderItem : {}", id);
        return billLineItemToOrderItemRepository.findById(id).map(billLineItemToOrderItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillLineItemToOrderItem : {}", id);
        billLineItemToOrderItemRepository.deleteById(id);
    }
}
