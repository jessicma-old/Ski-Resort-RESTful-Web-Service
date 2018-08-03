package edu.neu.bsds.client;


import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.bsds.client.models.SkiRecords;
import jersey.repackaged.com.google.common.collect.Lists;

/**
 *
 *
 *
 *
 */
public class LoadRecordsThread implements Runnable {

    private WebTarget webTarget;
    private Client client;
    private String baseURI;
    private static final String path = "rest/assignment2";
    private MyStats results;
    private List<SkiRecords> dataToLoad;
    public static final Integer batchSize = 1024;


    public LoadRecordsThread(String baseURI, MyStats results, List<SkiRecords> data) {
        this.client = ClientBuilder.newClient();
        this.baseURI  = baseURI; //"http://localhost:8080/rest"
        this.webTarget = client.target(this.baseURI).path(path);
        this.results = results;
        this.dataToLoad = data;
    }


    public Response load(Object requestEntity, Class responseType) {

        Response response = webTarget
                .path("loadAll")
                .request(MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, MediaType.APPLICATION_JSON));

        try {
            if (response.getStatus() == 200) {
                results.incrementNumSuccessfulRequests();
                return response;
            }
        } finally {
            response.close();
        }

        return null;
    }

    public synchronized void executeRequest(List<SkiRecords> batch) {
        long startTime = System.currentTimeMillis();
        this.load(batch, String.class);
        long endTime = System.currentTimeMillis();
        this.results.addLatency(endTime, endTime-startTime);
        this.results.incrementNumRequests();
    }

    public void close() {
        this.client.close();
    }

    @Override
    public void run() {

        try {
            List<List<SkiRecords>> batchRecords = Lists.partition(dataToLoad, batchSize);

            for (List<SkiRecords> batch : batchRecords) {
                executeRequest(batch);
            }
        } finally {
            this.close();
        }

    }


}
