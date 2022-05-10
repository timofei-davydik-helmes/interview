package com.helmes.interview.controller;

import com.helmes.interview.util.CounterStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/counter")
    public int getCounter() {
        return CounterStorage.get();
    }
}
