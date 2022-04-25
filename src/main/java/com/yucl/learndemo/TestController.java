package com.yucl.learndemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String,String> getData(){
        Map<String,String> data = new HashMap<>();
        data.put("aa","1000");
        return data;
    }

    @GetMapping("/f1")
    public Map<String,String> getDataF(){
        Map<String,String> data = new HashMap<>();
        data.put("highScore","console.log(\"hello\")");
        return data;
    }

}
