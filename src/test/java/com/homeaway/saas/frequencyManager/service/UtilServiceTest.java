package com.homeaway.saas.frequencyManager.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.PriorityQueue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilServiceTest {

    @Autowired
    private UtilService utilService;
    @Value("${srcDir}")
    private String srcDir;

    @Test
    public void getInitialFileListTest() {

        List<String> initialFileList = utilService.getInitialFileList(srcDir);
        initialFileList.forEach(f-> System.out.println(f));
        Assert.assertTrue(initialFileList.size()>0);
    }

    @Test
    public void getOldestFileNameSignature() {
//        long oldestFileNameSignature = utilService.getOldestFileNameSignature(LocalDateTime.now());
    }
}