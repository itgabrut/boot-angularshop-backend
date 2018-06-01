package controllers;

import dao.ClientRepository;
import dao.OrderRepository;
import model.Bucket;
import model.Client;
import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by imedvede on 01.06.2018.
 */
@RestController
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "secure/getBucketList", method = RequestMethod.GET)
    public Map<Item, Integer> getClientsBucketList(){
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.getByEmail(userName);
        List<Bucket> bucketList =  orderRepository.getBucketList(client);
        Map<Item,Integer> resultMAp = bucketList.stream().collect(Collectors.toMap(Bucket::getItem,Bucket::getQuantity));
        return resultMAp;
    }

    @RequestMapping(value = "secure/getBucketList", method = RequestMethod.PUT)
    public void syncBucket(@RequestBody Object[] ob){
        System.out.println(ob);
    }
}
