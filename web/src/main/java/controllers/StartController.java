package controllers;


import dao.ItemRepository;
import model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * Created by ilya on 27.01.2018.
 */
@RestController
public class StartController {

    @Autowired
    private ItemRepository itemrepository;

//    @RequestMapping( value = "/red")
//    public @ResponseBody Object getMe(){
//        String data = "";
//        ClassPathResource cpr = new ClassPathResource("static/sql_for_bucket.sql");
//        try {
//            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
//            data = new String(bdata);
//        } catch (IOException e) {
//
//        }
//        return data;
//    }

    @RequestMapping(value = "/catalog")
    @ResponseBody
    @CrossOrigin
    public List<String> getCatalog(){
        return itemrepository.getThemes(LocaleContextHolder.getLocale());
    }

    @RequestMapping(value = "/items")
    @ResponseBody
    @CrossOrigin
    public List<Item> getItemsByName(@RequestParam(name = "theme",defaultValue = "")String theme){
        Locale locale = LocaleContextHolder.getLocale();
        List<Item> list  = locale.getLanguage().equals(Locale.ENGLISH.getLanguage()) ? itemrepository.getItemsByThemeEng(theme) : itemrepository.getItemsByTheme(theme);
        return list.stream().map(item -> {
            item.setBucketQuant(1);
            return item;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/foto/{id}")
    @ResponseBody
    public byte[] getFoto(@PathVariable(name = "id")Integer id){
        return itemrepository.getFoto(id);
    }

    @RequestMapping(value = "/item")
    @ResponseBody
    @CrossOrigin
    public Item getSingle(@RequestParam( name = "id")Integer id){
        return itemrepository.getActiveItem(id);
    }

}
