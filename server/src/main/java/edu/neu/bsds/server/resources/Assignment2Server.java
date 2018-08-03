package edu.neu.bsds.server.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.bsds.server.dal.SkiRecordsDao;
import edu.neu.bsds.server.models.SkiRecords;

/**
 *
 */
@Path("/assignment2")
public class Assignment2Server {

    SkiRecordsDao skiRecordsDao = SkiRecordsDao.getInstance();


    @GET @Path("/{skierId}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response myVert(@PathParam("skierId") String skierId,
                           @PathParam("day") Integer day) throws SQLException {
        return Response
                .ok()
                .entity(skiRecordsDao.getStatsForSkierDay(skierId,day))
                .build();
    }

    //**
    @POST @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    public String load(SkiRecords ride) throws SQLException {
        skiRecordsDao.create(ride);
        return "Loaded successfully.";
    }
     //**/

    @POST @Path("/loadAll")
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized String loadAll(List<SkiRecords> rides) throws SQLException {
        for(SkiRecords ride : rides){
            skiRecordsDao.create(ride);
        }
        //skiRecordsDao.batchCreate(rides);
        //skiRecordsDao.poolCreate(rides);
        return "Loaded successfully.";
    }

}
