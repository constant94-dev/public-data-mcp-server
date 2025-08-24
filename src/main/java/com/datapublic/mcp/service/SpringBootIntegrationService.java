package com.datapublic.mcp.service;

import com.datapublic.mcp.client.SpringBootApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Boot 백엔드와 통신하는 MCP 도구들
 */
@Slf4j
public class SpringBootIntegrationService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static SpringBootApiClient apiClient;

    static {
        // 환경변수에서 Spring Boot 서버 URL 가져오기 (기본값: localhost:8080)
        String springBootUrl = System.getenv().getOrDefault("SPRING_BOOT_URL", "http://localhost:8080");
        apiClient = new SpringBootApiClient(springBootUrl);
    }

    /**
     * Spring Boot 백엔드 연결 테스트 도구
     */
    public static McpServerFeatures.SyncToolSpecification springBootHealthCheckTool() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "properties": {
                    "timeout": {
                      "type": "integer",
                      "description": "타임아웃 시간 (초, 기본값: 10)",
                      "default": 10
                    }
                  }
                }
                """;

        // 도구 정의
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("spring_boot_health_check", "Spring Boot 백엔드 서버의 헬스 체크를 수행합니다.", schema),
                (exchange, arguments) -> {
                    log.info("🏥 Spring Boot 헬스 체크 요청");
                    
                    try {
                        // 동기적으로 Mono 결과를 기다림
                        Boolean isHealthy = apiClient.testConnection().block();
                        
                        String message = isHealthy != null && isHealthy ? 
                                "Spring Boot 서버가 정상 작동 중입니다. (URL: " + apiClient.getBaseUrl() + ")" : 
                                "Spring Boot 서버에 연결할 수 없습니다. (URL: " + apiClient.getBaseUrl() + ")";
                        
                        log.info("✅ Spring Boot 헬스 체크 완료: {}", isHealthy != null && isHealthy);
                        
                        return new McpSchema.CallToolResult(
                                message,
                                isHealthy != null && isHealthy
                        );
                    } catch (Exception e) {
                        log.error("❌ Spring Boot 헬스 체크 실패", e);
                        return new McpSchema.CallToolResult(
                                "헬스 체크 중 오류가 발생했습니다: " + e.getMessage(),
                                false
                        );
                    }
                }
        );
    }

    /**
     * 사용자 정보 조회 도구
     */
    public static McpServerFeatures.SyncToolSpecification getUserInfoTool() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "required": ["userId"],
                  "properties": {
                    "userId": {
                      "type": "string",
                      "description": "조회할 사용자 ID"
                    }
                  }
                }
                """;

        // 도구 정의
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_user_info", "Spring Boot 백엔드에서 사용자 정보를 조회합니다.", schema),
                (exchange, arguments) -> {
                    String userId = (String) arguments.get("userId");
                    log.info("👤 사용자 정보 조회 요청: userId={}", userId);
                    
                    try {
                        Map<String, Object> userInfo = apiClient.getUserInfo(userId).block();
                        
                        if (userInfo != null) {
                            log.info("✅ 사용자 정보 조회 성공: {}", userId);
                            String message = "사용자 정보 조회 성공: " + objectMapper.writeValueAsString(userInfo);
                            return new McpSchema.CallToolResult(message, true);
                        } else {
                            log.warn("⚠️ 사용자 정보를 찾을 수 없음: {}", userId);
                            return new McpSchema.CallToolResult(
                                    "사용자 정보를 찾을 수 없습니다: " + userId,
                                    false
                            );
                        }
                    } catch (Exception e) {
                        log.error("❌ 사용자 정보 조회 실패: userId={}", userId, e);
                        return new McpSchema.CallToolResult(
                                "사용자 정보 조회 중 오류가 발생했습니다: " + e.getMessage(),
                                false
                        );
                    }
                }
        );
    }

    /**
     * 세션 정보 조회 도구
     */
    public static McpServerFeatures.SyncToolSpecification getSessionInfoTool() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "required": ["sessionId"],
                  "properties": {
                    "sessionId": {
                      "type": "string",
                      "description": "조회할 세션 ID"
                    }
                  }
                }
                """;

        // 도구 정의
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_session_info", "Spring Boot 백엔드에서 세션 정보를 조회합니다.", schema),
                (exchange, arguments) -> {
                    String sessionId = (String) arguments.get("sessionId");
                    log.info("🔐 세션 정보 조회 요청: sessionId={}", sessionId);
                    
                    try {
                        Map<String, Object> sessionInfo = apiClient.getSessionInfo(sessionId).block();
                        
                        if (sessionInfo != null) {
                            log.info("✅ 세션 정보 조회 성공: {}", sessionId);
                            String message = "세션 정보 조회 성공: " + objectMapper.writeValueAsString(sessionInfo);
                            return new McpSchema.CallToolResult(message, true);
                        } else {
                            log.warn("⚠️ 세션 정보를 찾을 수 없음: {}", sessionId);
                            return new McpSchema.CallToolResult(
                                    "세션 정보를 찾을 수 없습니다: " + sessionId,
                                    false
                            );
                        }
                    } catch (Exception e) {
                        log.error("❌ 세션 정보 조회 실패: sessionId={}", sessionId, e);
                        return new McpSchema.CallToolResult(
                                "세션 정보 조회 중 오류가 발생했습니다: " + e.getMessage(),
                                false
                        );
                    }
                }
        );
    }

    /**
     * 공공데이터 정보 조회 도구
     */
    public static McpServerFeatures.SyncToolSpecification getPublicDataInfoTool() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "required": ["dataId"],
                  "properties": {
                    "dataId": {
                      "type": "string",
                      "description": "조회할 공공데이터 ID"
                    }
                  }
                }
                """;

        // 도구 정의
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("get_public_data_info", "Spring Boot 백엔드에서 공공데이터 정보를 조회합니다.", schema),
                (exchange, arguments) -> {
                    String dataId = (String) arguments.get("dataId");
                    log.info("📊 공공데이터 정보 조회 요청: dataId={}", dataId);
                    
                    try {
                        Map<String, Object> publicDataInfo = apiClient.getPublicDataInfo(dataId).block();
                        
                        if (publicDataInfo != null) {
                            log.info("✅ 공공데이터 정보 조회 성공: {}", dataId);
                            String message = "공공데이터 정보 조회 성공: " + objectMapper.writeValueAsString(publicDataInfo);
                            return new McpSchema.CallToolResult(message, true);
                        } else {
                            log.warn("⚠️ 공공데이터 정보를 찾을 수 없음: {}", dataId);
                            return new McpSchema.CallToolResult(
                                    "공공데이터 정보를 찾을 수 없습니다: " + dataId,
                                    false
                            );
                        }
                    } catch (Exception e) {
                        log.error("❌ 공공데이터 정보 조회 실패: dataId={}", dataId, e);
                        return new McpSchema.CallToolResult(
                                "공공데이터 정보 조회 중 오류가 발생했습니다: " + e.getMessage(),
                                false
                        );
                    }
                }
        );
    }

    /**
     * 로그 기록 생성 도구
     */
    public static McpServerFeatures.SyncToolSpecification createLogTool() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "required": ["level", "message"],
                  "properties": {
                    "level": {
                      "type": "string",
                      "description": "로그 레벨 (INFO, WARN, ERROR, DEBUG)",
                      "enum": ["INFO", "WARN", "ERROR", "DEBUG"]
                    },
                    "message": {
                      "type": "string",
                      "description": "로그 메시지"
                    },
                    "source": {
                      "type": "string",
                      "description": "로그 소스 (기본값: mcp-server)",
                      "default": "mcp-server"
                    },
                    "metadata": {
                      "type": "object",
                      "description": "추가 메타데이터"
                    }
                  }
                }
                """;

        // 도구 정의
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("create_log", "Spring Boot 백엔드에 로그 기록을 생성합니다.", schema),
                (exchange, arguments) -> {
                    String level = (String) arguments.get("level");
                    String message = (String) arguments.get("message");
                    String source = (String) arguments.getOrDefault("source", "mcp-server");
                    
                    log.info("📝 로그 기록 생성 요청: level={}, message={}, source={}", level, message, source);
                    
                    try {
                        ObjectNode logData = objectMapper.createObjectNode();
                        logData.put("level", level);
                        logData.put("message", message);
                        logData.put("source", source);
                        
                        if (arguments.containsKey("metadata")) {
                            logData.set("metadata", objectMapper.valueToTree(arguments.get("metadata")));
                        }
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Object> result = apiClient.createLog((Map<String, Object>) objectMapper.convertValue(logData, Map.class)).block();
                        
                        if (result != null) {
                            log.info("✅ 로그 기록 생성 성공");
                            String resultMessage = "로그 기록 생성 성공: " + objectMapper.writeValueAsString(result);
                            return new McpSchema.CallToolResult(resultMessage, true);
                        } else {
                            log.warn("⚠️ 로그 기록 생성 실패");
                            return new McpSchema.CallToolResult(
                                    "로그 기록 생성에 실패했습니다.",
                                    false
                            );
                        }
                    } catch (Exception e) {
                        log.error("❌ 로그 기록 생성 실패", e);
                        return new McpSchema.CallToolResult(
                                "로그 기록 생성 중 오류가 발생했습니다: " + e.getMessage(),
                                false
                        );
                    }
                }
        );
    }
}
