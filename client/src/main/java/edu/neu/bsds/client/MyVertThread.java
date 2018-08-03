package edu.neu.bsds.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by jessicamalloy on 4/23/18.
 */
public class MyVertThread implements Runnable {

    private WebTarget webTarget;
    private Client client;
    private String baseURI;
    private static final String path = "rest/assignment2";
    private MyStats results;

    // used to calculate range of skier Ids to request
    private Integer threadNum;
    private final Integer requestsPerThread = 300;
    private final Integer day = 999;

    public MyVertThread(String baseURI, MyStats results, Integer threadNum) {
        this.client = ClientBuilder.newClient();
        this.baseURI  = baseURI; //"http://localhost:8080/"
        this.webTarget = client.target(this.baseURI).path(path);
        this.threadNum = threadNum;
        this.results = results;
    }

    public Response myVert(Integer skierId, Integer day) throws ClientErrorException {

        // {"totalVert":6800,"numRides":20}
        Response response = webTarget
                .path("{skierId}/{day}")
                .resolveTemplate("skierId", skierId)
                .resolveTemplate("day", day)
                .request()
                .get();

        //System.out.println("Status: " + response.getStatus());
        //System.out.println("Response: " + response.readEntity(String.class));
        try {
            if (response.getStatus() == 200) {
                results.incrementNumSuccessfulRequests();
                return response;
            } else {
                System.out.println("error code: " + response.getStatus());
            }
        } finally {
            response.close();
        }

        return null;
    }

    public void close() {
        this.client.close();
    }

    public synchronized void executeRequest(int numRequest) {
        long startTime = System.currentTimeMillis();
        this.myVert(numRequest, day);
        long endTime = System.currentTimeMillis();
        this.results.addLatency(endTime, endTime-startTime);
        this.results.incrementNumRequests();
    }

    @Override
    public void run() {
        try {

            int start = (threadNum * requestsPerThread) + 1;
            for (int i = start; i < (start + requestsPerThread); i++) {
            /*
            long startTime = System.currentTimeMillis();
            this.myVert(i, day);
            long endTime = System.currentTimeMillis();
            this.results.addLatency(endTime, endTime-startTime);
            this.results.incrementNumRequests();*/
                executeRequest(i);
            }
        } finally {
            this.close();
        }
    }
}
