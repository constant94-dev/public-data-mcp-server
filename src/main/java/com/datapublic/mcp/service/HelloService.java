package com.datapublic.mcp.service;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloService {

    public static McpServerFeatures.SyncToolSpecification helloWorldTool() {
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
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("hello_world", "간단한 Hello World 메시지를 반환합니다.", schema),
                (exchange, arguments) -> {
                    String name = (String) arguments.getOrDefault("name", "World");
                    String message = "안녕하세요, " + name + "! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉";

                    // 핸들러 내부 로그 예시 (파라미터 바인딩 사용 권장)
                    log.info("hello_world 호출 - name: {}", name);
                    log.info("hello_world 응답 준비 완료");

                    return new McpSchema.CallToolResult(
                            message,
                            true
                    );
                }
        );
    }
}
