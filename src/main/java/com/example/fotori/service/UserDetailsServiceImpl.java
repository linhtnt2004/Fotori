package com.example.fotori.service;

import com.example.fotori.model.Customer;
import com.example.fotori.model.Staff;
import com.example.fotori.repository.CustomerRepository;
import com.example.fotori.repository.StaffRepository;
import com.example.fotori.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public UserDetailsServiceImpl(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("JWT EMAIL = " + email);

        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer != null) {
            if (!customer.getIsActive()) {
                throw new UsernameNotFoundException("Customer account is inactive");
            }
            return new UserPrincipal(
                customer.getCustomerId(),
                customer.getEmail(),
                customer.getPasswordHash()
            );
        }

        Staff staff = staffRepository.findByEmail(email).orElse(null);
        if (staff != null) {
            if (!staff.getIsActive()) {
                throw new UsernameNotFoundException("Staff account is inactive");
            }
            return new UserPrincipal(
                staff.getStaffId(),
                staff.getEmail(),
                staff.getPasswordHash(),
                staff.getRole()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
