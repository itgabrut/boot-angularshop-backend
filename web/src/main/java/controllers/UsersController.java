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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import services.ClientServiceImpl;

import java.util.Map;

@RestController
public class UsersController {

    @Autowired
    private ClientServiceImpl clientService;

    @RequestMapping( value = "/users/register",method = RequestMethod.POST)
    public ResponseEntity<Client> registerClient(@RequestBody Client client){
        if(client.getId() !=0)return new ResponseEntity(null, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
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

    @RequestMapping(value = "secure/users/updateClient",method = RequestMethod.POST)
    public Client updateClient(@RequestBody Client client){
        if(client.getId() == 0)return null;
        return clientService.addClient(client);
    }

    @RequestMapping(value = "/secure/users/passChange", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String,String> params){
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client logged = clientService.getByEmail(userName);
        if(logged != null && clientService.checkPasswOnChange(logged,params.get("password"))){
            logged.setPassword(params.get("passwordNew"));
            clientService.updateChangePassOrMerge(logged);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        else return new ResponseEntity<Boolean>(false , HttpStatus.OK);
    }
}
