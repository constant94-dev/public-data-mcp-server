#!/bin/bash

# Public Data MCP Server 통합 실행 스크립트
# 이 스크립트는 MCP 서버를 빌드하고 실행합니다.

set -e  # 오류 발생 시 스크립트 중단

# 현재 디렉토리로 이동
cd "$(dirname "$0")"

# 로그 디렉토리 생성
mkdir -p logs

# 환경변수 파일 로드 (존재하는 경우)
if [ -f "/Users/ethan/Cursor/.env/.secrets.env" ]; then
    source "/Users/ethan/Cursor/.env/.secrets.env"
    echo "✅ 환경변수 파일 로드 완료"
else
    echo "⚠️ 환경변수 파일을 찾을 수 없어 기본값 사용"
fi

# 기본값 설정
export SPRING_BOOT_URL=${SPRING_BOOT_URL:-"http://localhost:8080"}
export SPRING_BOOT_TIMEOUT=${SPRING_BOOT_TIMEOUT:-10}

echo "🚀 Public Data MCP 서버를 시작합니다..."
echo "📋 워크스페이스: $(pwd)"
echo "📋 Spring Boot URL: $SPRING_BOOT_URL"

# Java 버전 확인
echo "📋 Java 버전 확인 중..."
java -version

# Gradle 버전 확인
echo "📋 Gradle 버전 확인 중..."
./gradlew --version

# JAR 파일이 있는지 확인하고 없으면 빌드
if [ ! -f "build/libs/public-data-mcp-server-1.0.0.jar" ]; then
    echo "🔨 JAR 파일이 없습니다. 빌드를 시작합니다..."
    ./gradlew clean build -q
fi

# 실행 모드 선택
if [ "$1" = "test" ]; then
    echo "🧪 테스트 모드로 실행합니다..."
    echo "📋 사용 가능한 도구:"
    echo "   - hello_world"
    echo "   - spring_boot_health_check"
    echo "   - get_user_info"
    echo "   - get_session_info"
    echo "   - get_public_data_info"
    echo "   - create_log"
    echo ""
    echo "🔄 MCP 서버가 실행 중입니다. 종료하려면 Ctrl+C를 누르세요."
    echo ""
    
    # 테스트 모드: 로그를 파일에 저장
    ./gradlew run 2>&1 | tee logs/mcp-server-$(date +%Y%m%d_%H%M%S).log
else
    echo "🚀 MCP 서버를 포그라운드에서 시작합니다..."
    echo "📋 이 서버는 Cursor에서 MCP 서버로 직접 실행됩니다."
    
    # 프로덕션 모드: 로그를 파일에 저장하고 백그라운드에서 실행
    nohup ./gradlew run > logs/mcp-server-$(date +%Y%m%d_%H%M%S).log 2>&1 &
    echo "✅ MCP 서버가 백그라운드에서 시작되었습니다."
    echo "📋 로그 파일: logs/mcp-server-*.log"
    echo "🔄 서버를 종료하려면: pkill -f 'public-data-mcp-server'"
fi
