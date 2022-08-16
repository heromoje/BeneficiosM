package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class ListPromotions extends PublicPrivateTokenCIC {
    @JsonProperty("idCategory")
    public String IdCategory;
}
