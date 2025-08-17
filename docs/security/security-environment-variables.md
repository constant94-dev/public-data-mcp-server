# 환경변수 설정 가이드

## 🔐 민감한 데이터 관리

### 환경변수 파일 구조
```
env/
├── .secrets.env          # 실제 환경변수 (Git에서 제외됨)
└── .secrets.env.example  # 환경변수 예시 파일
```

### 필수 환경변수

#### GitHub MCP 설정
```bash
# GitHub MCP API 키
GITHUB_MCP_KEY="<your-smithery-key>"

# GitHub MCP 프로필
GITHUB_MCP_PROFILE="<your-smithery-profile>"
```

### 환경변수 설정 방법

#### 1. 로컬 개발 환경
```bash
# .secrets.env 파일 생성
cp env/.secrets.env.example env/.secrets.env

# 환경변수 편집
nano env/.secrets.env
```

#### 2. Docker 컨테이너 환경
```bash
# 환경변수 파일로 실행
docker run --env-file env/.secrets.env your-image

# 개별 환경변수로 실행
docker run -e GITHUB_MCP_KEY="your-key" -e GITHUB_MCP_PROFILE="your-profile" your-image
```

### 보안 주의사항

#### Git에서 제외된 파일
- `.secrets.env`: 실제 API 키가 포함된 파일
- `*.key`: 개인 키 파일
- `*.pem`: 인증서 파일

#### 안전한 공유 방법
1. **예시 파일 사용**: `.secrets.env.example`에 더미 값 포함
2. **문서화**: 팀원에게 필요한 환경변수 목록 제공
3. **비밀 관리**: GitHub Secrets, AWS Secrets Manager 등 사용

### 환경변수 검증

#### 스크립트로 환경변수 확인
```bash
#!/bin/bash
# check-env.sh

required_vars=("GITHUB_MCP_KEY" "GITHUB_MCP_PROFILE")

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
source env/.secrets.env && ./check-env.sh
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
