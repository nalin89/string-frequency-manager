package com.homeaway.saas.frequencyManager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UtilService {

    @Value("${fileNameTimeSigntureStartIndex}")
    private int startIndex;
    @Value("${fileNameTimeSigntureEndIndex}")
    private int endIndex;
    @Value("${initialLoadTimeGap}")
    private int initialLoadTimeGap;


    public List<String> getInitialFileList(String srcDir) {

        long oldestFileSignature = getOldestFileNameSignature(LocalDateTime.now());

        return Stream.of(new File(srcDir).listFiles())
                .filter(file -> !file.isDirectory() &&
                        Long.parseLong(file.getName().substring(startIndex,endIndex)) > oldestFileSignature )
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
    }

    public long getOldestFileNameSignature(LocalDateTime now){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        String fileNameDateSignature = formatter.format(now.minusHours(initialLoadTimeGap));

        return Long.parseLong(fileNameDateSignature);

    }
}
