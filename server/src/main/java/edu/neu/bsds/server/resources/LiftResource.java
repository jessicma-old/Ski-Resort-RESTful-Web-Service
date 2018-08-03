package edu.neu.bsds.server.resources;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.neu.bsds.server.dal.LiftDao;
import edu.neu.bsds.server.models.Lifts;

/**
 * Temp class to check persistence. Gets info from lift table. Might keep to load lift data easily
 * from AWS....
 */
@Path("/lifts")
public class LiftResource {

    LiftDao liftDao = LiftDao.getInstance();

    @GET @Path("/{liftId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Lifts findById(@PathParam("liftId") String liftId) throws SQLException { //input:
        return liftDao.getVerticalFromID(liftId);
    }


}
