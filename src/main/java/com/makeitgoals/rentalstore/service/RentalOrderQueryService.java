package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.domain.*; // for static metamodels
import com.makeitgoals.rentalstore.domain.RentalOrder;
import com.makeitgoals.rentalstore.repository.RentalOrderRepository;
import com.makeitgoals.rentalstore.service.criteria.RentalOrderCriteria;
import com.makeitgoals.rentalstore.service.dto.RentalOrderDTO;
import com.makeitgoals.rentalstore.service.mapper.RentalOrderMapper;
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
 * Service for executing complex queries for {@link RentalOrder} entities in the database.
 * The main input is a {@link RentalOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RentalOrderDTO} or a {@link Page} of {@link RentalOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RentalOrderQueryService extends QueryService<RentalOrder> {

    private final Logger log = LoggerFactory.getLogger(RentalOrderQueryService.class);

    private final RentalOrderRepository rentalOrderRepository;

    private final RentalOrderMapper rentalOrderMapper;

    public RentalOrderQueryService(RentalOrderRepository rentalOrderRepository, RentalOrderMapper rentalOrderMapper) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    /**
     * Return a {@link List} of {@link RentalOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RentalOrderDTO> findByCriteria(RentalOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RentalOrder> specification = createSpecification(criteria);
        return rentalOrderMapper.toDto(rentalOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RentalOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RentalOrderDTO> findByCriteria(RentalOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RentalOrder> specification = createSpecification(criteria);
        return rentalOrderRepository.findAll(specification, page).map(rentalOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RentalOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RentalOrder> specification = createSpecification(criteria);
        return rentalOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link RentalOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RentalOrder> createSpecification(RentalOrderCriteria criteria) {
        Specification<RentalOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RentalOrder_.id));
            }
            if (criteria.getRentalIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRentalIssueDate(), RentalOrder_.rentalIssueDate));
            }
            if (criteria.getRentalReturnDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRentalReturnDate(), RentalOrder_.rentalReturnDate));
            }
            if (criteria.getRentalOrderStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getRentalOrderStatus(), RentalOrder_.rentalOrderStatus));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), RentalOrder_.code));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(RentalOrder_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getBillLineItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillLineItemId(),
                            root -> root.join(RentalOrder_.billLineItems, JoinType.LEFT).get(BillLineItem_.id)
                        )
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(RentalOrder_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
