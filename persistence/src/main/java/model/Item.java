package model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * Created by ilya on 20.08.2016.
 * item entity
 */
@NamedQueries({
        @NamedQuery(name = "Item.getThemes",query = "select distinct i.theme from Item i where i.active = true order by i.theme"),
        @NamedQuery(name = "Item.getThemesEng",query = "select distinct i.theme2 from Item i where i.active = true order by i.theme2")
})
@Entity
@Table(name = "items")
@Access(AccessType.FIELD)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Version
    @Column(name = "version" , nullable = false, columnDefinition = "integer default 0")
    private long version;

//    @UniqueName
    @NotEmpty
    @Column(name = "name")
    private String name;

   @NotNull
   @Min(value = 0)
    @Column(name = "price")
    private int price;


    @Column(name = "theme")
    private String theme;

    @Column(name = "theme2")
    private String theme2;

    @Min(value = 0)
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "foto",columnDefinition = "org.hibernate.type.BinaryType")
//    @Lob
//    @Column(name = "foto")
    @JsonIgnore
    private byte[] foto;

    @JsonIgnore
    @Transient
    private int proxyId;

    @JsonIgnore
    @Column(name = "active",nullable = false)
    private boolean active;

    @Transient
    private int bucketQuant;

    public int getBucketQuant() {
        return bucketQuant;
    }

    public void setBucketQuant(int bucketQuant) {
        this.bucketQuant = bucketQuant;
    }

    public String getTheme2() {
        return theme2;
    }

    public void setTheme2(String theme2) {
        this.theme2 = theme2;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getProxyId() {
        return proxyId;
    }

    public void setProxyId(int proxyId) {
        this.proxyId = proxyId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public String getName() {
            return name;
    }

    public int getPrice() {
        return price;
    }

    public String getTheme() {
        return theme;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getFoto() {
        return foto;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (active != item.active) return false;
        return name.equals(item.name);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
