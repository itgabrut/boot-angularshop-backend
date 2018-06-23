package services;


import dao.ItemRepository;
import dao.OrderRepository;
import model.Client;
import model.Item;
import model.Order;
import model.OrderForItem;
import model.enums_utils.Delivery_status;
import model.enums_utils.Pay_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ilya on 09.09.2016.
 * order service implementation
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository repository ;

    @Autowired
    private ItemRepository itemRepository;

//    public boolean addOrder(Order order){
//        return false;
//
//    }
    @Override
    @Transactional
    public boolean updateOrder(Map<String, Object> map) {
        Order order = repository.getById((Integer) map.get("orderId"));
        for(Map.Entry<String,Object> entr : map.entrySet()){
            switch (entr.getKey()){
                case "payway" : order.setPayway((String)entr.getValue());
                    break;
                case "delivery" : order.setDelivery((String)entr.getValue());
                    break;
                case "deliveryStatus" : order.setDeliveryStatus(Delivery_status.valueOf((String)entr.getValue()));
                    break;
                case "payStatus" : order.setPayStatus(Pay_status.valueOf((String)entr.getValue()));
                    break;
//                case "orderId" : order.setId(Integer.parseInt((String)entr.getValue()));
//                    break;
            }
        }
       Order updated =  repository.updateOrder(order);
        return updated != null;
    }

    @Override
    public List<Order> getAllSortedDate() {
        return repository.getAll();
    }

    /**
     * Searches for orders of client by it Id
     * @param id Id of Client entity
     * @return List of Order entities
     */
    @Override
    public List<Order> getOrdersByClientId(int id) {
       List<Order> list = repository.getByClientId(id);
        return list == null ? null : list;
    }

    @Override
    public Order getOrderById(int orderId) {
        return repository.getById(orderId);
    }

    @Override
    @Transactional
    public boolean verifyOrderOnLoggedClient(String orderId, int loggedClientId) {
        Order order = repository.getById(Integer.parseInt(orderId));
        return loggedClientId == order.getClient().getId();
    }

    /**
     *  Creates Order entity and populates it's fields, then tries save new Order entity to db
     * @param m  Map, witch keyset represents Id's of Items in this Order and valuesset quantity of each Item in Order
     * @param current Client entity whose order is formed
     * @return true if success. False if error occurred.
     */
    @Override
    @Transactional
    public boolean addOrder(Map<Integer, Integer> m, Client current) {
        Order order = Order.getSimpleOrder();
        List<OrderForItem> list = new ArrayList<>();
        for(Map.Entry<Integer,Integer> entr : m.entrySet()){
            Item item = itemRepository.getItem(entr.getKey());
            OrderForItem orderForItem = new OrderForItem();
            orderForItem.setQuantity(entr.getValue());
            orderForItem.setItem(item);
            orderForItem.setOrder(order);
            list.add(orderForItem);
        }
        order.setOrderForItems(list);
        order.setClient(current);
        return repository.addOrder(order);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional
    public List<Order> lazyLoad(Map<String, String> parameters) {
        String page = parameters.get("offset");
        String limit = parameters.get("limit");
        String sorted = parameters.get("sort");
        String orderSort =  "asc".equals(parameters.get("order")) ? "ASCENDING" : "DESCENDING";
       List<Order> list =  repository.getLazyList(Integer.parseInt(page), Integer.parseInt(limit),sorted,orderSort,parameters);
        return list;
    }
}
