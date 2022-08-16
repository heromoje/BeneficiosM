package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directions implements Serializable{
    public static final String StatusOk="OK";
    public static final String NotFound="NOT_FOUND";
    public static final String ZeroResults="ZERO_RESULTS";
    public static final String MaxWaypointsExceeded="MAX_WAYPOINTS_EXCEEDED";
    public static final String InvalidRequest="INVALID_REQUEST";
    public static final String OverQueryLimit="OVER_QUERY_LIMIT";
    public static final String RequestDenied="REQUEST_DENIED";
    public static final String UnknownError="UNKNOWN_ERROR";

    @JsonProperty("status")
    public String Status;

    @JsonProperty("routes")
    public List<Route> Routes;

    public boolean isOK(){
        return Status.equals(StatusOk);
    }
}