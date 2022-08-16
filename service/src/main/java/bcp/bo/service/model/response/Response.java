package bcp.bo.service.model.response;

//import com.fasterxml.jackson.annotation.JsonProperty;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bcp.bo.service.Sender;

/**
 * Created by BC2078 on 10/16/2015.
 */
public class Response<T> {
    @JsonProperty("state")
    public boolean State;
    @JsonProperty("message")
    public String Message;
    @JsonProperty("exceptions")
    public String Exceptions;
    @JsonProperty("data")
    public T Data;
}