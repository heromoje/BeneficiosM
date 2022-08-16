package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

import bcp.bo.service.Sender;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Login extends Sender {
    @JsonProperty("publicToken")
    public String Token;
    @JsonProperty("type")
    public String Type;
    @JsonProperty("nroDocument")
    public String NroDocument;
    @JsonProperty("extension")
    public String Extension;
    @JsonProperty("complement")
    public String Complement = "";
    @JsonProperty("PhoneToken")
    public String PhoneToken;
}