package controllers;

import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import services.ItemServiceImpl;

import java.util.List;

/**
 * Created by imedvede on 26.06.2018.
 */
@RestController
public class ItemsController {

    @Autowired
    private ItemServiceImpl itemService;


    @RequestMapping(value = "secure/admin/allItems",method = RequestMethod.GET)
    public List<Item> getAll(){
        return itemService.getAll();
    }
}
