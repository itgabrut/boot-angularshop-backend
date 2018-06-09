package services;


import dao.ItemRepository;
import dao.OrderRepository;
import model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ilya on 02.09.2016.
 * implementation
 */
@Service
public class ItemServiceImpl {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository ;

    private static final Logger LOG = LoggerFactory.getLogger(ItemServiceImpl.class);

//    static {
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        // print logback's internal status
//        StatusPrinter.print(lc);
//
//    }
    /**
     *
     * @param id Id of Item entity
     * @return   Item object
     */
    public Item getItem(int id) {
        LOG.info("Item {} asks get method",id);
        return itemRepository.getItem(id);
    }

    /**
     * Removes Item entity from db using it Id
     * @param id Id
     * @return true on success
     */
    @Transactional
    public boolean deleteItem(int id) {
        Item inactive = itemRepository.deleteSoft(id);
        LOG.info("Item {} delete Soft success",id);
        try {
            FotoSaver.deleteByItem(inactive.getName(),inactive.getTheme());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
    /**
     * It provides information about ordering
     * @param orderId  Id of Order entity
     * @return          Map, represents Item entity as key, and quantity as value
     */

    public Map<Item, Integer> getItemsAndQuantityByOrder(int orderId) {
        Map<Item,Integer> map = orderRepository.getItemsOfOrder(orderId);
        assert map!=null;
        LOG.info("Items counted for order id = {}",orderId);
        return map;
    }

    /**
     *  List of Items from db
     * @param map Map containing Id's of client's card
     * @return List of Item entities
     */
    public List<Item> getBucketItemsFromSession(Map<String,Integer> map) {
//        Map<String,Integer> map = (Map<String,Integer>)req.getSession().getAttribute("itemsMap");
        List<Item> list = new ArrayList<>();
        if(map!=null) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Item item = itemRepository.getItem(Integer.parseInt(entry.getKey()));
                list.add(item);
            }
            LOG.info("Items from Client card");
            return list;
        }
        return list;
    }

    /**
     * Returns List Item entities by categories (theme)
     * @param s String, Item theme
     * @return  Returns List Item entities
     */

    public List<Item> getItemsByTheme(String s, Locale locale) {
        List<Item> list;
        if(locale.getLanguage().equals(new Locale("en").getLanguage())){
            list = itemRepository.getItemsByThemeEng(s);
        }
        else list = itemRepository.getItemsByTheme(s);
        return list;
    }

    /**
     *
     * @return List of String objects from db, representing existing Item categories
     */

    public List<String> getThemes(Locale locale) {
        return itemRepository.getThemes(locale);
    }

    /**
     * Edits Item if no Id present or adds it to db
     * @param item Item entity
     */

    @Transactional
    public void addOrRedactItem(Item item) {
        itemRepository.save(item);
        LOG.info("Item {} successfully saved",item.getId());
    }

}
