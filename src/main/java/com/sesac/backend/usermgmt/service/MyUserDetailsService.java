package com.sesac.backend.usermgmt.service;


import com.sesac.backend.usermgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String userNumber) throws UsernameNotFoundException {
        if(repo.existsByUserNumber(userNumber)) {
            return repo.findByUserNumber(userNumber);
        }
        return null;
    }
}