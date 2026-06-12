package com.wotege.hotel.service;

import com.wotege.hotel.dto.cleaning.CleaningRequest;
import com.wotege.hotel.dto.cleaning.CleaningResponse;

import java.util.List;

public interface CleaningService {

    List<CleaningResponse> getAllCleanings();

    CleaningResponse createCleaning(CleaningRequest request);

    CleaningResponse updateCleaning(Long id, String status);
}
