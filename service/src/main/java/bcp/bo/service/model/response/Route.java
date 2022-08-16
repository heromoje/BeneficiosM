package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CARLA on 28-oct-15.
 */
public class Route implements Serializable {
    @JsonProperty("copyrights")
    public String CopyRights;
    @JsonProperty("legs")
    public List<Leg> Legs;

    public Route(){

    }
}