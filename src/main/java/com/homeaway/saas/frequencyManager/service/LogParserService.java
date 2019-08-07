package com.homeaway.saas.frequencyManager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@Component
public class LogParserService {

    @Autowired
    private ReverseIndex reverseIndex;

    private static final Logger LOG =
            LoggerFactory.getLogger(LogParserService.class);

    @Async
    public void parseLog(String filePath) throws IOException {
        LOG.info("Parsing log file: "+filePath);
        LOG.debug("Thread started: "+ Thread.currentThread().getName());

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int splitIndex = line.indexOf(",");
                String timestamp = line.substring(0, splitIndex);
                String stringVal = line.substring(splitIndex+1).trim();
                reverseIndex.addEntry(stringVal, Long.parseLong(timestamp));
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        LOG.info("Finished parsing log file: "+filePath);
        LOG.debug("Thread finished: "+ Thread.currentThread().getName());
    }

    public void bulkParseLog(){

    }
}
