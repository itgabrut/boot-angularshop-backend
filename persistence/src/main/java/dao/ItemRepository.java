package dao;




import model.Item;

import java.util.List;
import java.util.Locale;

/**
 * Created by ilya on 28.08.2016.
 * item repo
 */
public interface ItemRepository {

    Item getItem(int itemId);

    boolean isUniqueName(String name);

    void deleteItem(int itemId);

    Item deleteSoft(int itemId);

    List<Item> getAll();

     void save(Item item);

     List<String> getThemes(Locale locale);

     List<Item> getItemsByTheme(String theme);

    List<Item> getItemsByThemeEng(String theme2);

    public byte[] getFoto(int id);
}
