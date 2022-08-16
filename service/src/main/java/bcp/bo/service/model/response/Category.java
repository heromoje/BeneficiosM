package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Category implements Serializable{
    public final static String CATEGORY_RESTAURANTES="RESTAURANTES";
    public final static String CATEGORY_ROPA="ROPA";
    public final static String CATEGORY_ACCESORIOS="ACCESORIOS";
    public final static String CATEGORY_DIVERSION="DIVERSION";
    public final static String CATEGORY_VARIOS="VARIOS";

    @JsonProperty("id")
    public int Id;
    @JsonProperty("name")
    public String Name;
    @JsonProperty("countPromotion")
    public int CountPromotion;
}