#!/bin/bash

# GitHub MCP 서버 실행 스크립트
# 작업 디렉토리로 이동
cd /Users/ethan/Cursor/mcp-public-data

# 환경 변수 파일 로드
set -a
source ./env/.secrets.env
set +a

# Docker 컨테이너에서 GitHub MCP 서버 실행
docker exec -i \
  -e GITHUB_MCP_KEY \
  -e GITHUB_MCP_PROFILE \
  node24bg \
  npx -y @smithery/cli@latest \
  run @smithery-ai/github \
  --key "$GITHUB_MCP_KEY" \
  --profile "$GITHUB_MCP_PROFILE"
