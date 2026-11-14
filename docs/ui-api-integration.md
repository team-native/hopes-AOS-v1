# v2 UI · API 연결 목록 (#230)

Figma v2 퍼블리싱에서 **화면은 만들었지만 서버 연결이 필요한 곳**을 모았습니다.
이미 서버와 연결된 곳은 맨 아래 "연결 완료"에 따로 적었습니다.

표의 "지금 동작"은 API가 붙기 전 화면이 하는 일입니다.
"코드 위치"를 따라가면 예시 데이터나 빈 콜백이 있는 자리가 나옵니다.

## 홈 (피그마 30)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 커뮤니티 인기글 | 시안 예시 글 2개 | 인기글 목록 (id, 기수, 제목, 좋아요 수) | `HomeRoute`의 `samplePopularPosts` → `HomeUiState.popularPosts` |
| 인기글 누르기 | 커뮤니티 탭으로 이동 | 게시글 상세 (화면 없음) | `HomeRoute`의 `PopularPostClicked` |
| 알림 버튼 빨간 점 | 항상 꺼짐 | 읽지 않은 알림 여부 | `HomeUiState.hasUnreadNotification` |

## 커뮤니티 (피그마 19)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 게시글 목록 | 항상 빈 화면 | 게시글 목록 (기수 필터) | `CommunityRoute`의 `contentState = Empty` |
| 불러오기 실패 | 화면만 준비됨 (19-2) | 위 API 실패 시 `LoadError` | `CommunityContentState.LoadError` |
| 기수 필터 목록 | 8~11기 고정 | 기수 목록 | `CommunityRoute`의 `COMMUNITY_COHORTS` |
| 검색 · 내 활동 · 글쓰기 | 반응 없음 | 각 기능 | `CommunityRoute`의 `onSearchClick` 등 |

게시글이 있는 목록 화면은 시안을 받지 못해 아직 없습니다.

## 마이페이지 (피그마 31)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 작성글 · 댓글 · 대화 수 | 예시 값 2 / 5 / 12 | 활동 통계 | `MyPageRoute`의 `SAMPLE_ACTIVITY_STATS` |
| 내 활동 | 커뮤니티 탭으로 이동 | 내 활동 화면 (화면 없음) | `MyPageRoute`의 `MyActivityClicked` |
| 기수 ("10기") | 표시 안 함 | 프로필에 기수 필드 | `UserProfile`에 없음 |

## 알림 (피그마 25)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 알림 목록 | 시안 예시 4개 | 알림 목록 (종류, 제목, 내용) | `NotificationRoute`의 `sampleItems` |
| 편집 · 보기 · 모두 읽음 처리 | 반응 없음 | 읽음 처리, 이동 대상 | `NotificationRoute`의 콜백 |

## 설정 (피그마 32)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 알림 받기 토글 | 화면 안에서만 유지 (앱 재시작 시 켜짐) | 알림 설정 조회·저장 | `SettingsRoute`의 `isNotificationEnabled` |
| 앱 정보 "최신 버전 · 사용 중" | 항상 "사용 중" | 최신 버전 확인 (또는 행 삭제) | `AppInfoScreen` |

## 채팅 홈 (피그마 05 · 06 · 07)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 추천 질문 | 학과·전공에 맞춘 고정 질문 | 추천 질문 목록 | `recommendedQuestionTopics()`, `ChatUiState.recommendationState` |
| 추천 로딩 · 실패 | 화면만 준비됨 (05-4, 05-2) | 위 API 상태를 `Loading` / `LoadError`로 | `ChatRecommendationState` |
| 추천 새로 고침 | 반응 없음 | 위 API 다시 부르기 | `ChatViewModel`의 `RecommendationsRefreshClicked` |
| 학과 · 전공 선택 | 화면에서만 기억, 서버에 안 보냄 | 질문과 함께 보내거나 프로필에 저장 | `ChatUiState.department` / `major` |
| 입력창 + (자료 첨부) · 마이크 | 반응 없음 | 첨부 업로드 (HOP-01), 음성 입력 | `ChatComposer`의 `onAttachClick` / `onVoiceClick` |

## 채팅 상세 (피그마 08)

| 항목 | 지금 동작 | 필요한 API | 코드 위치 |
|---|---|---|---|
| 답변 표시 | 답변이 다 오면 한 번에 표시, 마크다운을 문단으로 정리 | (선택) 스트리밍 | `ChatDetailViewModel.sendReply` |
| 답변 중지 (08-2) | 화면 없음 | 생성 취소 | — |
| 좋아요 · 더 물어보기 · 추가 질문 (08) | 화면 없음 | 피드백, 연관 질문 목록 | — |
| 오프라인 (08-5) | 화면 없음 | 연결 상태 감시와 재전송 (앱 쪽 작업) | — |

## 연결 완료 (참고)

- 홈 인사말 이름: `GetProfileUseCase` (`HomeViewModel`)
- 마이페이지 프로필 요약, 프로필 수정 저장: `GetProfileUseCase`, `UpdateProfileUseCase`
- 개인 설정 프롬프트 조회·저장: `GetSettingsUseCase`, `UpdateCustomPromptUseCase`
- 문의하기 전송: `SubmitInquiryUseCase`
- 로그아웃, 회원탈퇴: `LogoutUseCase`, `DeleteAccountUseCase`
- 로그인 만료: API가 401을 받으면 `SessionState.Expired` → 만료 다이얼로그 (`SessionManager.expireSessionByServer`)
- 채팅 생성 · 조회 · 전송: `CreateChatUseCase`, `GetChatUseCase`, `SendChatMessageUseCase`
