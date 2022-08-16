package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

import bcp.bo.service.Sender;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class PrivateToken extends Sender implements Serializable{
    @JsonProperty("cic")
    public String CIC;
    @JsonProperty("condition")
    public int Condition;
    @JsonProperty("privateToken")
    public String PrivateToken;
    @JsonProperty("fullName")
    public String FullName;
    @JsonProperty("lastName")
    public String LastName;
    @JsonProperty("ahorro")
    public String AmountSaved;
}