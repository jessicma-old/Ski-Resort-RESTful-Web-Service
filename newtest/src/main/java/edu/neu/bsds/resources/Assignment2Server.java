package edu.neu.bsds.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.neu.bsds.models.MyVert;

/**
 *
 */
@Path("/assignment2")
public class Assignment2Server {

    /**
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return "This is assignment2";
    }

     @POST
     @Consumes(MediaType.TEXT_PLAIN)
     public int load(String content) {
     return (content.length());
     }
     **/

    //**
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MyVert myVert() {
        MyVert skier = new MyVert();
        skier.setNumRides(9);
        skier.setTotalVert(200);
        return skier;
    }
    //**/

}
