package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Address;
import com.ecommerce.ecommerce_backend.model.User;
import com.ecommerce.ecommerce_backend.repository.AddressRepository;
import com.ecommerce.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public Address addAddress(String email, Address address){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(address.isDefault()){
            // unset previous default
            List<Address> list = addressRepository.findByUserEmail(email);
            list.forEach(a -> a.setDefault(false));
            addressRepository.saveAll(list);
        }

        address.setUser(user);
        return addressRepository.save(address);
    }

    public List<Address> getMyAddresses(String email){
        return addressRepository.findByUserEmail(email);
    }



public Address setDefault(Long id, String email){

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if(!address.getUser().equals(user)){
        throw new RuntimeException("You cannot modify this address");
    }

    // remove old default
    addressRepository.findByUserAndIsDefaultTrue(user)
            .ifPresent(a -> {
                a.setDefault(false);
                addressRepository.save(a);
            });

    address.setDefault(true);
    return addressRepository.save(address);
}


public Address updateAddress(Long id, Address updated, String email){

    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    var address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if(!address.getUser().getId().equals(user.getId()))
        throw new RuntimeException("Not your address");

    address.setFullName(updated.getFullName());
    address.setPhone(updated.getPhone());
    address.setStreet(updated.getStreet());
    address.setCity(updated.getCity());
    address.setState(updated.getState());
    address.setCountry(updated.getCountry());
    address.setZipCode(updated.getZipCode());

    return addressRepository.save(address);
}

public void deleteAddress(Long id, String email) {
    Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));

    if (!address.getUser().getEmail().equals(email)) {
        throw new RuntimeException("You cannot delete this address");
    }

    addressRepository.delete(address);
}



}
