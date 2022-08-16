package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class City implements Serializable{
    @JsonProperty("id")
    public String Code;
    @JsonProperty("name")
    public String Name;
}