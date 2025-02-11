package com.example.urlshortener.controllers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ShortenerURL.controllers.ShortUrlController;
import com.example.ShortenerURL.models.UrlRequest;
import com.example.ShortenerURL.models.UrlResponse;
import com.example.ShortenerURL.models.UrlStatsResponse;
import com.example.ShortenerURL.services.ShortUrlService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ShortUrlController.class)
class ShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlService urlService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateShortUrl() throws Exception {
        UrlRequest request = new UrlRequest("https://example.com");
        LocalDateTime time = LocalDateTime.now();
        UrlResponse response = new UrlResponse(1L, "https://example.com", "abc123", time, time);
        
        when(urlService.createShortUrl(anyString())).thenReturn(response);
        
        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("https://example.com"));
    }

    @Test
    void testCreateShortUrl_EmptyUrl() throws Exception {
        UrlRequest request = new UrlRequest(""); // Empty URL

        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateShortUrl_InvalidUrl() throws Exception {
        UrlRequest request = new UrlRequest("invalid-url"); // No protocol (http, https, ftp)

        mockMvc.perform(post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRetrieveOriginalUrl() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        UrlResponse response = new UrlResponse(1L, "https://example.com", "abc123", time, time);
        
        when(urlService.retrieveOriginalUrl("abc123")).thenReturn(response);
        
        mockMvc.perform(get("/shorten/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com"))
                .andExpect(jsonPath("$.shortCode").value("abc123"));
    }

    @Test
    void testRetrieveOriginalUrl_NotFound() throws Exception {
        when(urlService.retrieveOriginalUrl("invalid"))
                .thenReturn(null);
        
        mockMvc.perform(get("/shorten/invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateShortUrl() throws Exception {
        UrlRequest request = new UrlRequest("https://updated.com");
        UrlResponse response = new UrlResponse(1L, "https://updated.com", "abc123", LocalDateTime.now(), LocalDateTime.now());
        
        when(urlService.updateShortUrl(eq("abc123"), anyString())).thenReturn(response);
        
        mockMvc.perform(put("/shorten/abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://updated.com"))
                .andExpect(jsonPath("$.shortCode").value("abc123"));
    }

    @Test
    void testUpdateShortUrl_EmptyUrl() throws Exception {
        UrlRequest request = new UrlRequest(""); // Empty URL

        mockMvc.perform(put("/shorten/abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateShortUrl_InvalidUrl() throws Exception {
        UrlRequest request = new UrlRequest("invalid-url"); // No protocol (http, https, ftp)

        mockMvc.perform(put("/shorten/abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateShortUrl_NotFound() throws Exception {
        UrlRequest request = new UrlRequest("https://updated.com");
        
        when(urlService.updateShortUrl(eq("invalid"), anyString())).thenReturn(null);
        
        mockMvc.perform(put("/shorten/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteShortUrl() throws Exception {
        when(urlService.deleteShortUrl("abc123")).thenReturn(true);
        
        mockMvc.perform(delete("/shorten/abc123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShortUrl_NotFound() throws Exception {
        when(urlService.deleteShortUrl("invalid")).thenReturn(false);
        
        mockMvc.perform(delete("/shorten/invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUrlStats() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        UrlStatsResponse stats = new UrlStatsResponse(1L, "https://example.com", "abc123", time, time,1);
        
        when(urlService.getUrlStats("abc123")).thenReturn(stats);
        
        mockMvc.perform(get("/shorten/abc123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://example.com"))
                .andExpect(jsonPath("$.shortCode").value("abc123"))
                .andExpect(jsonPath("$.accessCount").value(1));
    }

    @Test
    void testGetUrlStats_NotFound() throws Exception {
        when(urlService.getUrlStats("invalid"))
                .thenReturn(null);
        
        mockMvc.perform(get("/shorten/invalid/stats"))
                .andExpect(status().isNotFound());
    }
}
