# MCP Java SDK 서버 구성 작업 컨텍스트

## 📋 프로젝트 개요

**프로젝트명**: mcp-public-data  
**워크스페이스 경로**: `/Users/ethan/Cursor/mcp-public-data`  
**목표**: MCP(Model Context Protocol) Java SDK를 사용한 "Hello World" 응답 서버 구성  
**최종 커밋**: `[MCP/250810] mcp java sdk 서버 1차 구성`

## ⚙️ 전역 설정 참조
이 프로젝트는 워크스페이스 루트의 전역 설정을 사용합니다:
- **전역 Cursor AI 규칙**: `/Users/ethan/Cursor/.cursorrules`
- **전역 MCP 설정**: `/Users/ethan/Cursor/mcp.json`

## 🛠️ 기술 스택

- **Java**: 21 (SDKMAN으로 관리)
- **빌드 도구**: Gradle 8.8
- **MCP SDK**: 공식 MCP Java SDK (`io.modelcontextprotocol.sdk:mcp:0.11.1`)
- **패키지**: `com.datapublic.mcp`
- **메인 클래스**: `PublicDataMCPServer.java`

## 📁 프로젝트 구조

```
mcp-public-data/
├── build.gradle                 # Gradle 빌드 설정
├── gradle.properties           # Gradle 프로퍼티 (Java 21 경로 설정)
├── settings.gradle             # Gradle 프로젝트 설정
├── gradle/wrapper/             # Gradle Wrapper 파일들
├── src/main/java/com/datapublic/mcp/
│   └── PublicDataMCPServer.java # MCP 서버 메인 클래스
├── run-build-gradle.sh         # 빌드 및 실행 스크립트
├── run-github-mcp.sh           # GitHub MCP 실행 스크립트
├── README.md                   # 프로젝트 문서
├── CONTEXT.md                  # 프로젝트 컨텍스트 문서
└── env/                        # 환경 변수 디렉토리
    ├── .secrets.env           # GitHub MCP API 키 (실제 값)
    └── .secrets.env.example   # GitHub MCP API 키 템플릿
```

## 🔄 개발 과정

### 1단계: 초기 설정
- **워크스페이스**: `/Users/ethan/Cursor/mcp-public-data` 지정
- **빌드 도구**: Maven → Gradle로 변경
- **Java 버전**: 21 설정 (SDKMAN 사용)

### 2단계: MCP SDK 선택 과정
1. **Spring AI MCP** (초기 시도) → 정식 릴리스되지 않음으로 폐기
2. **codeboyzhou/mcp-declarative-java-sdk** (중간 시도) → 사용자 요청으로 폐기
3. **공식 MCP Java SDK** (최종 선택) → `io.modelcontextprotocol.sdk:mcp:0.11.1`

### 3단계: 패키지명 변경
- **초기**: `com.data.public.mcp` → Java 키워드 `public` 충돌
- **최종**: `com.datapublic.mcp`

### 4단계: 빌드 설정 최적화
- **Fat JAR 생성**: 런타임 의존성 포함
- **Gradle Wrapper**: 8.8 버전으로 업데이트
- **Java Home**: SDKMAN 경로 설정 (`/Users/ethan/.sdkman/candidates/java/current`)

## 💻 핵심 구현 내용

### PublicDataMCPServer.java
```java
package com.datapublic.mcp;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

public class PublicDataMCPServer {
    public static void main(String[] args) {
        // STDIO 전송 프로바이더 생성
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        
        // MCP 서버 생성
        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo("public-data-mcp-server", "1.0.0")
                .capabilities(ServerCapabilities.builder()
                        .tools(true)
                        .build())
                .build();
        
        // Hello World 도구 추가
        var helloWorldTool = getHelloWorldToolSpecification();
        server.addTool(helloWorldTool);
        
        // 서버 실행
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("서버가 종료되었습니다.");
        }
    }
    
    private static SyncToolSpecification getHelloWorldToolSpecification() {
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
        
        return new SyncToolSpecification(
                new Tool("hello_world", "간단한 Hello World 메시지를 반환합니다.", schema),
                (exchange, arguments) -> {
                    String name = (String) arguments.getOrDefault("name", "World");
                    String message = "안녕하세요, " + name + "! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉";
                    return new CallToolResult(message, true);
                }
        );
    }
}
```

### build.gradle (핵심 설정)
```gradle
plugins {
    id 'java'
    id 'application'
}

group = 'com.datapublic'
version = '1.0.0'
sourceCompatibility = '21'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.modelcontextprotocol.sdk:mcp:0.11.1'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
}

application {
    mainClass = 'com.datapublic.mcp.PublicDataMCPServer'
}

jar {
    manifest {
        attributes 'Main-Class': 'com.datapublic.mcp.PublicDataMCPServer'
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
```

## 🚀 실행 방법

### 1. 빌드 및 실행
```bash
./run-build-gradle.sh
```

### 2. MCP 도구 테스트
```bash
# 도구 목록 조회
echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/list"}' | java -jar build/libs/mcp-public-data-1.0.0.jar

# Hello World 도구 호출
echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "hello_world", "arguments": {"name": "사용자"}}}' | java -jar build/libs/mcp-public-data-1.0.0.jar
```

### 3. GitHub MCP 서버 연동
```bash
# GitHub MCP 서버 실행
./run-github-mcp.sh
```

## 🔗 GitHub MCP 서버 연동

### 구성 파일

#### mcp.json
```json
{
  "mcpServers": {
    "github": {
      "command": "/Users/ethan/Cursor/mcp-public-data/run-github-mcp.sh"
    }
  }
}
```

#### run-github-mcp.sh
```bash
#!/bin/bash

# GitHub MCP 서버 실행 스크립트
# 작업 디렉토리로 이동
cd /Users/ethan/Cursor/mcp-public-data

# 환경 변수 파일 로드
set -a
source ./env/.secrets.env
set +a

# Docker 컨테이너에서 GitHub MCP 서버 실행
docker exec -i \
  -e GITHUB_MCP_KEY \
  -e GITHUB_MCP_PROFILE \
  node24bg \
  npx -y @smithery/cli@latest \
  run @smithery-ai/github \
  --key "$GITHUB_MCP_KEY" \
  --profile "$GITHUB_MCP_PROFILE"
```

#### env/.secrets.env
```bash
GITHUB_MCP_KEY=<실제_GITHUB_MCP_KEY>
GITHUB_MCP_PROFILE=<실제_GITHUB_MCP_PROFILE>
```

### GitHub MCP 서버 특징
- **Docker 기반**: `node24bg` 컨테이너에서 실행
- **Smithery CLI**: `@smithery/cli@latest` 사용
- **GitHub 통합**: `@smithery-ai/github` 패키지 사용
- **환경 변수**: API 키와 프로필을 환경 변수로 관리
- **보안**: `.secrets.env` 파일로 민감한 정보 분리

### GitHub MCP 서버 기능
- GitHub 저장소 검색 및 조회
- 이슈 및 PR 관리
- 코드 검색 및 파일 조회
- 커밋 히스토리 조회
- 브랜치 및 태그 관리

## 📡 JSON vs JSON-RPC 응답 차이점

### 일반 JSON 응답
```json
{
  "message": "안녕하세요, 사용자! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉",
  "timestamp": "2024-01-15T20:17:32.435",
  "java_version": "21.0.7",
  "status": "success"
}
```

### MCP JSON-RPC 응답
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "안녕하세요, 사용자! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉"
      }
    ]
  }
}
```

## 🔧 해결된 문제들

### 1. Java 키워드 충돌
- **문제**: `package com.data.public.mcp`에서 `public` 키워드 충돌
- **해결**: `package com.datapublic.mcp`로 변경

### 2. MCP SDK 의존성 문제
- **문제**: Spring AI MCP가 정식 릴리스되지 않음
- **해결**: 공식 MCP Java SDK 사용

### 3. Fat JAR 생성 문제
- **문제**: `NoClassDefFoundError` 발생
- **해결**: `build.gradle`에서 런타임 의존성 포함 설정

### 4. Gradle Java Home 설정
- **문제**: 환경변수 `$HOME` 확장 안됨
- **해결**: 절대 경로 `/Users/ethan/.sdkman/candidates/java/current` 사용

## 📚 문서화 완료

### README.md 주요 섹션
1. **프로젝트 소개** - MCP Java SDK 기반 서버 설명
2. **기술 스택** - Java 21, Gradle, Docker 배지 포함
3. **빌드 및 실행** - 상세한 단계별 가이드
4. **프로젝트 구조** - 파일 및 디렉토리 설명
5. **구현 내용** - MCP 서버 구현 상세 설명
6. **테스트 결과** - JSON-RPC 응답 예시
7. **JSON vs JSON-RPC** - 응답 형식 차이점 비교

## 🎯 현재 상태

### ✅ 완료된 작업
- [x] MCP Java SDK 서버 1차 구성
- [x] Gradle 빌드 시스템 설정
- [x] Java 21 환경 구성
- [x] Hello World 도구 구현
- [x] JSON-RPC 응답 생성
- [x] Fat JAR 빌드 설정
- [x] 실행 스크립트 작성
- [x] GitHub MCP 서버 연동 구성
- [x] 환경 변수 보안 설정
- [x] README.md 문서화
- [x] Git 커밋 및 푸시

### 🔄 다음 단계 가능성
- [ ] 공공데이터포털 API 연동
- [ ] 추가 MCP 도구 구현
- [ ] 에러 처리 강화
- [ ] 로깅 시스템 추가
- [ ] Docker 컨테이너화
- [ ] CI/CD 파이프라인 구성

## 📝 Git 히스토리

### 최종 커밋
```
[MCP/250810] mcp java sdk 서버 1차 구성

+ MCP 공식 홈페이지 권장 java sdk 사용
+ json-rpc 응답 생성
+ 구현 내용 README 작성
+ build, github mcp 실행 스크립트 작성
```

### 커밋 통계
- **커밋 해시**: `a2e9a22`
- **변경된 파일**: 13개
- **추가된 라인**: 818줄
- **삭제된 라인**: 33줄

## 🔗 관련 링크

- **공식 MCP Java SDK**: https://github.com/modelcontextprotocol/java-sdk
- **GitHub 저장소**: https://github.com/constant94-dev/mcp-public-data
- **MCP 프로토콜**: https://modelcontextprotocol.io/

---

**마지막 업데이트**: 2024-01-15  
**작성자**: Ethan  
**상태**: 1차 구성 완료 ✅
