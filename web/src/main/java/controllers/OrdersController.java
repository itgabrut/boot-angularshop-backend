package controllers;

import dao.ClientRepository;
import dao.OrderRepository;
import model.Bucket;
import model.Client;
import model.Item;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import services.OrderService;

import javax.persistence.OptimisticLockException;
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
    private OrderService orderService;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(value = "secure/getBucketList", method = RequestMethod.GET)
    public List<Item> getClientsBucketList(){
        Client client = getClient();
        List<Bucket> bucketList =  orderRepository.getBucketList(client);
        List<Item> result = bucketList.stream().map(bucket -> {
            bucket.getItem().setBucketQuant(bucket.getQuantity());
            return bucket.getItem();
        }).collect(Collectors.toList());

        return result;
    }

    @RequestMapping(value = "secure/getBucketList", method = RequestMethod.PUT)
    public void syncBucket(@RequestBody List<Item> items){
        Client client = getClient();
       List<Bucket> ll =  items.stream().map(item -> {
            Bucket bucket = new Bucket();
            bucket.setClient(client);
            bucket.setItem(item);
            bucket.setQuantity(item.getBucketQuant());
            return bucket;
        }).collect(Collectors.toList());

        orderRepository.saveBucketList(ll);
    }

    @RequestMapping(value = "secure/getBucketList",method = RequestMethod.DELETE)
    public ResponseEntity clearBucket(){
        Client client = getClient();
        try{
            orderRepository.clearBucket(client);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/secure/order",method = RequestMethod.POST)
    public ResponseEntity postOrder(@RequestBody List<Item> items){
       Map<Integer,Integer> map =  items.stream().collect(Collectors.toMap(Item::getId,Item::getBucketQuant));
        boolean success = false;
        try{
            success = orderService.addOrder(map,getClient());
        }
        catch (OptimisticLockException ex){
            success = false;
        }
        if(success)return new ResponseEntity(HttpStatus.CREATED);
        else return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/secure/order",method = RequestMethod.GET)
    public List<Order> getOrders(){
        return orderService.getOrdersByClientId(getClient().getId());
    }

    @RequestMapping(value = "/secure/order/{id}")
    public List<Item> getItemsByOrder(@PathVariable(value = "id") String orderId){
        if(orderService.verifyOrderOnLoggedClient(orderId,getClient().getId()) || getClient().isAdmin()){
            Map<Item, Integer> orders = orderRepository.getItemsOfOrder(Integer.parseInt(orderId));
           List<Item> result =  orders.entrySet().stream().map(entry -> {
                entry.getKey().setBucketQuant(entry.getValue());
                return entry.getKey();
            }).collect(Collectors.toList());
           return result;
        }
        else return null;
    }

    @RequestMapping(value = "/secure/admin/order",method = RequestMethod.GET)
    public List<Order> getAllOrders(){
        return orderService.getAllSortedDate();
    }

    @RequestMapping(value = "/secure/admin/order",method = RequestMethod.PUT)
    public ResponseEntity changeOrder(@RequestBody Map<String,Object> orderMap){
        return orderService.updateOrder(orderMap) ? new ResponseEntity( HttpStatus.OK) : new ResponseEntity(HttpStatus.CONFLICT);
    }

    private  Client getClient(){
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.getByEmail(userName);
        return client;
    }
}
