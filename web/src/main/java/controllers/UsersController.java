package controllers;

import model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.ClientServiceImpl;

@RestController
public class UsersController {

    @Autowired
    private ClientServiceImpl clientService;

    @RequestMapping( value = "/users/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client){
        clientService.addClient(client);
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @RequestMapping(value = "secure/users/getClient")
    public Client getClient(@AuthenticationPrincipal User user){
        if(user == null){
          String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          return clientService.getByEmail(userName);
        }
        return clientService.getByEmail(user.getUsername());
    }
}
