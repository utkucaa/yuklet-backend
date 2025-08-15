package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.PageResponse;
import com.project.yuklet.dto.SearchCriteria;
import com.project.yuklet.entities.CargoRequest;
import com.project.yuklet.entities.CargoType;
import com.project.yuklet.entities.TransportOffer;
import com.project.yuklet.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    @GetMapping("/cargo")
    public ResponseEntity<ApiResponse<PageResponse<CargoRequest>>> searchCargo(
            @RequestParam(required = false) String pickupCity,
            @RequestParam(required = false) String deliveryCity,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) CargoType cargoType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        SearchCriteria criteria = new SearchCriteria();
        criteria.setPickupCity(pickupCity);
        criteria.setDeliveryCity(deliveryCity);
        criteria.setMinWeight(minWeight);
        criteria.setMaxWeight(maxWeight);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        criteria.setCargoType(cargoType);
        criteria.setKeyword(keyword);
        criteria.setSortBy(sortBy);
        criteria.setSortDirection(sortDirection);
        criteria.setPage(page);
        criteria.setSize(size);
        
        PageResponse<CargoRequest> result = searchService.searchCargoRequests(criteria);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @GetMapping("/transport-offers")
    public ResponseEntity<ApiResponse<PageResponse<TransportOffer>>> searchTransportOffers(
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFromCity(fromCity);
        criteria.setToCity(toCity);
        criteria.setMinWeight(minWeight);
        criteria.setMaxWeight(maxWeight);
        criteria.setMaxPrice(maxPrice);
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        criteria.setKeyword(keyword);
        criteria.setSortBy(sortBy);
        criteria.setSortDirection(sortDirection);
        criteria.setPage(page);
        criteria.setSize(size);
        
        PageResponse<TransportOffer> result = searchService.searchTransportOffers(criteria);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @PostMapping("/cargo/advanced")
    public ResponseEntity<ApiResponse<PageResponse<CargoRequest>>> advancedCargoSearch(@RequestBody SearchCriteria criteria) {
        PageResponse<CargoRequest> result = searchService.searchCargoRequests(criteria);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @PostMapping("/transport-offers/advanced")
    public ResponseEntity<ApiResponse<PageResponse<TransportOffer>>> advancedTransportOfferSearch(@RequestBody SearchCriteria criteria) {
        PageResponse<TransportOffer> result = searchService.searchTransportOffers(criteria);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
