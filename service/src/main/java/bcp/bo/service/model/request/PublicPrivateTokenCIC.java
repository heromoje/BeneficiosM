package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class PublicPrivateTokenCIC extends PublicPrivateToken implements Serializable {
    @JsonProperty("cic")
    public String CIC;
    @JsonProperty("city")
    public String City;

    public void set(PublicPrivateTokenCIC publicPrivateTokenCIC) {
        super.PublicToken = publicPrivateTokenCIC.PublicToken;
        super.PrivateToken = publicPrivateTokenCIC.PrivateToken;
        CIC = publicPrivateTokenCIC.CIC;
        City = publicPrivateTokenCIC.City;
    }
}