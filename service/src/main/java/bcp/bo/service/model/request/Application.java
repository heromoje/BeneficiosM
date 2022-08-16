package bcp.bo.service.model.request;

import org.codehaus.jackson.annotate.JsonProperty;

import bcp.bo.service.Sender;

/**
 * Created by BC2078 on 10/15/2015.
 */
public class Application extends Sender{
    @JsonProperty("application")
    public String Application;
    @JsonProperty("password")
    public String Password;

    public Application(String application,String password){
        Application=application;
        Password=password;
    }
}