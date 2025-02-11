package com.example.ShortenerURL.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UrlRequest {
    @NotBlank(message = "URL cannot be empty")
    @Pattern(regexp = "^(https?|ftp)://.+$", message = "Invalid URL format")
    private String url;

    public UrlRequest() {}  

    public UrlRequest(String url) {
        this.url = url;
    }

    // Getter and Setter
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
