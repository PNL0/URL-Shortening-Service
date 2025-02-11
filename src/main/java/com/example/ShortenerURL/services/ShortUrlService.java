package com.example.ShortenerURL.services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ShortenerURL.models.ShortUrl;
import com.example.ShortenerURL.models.UrlResponse;
import com.example.ShortenerURL.models.UrlStatsResponse;
import com.example.ShortenerURL.repositories.ShortUrlRepository;

@Service
public class ShortUrlService {
    
    private ShortUrlRepository urlRepository;
    private final Random random = new Random();

    public ShortUrlService (ShortUrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    // Generate a random short code
    private String generateShortCode() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortCode = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            shortCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortCode.toString();
    }

    public UrlResponse createShortUrl(String url){
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));
        LocalDateTime time = LocalDateTime.now();
        ShortUrl shortUrl = new ShortUrl(null, url, shortCode, time, time,0);
        shortUrl = urlRepository.save(shortUrl);

        return new UrlResponse(
            shortUrl.getId(), 
            shortUrl.getUrl(), 
            shortUrl.getShortCode(), 
            shortUrl.getCreatedAt(), 
            shortUrl.getUpdatedAt()
        );
    }

    public UrlResponse retrieveOriginalUrl(String shortCode) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode);
        if(shortUrl == null) // Not Found
            return null;

        // Update access count
        shortUrl.setAccessCount(shortUrl.getAccessCount() + 1);
        shortUrl = urlRepository.save(shortUrl);

        return new UrlResponse(
            shortUrl.getId(), 
            shortUrl.getUrl(), 
            shortUrl.getShortCode(), 
            shortUrl.getCreatedAt(), 
            shortUrl.getUpdatedAt()
        );
    } 

    public UrlResponse updateShortUrl(String shortCode, String url) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode);
        if(shortUrl == null) // Not Found
            return null;

        // Updates
        shortUrl.setUrl(url);
        shortUrl.setUpdatedAt(LocalDateTime.now());
        shortUrl = urlRepository.save(shortUrl);

        return new UrlResponse(
            shortUrl.getId(), 
            shortUrl.getUrl(), 
            shortUrl.getShortCode(), 
            shortUrl.getCreatedAt(), 
            shortUrl.getUpdatedAt()
        );
    } 

    @Transactional
    public boolean deleteShortUrl(String shortCode) {
        if (urlRepository.existsByShortCode(shortCode)) {
            urlRepository.deleteByShortCode(shortCode);
            return true;
        }
        return false;  // Short URL not found
    }

    public UrlStatsResponse getUrlStats(String shortCode) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode);
        if(shortUrl == null) // Not Found
            return null;

        return new UrlStatsResponse(
            shortUrl.getId(), 
            shortUrl.getUrl(), 
            shortUrl.getShortCode(), 
            shortUrl.getCreatedAt(), 
            shortUrl.getUpdatedAt(), 
            shortUrl.getAccessCount()
        );
    }
}
