package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.dto.TransportOfferDto;
import com.project.yuklet.entities.TransportOffer;
import com.project.yuklet.services.TransportOfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transport-offers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransportOfferController {
    
    @Autowired
    private TransportOfferService transportOfferService;
    
    @PostMapping
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<TransportOffer>> createTransportOffer(@Valid @RequestBody TransportOfferDto transportOfferDto) {
        TransportOffer transportOffer = transportOfferService.createTransportOffer(transportOfferDto);
        return ResponseEntity.ok(ApiResponse.success("Transport offer created successfully", transportOffer));
    }
    
    @PutMapping("/{offerId}")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<TransportOffer>> updateTransportOffer(@PathVariable Long offerId,
                                                                           @Valid @RequestBody TransportOfferDto transportOfferDto) {
        TransportOffer transportOffer = transportOfferService.updateTransportOffer(offerId, transportOfferDto);
        return ResponseEntity.ok(ApiResponse.success("Transport offer updated successfully", transportOffer));
    }
    
    @GetMapping("/my")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<List<TransportOffer>>> getMyTransportOffers() {
        List<TransportOffer> transportOffers = transportOfferService.getMyTransportOffers();
        return ResponseEntity.ok(ApiResponse.success(transportOffers));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TransportOffer>>> searchTransportOffers(
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        
        List<TransportOffer> transportOffers = transportOfferService.searchTransportOffers(
                fromCity, toCity, minWeight, maxPrice, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(transportOffers));
    }
    
    @GetMapping("/{offerId}")
    public ResponseEntity<ApiResponse<TransportOffer>> getTransportOffer(@PathVariable Long offerId) {
        TransportOffer transportOffer = transportOfferService.getTransportOffer(offerId);
        return ResponseEntity.ok(ApiResponse.success(transportOffer));
    }
    
    @PutMapping("/{offerId}/cancel")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<String>> cancelTransportOffer(@PathVariable Long offerId) {
        transportOfferService.cancelTransportOffer(offerId);
        return ResponseEntity.ok(ApiResponse.success("Transport offer cancelled successfully", null));
    }
    
    @PutMapping("/{offerId}/complete")
    @PreAuthorize("hasRole('CARRIER')")
    public ResponseEntity<ApiResponse<String>> completeTransportOffer(@PathVariable Long offerId) {
        transportOfferService.completeTransportOffer(offerId);
        return ResponseEntity.ok(ApiResponse.success("Transport offer completed successfully", null));
    }
}
