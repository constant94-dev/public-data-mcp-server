# JSON vs JSON-RPC 비교 분석

## 📡 프로토콜 개요

### 일반 JSON 응답
일반적인 REST API나 웹 서비스에서 제공하는 JSON 응답 형식

### MCP JSON-RPC 응답
MCP (Model Context Protocol)에서 사용하는 표준화된 JSON-RPC 2.0 응답 형식

## 🔍 상세 비교

### 일반 JSON 응답 예시
```json
{
  "message": "안녕하세요, 사용자! Java 21과 Gradle로 만든 Public Data MCP 서버입니다! 🎉",
  "timestamp": "2024-01-15T20:17:32.435",
  "java_version": "21.0.7",
  "status": "success"
}
```

### MCP JSON-RPC 응답 예시
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

## 📊 구조적 차이점

| 구분 | 일반 JSON | MCP JSON-RPC |
|------|-----------|--------------|
| **프로토콜** | REST API | JSON-RPC 2.0 |
| **요청 ID** | 없음 | `id` 필드로 요청-응답 매칭 |
| **메타데이터** | 없음 | `jsonrpc` 버전 정보 |
| **응답 구조** | 자유로운 구조 | `result` 객체로 래핑 |
| **콘텐츠 타입** | 단순 텍스트 | `content` 배열로 구조화 |
| **에러 처리** | HTTP 상태 코드 | `error` 객체로 표준화 |
| **호환성** | API별 상이 | MCP 표준 준수 |

## 🎯 MCP JSON-RPC의 장점

### 1. 표준화
- 모든 MCP 서버가 동일한 응답 형식 사용
- 클라이언트 개발 시 일관된 처리 로직 적용 가능

### 2. 요청 추적
- `id` 필드로 요청-응답 매칭 가능
- 비동기 처리 시 요청 식별 용이

### 3. 에러 처리
- 표준화된 에러 응답 형식
- 구조화된 에러 정보 제공

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "error": {
    "code": -32601,
    "message": "Method not found",
    "data": {
      "method": "invalid_method"
    }
  }
}
```

### 4. 확장성
- `content` 배열로 다양한 타입의 응답 지원
- 텍스트, 이미지, 데이터 등 다양한 콘텐츠 타입 가능

### 5. 호환성
- MCP 클라이언트와의 완벽한 호환성
- 프로토콜 버전 관리 용이

## 🔧 구현 예시

### 일반 JSON 응답 구현 (Spring Boot)
```java
@RestController
public class ApiController {
    
    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "안녕하세요, " + name + "!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "success");
        return response;
    }
}
```

### MCP JSON-RPC 응답 구현
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

## 📈 성능 비교

### 메모리 사용량
- **일반 JSON**: 더 적은 메모리 사용 (메타데이터 없음)
- **JSON-RPC**: 약간 더 많은 메모리 (프로토콜 오버헤드)

### 네트워크 오버헤드
- **일반 JSON**: 최소한의 오버헤드
- **JSON-RPC**: 약 10-15% 추가 오버헤드

### 처리 속도
- **일반 JSON**: 빠른 파싱 (단순 구조)
- **JSON-RPC**: 약간 느린 파싱 (복잡한 구조)

## 🛠️ 사용 시나리오

### 일반 JSON 사용 권장
- **단순한 REST API**
- **웹 애플리케이션**
- **모바일 앱 API**
- **마이크로서비스 간 통신**

### JSON-RPC 사용 권장
- **MCP 프로토콜**
- **RPC 스타일 통신**
- **복잡한 요청-응답 패턴**
- **표준화된 프로토콜 필요**

## 🔍 디버깅 및 테스트

### 일반 JSON 테스트
```bash
# curl을 사용한 테스트
curl -X GET "http://localhost:8080/hello?name=사용자" \
  -H "Content-Type: application/json"
```

### JSON-RPC 테스트
```bash
# MCP 프로토콜 테스트
echo '{"jsonrpc": "2.0", "id": 2, "method": "tools/call", "params": {"name": "hello_world", "arguments": {"name": "사용자"}}}' | java -jar build/libs/mcp-public-data-1.0.0.jar
```

## 📚 참고 자료

- [JSON-RPC 2.0 스펙](https://www.jsonrpc.org/specification)
- [Model Context Protocol](https://modelcontextprotocol.io/)
- [MCP Java SDK](https://modelcontextprotocol.io/sdk/java/mcp-overview)

---

**마지막 업데이트**: 2025-08-17  
**작성자**: Ethan  
**상태**: JSON vs JSON-RPC 비교 분석 완성 ✅
