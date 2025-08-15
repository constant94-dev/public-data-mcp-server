## 공공데이터포털 API를 사용해 MCP Server를 만들고 있습니다.

### ✅ 성공적으로 구현된 기능
- **Hello World 도구**: 사용자 이름을 받아서 인사 메시지를 반환
- **MCP 프로토콜 지원**: Model Context Protocol 표준 준수
- **Java 21**: Java 기능 활용
- **Gradle 빌드**: 빌드 도구
- **STDIO 통신**: 표준 입출력을 통한 MCP 프로토콜 통신

### 💻 기술스택

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/gradle-%2302303A.svg?style=for-the-badge&logo=gradle&logoColor=white)
![MCP](https://img.shields.io/badge/MCP-Protocol-blue?style=for-the-badge)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

#### _[Server]_
- Java 21
- Gradle 8.8
- MCP Java SDK 0.11.1
- Docker

#### _[MCP Tool]_
- github

### 📋 요구사항

#### _[요청 & 응답 다이어그램]_

![request-response-Diagram](https://github.com/constant94-dev/mcp-public-data/blob/main/post/request-response-sequence.png)

#### _[아키텍처(컴포넌트) 다이어그램]_

![request-response-Diagram](https://github.com/constant94-dev/mcp-public-data/blob/main/post/arch-component-flowchart.png)

#### _환경 변수 설정 (Secrets 분리)_
`./env/.secrets.env` 파일에 아래 값을 정의 민감한 데이터를 관리합니다. 이 파일은 `.gitignore`에 의해 커밋되지 않습니다. 예시는 `./env/.secrets.env.example`를 참고

Docker 컨테이너(`node24bg`)에서 사용하려면 컨테이너에 동일한 환경 변수가 전달되어야 합니다. 필요 시 컨테이너 실행 시 `--env-file` 혹은 `-e`로 값을 주입하세요.

```bash
GITHUB_MCP_KEY="<your-smithery-key>"
GITHUB_MCP_PROFILE="<your-smithery-profile>"
```

### 🔧 구현 과정

#### 1. 의존성 설정
공식 MCP Java SDK를 사용하기 위해 `build.gradle`에 의존성을 추가했습니다:

```gradle
dependencies {
    // 공식 MCP Java SDK
    implementation platform("io.modelcontextprotocol.sdk:mcp-bom:0.11.1")
    implementation 'io.modelcontextprotocol.sdk:mcp'
    
    // JSON 처리
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
    
    // 로깅
    implementation 'org.slf4j:slf4j-api:2.0.17'
    implementation 'ch.qos.logback:logback-classic:1.4.14'

    // Lombok
    compileOnly 'org.projectlombok:lombok:1.18.34'
    annotationProcessor 'org.projectlombok:lombok:1.18.34'
    testCompileOnly 'org.projectlombok:lombok:1.18.34'
    testAnnotationProcessor 'org.projectlombok:lombok:1.18.34'

    // 테스트
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}
```

#### 2. MCP 서버 구현
`PublicDataMCPServer.java`에서 공식 MCP Java SDK API를 사용하여 서버를 구현했습니다:

```java
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
```

#### 3. Hello World 도구 구현
`SyncToolSpecification`을 사용하여 Hello World 도구를 구현했습니다:

```java
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
```

### ✅ 구현 결과

#### 빌드 성공
```
BUILD SUCCESSFUL in 1s
6 actionable tasks: 6 executed
```

#### 서버 실행 로그
```
🚀 Public Data MCP 서버를 시작합니다...
📋 Java 버전: 21.0.7
✅ Public Data MCP 서버가 성공적으로 시작되었습니다!
📝 사용 가능한 도구: hello_world
🔄 STDIO MCP 서버가 실행 중입니다. 종료하려면 Ctrl+C를 누르세요.
```

#### MCP 프로토콜 통신 확인
- **도구 등록**: `hello_world` 도구가 성공적으로 등록됨
- **JSON-RPC 통신**: 표준 MCP 프로토콜을 통한 통신 확인
- **STDIO 전송**: 표준 입출력을 통한 안정적인 통신

## 📡 JSON vs JSON-RPC 응답 차이점

### 일반 JSON 응답
일반적인 REST API나 웹 서비스에서 제공하는 JSON 응답:

```json
{
  "message": "안녕하세요, 사용자! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉",
  "timestamp": "2024-01-15T20:17:32.435",
  "java_version": "21.0.7",
  "status": "success"
}
```

### MCP JSON-RPC 응답
MCP 프로토콜에서 제공하는 표준화된 JSON-RPC 응답:

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

### 주요 차이점

| 구분 | 일반 JSON | MCP JSON-RPC |
|------|-----------|--------------|
| **프로토콜** | REST API | JSON-RPC 2.0 |
| **요청 ID** | 없음 | `id` 필드로 요청-응답 매칭 |
| **메타데이터** | 없음 | `jsonrpc` 버전 정보 |
| **응답 구조** | 자유로운 구조 | `result` 객체로 래핑 |
| **콘텐츠 타입** | 단순 텍스트 | `content` 배열로 구조화 |
| **에러 처리** | HTTP 상태 코드 | `error` 객체로 표준화 |
| **호환성** | API별 상이 | MCP 표준 준수 |

### MCP JSON-RPC의 장점

1. **표준화**: 모든 MCP 서버가 동일한 응답 형식 사용
2. **요청 추적**: `id` 필드로 요청-응답 매칭 가능
3. **에러 처리**: 표준화된 에러 응답 형식
4. **확장성**: `content` 배열로 다양한 타입의 응답 지원
5. **호환성**: MCP 클라이언트와의 완벽한 호환성

### 🛠️ 설치 및 실행

#### 1. Java 21 확인

```bash
java -version
```

#### 2. Gradle 확인

```bash
gradle --version
```

#### 3. 프로젝트 빌드

```bash
cd /Users/ethan/Cursor/mcp-public-data
gradle clean build
```

#### 4. 서버 실행

```bash
gradle bootRun
```

또는 JAR 파일로 실행:

```bash
gradle bootJar
java -jar build/libs/mcp-hello-world-1.0.0.jar
```

#### 5. 실행 확인

서버가 성공적으로 시작되면 다음과 같은 메시지가 출력됩니다:

```
🚀 Public Data MCP 서버를 시작합니다...
📋 Java 버전: 21.0.7
✅ Public Data MCP 서버가 성공적으로 시작되었습니다!
📝 사용 가능한 도구: hello_world
🔄 STDIO MCP 서버가 실행 중입니다. 종료하려면 Ctrl+C를 누르세요.
```

### 6. MCP 프로토콜 테스트

MCP 클라이언트를 사용하여 도구를 호출할 수 있습니다:

```bash
# 도구 목록 조회
echo '{"jsonrpc": "2.0", "id": 1, "method": "tools/list"}' | java -jar build/libs/mcp-public-data-1.0.0.jar

# Hello World 도구 호출
echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "hello_world", "arguments": {"name": "사용자"}}}' | java -jar build/libs/mcp-public-data-1.0.0.jar
```

### 🛠️ 사용 가능한 도구

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

### 🔧 개발

#### 프로젝트 구조

```
mcp-public-data/
+---📦architecture
┃    ┗ 📜mcp_project_structure.md
┃
+---📦diagram
┃    ┣ 📜arch-component-flowchart.mmd
┃    ┗ 📜request-response-sequence.mmd
┃
+---📦env
┃    ┗ 📜.secrets.env.example
┃
+---📦post
┃    ┣ 📜arch-component-flowchart.png
┃    ┣ 📜modelcontextprotocol.svg
┃    ┗ 📜request-response-sequence.png
┃
+---📦required
┃    ┣ 📜mcp_requirements_with_rationale.md
┃    ┗ 📜mcp_server_execution_plan.md
┃
+---📦src
┃    ┗ 📂main
┃    ┃ ┗ 📂java
┃    ┃ ┃ ┗ 📂com
┃    ┃ ┃ ┃ ┗ 📂datapublic
┃    ┃ ┃ ┃ ┃ ┗ 📂mcp
┃    ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
┃    ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MCPServerController.java
┃    ┃ ┃ ┃ ┃ ┃ ┣ 📂service
┃    ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜HelloService.java
┃    ┃ ┃ ┃ ┃ ┃ ┗ 📜PublicDataMCPServer.java
┃   .cursorrules
┃   .gitignore
┃   build.gradle
┃   conversation-management-guide.md
┃   cursor-context.md
┃   gradle.properties
┃   gradlew
┃   gradlew.bat
┃   mcp-public-data.iml
┃   mcp.json
┃   README.md
┃   run-build-gradle.sh
┃   run-github-mcp.sh
\   settings.gradle
```

### 🧰 새로운 도구 추가

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
                // 도구 로직 구현
                return new CallToolResult("도구 실행 결과", true);
            }
    );
}
```

### 📚 참고 자료

- [공식 문서 MCP Java SDK](https://modelcontextprotocol.io/sdk/java/mcp-overview)
- [공식 MCP GitHub Java SDK](https://github.com/modelcontextprotocol/java-sdk)
- [Model Context Protocol](https://modelcontextprotocol.io/)
- [Gradle 공식 문서](https://gradle.org/docs/)

### 🤝 기여

이슈나 풀 리퀘스트를 통해 기여해주세요!
