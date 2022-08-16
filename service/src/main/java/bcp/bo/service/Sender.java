package bcp.bo.service;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Sender {
    public String toJSON(){
        ObjectMapper mapper=new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }
    public static <T> T fromJSON(String json,Class<T> classType){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.readValue(json, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T fromJSON(String json,final TypeReference<T> type){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
