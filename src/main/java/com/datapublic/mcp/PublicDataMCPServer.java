package com.datapublic.mcp;

import com.datapublic.mcp.controller.MCPServerController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublicDataMCPServer {

    public static void main(String[] args) {
        log.info("🚀 Public Data MCP 서버를 시작합니다...");
        log.info("📋 Java 버전: {}", System.getProperty("java.version"));

        MCPServerController mcpServerController = new MCPServerController();
        mcpServerController.run();
    }
}
