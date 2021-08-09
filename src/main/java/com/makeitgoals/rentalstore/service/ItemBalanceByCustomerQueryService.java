package com.makeitgoals.rentalstore.service;

import com.makeitgoals.rentalstore.domain.*; // for static metamodels
import com.makeitgoals.rentalstore.domain.ItemBalanceByCustomer;
import com.makeitgoals.rentalstore.repository.ItemBalanceByCustomerRepository;
import com.makeitgoals.rentalstore.service.criteria.ItemBalanceByCustomerCriteria;
import com.makeitgoals.rentalstore.service.dto.ItemBalanceByCustomerDTO;
import com.makeitgoals.rentalstore.service.mapper.ItemBalanceByCustomerMapper;
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
 * Service for executing complex queries for {@link ItemBalanceByCustomer} entities in the database.
 * The main input is a {@link ItemBalanceByCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemBalanceByCustomerDTO} or a {@link Page} of {@link ItemBalanceByCustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemBalanceByCustomerQueryService extends QueryService<ItemBalanceByCustomer> {

    private final Logger log = LoggerFactory.getLogger(ItemBalanceByCustomerQueryService.class);

    private final ItemBalanceByCustomerRepository itemBalanceByCustomerRepository;

    private final ItemBalanceByCustomerMapper itemBalanceByCustomerMapper;

    public ItemBalanceByCustomerQueryService(
        ItemBalanceByCustomerRepository itemBalanceByCustomerRepository,
        ItemBalanceByCustomerMapper itemBalanceByCustomerMapper
    ) {
        this.itemBalanceByCustomerRepository = itemBalanceByCustomerRepository;
        this.itemBalanceByCustomerMapper = itemBalanceByCustomerMapper;
    }

    /**
     * Return a {@link List} of {@link ItemBalanceByCustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemBalanceByCustomerDTO> findByCriteria(ItemBalanceByCustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemBalanceByCustomer> specification = createSpecification(criteria);
        return itemBalanceByCustomerMapper.toDto(itemBalanceByCustomerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItemBalanceByCustomerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemBalanceByCustomerDTO> findByCriteria(ItemBalanceByCustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemBalanceByCustomer> specification = createSpecification(criteria);
        return itemBalanceByCustomerRepository.findAll(specification, page).map(itemBalanceByCustomerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemBalanceByCustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ItemBalanceByCustomer> specification = createSpecification(criteria);
        return itemBalanceByCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemBalanceByCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ItemBalanceByCustomer> createSpecification(ItemBalanceByCustomerCriteria criteria) {
        Specification<ItemBalanceByCustomer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ItemBalanceByCustomer_.id));
            }
            if (criteria.getOutstandingBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutstandingBalance(), ItemBalanceByCustomer_.outstandingBalance));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(ItemBalanceByCustomer_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(ItemBalanceByCustomer_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(ItemBalanceByCustomer_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
