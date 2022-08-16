package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class RatingTicket extends PublicPrivateTokenCIC {
    @JsonProperty("numberTicket")
    public String NumberTicket;
    @JsonProperty("ratingTicket")
    public String RatingTicket;
}
