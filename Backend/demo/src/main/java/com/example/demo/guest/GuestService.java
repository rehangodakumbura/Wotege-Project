package com.example.demo.guest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Transactional(readOnly = true)
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Guest findById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
    }

    @Transactional(readOnly = true)
    public List<Guest> search(String q) {
        return guestRepository.search(q);
    }

    public Guest create(Guest payload) {
        Guest guest = new Guest();
        apply(payload, guest);
        return guestRepository.save(guest);
    }

    public Guest update(Long id, Guest payload) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        apply(payload, existing);
        return guestRepository.save(existing);
    }

    public void delete(Long id) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        guestRepository.delete(existing);
    }

    private void apply(Guest payload, Guest target) {
        target.setFullName(payload.getFullName());
        target.setPhone(payload.getPhone());
        target.setEmail(payload.getEmail());
        target.setNicPassport(payload.getNicPassport());
        target.setNationality(payload.getNationality());
        target.setDateOfBirth(payload.getDateOfBirth());
        target.setGender(payload.getGender());
    }
}
