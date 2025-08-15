package com.project.yuklet.services;

import com.project.yuklet.dto.TransportOfferDto;
import com.project.yuklet.entities.RequestStatus;
import com.project.yuklet.entities.TransportOffer;
import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserType;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.exception.UnauthorizedException;
import com.project.yuklet.reporsitory.TransportOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransportOfferService {
    
    @Autowired
    private TransportOfferRepository transportOfferRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;
    
    public TransportOffer createTransportOffer(TransportOfferDto transportOfferDto) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.getUserType() != UserType.CARRIER) {
            throw new UnauthorizedException("Only carriers can create transport offers");
        }
        
        TransportOffer transportOffer = new TransportOffer(currentUser.getId(),
                transportOfferDto.getVehicleId(),
                transportOfferDto.getFromCity(),
                transportOfferDto.getToCity(),
                transportOfferDto.getAvailableDate(),
                transportOfferDto.getAvailableWeightKg());
        
        transportOffer.setSuggestedPrice(transportOfferDto.getSuggestedPrice());
        transportOffer.setDescription(transportOfferDto.getDescription());
        
        TransportOffer savedOffer = transportOfferRepository.save(transportOffer);
        
        notificationService.notifyShippersAboutNewOffer(savedOffer);
        
        return savedOffer;
    }
    
    public TransportOffer updateTransportOffer(Long offerId, TransportOfferDto transportOfferDto) {
        User currentUser = userService.getCurrentUser();
        TransportOffer transportOffer = transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
        
        if (!transportOffer.getCarrierId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own transport offers");
        }
        
        if (transportOffer.getStatus() != RequestStatus.ACTIVE) {
            throw new UnauthorizedException("Cannot update completed or cancelled transport offers");
        }
        
        transportOffer.setVehicleId(transportOfferDto.getVehicleId());
        transportOffer.setFromCity(transportOfferDto.getFromCity());
        transportOffer.setToCity(transportOfferDto.getToCity());
        transportOffer.setAvailableDate(transportOfferDto.getAvailableDate());
        transportOffer.setAvailableWeightKg(transportOfferDto.getAvailableWeightKg());
        transportOffer.setSuggestedPrice(transportOfferDto.getSuggestedPrice());
        transportOffer.setDescription(transportOfferDto.getDescription());
        
        return transportOfferRepository.save(transportOffer);
    }
    
    public List<TransportOffer> getMyTransportOffers() {
        User currentUser = userService.getCurrentUser();
        return transportOfferRepository.findByCarrierId(currentUser.getId());
    }
    
    public List<TransportOffer> searchTransportOffers(String fromCity, String toCity, 
                                                     Integer minWeight, BigDecimal maxPrice, 
                                                     LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromCity != null && toCity != null) {
            return transportOfferRepository.findByRouteAndStatus(fromCity, toCity, RequestStatus.ACTIVE);
        } else if (minWeight != null) {
            return transportOfferRepository.findByMinWeightAndStatus(minWeight, RequestStatus.ACTIVE);
        } else if (maxPrice != null) {
            return transportOfferRepository.findByMaxPriceAndStatus(maxPrice, RequestStatus.ACTIVE);
        } else if (fromDate != null && toDate != null) {
            return transportOfferRepository.findByDateRangeAndStatus(fromDate, toDate, RequestStatus.ACTIVE);
        } else {
            return transportOfferRepository.findByStatus(RequestStatus.ACTIVE);
        }
    }
    
    public TransportOffer getTransportOffer(Long offerId) {
        return transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
    }
    
    public void cancelTransportOffer(Long offerId) {
        User currentUser = userService.getCurrentUser();
        TransportOffer transportOffer = transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
        
        if (!transportOffer.getCarrierId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only cancel your own transport offers");
        }
        
        transportOffer.setStatus(RequestStatus.CANCELLED);
        transportOfferRepository.save(transportOffer);
    }
    
    public void completeTransportOffer(Long offerId) {
        User currentUser = userService.getCurrentUser();
        TransportOffer transportOffer = transportOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("TransportOffer", "id", offerId));
        
        if (!transportOffer.getCarrierId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only complete your own transport offers");
        }
        
        transportOffer.setStatus(RequestStatus.COMPLETED);
        transportOfferRepository.save(transportOffer);
    }
}
