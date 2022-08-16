package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by CARLA on 28-oct-15.
 */
public class Location implements Serializable {
    public Location(){

    }
    @JsonProperty("lat")
    public double Latitude;

    @JsonProperty("lng")
    public double Longitude;
}
