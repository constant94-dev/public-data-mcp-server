# MCP 서버 프로젝트 초기 구조
```
mcp-server/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com.example.mcpserver/
│  │  │     ├─ controller/         # API 요청 처리 컨트롤러
│  │  │     │    └─ ApiController.java
│  │  │     ├─ service/            # 비즈니스 로직, MCP 핵심 처리
│  │  │     │    ├─ McpService.java
│  │  │     │    ├─ LlmService.java
│  │  │     │    └─ PublicApiClient.java
│  │  │     ├─ dto/                # 요청 및 응답 데이터 전송 객체
│  │  │     │    └─ SessionDto.java
│  │  │     ├─ config/             # 설정 관련 (Redis, MCP SDK 등)
│  │  │     │    └─ RedisConfig.java
│  │  │     ├─ exception/          # 커스텀 예외 처리
│  │  │     │    └─ GlobalExceptionHandler.java
│  │  │     └─ util/               # 공통 유틸리티 클래스
│  │  │          └─ JsonUtil.java
│  │  └─ resources/
│  │       ├─ application.yml      # 환경설정 파일
│  │       └─ ...                  # 기타 리소스
├─ build.gradle or pom.xml          # 빌드 스크립트
├─ Dockerfile                      # 도커 이미지 빌드 설정
└─ README.md                      # 프로젝트 설명 문서
```

## 설계 포인트  
- **controller**: HTTP 요청/응답 책임 (API 명세와 직접 연동)  
- **service**: MCP 프로토콜 처리, LLM 연동, 공공 API 호출 등 핵심 비즈니스 로직 집중  
- **dto**: 클라이언트와 서버 간 명확한 데이터 계약 유지  
- **config**: Redis, MCP SDK, DB, 외부 API 등 환경설정 분리  
- **exception**: 전역 예외 처리 및 사용자 친화적 에러 응답  
- **util**: JSON 변환, 공통 로직 재사용  

## 추가 권장사항  
- **패키지 단위 인터페이스 정의**: `service` 내에서 인터페이스와 구현 분리로 유연성 확보  
- **테스트 코드 별도 패키지 구성**: `src/test/java` 아래 동일 패키지 구조로 유닛/통합 테스트 작성  
- **CI/CD 연동**: GitHub Actions, Jenkins, Azure Pipelines 등과 연계한 자동 빌드 및 배포  
