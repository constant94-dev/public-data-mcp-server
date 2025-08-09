## 공공데이터포털 API를 사용해 MCP Server를 만들고 있습니다.

### 📠 기술스택

![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%6DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

### [요구사항 정의 및 PRD(Product Requirements Document) 작성]
- ChatGPT

### [AI Client]
- Cursor

### [View]
- Vue.js
- Typescript

### [Server]
- Java
- Spring Boot
- Gradle
- Docker

### [MCP Tool]
- github
- server-sequential-thinking
- browser-tools-mcp

### [Third Party]
- 공공데이터포털

### [전체 Sequence]

![이미지설명](https://github.com/constant94-dev/mcp-public-data/blob/main/post/sequence-diagram.png)

### 환경 변수 설정 (Secrets 분리)
`env/.env-local` 파일에 아래 값을 정의해 민감정보를 관리합니다. 이 파일은 `.gitignore`에 의해 커밋되지 않습니다. 예시는 `env/.env-local.example`를 참고하세요.

```bash
GITHUB_MCP_KEY="<your-smithery-key>"
GITHUB_MCP_PROFILE="<your-smithery-profile>"
```

Docker 컨테이너(`node24bg`)에서 사용하려면 컨테이너에 동일한 환경 변수가 전달되어야 합니다. 필요 시 컨테이너 실행 시 `--env-file` 혹은 `-e`로 값을 주입하세요.
