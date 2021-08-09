package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.domain.*; // for static metamodels
import com.makeitgoals.rentalstore.domain.BillLineItemToOrderItem;
import com.makeitgoals.rentalstore.repository.BillLineItemToOrderItemRepository;
import com.makeitgoals.rentalstore.service.criteria.BillLineItemToOrderItemCriteria;
import com.makeitgoals.rentalstore.service.dto.BillLineItemToOrderItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemToOrderItemMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BillLineItemToOrderItem} entities in the database.
 * The main input is a {@link BillLineItemToOrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillLineItemToOrderItemDTO} or a {@link Page} of {@link BillLineItemToOrderItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillLineItemToOrderItemQueryService extends QueryService<BillLineItemToOrderItem> {

    private final Logger log = LoggerFactory.getLogger(BillLineItemToOrderItemQueryService.class);

    private final BillLineItemToOrderItemRepository billLineItemToOrderItemRepository;

    private final BillLineItemToOrderItemMapper billLineItemToOrderItemMapper;

    public BillLineItemToOrderItemQueryService(
        BillLineItemToOrderItemRepository billLineItemToOrderItemRepository,
        BillLineItemToOrderItemMapper billLineItemToOrderItemMapper
    ) {
        this.billLineItemToOrderItemRepository = billLineItemToOrderItemRepository;
        this.billLineItemToOrderItemMapper = billLineItemToOrderItemMapper;
    }

    /**
     * Return a {@link List} of {@link BillLineItemToOrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillLineItemToOrderItemDTO> findByCriteria(BillLineItemToOrderItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BillLineItemToOrderItem> specification = createSpecification(criteria);
        return billLineItemToOrderItemMapper.toDto(billLineItemToOrderItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillLineItemToOrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillLineItemToOrderItemDTO> findByCriteria(BillLineItemToOrderItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BillLineItemToOrderItem> specification = createSpecification(criteria);
        return billLineItemToOrderItemRepository.findAll(specification, page).map(billLineItemToOrderItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillLineItemToOrderItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BillLineItemToOrderItem> specification = createSpecification(criteria);
        return billLineItemToOrderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link BillLineItemToOrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BillLineItemToOrderItem> createSpecification(BillLineItemToOrderItemCriteria criteria) {
        Specification<BillLineItemToOrderItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BillLineItemToOrderItem_.id));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), BillLineItemToOrderItem_.details));
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(BillLineItemToOrderItem_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getRentalOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRentalOrderId(),
                            root -> root.join(BillLineItemToOrderItem_.rentalOrder, JoinType.LEFT).get(RentalOrder_.id)
                        )
                    );
            }
            if (criteria.getBillLineItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillLineItemId(),
                            root -> root.join(BillLineItemToOrderItem_.billLineItem, JoinType.LEFT).get(BillLineItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
