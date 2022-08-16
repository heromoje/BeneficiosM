package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by CARLA on 28-oct-15.
 */
public class Step implements Serializable {
    @JsonProperty("start_location")
    public Location StartLocation;

    @JsonProperty("end_location")
    public Location EndLocation;
}
