package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Condition extends PublicPrivateTokenCIC {
    @JsonProperty("phone")
    public int Phone;
    @JsonProperty("email")
    public String Email;
    @JsonProperty("condition")
    public int Condition;
}