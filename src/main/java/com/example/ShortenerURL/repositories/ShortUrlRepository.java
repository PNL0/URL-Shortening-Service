package com.example.ShortenerURL.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.ShortenerURL.models.ShortUrl;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    boolean existsByShortCode(String shortCode);

    ShortUrl findByShortCode(String shortCode);
    
    @Transactional
    void deleteByShortCode(String shortCode);
}