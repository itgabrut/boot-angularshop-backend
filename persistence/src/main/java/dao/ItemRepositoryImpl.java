package dao;


import model.Item;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilya on 28.08.2016.
 * item repoimpl
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @PersistenceContext
   private EntityManager entityManager;

    public Item getItem(int itemId) {
        return (Item) entityManager.createQuery("select i from Item i where i.id =:id").setParameter("id",itemId).getSingleResult();
    }

    @Override
    public Item getActiveItem(int itemId) {
        return (Item) entityManager.createQuery("select i from Item i where i.active = true and i.id =:id").setParameter("id",itemId).getSingleResult();
    }

    @Override
   public boolean isUniqueName(String name) {
        long a = (long)entityManager.createQuery("select count(i.name) from Item i where i.name =:curr").setParameter("curr",name).getSingleResult();
       return a == 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Item deleteSoft(int itemId) {
        Item persisted = entityManager.find(Item.class,itemId);
        if(persisted.isActive())persisted.setActive(false);
        return persisted;
    }

    @Transactional
    public Item deleteItem(int itemId) {
        Item item = entityManager.find(Item.class,itemId);
        entityManager.remove(item);
        return item;
    }

    public List<Item> getAll() {
        List<Item> list = entityManager.createQuery("select i from Item i",Item.class).getResultList();
        entityManager.close();
        return list;
    }

    @Transactional
    public Item save(Item item) {
        if(item.getProxyId()==0) {
            long a = (long) entityManager.createQuery("select count(i.name) from Item i where i.name =:curr").setParameter("curr", item.getName()).getSingleResult();
            if (a < 1) {
                entityManager.persist(item);
                return item;
            }
            return null;
        }
        else {
            entityManager.clear();
            Item it = entityManager.find(Item.class,item.getProxyId());
            entityManager.lock(it, LockModeType.OPTIMISTIC);
            it.setName(item.getName());
            it.setQuantity(item.getQuantity());
            it.setDescription(item.getDescription());
            if(item.getFoto()!=null) it.setFoto(item.getFoto());
            it.setPrice(item.getPrice());
            it.setTheme(item.getTheme());
            it.setTheme2(item.getTheme2());
            entityManager.flush();
            return item;
        }
    }

    public List<String> getThemes(Locale locale){
        if(locale.getLanguage().equals(new Locale("en").getLanguage())){
            return entityManager.createNamedQuery("Item.getThemesEng",String.class).getResultList();
        }
         return entityManager.createNamedQuery("Item.getThemes",String.class).getResultList();
    }

    public List<Item> getItemsByTheme(String theme){
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.active = true and i.theme = :nameOfTheme",Item.class);
        return query.setParameter("nameOfTheme",theme).getResultList();
    }

    public List<Item> getItemsByThemeEng(String theme2){
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.active = true and i.theme2 = :nameOfTheme",Item.class);
        return query.setParameter("nameOfTheme",theme2).getResultList();
    }

    public byte[] getFoto(int id){
      return  (byte[]) entityManager.createQuery("select i.foto from Item i where i.id =:id").setParameter("id",id).getSingleResult();
    }

    public void detach(Item item){
        entityManager.detach(item);
    }
}
