package com.wotege.service;

import com.wotege.dto.TableRequest;
import com.wotege.dto.TableResponse;
import com.wotege.entity.RestaurantTable;
import com.wotege.exception.ResourceNotFoundException;
import com.wotege.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableService {

    private final RestaurantTableRepository tableRepository;

    public List<TableResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TableResponse createTable(TableRequest request) {
        RestaurantTable table = new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setStatus(RestaurantTable.TableStatus.AVAILABLE);
        return toResponse(tableRepository.save(table));
    }

    @Transactional
    public TableResponse updateTable(Long id, TableRequest request) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        table.setTableNumber(request.getTableNumber());
        return toResponse(tableRepository.save(table));
    }

    @Transactional
    public void updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        table.setStatus(status);
        tableRepository.save(table);
    }

    public RestaurantTable findById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    }

    private TableResponse toResponse(RestaurantTable table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .status(table.getStatus().name())
                .build();
    }
}
