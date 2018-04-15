package config.sec;


import dao.ClientRepository;
import model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service(value = "UDS")
public class CustomUDS implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.getByEmail(username);
        if(client == null){
            throw new UsernameNotFoundException(username+" is not found");
        }
        else{
           return new User(client.getEmail(),client.getPassword(), client.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name()))
                   .collect(Collectors.toList()));
        }
    }
}
