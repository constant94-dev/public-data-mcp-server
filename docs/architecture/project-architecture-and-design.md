# Public Data MCP Server - 프로젝트 구조

## 🏗️ 현재 프로젝트 구조

```
public-data-mcp-server/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── datapublic/
│                   └── mcp/
│                       ├── PublicDataMCPServer.java    # 메인 서버 클래스
│                       ├── controller/
│                       │   └── MCPServerController.java # MCP 컨트롤러
│                       └── service/
│                           └── HelloService.java       # Hello World 서비스
├── docs/                                           # 기술 문서
│   ├── architecture/                              # 아키텍처 문서
│   ├── diagram/                                   # 다이어그램 파일
│   ├── post/                                      # 다이어그램 이미지
│   ├── required/                                  # 요구사항 문서
│   ├── environment-setup.md                       # 환경변수 설정
│   └── json-vs-jsonrpc.md                         # 프로토콜 비교
├── env/                                           # 환경변수 파일
│   ├── .secrets.env.example                       # 환경변수 예시
│   └── .secrets.env                               # 실제 환경변수 (Git 제외)
├── build.gradle                                   # Gradle 빌드 설정
├── gradlew                                        # Gradle Wrapper
├── gradlew.bat                                    # Gradle Wrapper (Windows)
├── settings.gradle                                # Gradle 프로젝트 설정
├── gradle.properties                              # Gradle 속성
├── run-build-gradle.sh                           # 빌드 실행 스크립트
├── run-github-mcp.sh                             # GitHub MCP 실행 스크립트
└── README.md                                     # 프로젝트 설명
```

## 📦 패키지 구조 설명

### `com.datapublic.mcp`
- **PublicDataMCPServer.java**: MCP 서버의 메인 클래스
  - STDIO 전송 프로바이더 설정
  - MCP 서버 초기화 및 도구 등록
  - 서버 실행 및 종료 처리

### `com.datapublic.mcp.controller`
- **MCPServerController.java**: MCP 프로토콜 컨트롤러
  - 도구 등록 및 관리
  - 요청 라우팅 처리
  - 응답 포맷팅

### `com.datapublic.mcp.service`
- **HelloService.java**: Hello World 도구 구현
  - 도구 로직 처리
  - 응답 생성
  - 에러 처리

## 🔧 설계 원칙

### 1. 단일 책임 원칙 (SRP)
- 각 클래스는 하나의 명확한 책임만 가짐
- **PublicDataMCPServer**: 서버 초기화 및 실행
- **MCPServerController**: MCP 프로토콜 처리
- **HelloService**: 도구별 비즈니스 로직

### 2. 의존성 분리
- **MCP SDK**: 프로토콜 처리
- **Service Layer**: 비즈니스 로직
- **Controller Layer**: 요청/응답 처리

### 3. 확장성 고려
- 새로운 도구 추가 시 Service 패키지에 클래스 추가
- 공통 기능은 별도 유틸리티 클래스로 분리
- 설정은 별도 Config 클래스로 관리

## 🚀 향후 확장 계획

### Phase 1: 공공데이터 API 연동
```
com.datapublic.mcp/
├── PublicDataMCPServer.java
├── controller/
│   └── MCPServerController.java
├── service/
│   ├── HelloService.java
│   ├── PublicDataService.java          # 공공데이터 API 서비스
│   └── WeatherService.java             # 날씨 정보 서비스
├── client/                             # 외부 API 클라이언트
│   ├── PublicDataApiClient.java        # 공공데이터 API 클라이언트
│   └── WeatherApiClient.java           # 날씨 API 클라이언트
├── dto/                                # 데이터 전송 객체
│   ├── PublicDataRequest.java
│   └── PublicDataResponse.java
├── config/                             # 설정 클래스
│   ├── ApiConfig.java                  # API 설정
│   └── CacheConfig.java                # 캐시 설정
└── util/                               # 유틸리티
    ├── JsonUtil.java                   # JSON 처리
    └── ValidationUtil.java             # 검증 유틸리티
```

### Phase 2: 고급 기능 추가
```
com.datapublic.mcp/
├── cache/                              # 캐싱 시스템
│   ├── RedisCacheService.java
│   └── CacheManager.java
├── exception/                          # 예외 처리
│   ├── ApiException.java
│   └── GlobalExceptionHandler.java
├── logging/                            # 로깅 시스템
│   └── LoggingService.java
└── monitoring/                         # 모니터링
    └── MetricsService.java
```

## 🔄 개발 워크플로우

### 1. 새로운 도구 추가
1. **Service 클래스 생성**: `service/NewToolService.java`
2. **도구 스펙 정의**: JSON 스키마 작성
3. **컨트롤러에 등록**: `MCPServerController`에 도구 추가
4. **테스트 작성**: 단위 테스트 및 통합 테스트

### 2. 외부 API 연동
1. **Client 클래스 생성**: `client/NewApiClient.java`
2. **DTO 클래스 생성**: `dto/NewApiRequest.java`, `dto/NewApiResponse.java`
3. **Service 클래스 수정**: 새로운 API 클라이언트 사용
4. **설정 추가**: `config/ApiConfig.java`에 설정 추가

### 3. 캐싱 시스템 추가
1. **Cache Service 생성**: `cache/NewCacheService.java`
2. **설정 추가**: `config/CacheConfig.java`
3. **Service 클래스 수정**: 캐싱 로직 추가
4. **성능 테스트**: 캐싱 효과 검증

## 📊 성능 고려사항

### 메모리 사용량
- **현재**: ~50MB (기본 MCP 서버)
- **목표**: <512MB (API 연동 후)

### 응답 시간
- **현재**: <100ms (Hello World 도구)
- **목표**: <1초 (공공데이터 API 호출 포함)

### 동시 요청 처리
- **현재**: 단일 요청 처리
- **목표**: 10개 동시 요청 처리

---

**마지막 업데이트**: 2025-08-17  
**작성자**: Ethan  
**상태**: 아키텍처 문서 최신화 완료 ✅
