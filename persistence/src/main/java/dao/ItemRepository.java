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

    Item getActiveItem(int itemId);

    boolean isUniqueName(String name);

    Item deleteItem(int itemId);

    Item deleteSoft(int itemId);

    List<Item> getAll();

     Item save(Item item);

     List<String> getThemes(Locale locale);

     List<Item> getItemsByTheme(String theme);

    List<Item> getItemsByThemeEng(String theme2);

    public byte[] getFoto(int id);

    public void detach(Item item);
}
