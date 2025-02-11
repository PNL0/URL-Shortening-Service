package com.example.urlshortener.services;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.ShortenerURL.models.ShortUrl;
import com.example.ShortenerURL.models.UrlResponse;
import com.example.ShortenerURL.models.UrlStatsResponse;
import com.example.ShortenerURL.repositories.ShortUrlRepository;
import com.example.ShortenerURL.services.ShortUrlService;

class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository urlRepository;

    @InjectMocks
    private ShortUrlService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShortUrl() {
        String url = "https://example.com";
        String shortCode = "abc123";
        LocalDateTime time = LocalDateTime.now();
        ShortUrl shortUrl = new ShortUrl(1L, url, shortCode, time, time, 0);

        when(urlRepository.existsByShortCode(anyString())).thenReturn(false);
        when(urlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);

        UrlResponse response = urlService.createShortUrl(url);

        assertNotNull(response);
        assertEquals(url, response.getUrl());
    }

    @Test
    void testRetrieveOriginalUrl_Found() {
        String url = "https://example.com";
        String shortCode = "abc123";
        LocalDateTime time = LocalDateTime.now();
        ShortUrl shortUrl = new ShortUrl(1L, url, shortCode, time, time, 0);

        when(urlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);
        when(urlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);

        UrlResponse response = urlService.retrieveOriginalUrl(shortCode);

        assertNotNull(response);
        assertEquals(url, response.getUrl());
        assertEquals(shortCode, response.getShortCode());
    }

    @Test
    void testRetrieveOriginalUrl_NotFound() {
        when(urlRepository.findByShortCode("invalid")).thenReturn(null);
        
        UrlResponse response = urlService.retrieveOriginalUrl("invalid");
        
        assertNull(response);
    }

    @Test
    void testUpdateShortUrl_Found() {
        String shortCode = "abc123";
        String url = "https://example.com";
        LocalDateTime time = LocalDateTime.now();
        ShortUrl shortUrl = new ShortUrl(1L, url, shortCode, time, time, 0);

        when(urlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);
        when(urlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UrlResponse response = urlService.updateShortUrl(shortCode, "https://updated.com");
        
        assertNotNull(response);
        assertEquals("https://updated.com", response.getUrl());
        assertEquals(shortCode, response.getShortCode());
        assertNotEquals(time, response.getUpdatedAt());
    }

    @Test
    void testUpdateShortUrl_NotFound() {
        when(urlRepository.findByShortCode("invalid")).thenReturn(null);
        
        UrlResponse response = urlService.updateShortUrl("invalid", "https://updated.com");
        
        assertNull(response);
    }

    @Test
    void testDeleteShortUrl_Found() {
        when(urlRepository.existsByShortCode("abc123")).thenReturn(true);
        doNothing().when(urlRepository).deleteByShortCode("abc123");

        boolean result = urlService.deleteShortUrl("abc123");
        
        assertTrue(result);
    }

    @Test
    void testDeleteShortUrl_NotFound() {
        when(urlRepository.existsByShortCode("invalid")).thenReturn(false);
        
        boolean result = urlService.deleteShortUrl("invalid");
        
        assertFalse(result);
    }

    @Test
    void testGetUrlStats_Found() {
        String shortCode = "abc123";
        ShortUrl shortUrl = new ShortUrl(1L, "https://example.com", shortCode, LocalDateTime.now(), LocalDateTime.now(), 10);

        when(urlRepository.findByShortCode(shortCode)).thenReturn(shortUrl);

        UrlStatsResponse response = urlService.getUrlStats(shortCode);
        
        assertNotNull(response);
        assertEquals(shortCode, response.getShortCode());
        assertEquals(10, response.getAccessCount());
    }

    @Test
    void testGetUrlStats_NotFound() {
        when(urlRepository.findByShortCode("invalid")).thenReturn(null);
        
        UrlStatsResponse response = urlService.getUrlStats("invalid");
        
        assertNull(response);
    }
}
