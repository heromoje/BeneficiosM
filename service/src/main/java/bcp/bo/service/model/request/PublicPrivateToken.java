package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

import bcp.bo.service.Sender;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class PublicPrivateToken extends Sender{
    @JsonProperty("publicToken")
    public String PublicToken;
    @JsonProperty("privateToken")
    public String PrivateToken;
}
