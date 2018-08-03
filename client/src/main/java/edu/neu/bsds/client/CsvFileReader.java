package edu.neu.bsds.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.bsds.client.models.SkiRecords;

/**
 * Reads ski data from CSV file format.
 */
public class CsvFileReader {

    private List<SkiRecords> data;
    private String fileName;
    private Integer numData;

    public CsvFileReader(String fileName) {
        this.fileName = fileName;
        this.data = new ArrayList<>();
        this.numData = 0;
    }

    public List<SkiRecords> getData() {
        return data;
    }

    public void setData(List<SkiRecords> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNumData() {
        return numData;
    }

    public List<SkiRecords> loadFromCSV(){

        BufferedReader reader = null;

        try {
            File file = new File(this.fileName);
            reader = new BufferedReader(new FileReader(file));
            //reader.readLine(); // read first line to get rid of column names

            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                data.add(createFromString(line));
                numData++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("successfully read spreadsheet");
        return data;
    }


    public SkiRecords createFromString(String line) {
        String[] fields = line.split(",");
        //System.out.println(Arrays.toString(fields));

        // just defining indices here to see it/change it nicely
        Integer resortId = 0;
        Integer day = 1;
        Integer skierId = 2;
        Integer liftId = 3;
        Integer time = 4;


        return new SkiRecords(fields[resortId],
                Integer.valueOf(fields[day]),
                fields[time],
                fields[skierId],
                fields[liftId]);
    }


    public void writeStatData(MyStats calc, String filePrefix,Integer numThreads,Integer batchSize) {
        try{
            FileWriter fw=new FileWriter(filePrefix + "_latencies.csv");

            String heading = "timestamp,latency\n";
            fw.write(heading);
            for(Map.Entry<Long, List<Long>> latencyList: calc.getLatenciesWithTimeStamp().entrySet()) {
                for(Long latency: latencyList.getValue()){
                    fw.write(Long.toString(latencyList.getKey())+ ","
                            + Long.toString(latency) + "\n");
                }
            }
            fw.flush();
            fw.close();

            System.out.println("Successfully wrote latency data to file "
                    + filePrefix
                    + "_latencies.csv");

            fw= new FileWriter(filePrefix + "_info.csv");

            heading = "numRequests,numSuccessfulRequests,wallTime,numThreads,batchSize\n";
            fw.write(heading);
            fw.write(calc.getNumRequests() + ",");
            fw.write(calc.getNumSuccessfulRequests() + ",");
            fw.write(calc.getWallTime() + ",");
            fw.write(numThreads + ",");
            fw.write(batchSize + "\n");
            fw.flush();
            fw.close();

            System.out.println("Successfully wrote latency data to file "
                    + filePrefix
                    + "_info.csv");

        } catch(Exception e) {System.out.println(e);}
        System.out.println("Success...");
    }


}


