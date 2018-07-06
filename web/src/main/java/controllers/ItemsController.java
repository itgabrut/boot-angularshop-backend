package controllers;

import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

//    @RequestMapping(value = "secure/admin/postItem",method = RequestMethod.POST,consumes = "multipart/form-data")
//    public ResponseEntity postItem(
//                                   @RequestParam(value = "file",required = false) MultipartFile file,
//                                   @ModelAttribute Item item
//                                   ){
////        System.out.println(item);
//        System.out.println(file.getName());
//        return new ResponseEntity(HttpStatus.OK);
//    }


    @RequestMapping(value = "secure/admin/postItem",method = RequestMethod.POST)
    public ResponseEntity postItem(@RequestBody Item item
    ){
        System.out.println(item);
        itemService.addOrRedactItem(item);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "secure/admin/removeItem",method = RequestMethod.POST)
    public ResponseEntity<Boolean> deleteItem(@RequestParam(value = "id")Integer id){
        try{
            itemService.deleteItem(id);
        }
        catch (Exception e){
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Boolean>(true,HttpStatus.OK);
    }

    @RequestMapping( value = "secure/admin/activateItem/{id}",method = RequestMethod.GET)
    public boolean makeActive(@PathVariable(value = "id")Integer id,@RequestParam(value = "active")boolean active){
        boolean res;
        if(active){
           res =  itemService.activateItem(id);
        }
        else{
            res = itemService.deleteItemSoft(id);
        }
        return res;
    }
}
