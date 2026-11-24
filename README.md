# hopes AOS

광주소프트웨어마이스터고 학생을 위한 AI 상담 서비스 Android 앱입니다. Compose 기반 최신 UI에서 계정, 상담 채팅, 커뮤니티, 학과·전공 추천, 교내 위치 안내 기능을 제공합니다.

## 기능

- 이메일 인증, 회원가입, 로그인, 비밀번호 재설정
- AI 상담 채팅과 대화 기록
- 프로필·개인 설정·문의·회원 탈퇴
- 익명 커뮤니티 글·댓글·좋아요·신고·내 활동
- 학과·전공 선택과 추천 질문
- 채팅 첨부 파일 및 교내 위치 안내

각 기능의 서버 연결 현황은 [UI/API 연결 목록](docs/ui-api-integration.md)을 참고하세요.

## 기술 구성

| 구분 | 기술 |
|---|---|
| 언어·UI | Kotlin 2.2.10, Jetpack Compose, Material 3 |
| Android | AGP 9.2.1, compileSdk 37, minSdk 34, targetSdk 36 |
| Navigation | Navigation 3 |
| 비동기 처리 | Kotlin Coroutines, Flow |
| DI | Hilt 2.60.1, KSP |
| 네트워크 | Retrofit 2.11.0, OkHttp 4.12.0, kotlinx.serialization 1.8.0 |
| 로컬 저장소 | DataStore Preferences |
| 빌드 | Gradle Wrapper, Kotlin DSL, Version Catalog |

## 아키텍처

앱은 `app` 단일 모듈이며, UI는 Compose와 Route/Screen/ViewModel 구조를 사용합니다. 서버 기능은 ApiService를 RemoteDataSource가 감싸고, Repository와 UseCase를 거쳐 ViewModel의 UI 상태로 전달합니다.

```text
Compose Screen → Route → ViewModel → UseCase
                                      ↓
Compose UI ← UiState ← Repository ← RemoteDataSource ← ApiService
```

주요 코드 위치:

```text
app/src/main/java/com/example/hopes/
├── core/          # 네트워크, 세션, 디자인 시스템, 연결 상태
├── data/          # API, RemoteDataSource, Repository 구현, Mapper
├── di/            # Hilt 및 Retrofit 설정
├── domain/        # 모델, Repository 계약, UseCase
├── feature/       # auth, chat, community, home, settings 등 화면 기능
└── navigation/    # 앱 목적지와 Navigation 3 구성
```

## 빌드 및 실행

### 요구사항

- Android Studio와 Android SDK 37
- JDK 21 (Gradle daemon toolchain 기준)
- Android 14(API 34) 이상 기기 또는 emulator

### 실행

```bash
./gradlew assembleDebug
```

Android Studio에서 저장소를 열고 `app` 구성을 실행해도 됩니다. debug와 release의 API 서버 주소는 `app/build.gradle.kts`의 `BASE_URL` 설정을 따릅니다. 비밀 값은 저장소에 기록하지 않습니다.

## 테스트와 검증

| 목적 | 명령 |
|---|---|
| Kotlin 컴파일 | `./gradlew :app:compileDebugKotlin` |
| 단위 테스트 | `./gradlew :app:testDebugUnitTest` |
| debug APK 빌드 | `./gradlew :app:assembleDebug` |
| Android 계측 테스트 | `./gradlew :app:connectedDebugAndroidTest` |

계측 테스트는 Android 14(API 34) 이상 emulator/device가 연결되어 있어야 합니다.

## 문서

- [UI/API 연결 목록](docs/ui-api-integration.md)
- PRD: 저장소에서 확인되지 않았습니다.
- 제품 의도 브리프: 아직 작성되지 않았습니다.

## 개발 흐름

저장소의 `.agents/skills/android-github-workflow/SKILL.md`와 팀의 `AGENTS.md` 작업 규칙을 확인한 뒤 진행합니다. 현재 저장소 루트에는 `AGENTS.md`가 없으며, 앱 작업 지침은 `.agents/skills/` 아래에 있습니다.
