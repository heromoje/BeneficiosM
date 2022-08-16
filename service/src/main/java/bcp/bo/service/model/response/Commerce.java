package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Commerce implements Serializable{
    @JsonProperty("idCategoryCommerce")
    public int IdCategoryCommerce;
    @JsonProperty("nameCategoryCommerce")
    public String Category;
    @JsonProperty("nameCommerce")
    public String Name;
    @JsonProperty("idCommerce")
    public int Id;
    @JsonProperty("promotions")
    public void setCommerce(List<Promotion> promotions){
        Promotions=promotions;
        for (Promotion promotion:promotions) {
            promotion.setCommerce(this);
        }
    }
    public List<Promotion> Promotions;
    @JsonProperty("contacts")
    public void setContacts(List<Contact> contacts){
        Contacts=contacts;
        for (Contact contact:contacts) {
            contact.setCommerce(this);
        }
    }
    public List<Contact> Contacts;

    /*private Category _category;
    public void setCategory(Category category){
        _category=category;
    }
    public Category getCategory(){
        return _category;
    }*/
}