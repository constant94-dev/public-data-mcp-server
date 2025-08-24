# 환경변수 설정 가이드

## 🔐 민감한 데이터 관리

### 환경변수 파일 구조
```
워크스페이스 루트의 .env 디렉토리 사용
/Users/ethan/Cursor/.env/
├── .secrets.env.example  # 플레이스홀더 + 상세 주석 (Git에 포함됨)
└── .secrets.env          # 실제 환경변수 (Git에서 제외됨)
```

### 필수 환경변수

#### GitHub MCP 설정
```bash
# Smithery에서 발급받은 GitHub MCP API 키
GITHUB_MCP_KEY="<your-smithery-key>"

# Smithery에서 설정한 GitHub MCP 프로필 이름
GITHUB_MCP_PROFILE="<your-smithery-profile>"

# GitHub Personal Access Token (GitHub API 접근용)
GITHUB_PERSONAL_ACCESS_TOKEN="<your-github-token>"
```

#### Spring Boot 백엔드 통신 설정
```bash
# Spring Boot 백엔드 서버 URL (기본값: http://localhost:8080)
SPRING_BOOT_URL="http://localhost:8080"

# Spring Boot API 호출 타임아웃 시간 (초, 기본값: 10)
SPRING_BOOT_TIMEOUT="10"
```

### 환경변수 파일 예시

#### `.secrets.env.example` (플레이스홀더 + 주석)
```bash
# GitHub MCP 서버 설정
# Smithery에서 발급받은 GitHub MCP API 키
GITHUB_MCP_KEY=<GITHUB_MCP_KEY>

# Smithery에서 설정한 GitHub MCP 프로필 이름
GITHUB_MCP_PROFILE=<GITHUB_MCP_PROFILE>

# GitHub Personal Access Token (GitHub API 접근용)
GITHUB_PERSONAL_ACCESS_TOKEN=<GITHUB_PERSONAL_ACCESS_TOKEN>

# Spring Boot 백엔드 통신 설정
# Spring Boot 백엔드 서버 URL (기본값: http://localhost:8080)
SPRING_BOOT_URL=<SPRING_BOOT_URL>

# Spring Boot API 호출 타임아웃 시간 (초, 기본값: 10)
SPRING_BOOT_TIMEOUT=<SPRING_BOOT_TIMEOUT>
```

#### `.secrets.env` (실제 값만)
```bash
GITHUB_MCP_KEY=your-smithery-key-here
GITHUB_MCP_PROFILE=your-smithery-profile-here
GITHUB_PERSONAL_ACCESS_TOKEN=your-github-token-here
SPRING_BOOT_URL=http://localhost:8080
SPRING_BOOT_TIMEOUT=10
```

### 환경변수 설정 방법

#### 1. 로컬 개발 환경
```bash
# 워크스페이스 루트의 .env 디렉토리 사용
# 방법 1: 환경변수 파일 로드
source /Users/ethan/Cursor/.env/.secrets.env

# 방법 2: 시스템 환경변수로 설정
export SPRING_BOOT_URL="http://localhost:8080"
export GITHUB_MCP_KEY="your-smithery-key"
export GITHUB_MCP_PROFILE="your-smithery-profile"
```

#### 2. Docker 컨테이너 환경
```bash
# 개별 환경변수로 실행
docker run -e SPRING_BOOT_URL="http://localhost:8080" -e GITHUB_MCP_KEY="your-key" -e GITHUB_MCP_PROFILE="your-profile" your-image

# 또는 환경변수 파일로 실행 (워크스페이스 루트의 .env 파일 사용)
docker run --env-file /Users/ethan/Cursor/.env/.secrets.env your-image
```

### 보안 주의사항

#### Git에서 제외된 파일
- `/Users/ethan/Cursor/.env/.secrets.env`: 실제 API 키가 포함된 파일
- `*.key`: 개인 키 파일
- `*.pem`: 인증서 파일

#### Git에 포함된 파일
- `/Users/ethan/Cursor/.env/.secrets.env.example`: 플레이스홀더와 상세 주석이 포함된 예시 파일

#### 안전한 공유 방법
1. **예시 파일 사용**: `.secrets.env.example`에 플레이스홀더와 상세 주석 포함
2. **문서화**: 팀원에게 필요한 환경변수 목록 및 용도 설명 제공
3. **비밀 관리**: GitHub Secrets, AWS Secrets Manager 등 사용

### 환경변수 검증

#### 스크립트로 환경변수 확인
```bash
#!/bin/bash
# check-env.sh

required_vars=("GITHUB_MCP_KEY" "GITHUB_MCP_PROFILE" "SPRING_BOOT_URL")

for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        echo "❌ 필수 환경변수가 설정되지 않았습니다: $var"
        exit 1
    else
        echo "✅ $var 설정됨"
    fi
done

echo "🎉 모든 환경변수가 올바르게 설정되었습니다!"
```

#### 실행 방법
```bash
# 환경변수 로드 후 검증
source /Users/ethan/Cursor/.env/.secrets.env && ./check-env.sh

# 또는 시스템 환경변수로 설정 후 검증
export SPRING_BOOT_URL="http://localhost:8080" && ./check-env.sh
```

### 개발/운영 환경 분리

#### 개발 환경 (.env.development)
```bash
GITHUB_MCP_KEY="dev-key"
GITHUB_MCP_PROFILE="dev-profile"
LOG_LEVEL="DEBUG"
```

#### 운영 환경 (.env.production)
```bash
GITHUB_MCP_KEY="prod-key"
GITHUB_MCP_PROFILE="prod-profile"
LOG_LEVEL="INFO"
```

### 문제 해결

#### 일반적인 문제들
1. **환경변수 인식 안됨**: 파일 경로 확인, 소스 명령어 확인
2. **권한 문제**: 파일 권한 설정 (600 권장)
3. **Docker 환경변수 전달 실패**: `--env-file` 또는 `-e` 옵션 확인

#### 디버깅 명령어
```bash
# 환경변수 확인
env | grep GITHUB

# Docker 컨테이너 내부 확인
docker exec -it container-name env | grep GITHUB
```

---

**마지막 업데이트**: 2025-08-17  
**작성자**: Ethan  
**상태**: 환경변수 설정 가이드 완성 ✅
