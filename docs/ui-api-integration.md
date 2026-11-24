# 사용자 UI · 서버 API 연결 현황 (#256)

검토 기준: Swagger OAS 3.1 `https://hopes-v1.https.gsmsv.site/v3/api-docs` (2026-11-24 확인). Figma 링크는 제공받았으나 연결된 계정에서 읽기 권한만 확인되어 화면 상세는 조회하지 못했습니다. 현재 UI와 Swagger 계약으로 구현했습니다. 알림 UI 전체 제거와 HTTPS 서버 URL 사용은 사용자 승인 범위입니다.

## 연결 및 수정

- debug/release API Base URL을 `https://hopes-v1.https.gsmsv.site/`로 변경했습니다.
- 비밀번호 재설정 요청은 `newPassword`, 문의 요청은 `content`와 `replyEmail` 필드를 사용합니다.
- 대화 기록 삭제는 `DELETE /api/chats/{id}`의 204 처리 경로를 사용합니다. 실패 시 기록은 화면에서 제거하지 않습니다.
- 채팅 메시지 DTO에 `attachmentIds`, 학과·전공·위치 관련 필드와 `X-Hopes-Client: APP` 헤더를 반영했습니다. 각 전송의 `clientRequestId`는 상세 ViewModel에서 생성하고 재시도 시 같은 ID를 use case/repository까지 전달합니다. OAS에는 이 필드만 정의되어 있고 서버의 중복 제거 동작은 명시되지 않았습니다.
- 첨부 `POST /api/attachments` multipart 업로드와 `DELETE /api/attachments/{attachmentId}`를 remote data source·repository·use case에 연결했습니다. 크기를 알 수 없는 URI도 전송 전 스트림 복사 단계에서 20 MiB 상한을 적용합니다. 이미지 업로드 사본에서 GPS IFD 표준 필드 전체를 Android framework `ExifInterface`로 제거하며 원본은 수정하지 않습니다. 진행 중 제거는 UI에서 즉시 제외하고 업로드 응답 ID를 받은 뒤 서버 삭제를 요청합니다. 삭제가 실패하면 실패한 ID를 화면 상태에 보존하고 재시도 버튼을 제공합니다. 실패한 첨부는 제외하고 전송할 수 있습니다.
- 커뮤니티 API를 remote data source → DTO mapper → `CommunityRepository` → `CommunityStore` facade로 연결했습니다. 게시판 목록, 게시글 목록/검색/paging/상세/등록/수정/삭제, 댓글 전체 페이지 조회/등록/수정/삭제, 좋아요/취소(실패 시 feed와 my-posts cache를 모두 롤백), 신고, 내 글/댓글 전체 페이지 조회를 호출합니다. 내 글 캐시는 global feed와 분리했습니다. 다음 페이지와 검색은 로딩·실패·재시도 상태를 표현합니다. 검색 API 실패는 기존 캐시나 빈 결과로 표시하지 않습니다. 댓글 등록과 게시글 등록은 POST 응답을 댓글·내 댓글 또는 feed·내 글 캐시에 즉시 반영하므로 목록 GET 실패가 등록 실패로 바뀌지 않습니다. 내 활동의 댓글 create/update/delete도 my-comments cache에 반영합니다. 전체 페이지 요청은 서버 page가 요청 cursor와 다르거나 100페이지를 넘으면 중단합니다. OAS int64 count는 DTO와 community model에서 `Long`으로 유지합니다. 최근 검색어만 로컬 상태입니다.
- 채팅 시작 UI의 학과·전공 선택은 `ChatAssistRemoteDataSource` → `ChatAssistRepository` → UseCase를 통해 서버 카테고리에서 현재 UI가 지원하는 활성 항목을 찾아 ID와 함께 유지합니다. 질문 추천은 같은 경계를 거쳐 `departmentId`, `specialtyId`, `limit`, `refreshToken`, `excludeIds`, `personalized`, `specialtyIds`를 보내고 서버 질문 문구와 refresh token을 UI에 반영합니다. 선택된 학과·전공은 chat preferences PATCH로 저장합니다.
- 추천 동의 상태는 `ChatAssistRepository`·UseCase를 거쳐 GET으로 읽습니다. 아직 동의하지 않은 경우 UI에서 명시적으로 동의 버튼을 누른 때만 GET 응답의 `policyVersion`을 사용해 동의 PUT을 보냅니다. 동의된 화면에는 철회 동작을 제공하며, 철회 PUT 성공 시 추천 질문을 지웁니다. 이 API는 동의 상태와 정책 버전을 제공하지만 정책 본문/열람 URL은 제공하지 않습니다.
- 채팅 상세의 위치 확인 CTA는 사용자가 누른 뒤에만 coarse 위치 권한을 요청합니다. 요청은 `CampusLocationRemoteDataSource` → `CampusLocationRepository` → `CheckCampusLocationUseCase`를 거쳐 `POST /api/campus/location-check`로 전송합니다. OAS의 latitude, longitude, horizontalAccuracyMeters, measuredAt, clientRequestId 필드를 유지합니다. 결과의 서버 status/message/accuracy를 그대로 표시하고 임의 정확도 threshold나 상태 의미를 적용하지 않습니다. OAS status는 자유 문자열이며 정확도 기준은 계약에 없습니다. 응답 building ID는 이름으로 추정하지 않습니다. 위치 권한은 Android `ACCESS_COARSE_LOCATION` 하나만 선언했습니다.
- 설정의 개인정보 처리방침 진입은 기존 Notion 링크 대신 공개 `GET /api/privacy/policy` (`text/html`)로 연결했습니다. UI는 HTTPS base URL에서 경로를 조합해 기본 브라우저로 엽니다.
- 디버그 HTTP 진단은 BASIC 수준으로 제한하고 URL query 문자열을 가립니다. request/response body와 인증 header는 기록하지 않습니다. release는 기존 `ENABLE_LOG=false` 설정으로 로깅하지 않습니다.
- 첫 진입 가입 버튼을 `학교 이메일로 가입하기`로 명확히 했습니다.
- 설정의 알림 토글, 홈 알림 진입·배지, 마이페이지 진입, 알림 목록·상세 화면/내비게이션/샘플/문자열/아이콘을 삭제했습니다.
- 사용자 설정·프로필·로그인·회원가입·기존 대화 API 연결은 기존 구현을 유지했습니다.

## 미연결·부분 연결

| 기능 | 상태 및 근거 |
|---|---|
| 커뮤니티 목록 paging | 목록 UI는 스크롤 끝에서 다음 페이지를 요청합니다. 댓글·내 활동 조회는 page 불일치와 100페이지 상한으로 반복 호출을 제한합니다. 상세 mutation 오류 표현은 제한적입니다. |
| 커뮤니티 작성자·게시판 표시 | OAS `authorLabel`을 기존 UI의 기수 표시로 변환하는 정보는 계약에 없습니다. 현재 숫자가 포함된 경우에만 숫자를 사용하고, 표시 이름/기수 의미가 다른 서버 응답이면 UI 모델 보완이 필요합니다. API 게시판 응답을 기수 게시판과 매칭할 때 `cohort` 값을 사용합니다. |
| 추천 카테고리·preferences PATCH body | OAS departments/specialties 응답은 map-of-array이고 Chat UI에는 기존 enum 선택지만 있습니다. 활성 카테고리 중 기존 UI와 이름 매칭되는 항목만 노출합니다. preferences PATCH request schema는 OAS에서 `{}`(JsonNode)로만 정의되어 있어 `departmentId`, `specialtyId`, `specialtyIds`를 보내는 것은 응답 DTO에 맞춘 추론입니다. 실제 서버 계약 확인이 필요합니다. |
| 추천 동의 | 동의는 선택 시에만 켜고 철회 API도 제공합니다. consent GET 실패 또는 policyVersion 누락이면 동의 조작을 숨깁니다. `/api/privacy/recommendation-consent`는 동의 상태·정책 버전만 반환하며 정책 본문/열람 URL은 포함하지 않습니다. 일반 개인정보 처리방침은 별도 `/api/privacy/policy` HTML API로 설정 화면에서 열 수 있습니다. 추천 동의 정책 원문으로 직접 이동하는 경로는 확인되지 않았습니다. |
| 첨부 metadata/content GET | OAS에 `GET /api/attachments/{attachmentId}`(metadata)와 `/content`(binary)가 있습니다. 현재 `ChatResponseDto.messages.attachments`가 `AttachmentDto` metadata를 이미 포함하며, `ChatMessageAttachments`는 이미지 tile/PDF chip만 렌더하고 다운로드·미리보기 클릭 동작을 제공하지 않습니다. 따라서 GET operations는 대응 사용자 동작이 없어 연결하지 않았습니다. 별도 파일 열기/미리보기 UI가 추가되면 content endpoint 연결이 필요합니다. |
| 위치 확인 권한/API | OAS `POST /api/campus/location-check` 요청/응답 DTO를 연결했습니다. coarse 위치만 요청하고 권한 거부, 비활성 provider, 측정 실패, API 실패는 실패/재시도 상태로 표시합니다. 응답 status/message/accuracy를 그대로 화면에 내보냅니다. OAS는 status enum과 accuracy threshold를 정의하지 않으므로 low-accuracy 분류를 추측하지 않습니다. `matchedBuilding`은 numeric ID로 보존하되 지도 건물명으로 변환하지 않습니다. `MessageResponse.location`은 `JsonNode`이므로 메시지 생성으로 위치 요청을 유추하지 않습니다. |
| 위치 확인 권한/API | OAS `POST /api/campus/location-check` 요청/응답 DTO를 연결했습니다. coarse 위치만 요청하고 권한 거부, 비활성 provider, 측정 실패, API 실패는 실패/재시도 상태로 표시합니다. 응답 status/message/accuracy를 그대로 화면에 내보냅니다. OAS는 status enum과 accuracy threshold를 정의하지 않으므로 low-accuracy 분류를 추측하지 않습니다. `matchedBuilding`은 numeric ID로 보존하되 지도 건물명으로 변환하지 않습니다. `MessageResponse.location`은 `JsonNode`이므로 메시지 생성으로 위치 요청을 유추하지 않습니다. 사용자는 2026-11-24에 “현재처럼 캠퍼스 위치 결과 API만 사용(추천)”을 선택했습니다. 대안 `POST /api/chats/{id}/location-check`는 응답 스키마가 `{}`로 정의되어 있어 이 CTA에서 선택하지 않았으며, 현재 캠퍼스 위치 결과 API를 유지합니다. |
| 개인정보 처리방침 | OAS `GET /api/privacy/policy`는 인증 없이 `text/html`을 반환하며 기존 설정 화면의 외부 링크 UI가 있어 공개 서버 정책 URL로 연결했습니다. 약관 문서는 별도 OAS operation/URL이 없어 가입 화면에서 열 수 있는 경로는 확인되지 않았습니다. |
| 캠퍼스 지도·시설·층별 도면·내비게이션 | OAS 응답 스키마가 `{}`로 정의되어 있어 payload 모델을 추측하지 않았습니다. 실제 서버 응답 계약 확인 전 미연결입니다. |
| 이메일 인증 토큰 교환 | 현재 앱 토큰 저장·요청 인증 구조와 Swagger 응답 계약을 대조한 추가 확인이 남아 있습니다. |

## 출시 안전 체크

출처: `launch-safety-checklist` (2026-11-24 확인). 법적 정책 문구의 적정성 판단은 포함하지 않습니다.

| 항목 | 코드 근거 | 판정 |
|---|---|---|
| 가입 동의 기본값·철회 | `SignupUiState.isTermsAgreed` 기본값은 false. 추천 선택 동의는 `ChatViewModel`에서 명시적 opt-in/철회를 처리합니다. 가입 필수동의의 계정 생성 후 철회 경로는 앱에서 확인되지 않았습니다. | 부분 확인; 필수동의 철회 경로 확인 필요 |
| 약관·처리방침 진입점 | 설정 개인정보 처리방침 링크는 `/api/privacy/policy`로 연결했습니다. 가입 화면의 약관·필수 동의 문구는 별도 약관 URL이 없고, OAS에 이용약관 operation도 없습니다. | 처리방침 PASS; 약관 확인 필요 |
| 권한 최소 수집 | `AndroidManifest.xml`의 위치 권한은 coarse 하나이며, 사용자가 위치 확인 CTA를 누른 뒤 런타임 요청합니다. 서버 위치 권한 분리는 클라이언트 코드에서 판정할 수 없습니다. | 앱 권한 PASS; 서버 역할 확인 필요 |
| API 키 | 이번 변경에서 앱 API key를 추가하지 않았습니다. | 해당 없음 |
| 테스트/실데이터 분리 | `app/build.gradle.kts`에서 debug/release가 같은 Swagger base URL을 사용합니다. 테스트 전용 서버/데이터 격리는 확인되지 않았습니다. | 확인 필요 |
| 결제 | 이번 기능에 결제 API·화면이 없습니다. | 해당 없음 |

## 검증·제약

- `SettingsApiServiceContractTest`: PASS (비밀번호 재설정·문의 JSON 계약).
- 사후 수정 최종 변경 기준 `:app:compileDebugKotlin`: PASS, `:app:testDebugUnitTest`: PASS, `:app:assembleDebug`: PASS.
- 이전 빌드 APK 설치·에뮬레이터 비로그인 기동: PASS (`emulator-5554`, `com.teamnative.hopes/.MainActivity` resumed). 최종 코드 기준 APK 경로: `app/build/outputs/apk/debug/app-debug.apk`. 최종 APK 재설치는 현재 셸에 `adb`가 없어 미확인입니다. 로그인 후 서버 기능 검증은 테스트 계정 위치가 없어 미수행입니다.
- 첫 Gradle 실행은 기본 사용자 캐시의 `.lck` 파일 `Operation not permitted`로 차단되어 task-specific `/private/tmp/hopes-gradle-home`을 사용했습니다.
- 사후 수정 targeted tests: PASS (`AttachmentUploadPolicyTest`, `CommunityStoreTest`, `SendChatMessageUseCaseTest`, `LocationCheckContractTest`). 전체 `:app:testDebugUnitTest`: PASS, 최종 APK 재생성: PASS.
- 추가 수정 라운드: `CommunityStoreTest`, `CommunitySearchTest` 회귀 테스트 PASS. 최종 `:app:compileDebugKotlin :app:testDebugUnitTest :app:assembleDebug`: PASS; `git diff --check`: PASS. 생성 APK는 `app/build/outputs/apk/debug/app-debug.apk` (2026-11-24 14:34:37)입니다.
- 디버그 위치 권한/서버 응답 흐름은 에뮬레이터에서 별도 확인하지 않았습니다. 이전 셸에서 `adb: command not found`가 발생했고 비운영 테스트 계정 경로도 없습니다.
