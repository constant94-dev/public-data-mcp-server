# Public Data MCP Server

## 🎯 프로젝트 개요

공공데이터포털 API를 사용하는 MCP (Model Context Protocol) 서버입니다. Java 21과 Gradle을 사용하여 구현되었습니다.

### 주요 기능
- **MCP 프로토콜 지원**: Model Context Protocol 표준 준수
- **Hello World 도구**: 사용자 이름을 받아서 인사 메시지를 반환
- **STDIO 통신**: 표준 입출력을 통한 MCP 프로토콜 통신

## 🛠️ 기술 스택

- **Java**: 21
- **Gradle**: 8.8
- **MCP Java SDK**: 0.11.1
- **Docker**: 컨테이너 지원

## ✅ 완료된 작업

- ✅ MCP Java SDK 기반 서버 구현
- ✅ Hello World 도구 구현 및 등록
- ✅ STDIO 전송 프로토콜 구현
- ✅ JSON-RPC 2.0 프로토콜 지원
- ✅ Docker 컨테이너 지원

## 📋 다음 작업

### 1순위: 공공데이터 API 연동
- [ ] 공공데이터포털 API 클라이언트 구현
- [ ] 데이터 조회 도구 구현
- [ ] API 키 관리 시스템

### 2순위: 고급 기능
- [ ] 데이터 캐싱 시스템
- [ ] 에러 처리 개선
- [ ] 로깅 시스템

## 🚀 시작하기

### 요구사항
- Java 21+
- Gradle 8.0+

### 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew clean build

# 서버 실행
./gradlew bootRun
```

### 실행 확인

서버가 성공적으로 시작되면 다음과 같은 메시지가 출력됩니다:

```
🚀 Public Data MCP 서버를 시작합니다...
📋 Java 버전: 21.0.7
✅ Public Data MCP 서버가 성공적으로 시작되었습니다!
📝 사용 가능한 도구: hello_world
🔄 STDIO MCP 서버가 실행 중입니다. 종료하려면 Ctrl+C를 누르세요.
```

## 🛠️ 사용 가능한 도구

### Hello World 도구
간단한 Hello World 메시지를 반환하는 도구입니다.

**매개변수:**
- `name` (선택사항): 인사할 이름 (기본값: "World")

**예시 응답:**
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

## 🔧 개발

### 프로젝트 구조
```
src/main/java/com/datapublic/mcp/
├── PublicDataMCPServer.java    # 메인 서버 클래스
├── controller/
│   └── MCPServerController.java # MCP 컨트롤러
└── service/
    └── HelloService.java       # Hello World 서비스
```

### 새로운 도구 추가

새로운 MCP 도구를 추가하려면 `SyncToolSpecification`을 사용하세요:

```java
private static SyncToolSpecification getMyNewToolSpecification() {
    String schema = """
            {
              "type": "object",
              "properties": {
                "param1": {
                  "type": "string",
                  "description": "첫 번째 매개변수"
                }
              }
            }
            """;
    
    return new SyncToolSpecification(
            new Tool("my_new_tool", "새로운 도구 설명", schema),
            (exchange, arguments) -> {
                String param1 = (String) arguments.getOrDefault("param1", "기본값");
                return new CallToolResult("도구 실행 결과", true);
            }
    );
}
```

## 🔗 관련 프로젝트

- **[spring-boot-mcp-integration](https://github.com/constant94-dev/spring-boot-mcp-integration)**: Spring Boot 통합 서버
- **[vue-mcp-integration](https://github.com/constant94-dev/vue-mcp-integration)**: Vue.js 프론트엔드 클라이언트

## 📚 참고 자료

- [MCP Java SDK 공식 문서](https://modelcontextprotocol.io/sdk/java/mcp-overview)
- [Model Context Protocol](https://modelcontextprotocol.io/)

## 📖 기술 문서

### 🔧 개발 가이드
- **[요구사항 및 로드맵](./docs/development/requirements-and-roadmap.md)**: 기능/비기능 요구사항 및 향후 계획
- **[개발 실행 계획](./docs/development/development-execution-plan.md)**: 단계별 구현 계획 및 위험 요소

### 🏗️ 아키텍처 및 설계
- **[프로젝트 아키텍처 및 설계](./docs/architecture/project-architecture-and-design.md)**: 프로젝트 구조, 설계 원칙, 확장 계획

### 🔐 보안 및 환경
- **[보안 환경변수 설정](./docs/security/security-environment-variables.md)**: 민감한 데이터 관리 및 환경변수 설정

### 📡 프로토콜 및 통신
- **[JSON vs JSON-RPC 비교](./docs/protocols/protocol-comparison-json-jsonrpc.md)**: 프로토콜 차이점 및 구현 방법

### 📊 다이어그램 및 시각화
- **[시스템 아키텍처 플로우차트](./docs/diagrams/system-architecture-flowchart.mmd)**: 시스템 컴포넌트 구조
- **[통신 시퀀스 다이어그램](./docs/diagrams/communication-sequence-diagram.mmd)**: 통신 흐름 시퀀스
- **[아키텍처 컴포넌트 이미지](./docs/diagrams/arch-component-flowchart.png)**: 시스템 컴포넌트 구조 이미지
- **[통신 시퀀스 이미지](./docs/diagrams/request-response-sequence.png)**: 통신 흐름 이미지
- **[MCP 프로토콜 이미지](./docs/diagrams/modelcontextprotocol.svg)**: MCP 프로토콜 구조 이미지

---

**마지막 업데이트**: 2025-08-17  
**작성자**: Ethan  
**상태**: 기본 MCP 서버 구현 완료 ✅ (공공데이터 API 연동 준비됨)
