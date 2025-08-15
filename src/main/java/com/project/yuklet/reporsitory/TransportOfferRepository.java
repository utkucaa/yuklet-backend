package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.TransportOffer;
import com.project.yuklet.entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransportOfferRepository extends JpaRepository<TransportOffer, Long> {
    
    List<TransportOffer> findByCarrierId(Long carrierId);
    
    List<TransportOffer> findByStatus(RequestStatus status);
    
    List<TransportOffer> findByFromCity(String fromCity);
    
    List<TransportOffer> findByToCity(String toCity);
    
    @Query("SELECT t FROM TransportOffer t WHERE t.fromCity = :fromCity AND t.toCity = :toCity AND t.status = :status")
    List<TransportOffer> findByRouteAndStatus(String fromCity, String toCity, RequestStatus status);
    
    @Query("SELECT t FROM TransportOffer t WHERE t.availableWeightKg >= :minWeight AND t.status = :status")
    List<TransportOffer> findByMinWeightAndStatus(Integer minWeight, RequestStatus status);
    
    @Query("SELECT t FROM TransportOffer t WHERE t.suggestedPrice <= :maxPrice AND t.status = :status")
    List<TransportOffer> findByMaxPriceAndStatus(BigDecimal maxPrice, RequestStatus status);
    
    @Query("SELECT t FROM TransportOffer t WHERE t.availableDate >= :fromDate AND t.availableDate <= :toDate AND t.status = :status")
    List<TransportOffer> findByDateRangeAndStatus(LocalDateTime fromDate, LocalDateTime toDate, RequestStatus status);
}
