#!/bin/bash

echo "🚀 Public Data MCP 서버를 시작합니다..."
echo "📋 워크스페이스: /Users/ethan/Cursor/mcp-public-data"

# Java 버전 확인
echo "📋 Java 버전 확인 중..."
java -version

# Gradle 버전 확인
echo "📋 Gradle 버전 확인 중..."
./gradlew --version

# 프로젝트 빌드
echo "🔨 프로젝트 빌드 중..."
./gradlew clean build

if [ $? -eq 0 ]; then
    echo "✅ 빌드 성공!"
    echo "🚀 서버 시작 중..."
    ./gradlew bootRun
else
    echo "❌ 빌드 실패!"
    exit 1
fi
