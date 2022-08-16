package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CARLA on 28-oct-15.
 */
public class Leg implements Serializable {
    @JsonProperty("start_location")
    public Location StartLocation;
    @JsonProperty("end_location")
    public Location EndLocation;
    @JsonProperty("steps")
    public List<Step> Steps;

    public Leg(){

    }
}
