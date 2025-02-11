package com.example.ShortenerURL.controllers;

import jakarta.validation.Valid;

import com.example.ShortenerURL.services.ShortUrlService;
import com.example.ShortenerURL.models.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten")
public class ShortUrlController {

    private ShortUrlService urlService;

    public ShortUrlController(ShortUrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        UrlResponse createdShortUrl = urlService.createShortUrl(urlRequest.getUrl());
        return new ResponseEntity<>(createdShortUrl, HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> retrieveOriginalUrl(@PathVariable String shortCode) {
        UrlResponse urlResponse = urlService.retrieveOriginalUrl(shortCode);
        if (urlResponse != null) 
            return new ResponseEntity<>(urlResponse, HttpStatus.OK);
        else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> updateShortUrl(@PathVariable String shortCode, 
                                                      @Valid @RequestBody UrlRequest urlRequest) {
        UrlResponse urlUpdated = urlService.updateShortUrl(shortCode, urlRequest.getUrl());
        if (urlUpdated != null) 
            return new ResponseEntity<>(urlUpdated, HttpStatus.OK);
        else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        boolean isDeleted = urlService.deleteShortUrl(shortCode);
        if (isDeleted) 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        UrlStatsResponse stats = urlService.getUrlStats(shortCode);
        if (stats != null) 
            return new ResponseEntity<>(stats, HttpStatus.OK);
        else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);    
    }
}