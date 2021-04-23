package com.paxian.security;

import com.paxian.db.DataRepository;
import com.paxian.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    public DataRepository dataRepository;

    public UserPrincipalDetailsService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel user = this.dataRepository.findByUsername(s);

        return new UserPrincipal(user);
    }
}
