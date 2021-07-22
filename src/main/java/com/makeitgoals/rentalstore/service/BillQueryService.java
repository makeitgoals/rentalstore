package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.domain.*; // for static metamodels
import com.makeitgoals.rentalstore.domain.Bill;
import com.makeitgoals.rentalstore.repository.BillRepository;
import com.makeitgoals.rentalstore.service.criteria.BillCriteria;
import com.makeitgoals.rentalstore.service.dto.BillDTO;
import com.makeitgoals.rentalstore.service.mapper.BillMapper;
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
 * Service for executing complex queries for {@link Bill} entities in the database.
 * The main input is a {@link BillCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillDTO} or a {@link Page} of {@link BillDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillQueryService extends QueryService<Bill> {

    private final Logger log = LoggerFactory.getLogger(BillQueryService.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    public BillQueryService(BillRepository billRepository, BillMapper billMapper) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
    }

    /**
     * Return a {@link List} of {@link BillDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillDTO> findByCriteria(BillCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bill> specification = createSpecification(criteria);
        return billMapper.toDto(billRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillDTO> findByCriteria(BillCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bill> specification = createSpecification(criteria);
        return billRepository.findAll(specification, page).map(billMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BillCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bill> specification = createSpecification(criteria);
        return billRepository.count(specification);
    }

    /**
     * Function to convert {@link BillCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bill> createSpecification(BillCriteria criteria) {
        Specification<Bill> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bill_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Bill_.code));
            }
            if (criteria.getCostBillNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCostBillNumber(), Bill_.costBillNumber));
            }
            if (criteria.getBillStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getBillStatus(), Bill_.billStatus));
            }
            if (criteria.getBillTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillTotal(), Bill_.billTotal));
            }
            if (criteria.getTaxPercent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxPercent(), Bill_.taxPercent));
            }
            if (criteria.getBillTotalWithTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillTotalWithTax(), Bill_.billTotalWithTax));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Bill_.customer, JoinType.LEFT).get(Customer_.id))
                    );
            }
        }
        return specification;
    }
}
