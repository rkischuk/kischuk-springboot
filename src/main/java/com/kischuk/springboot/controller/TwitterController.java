package com.kischuk.springboot.controller;

import com.kischuk.springboot.api.TwitterClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import twitter4j.TwitterException;

@RestController
@EnableWebMvc
public class TwitterController {
    @RequestMapping(path = "/twitter/{handle}", method = RequestMethod.GET)
    public Map<String, Object> twitterProfile( @PathVariable String handle ) {
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("result", TwitterClient.getUserTimeline( handle ));
        } catch( TwitterException te ) {
            response.put("error", te.getMessage());
            System.out.println(te.getMessage());
        } catch( IOException te ) {
            response.put("error", te.getMessage());
            System.out.println(te.getMessage());
        }

        return response;
    }
}
