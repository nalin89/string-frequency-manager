package com.homeaway.saas.frequencyManager.controller;

import com.homeaway.saas.frequencyManager.model.ResponseVal;
import com.homeaway.saas.frequencyManager.service.LogParserService;
import com.homeaway.saas.frequencyManager.service.ReverseIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrequencyManagerController {

    @Autowired
    private LogParserService logParserService;
    @Autowired
    private ReverseIndex reverseIndex;

    @RequestMapping("/isStringValid")
    public ResponseEntity<ResponseVal> isStringValid(@RequestParam String string){
        if (string.length()>40){
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseVal("The caller can only send a string of up to 40 characters"));
        }

        return ResponseEntity.ok(new ResponseVal(Boolean.toString(reverseIndex.isValid(string))));
    }
}
