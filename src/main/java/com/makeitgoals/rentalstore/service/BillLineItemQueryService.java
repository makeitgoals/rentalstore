package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.domain.*; // for static metamodels
import com.makeitgoals.rentalstore.domain.BillLineItem;
import com.makeitgoals.rentalstore.repository.BillLineItemRepository;
import com.makeitgoals.rentalstore.service.criteria.BillLineItemCriteria;
import com.makeitgoals.rentalstore.service.dto.BillLineItemDTO;
import com.makeitgoals.rentalstore.service.mapper.BillLineItemMapper;
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
 * Service for executing complex queries for {@link BillLineItem} entities in the database.
 * The main input is a {@link BillLineItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillLineItemDTO} or a {@link Page} of {@link BillLineItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillLineItemQueryService extends QueryService<BillLineItem> {

    private final Logger log = LoggerFactory.getLogger(BillLineItemQueryService.class);

    private final BillLineItemRepository billLineItemRepository;

    private final BillLineItemMapper billLineItemMapper;

    public BillLineItemQueryService(BillLineItemRepository billLineItemRepository, BillLineItemMapper billLineItemMapper) {
        this.billLineItemRepository = billLineItemRepository;
        this.billLineItemMapper = billLineItemMapper;
    }

    /**
     * Return a {@link List} of {@link BillLineItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillLineItemDTO> findByCriteria(BillLineItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BillLineItem> specification = createSpecification(criteria);
        return billLineItemMapper.toDto(billLineItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillLineItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillLineItemDTO> findByCriteria(BillLineItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BillLineItem> specification = createSpecification(criteria);
        return billLineItemRepository.findAll(specification, page).map(billLineItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillLineItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BillLineItem> specification = createSpecification(criteria);
        return billLineItemRepository.count(specification);
    }

    /**
     * Function to convert {@link BillLineItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BillLineItem> createSpecification(BillLineItemCriteria criteria) {
        Specification<BillLineItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BillLineItem_.id));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), BillLineItem_.details));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), BillLineItem_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), BillLineItem_.toDate));
            }
            if (criteria.getOutstandingQuantity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutstandingQuantity(), BillLineItem_.outstandingQuantity));
            }
            if (criteria.getLineAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLineAmount(), BillLineItem_.lineAmount));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(BillLineItem_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getBillId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillId(), root -> root.join(BillLineItem_.bill, JoinType.LEFT).get(Bill_.id))
                    );
            }
            if (criteria.getRentalOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRentalOrderId(),
                            root -> root.join(BillLineItem_.rentalOrder, JoinType.LEFT).get(RentalOrder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
