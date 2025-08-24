package com.datapublic.mcp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * Spring Boot 백엔드와 통신하는 REST API 클라이언트
 */
@Slf4j
public class SpringBootApiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public SpringBootApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1MB
                .build();
        
        log.info("🌐 Spring Boot API 클라이언트 초기화 완료: {}", baseUrl);
    }

    /**
     * 기본 생성자 - localhost:8080 사용
     */
    public SpringBootApiClient() {
        this("http://localhost:8080");
    }

    /**
     * 헬스 체크
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> healthCheck() {
        log.debug("🏥 헬스 체크 요청: {}/api/health", baseUrl);
        
        return webClient.get()
                .uri("/api/health")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("✅ 헬스 체크 성공: {}", response))
                .doOnError(error -> log.error("❌ 헬스 체크 실패: {}", error.getMessage()));
    }

    /**
     * 사용자 정보 조회
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getUserInfo(String userId) {
        log.debug("👤 사용자 정보 조회: {}/api/users/{}", baseUrl, userId);
        
        return webClient.get()
                .uri("/api/users/{userId}", userId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("✅ 사용자 정보 조회 성공: {}", response))
                .doOnError(error -> log.error("❌ 사용자 정보 조회 실패: {}", error.getMessage()));
    }

    /**
     * 세션 정보 조회
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getSessionInfo(String sessionId) {
        log.debug("🔐 세션 정보 조회: {}/api/sessions/{}", baseUrl, sessionId);
        
        return webClient.get()
                .uri("/api/sessions/{sessionId}", sessionId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("✅ 세션 정보 조회 성공: {}", response))
                .doOnError(error -> log.error("❌ 세션 정보 조회 실패: {}", error.getMessage()));
    }

    /**
     * 공공데이터 정보 조회
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getPublicDataInfo(String dataId) {
        log.debug("📊 공공데이터 정보 조회: {}/api/public-data/{}", baseUrl, dataId);
        
        return webClient.get()
                .uri("/api/public-data/{dataId}", dataId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("✅ 공공데이터 정보 조회 성공: {}", response))
                .doOnError(error -> log.error("❌ 공공데이터 정보 조회 실패: {}", error.getMessage()));
    }

    /**
     * 로그 기록 생성
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> createLog(Map<String, Object> logData) {
        log.debug("📝 로그 기록 생성: {}/api/logs", baseUrl);
        
        return webClient.post()
                .uri("/api/logs")
                .bodyValue(logData)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> log.debug("✅ 로그 기록 생성 성공: {}", response))
                .doOnError(error -> log.error("❌ 로그 기록 생성 실패: {}", error.getMessage()));
    }

    /**
     * 연결 테스트
     */
    public Mono<Boolean> testConnection() {
        return healthCheck()
                .map(response -> {
                    String status = (String) response.get("status");
                    boolean isHealthy = "UP".equals(status);
                    log.info("🔗 연결 테스트 결과: {} (status: {})", isHealthy ? "성공" : "실패", status);
                    return isHealthy;
                })
                .onErrorReturn(false)
                .doOnError(error -> log.error("❌ 연결 테스트 실패: {}", error.getMessage()));
    }

    /**
     * WebClient 인스턴스 반환 (고급 사용자용)
     */
    public WebClient getWebClient() {
        return webClient;
    }

    /**
     * 기본 URL 반환
     */
    public String getBaseUrl() {
        return baseUrl;
    }
}
