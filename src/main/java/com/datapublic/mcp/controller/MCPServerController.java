package com.datapublic.mcp.controller;

import com.datapublic.mcp.service.HelloService;
import com.datapublic.mcp.service.SpringBootIntegrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MCPServerController {

    public void run() {
        log.info("Starting MCPServerController....");

        try {
            // 1) STDIO 전송 선언
            StdioServerTransportProvider transportProvider = createStdIoTransport();
            // 2) MCP 서버 생성
            McpSyncServer server = createServer(
                    transportProvider,
                    "public-data-mcp-server",
                    "1.0.0"
            );
            // 3) 서비스에서 도구 규격 수집 후 등록
            List<McpServerFeatures.SyncToolSpecification> tools = List.of(
                    HelloService.helloWorldTool(), // Hello World 도구
                    SpringBootIntegrationService.springBootHealthCheckTool(), // Spring Boot 헬스 체크
                    SpringBootIntegrationService.getUserInfoTool(), // 사용자 정보 조회
                    SpringBootIntegrationService.getSessionInfoTool(), // 세션 정보 조회
                    SpringBootIntegrationService.getPublicDataInfoTool(), // 공공데이터 정보 조회
                    SpringBootIntegrationService.createLogTool() // 로그 기록 생성
            );
            registerTools(server, tools);

            log.info("✅ Public Data MCP 서버가 성공적으로 시작되었습니다!");
            log.info("✅ 서버 초기화 완료. 도구 목록: {}", tools.stream()
                    .map(t -> t.tool().name()).toList());
            log.info("🔄 STDIO MCP 서버 대기 중 (Ctrl+C 종료)");
        } catch (Exception e) {
            log.error("❌ MCP 서버 시작 중 오류가 발생했습니다.", e);
        }
    }

    private void registerTools(
            McpSyncServer server,
            List<McpServerFeatures.SyncToolSpecification> tools
    ) {
        for (McpServerFeatures.SyncToolSpecification spec : tools) {
            log.info("도구 등록: {}", spec.tool().name());
            server.addTool(spec);
        }
    }

    private McpSyncServer createServer(
            StdioServerTransportProvider transportProvider,
            String serverName,
            String version
    ) {
        log.debug("MCP 서버 생성: name={}, version={}", serverName, version);

        return McpServer.sync(transportProvider)
                .serverInfo(serverName, version)
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true) // tools 기능 활성화...
                        .build())
                .build();
    }

    private StdioServerTransportProvider createStdIoTransport() {
        log.debug("STDIO 전송 프로바이더 생성");

        return new StdioServerTransportProvider(new ObjectMapper());
    }
}
