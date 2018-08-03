package edu.neu.bsds.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.neu.bsds.client.models.SkiRecords;

/**
 * Spawns threads.
 */
public class ClientMain {

    private static ExecutorService threadPool = null;
    private static final int nThreadsPOST = 10;
    private static final int nThreadsGET = 10;
    private static final String url = "http://35.166.224.206:8080/unnamed/";
    //private static final String url = "http://localhost:8080/";

    public static void main(String[] args) throws InterruptedException {

        //executeLoadThreads();
        executeGetThreads();
        //executeGetAndPostThreads();
    }

    private static void executeLoadThreads() throws InterruptedException {
        // read data from .csv file for loading
        CsvFileReader reader = new CsvFileReader("../db/BSDSAssignment2Day999.csv");
        List<SkiRecords> skiData = reader.loadFromCSV();


        threadPool = Executors.newFixedThreadPool(nThreadsPOST);

        System.out.println(new Date());

        MyStats results = new MyStats();

        for(int i = 0; i < nThreadsPOST; i++){
            threadPool.submit(new LoadRecordsThread(url,
                    results,
                    getSlice(skiData,i)));
        }

        threadPool.shutdown();
        threadPool.awaitTermination(14400, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        results.setWallTime(endTime);
        results.outputStats();


        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String filename = "../stats/" + "load_" + timeStamp;
        reader.writeStatData(results, filename,nThreadsPOST,LoadRecordsThread.batchSize);

        System.out.println(new Date());
    }

    private static void executeGetThreads() throws InterruptedException {


        threadPool = Executors.newFixedThreadPool(nThreadsGET);

        System.out.println(new Date());

        MyStats results = new MyStats();

        for(int i = 0; i < nThreadsGET; i++){
            threadPool.submit(new MyVertThread(url,
                    results,
                    i));
        }

        threadPool.shutdown();
        threadPool.awaitTermination(14400, TimeUnit.SECONDS);
        results.setWallTime(System.currentTimeMillis());
        results.outputStats();

        System.out.println("num latencies recorded: " + results.getAllLatencies().size());

        CsvFileReader reader = new CsvFileReader("../db/BSDSAssignment2Day1.csv");
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String filename = "../stats/" + "myvert_" + timeStamp;
        reader.writeStatData(results,filename,nThreadsGET,1);

        System.out.println(new Date());
    }

    private static void executeGetAndPostThreads() throws InterruptedException {

        ExecutorService getThreadPool = Executors.newFixedThreadPool(nThreadsGET);
        ExecutorService postThreadPool = Executors.newFixedThreadPool(nThreadsPOST);

        MyStats getResults = new MyStats();
        MyStats postResults = new MyStats();

        CsvFileReader reader = new CsvFileReader("../db/BSDSAssignment2Day2.csv");
        List<SkiRecords> skiData = reader.loadFromCSV();

        for(int i = 0; i < nThreadsPOST; i++){
            postThreadPool.submit(new LoadRecordsThread(url,
                    postResults,
                    getSlice(skiData,i)));
        }

        for(int i = 0; i < nThreadsGET; i++){
            getThreadPool.submit(new MyVertThread(url,
                    getResults,
                    i));
        }

        getThreadPool.shutdown();
        getThreadPool.awaitTermination(14400, TimeUnit.SECONDS);
        long getEndTime = System.currentTimeMillis();

        postThreadPool.shutdown();
        postThreadPool.awaitTermination(14400, TimeUnit.SECONDS);
        long postEndTime = System.currentTimeMillis();


        postResults.setWallTime(postEndTime);
        postResults.outputStats();

        getResults.setWallTime(getEndTime);
        getResults.outputStats();

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String filename = "../stats/" + "myvert_" + timeStamp;
        reader.writeStatData(getResults,filename,nThreadsGET,1);


        timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        filename = "../stats/" + "load_" + timeStamp;
        reader.writeStatData(postResults, filename,nThreadsPOST,LoadRecordsThread.batchSize);





    }



    private static List<SkiRecords> getSlice(List<SkiRecords> skiData, Integer numThread) {

        Integer rowsPerThread = skiData.size()/ nThreadsPOST
                + ((skiData.size() % nThreadsPOST == 0) ? 0 : 1);

        Integer start = numThread*rowsPerThread;
        Integer end = start+rowsPerThread;
        if(end > skiData.size()) end = skiData.size();

        return skiData.subList(start, end);
    }
}
