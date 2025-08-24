# Public Data MCP Server

MCP (Model Context Protocol) 서버로, AI 도구와 Spring Boot 백엔드를 연결하는 중간 계층 역할을 합니다.

## 🚀 빠른 시작

### 실행 방법

```bash
# 기본 실행 (프로덕션 모드)
./run-mcp-server.sh

# 테스트 모드 실행
./run-mcp-server.sh test
```

### 사용 가능한 도구

- `hello_world`: 사용자 인사 메시지 반환
- `spring_boot_health_check`: Spring Boot 서버 헬스 체크
- `get_user_info`: 사용자 정보 조회
- `get_session_info`: 세션 정보 조회
- `get_public_data_info`: 공공데이터 정보 조회
- `create_log`: 로그 기록 생성

## 📁 프로젝트 구조

```
public-data-mcp-server/
├── src/main/java/com/datapublic/mcp/
│   ├── PublicDataMCPServer.java     # 메인 서버 클래스
│   ├── client/
│   │   └── SpringBootApiClient.java # Spring Boot API 클라이언트
│   ├── controller/
│   │   └── MCPServerController.java # MCP 컨트롤러
│   └── service/
│       ├── HelloService.java        # Hello World 서비스
│       └── SpringBootIntegrationService.java # Spring Boot 연동 서비스
├── logs/                           # 로그 파일
├── run-mcp-server.sh               # 통합 실행 스크립트
├── build.gradle                    # 빌드 설정
└── README.md                       # 이 파일
```

## 🔧 환경 설정

### 환경변수

워크스페이스 루트의 `.env/.secrets.env` 파일에서 환경변수를 설정할 수 있습니다:

```bash
# Spring Boot 백엔드 URL
SPRING_BOOT_URL=http://localhost:8080

# 타임아웃 설정 (초)
SPRING_BOOT_TIMEOUT=10
```

### 로그 관리

- 로그 파일은 `logs/` 폴더에 저장됩니다
- 파일명 형식: `mcp-server-YYYYMMDD_HHMMSS.log`
- PID 파일은 생성되지 않습니다

## 🛠️ 개발

### 빌드

```bash
./gradlew clean build
```

### 테스트

```bash
./gradlew test
```

### 실행

```bash
# 개발 모드
./gradlew run

# 프로덕션 모드
./run-mcp-server.sh
```

## 🔗 관련 프로젝트

- **[spring-boot-mcp-integration](https://github.com/constant94-dev/spring-boot-mcp-integration)**: Spring Boot 백엔드 (공공데이터 API 통합 완료)
- **[vue-mcp-integration](https://github.com/constant94-dev/vue-mcp-integration)**: Vue.js 프론트엔드 클라이언트

## 📝 업데이트 히스토리

### 2025-08-24
- ✅ **스크립트 통합**: 3개의 스크립트를 1개로 통합
- ✅ **PID 파일 제거**: PID 파일 생성 로직 제거
- ✅ **로그 관리 개선**: logs 폴더에 로그 파일 저장
- ✅ **프로젝트명 통일**: JAR 파일명을 프로젝트명에 맞게 수정

### 2025-08-17
- ✅ **MCP Java SDK 기반 서버 구현**: PublicDataMCPServer.java
- ✅ **Hello World 도구 구현**: 사용자 인사 메시지 반환
- ✅ **STDIO 전송 프로토콜**: 표준 입출력 통신 구현
- ✅ **JSON-RPC 2.0 프로토콜**: MCP 표준 프로토콜 지원
- ✅ **Docker 컨테이너 지원**: 컨테이너 기반 실행 환경
- ✅ **Spring Boot API 클라이언트 구현**: WebClient 기반 REST API 클라이언트
- ✅ **Spring Boot 통합 MCP 도구 구현**: 6개 도구 완성
- ✅ **환경변수 설정 확장**: 워크스페이스 루트 `.env` 디렉토리 활용
- ✅ **에러 처리 및 로깅**: 상세한 에러 처리 및 로깅 시스템

---

**마지막 업데이트**: 2025-08-24  
**작성자**: Ethan  
**상태**: 스크립트 통합 및 로그 관리 개선 완료 ✅
