---
name: android-github-workflow
description: Manage Android project GitHub work using the team's issue-first workflow. Use when creating or updating GitHub issues, issue-linked branches, commits, pull requests, labels, or release pull requests for this repository.
---

# Android GitHub Workflow

## Overview

Follow the Notion Android convention: create an issue before work, include the issue number in the branch, commit, and PR, and merge implementation PRs into `dev`.

## Workflow

1. Confirm the repository, issue scope, and whether the change is a feature, fix, refactor, chore, or hotfix.
2. Create or select the GitHub issue using the required template. Apply exactly one applicable label.
3. Create the work branch from `dev` as `codex/<type>/<issue-number>-<kebab-case-summary>`.
4. Make and validate the Android change. Run relevant Gradle build, unit-test, lint, and UI/Preview checks.
5. Commit with Conventional Commits and include the issue number in the commit body.
6. Push the branch and open a draft PR targeting `dev` with the required title and body.
7. After integration testing on `dev`, create a release chore issue and a release branch for the `dev` to `main` PR.

## Issue Convention

Use this format:

```md
제목: [기능] 구현할 작업

## 작업 내용
-

## 완료 기준
- [ ]
- [ ]

## 참고 사항
- 브랜치: `codex/<type>/<issue-number>-<summary>`
- PR 대상: `dev`
```

## Label Convention

- `✨ feature`: 새로운 기능 구현
- `🚨 fix`: 버그 수정
- `♻️ refactor`: 동작 변경 없는 구조 개선
- `🔧 chore`: 설정, 빌드, dependency 작업
- `🔥 hotfix`: `main`에 반영된 긴급 수정

## Commit Convention

Use Conventional Commits:

```text
<type>(<scope>): <present-tense summary>

Related issue: #<issue-number>
```

Use `!HOTFIX:` only for an urgent production correction. Keep each commit limited to the linked issue’s scope.

## Pull Request Convention

Target `dev` for implementation work. Use this title and body:

```md
제목: [작업요약] - #<issue-number> :: <작업명>

## 변경 사항
-

## 검증
- [ ] Build
- [ ] Unit Test
- [ ] Lint
- [ ] UI / Preview 확인

## 체크리스트
- [ ] DTO·ApiService가 Presentation/Domain에 노출되지 않음
- [ ] 문자열·색상·간격 하드코딩 없음
- [ ] Hilt binding 확인
- [ ] 민감 정보 포함 없음

## 관련 이슈
Closes #<issue-number>
```

Use a draft PR unless the user explicitly requests ready-for-review. Verify the title, issue number, `dev` base branch, selected label, and `Closes` directive before opening it.

## Release Convention

Create a `🔧 chore` issue for each release. Branch from `dev` as `codex/chore/<issue-number>-release-<version>`, validate the release candidate, and open the release PR from that branch to `main`.
