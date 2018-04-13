package dao;


import model.Item;
import model.Order;
import model.OrderForItem;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ilya on 21.08.2016.
 * order repo impl
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {


    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     * @param orderId orderId
     * @return Map with items an their quantities
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<Item,Integer> getItemsOfOrder(int orderId) {
        Order or = entityManager.find(Order.class, orderId);
        List<OrderForItem> list = or.getOrderForItems();
        if (list != null) {
            Map<Item, Integer> map = new HashMap<>();
            for (OrderForItem orf : list) {
                map.put(orf.getItem(), orf.getQuantity());
            }
            return map;
        }
        return null;
    }

    @Override
    public List<Order> getAll() {
        return entityManager.createQuery("select o from Order o order by o.date desc").getResultList();
    }

    @Override
    public Order getById(int id) {
        return entityManager.find(Order.class,id);
    }

    @Override
    public List<Order> getByClientId(int id) {
        return  entityManager.createQuery("SELECT o FROM Order o WHERE o.client.id =:clid",Order.class).setParameter("clid",id).getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Order updateOrder(Order order) {
       return   entityManager.merge(order);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public  boolean addOrder(Order order) {
        int a;
        for (OrderForItem orderForItem : order.getOrderForItems()) {
//            entityManager.lock(i, LockModeType.OPTIMISTIC);
            Item i = orderForItem.getItem();
            if ((a = (i.getQuantity() - orderForItem.getQuantity())) < 0) {
                return false;
            }
            i.setQuantity(a);
        }
        entityManager.persist(order);
        return true;
    }

    /**
     *
     * @param from from date
     * @param to to date
     * @return Orders list
     */
    @Override
    public List<Order> getBetween(String from, String to) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return (List<Order>)entityManager.createQuery("select o from Order o where o.date between :froms and :to")
                    .setParameter("froms",dateFrom)
                    .setParameter("to",dateTo)
                    .getResultList();
        }
        catch (Exception e){
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    /**
     *
     * @param first first page
     * @param pageSize orders for page
     * @param sortField sort this field
     * @param sortOrder sort order (asc||desc)
     * @param filters map of filters
     * @return list prepared orders
     */
    @Override
    @Transactional( propagation = Propagation.REQUIRED)
    public List<Order> getLazyList(int first, int pageSize, String sortField, String sortOrder, Map<String, String> filters) {
        filters.remove("first");
        filters.remove("pageSize");
        filters.remove("sortField");
        filters.remove("sortOrder");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> q = cb.createQuery(Order.class);
        Root<Order> site = q.from(Order.class);
        q.select(site);

        Path<?> path = getPath(sortField, site);
        if (sortOrder == null){
            //just don't sort
        }else if (sortOrder.equals("ASCENDING")){
            q.orderBy(cb.asc(path));
        }else if (sortOrder.equals("DESCENDING")){
            q.orderBy(cb.desc(path));
        }else if (sortOrder.equals("UNSORTED")){
            //just don't sort
        }else{
            q.orderBy(cb.desc(path));
        }
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (filter.getKey().equals("id")) {
                Path<?> pathFilterNonString = getPath(filter.getKey(), site);
                filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
            }
        }
        q.where(filterCondition);
        TypedQuery<Order> tq = entityManager.createQuery(q);
        if (pageSize >= 0){
            tq.setMaxResults(pageSize);
        }
        if (first >= 0){
            tq.setFirstResult(first);
        }
        return tq.getResultList();
    }

    private Path<?> getPath(String sortField, Root<Order> site){
        Path<?> path = null;
        if(sortField == null){
            path =  site.get("id");
        }
        else if(sortField.equals("deliveryStatus")) path = site.get("deliveryStatus");
        else if(sortField.equals("payStatus")) path = site.get("payStatus");
        else if(sortField.equals("date")) path = site.get("date");
        else path =  site.get("id");
        return path;
    }

    @Override
    public long count() {
        return (long)entityManager.createQuery("select count(ord.id) from Order ord").getSingleResult();
    }
}
