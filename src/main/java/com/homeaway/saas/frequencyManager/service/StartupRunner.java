package com.homeaway.saas.frequencyManager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    private LogParserService logParserService;
    @Autowired
    private UtilService utilService;
    @Value("${srcDir}")
    private String srcDir;

    private static final Logger LOG =
            LoggerFactory.getLogger(StartupRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Initializing first bulk load...");
        List<String> initialFileList = utilService.getInitialFileList(srcDir);
        for (String filePath : initialFileList){
            logParserService.parseLog(filePath);
        }

        LOG.info("Bulk load completed.");
    }

    public void startListeningForNewFiles() throws IOException, InterruptedException {
        LOG.info("Listening to the source directory for new files...");
        LOG.debug("Thread: "+Thread.currentThread().getName());
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(srcDir);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;
        int retry = 3;
        while (retry>0) {
            while ((key = watchService.poll(1, TimeUnit.HOURS)) != null) {
                for (WatchEvent event : key.pollEvents()) {
                    LOG.info(event.kind() + ":" + event.context());
                    logParserService.parseLog(srcDir+ File.separator+event.context());
                }
                key.reset();
                retry=3;
            }
            retry--;
        }
        LOG.info("Directory listening service terminated after 3 retries. No new file will be processed. Please restart the application.");
    }
}
