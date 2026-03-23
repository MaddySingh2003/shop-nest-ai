package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.model.Address;
import com.ecommerce.ecommerce_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<Address> add(@RequestBody Address address){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return ResponseEntity.ok(addressService.addAddress(email, address));
    }

    @GetMapping("/my")
    public List<Address> myAddresses(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return addressService.getMyAddresses(email);
    }
    
    @PutMapping("/set-default/{id}")
public ResponseEntity<?> setDefault(@PathVariable Long id) {
    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();

    return ResponseEntity.ok(addressService.setDefault(id, email));
}


@PutMapping("/update/{id}")
public ResponseEntity<?> updateAddress(
        @PathVariable Long id,
        @RequestBody Address updated,
        Authentication auth){

    String email = auth.getName();
    return ResponseEntity.ok(addressService.updateAddress(id, updated, email));
}


@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
    String email = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();

    addressService.deleteAddress(id, email);
    return ResponseEntity.ok("Address deleted successfully");
}



    
}
