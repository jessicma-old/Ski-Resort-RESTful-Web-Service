package edu.neu.bsds.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Ignore this, we're not using it anymore
 */
public class TestClient implements Runnable {

    private WebTarget webTarget;
    private Client client;
    private String baseURI;
    private static final String path = "hello";
    private MyCalc results;


    public TestClient(String baseURI, MyCalc results) {
        this.client = ClientBuilder.newClient();
        this.baseURI  = baseURI; //"http://localhost:8080/rest"
        this.webTarget = client.target(this.baseURI).path(path);
        this.results = results;
    }

    public TestClient(String baseURI, Integer port, Integer numThreads, Integer numIterations) {
        this.client = ClientBuilder.newClient();
        this.baseURI  = baseURI + ":" + port.toString();
        this.webTarget = client.target(this.baseURI).path("rest/" + path);
    }





    public <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                        .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN),
                                responseType);
    }


    public Response getStatus() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(Response.class);
    }


    public void addSuccessfulRequest(){

        if (getStatus().getStatus() == 200){
            this.results.incrementSuccessfulRequests();
        } else {
            return;
        }
    }



    public void close() {
        this.client.close();
    }


    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        Response getres =  this.getStatus();
        this.postText("this is a test", Response.class); // Changed from string.class
        long endTime = System.currentTimeMillis();
        this.results.addLatency(endTime - startTime, endTime);

        this.results.incrementNumRequests();
        addSuccessfulRequest(); // doing this at the end makes a huge reduction in latencies.

        this.close();

        /**
        TestClient singleThread = new TestClient("http://localhost:8080/rest");

        String testStr = "testing post";
        System.out.println("Starting ==========");
        System.out.println(singleThread.getStatus());

        System.out.println("POST method ==========");
        System.out.println(singleThread.postText(testStr, String.class));

        singleThread.close();
         **/

    }
}











