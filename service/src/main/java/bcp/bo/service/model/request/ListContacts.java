package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class ListContacts extends PublicPrivateTokenCIC {
    @JsonProperty("idComerce")
    public String IdCommrce;
}
