package com.wotege.controller;

import com.wotege.dto.ApiResponse;
import com.wotege.dto.CustomerRequestDTO;
import com.wotege.dto.CustomerResponseDTO;
import com.wotege.dto.DashboardStatisticsDTO;
import com.wotege.enums.CustomerStatus;
import com.wotege.enums.LoyaltyTier;
import com.wotege.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO customer = customerService.createCustomer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Customer created successfully", customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO customer = customerService.updateCustomer(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        CustomerStatus status = CustomerStatus.valueOf(body.get("status"));
        CustomerResponseDTO customer = customerService.changeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", customer));
    }

    @GetMapping("/tier/{tier}")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getCustomersByTier(@PathVariable LoyaltyTier tier) {
        List<CustomerResponseDTO> customers = customerService.getCustomersByTier(tier);
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("/vips")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getVipCustomers() {
        List<CustomerResponseDTO> customers = customerService.getVipCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> searchCustomers(@RequestParam String keyword) {
        List<CustomerResponseDTO> customers = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatisticsDTO>> getDashboardStatistics() {
        DashboardStatisticsDTO stats = customerService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
