package com.datapublic.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

import java.time.LocalDateTime;
import java.util.Map;

public class PublicDataMCPServer {

    public static void main(String[] args) {
        System.out.println("🚀 Public Data MCP 서버를 시작합니다...");
        System.out.println("📋 Java 버전: " + System.getProperty("java.version"));
        
        // STDIO 전송 프로바이더 생성
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        
        // MCP 서버 생성
        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo("public-data-mcp-server", "1.0.0")
                .capabilities(ServerCapabilities.builder().tools(true).build())
                .build();

        // Hello World 도구 추가
        var helloWorldTool = getHelloWorldToolSpecification();
        server.addTool(helloWorldTool);
        
        System.out.println("✅ Public Data MCP 서버가 성공적으로 시작되었습니다!");
        System.out.println("📝 사용 가능한 도구: hello_world");
        System.out.println("🔄 STDIO MCP 서버가 실행 중입니다. 종료하려면 Ctrl+C를 누르세요.");
    }

    private static SyncToolSpecification getHelloWorldToolSpecification() {
        // schema 정의
        String schema = """
                {
                  "type": "object",
                  "properties": {
                    "name": {
                      "type": "string",
                      "description": "인사할 이름 (기본값: World)"
                    }
                  }
                }
                """;
        
        // 도구 정의
        return new SyncToolSpecification(
                new Tool("hello_world", "간단한 Hello World 메시지를 반환합니다.", schema),
                (exchange, arguments) -> {
                    String name = (String) arguments.getOrDefault("name", "World");
                    String message = "안녕하세요, " + name + "! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉";
                    
                    return new CallToolResult(
                        "안녕하세요, " + name + "! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉",
                        true
                    );
                }
        );
    }
}
