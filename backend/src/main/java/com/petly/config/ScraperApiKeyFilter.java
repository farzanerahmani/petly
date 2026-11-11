package com.petly.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public class ScraperApiKeyFilter extends OncePerRequestFilter {

    private static final String SCRAPER_PATH =
            "/api/v1/scraper/";

    private static final String API_KEY_HEADER =
            "X-API-KEY";

    private final byte[] configuredApiKey;

    public ScraperApiKeyFilter(
            @Value("${petly.scraper.api-key}") String apiKey
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Scraper API key must not be empty."
            );
        }

        this.configuredApiKey =
                apiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        boolean isScraperPath =
                path.equals("/api/v1/scraper")
                        || path.startsWith("/api/v1/scraper/");

        boolean isPost =
                "POST".equalsIgnoreCase(request.getMethod());

        return !isScraperPath || !isPost;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String providedKey = request.getHeader(API_KEY_HEADER);

        if (providedKey == null
                || !MessageDigest.isEqual(
                configuredApiKey,
                providedKey.getBytes(StandardCharsets.UTF_8)
        )) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid scraper API key."
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}

