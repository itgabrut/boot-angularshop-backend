package controllers;

import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import services.FotoSaver;
import services.ItemServiceImpl;

import java.io.IOException;
import java.util.Arrays;
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
    public ResponseEntity postItem(@RequestBody Item item){
        try{
            itemService.addOrRedactItem(item);
        }
        catch (IOException ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "secure/admin/putItem", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity changeItem(@RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
                                     @RequestParam(value = "images", required = false) MultipartFile[] files,
                                     @RequestParam(value = "photoToDelete", required = false) String[] paths,
                                     @ModelAttribute Item item) {
        System.out.println(item);
        try {

            if(paths != null && paths.length > 0){
                Item previous = itemService.getItem(item.getId());
                FotoSaver.deleteByPhotoName(previous.getName(),previous.getTheme(),paths);
            }
            if (mainImage != null) item.setFoto(mainImage.getBytes());

            item.setProxyId(item.getId());
            itemService.addOrRedactItem(item);

            if (files.length > 0) {
                Arrays.stream(files).forEach(file -> {
                    try {
                        FotoSaver.saveFotoToFileSystem(file.getInputStream(), item.getTheme(), item.getName(), file.getOriginalFilename());
                    } catch (IOException ex) {

                    }

                });
            }
        } catch (IOException exc) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
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

    @RequestMapping(value = "secure/admin/item")
    public Item getItem(@RequestParam( name = "id")Integer id){
        return itemService.getItem(id);
    }
}
