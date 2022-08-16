package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Contact implements Serializable{
    @JsonProperty("address")
    public String Address;
    @JsonProperty("email")
    public String email;
    @JsonProperty("idContact")
    public int IdContact;
    @JsonProperty("latitude")
    public float Latitude;
    @JsonProperty("longitude")
    public float Longitude;
    @JsonProperty("phone")
    public int Phone;

    private Commerce _commerce;

    public Commerce getCommerce(){
        return _commerce;
    }
    public void setCommerce(Commerce commerce){
        _commerce=commerce;
    }
}