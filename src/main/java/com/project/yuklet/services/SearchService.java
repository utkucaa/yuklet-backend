package com.project.yuklet.services;

import com.project.yuklet.dto.PageResponse;
import com.project.yuklet.dto.SearchCriteria;
import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.entities.RequestStatus;
import com.project.yuklet.entities.TransportOffer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    
    @Autowired
    private EntityManager entityManager;

    
    public PageResponse<CargoRequest> searchCargoRequests(SearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CargoRequest> query = cb.createQuery(CargoRequest.class);
        Root<CargoRequest> root = query.from(CargoRequest.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(cb.equal(root.get("status"), RequestStatus.ACTIVE));
        
        if (criteria.getPickupCity() != null && !criteria.getPickupCity().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("pickupCity")), 
                    "%" + criteria.getPickupCity().toLowerCase() + "%"));
        }
        
        if (criteria.getDeliveryCity() != null && !criteria.getDeliveryCity().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("deliveryCity")), 
                    "%" + criteria.getDeliveryCity().toLowerCase() + "%"));
        }
        
        if (criteria.getMinWeight() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("weightKg"), criteria.getMinWeight()));
        }
        
        if (criteria.getMaxWeight() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("weightKg"), criteria.getMaxWeight()));
        }
        
        if (criteria.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("budgetMin"), criteria.getMinPrice()));
        }
        
        if (criteria.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("budgetMax"), criteria.getMaxPrice()));
        }
        
        if (criteria.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("pickupDate"), criteria.getStartDate()));
        }
        
        if (criteria.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("pickupDate"), criteria.getEndDate()));
        }
        
        if (criteria.getCargoType() != null) {
            predicates.add(cb.equal(root.get("cargoType"), criteria.getCargoType()));
        }
        
        if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
            String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), keyword);
            Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), keyword);
            predicates.add(cb.or(titlePredicate, descriptionPredicate));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        if (criteria.getSortDirection().equalsIgnoreCase("DESC")) {
            query.orderBy(cb.desc(root.get(criteria.getSortBy())));
        } else {
            query.orderBy(cb.asc(root.get(criteria.getSortBy())));
        }
        
        TypedQuery<CargoRequest> countQuery = entityManager.createQuery(query);
        List<CargoRequest> allResults = countQuery.getResultList();
        long totalElements = allResults.size();
        
        TypedQuery<CargoRequest> pagedQuery = entityManager.createQuery(query);
        pagedQuery.setFirstResult(criteria.getPage() * criteria.getSize());
        pagedQuery.setMaxResults(criteria.getSize());
        
        List<CargoRequest> content = pagedQuery.getResultList();
        
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize());
        Page<CargoRequest> page = new PageImpl<>(content, pageRequest, totalElements);
        
        return new PageResponse<>(page);
    }
    
    public PageResponse<TransportOffer> searchTransportOffers(SearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransportOffer> query = cb.createQuery(TransportOffer.class);
        Root<TransportOffer> root = query.from(TransportOffer.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(cb.equal(root.get("status"), RequestStatus.ACTIVE));
        
        if (criteria.getFromCity() != null && !criteria.getFromCity().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("fromCity")), 
                    "%" + criteria.getFromCity().toLowerCase() + "%"));
        }
        
        if (criteria.getToCity() != null && !criteria.getToCity().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("toCity")), 
                    "%" + criteria.getToCity().toLowerCase() + "%"));
        }
        
        if (criteria.getMinWeight() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("availableWeightKg"), criteria.getMinWeight()));
        }
        
        if (criteria.getMaxWeight() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("availableWeightKg"), criteria.getMaxWeight()));
        }
        
        if (criteria.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("suggestedPrice"), criteria.getMaxPrice()));
        }
        
        if (criteria.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("availableDate"), criteria.getStartDate()));
        }
        
        if (criteria.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("availableDate"), criteria.getEndDate()));
        }
        
        if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
            String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("description")), keyword));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        if (criteria.getSortDirection().equalsIgnoreCase("DESC")) {
            query.orderBy(cb.desc(root.get(criteria.getSortBy())));
        } else {
            query.orderBy(cb.asc(root.get(criteria.getSortBy())));
        }
        
        TypedQuery<TransportOffer> countQuery = entityManager.createQuery(query);
        List<TransportOffer> allResults = countQuery.getResultList();
        long totalElements = allResults.size();
                
        TypedQuery<TransportOffer> pagedQuery = entityManager.createQuery(query);
        pagedQuery.setFirstResult(criteria.getPage() * criteria.getSize());
        pagedQuery.setMaxResults(criteria.getSize());
        
        List<TransportOffer> content = pagedQuery.getResultList();
        
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize());
        Page<TransportOffer> page = new PageImpl<>(content, pageRequest, totalElements);
        
        return new PageResponse<>(page);
    }
}
