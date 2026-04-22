# Claude Pet — AI 스프라이트 생성 프롬프트 (Phase 8 SPEC-021)

**스타일 확정**: 픽셀 아트 (8-bit 풍 chibi blob).
**저작권**: 원본 레포 이미지는 All Rights Reserved, 복제 금지. 본 프롬프트로 **오리지널 캐릭터** 를 생성한다.

## 생성 결과 배치

생성한 10장의 PNG 를 아래 경로에 **정확한 파일명** 으로 저장한다:

```
composeApp/src/jvmMain/resources/pet/
├── idle_default.png
├── idle_smile.png
├── idle_boring.png
├── idle_jumping.png
├── idle_touch.png
├── idle_working_prepare.png
├── idle_working.png
├── idle_working_end.png
├── idle_hungry.png
└── idle_fed.png
```

파일명이 다르면 `PetSprite` 가 못 찾아 Canvas fallback 으로 떨어진다.

---

## 필수 규격

- **투명 배경 PNG (RGBA)** — 불투명 배경 이미지는 사용 불가
- **정사각형, 256×256 px** 또는 **512×512 px** (2의 제곱 권장)
- 캐릭터는 중앙 정렬, 가장자리 8~16px 패딩
- **10장 모두 동일 캐릭터** — 시드 고정 / 이전 이미지 참조로 일관성 확보

---

## 1순위 추천 — Google Gemini

gemini.google.com 에 구글 계정으로 로그인. **한 대화창 내에서 총 10번** 개별 메시지를 보내야 10장이 모인다 (Gemini 는 한 메시지당 1장만 생성). 아래 순서 엄수.

### Step 1 — 공통 캐릭터 고정 프롬프트 (첫 메시지, 이것만 먼저 보내고 결과 확인)

```
Generate a 256x256 transparent PNG pixel art sprite.

Character design (this is my desktop pet — a friendly AI assistant mascot.
Keep it visually consistent across all the images I will ask you for next):

- Tiny chibi round-body creature inspired by a spark / flame motif
- Head top has a small warm-orange spark or sparkle shape (like a soft
  starburst or tiny flame silhouette), coral-orange (#d97757 tone)
- Body is round and cream-peach colored, with soft pink cheek blush
- Large shiny shy eyes (dark brown pupils with a small white highlight)
- Tiny short arms, no visible legs (body just sits on the ground)
- Dark brown 1px outline, limited retro color palette
- True pixel art: 32x32 source upscaled to 256x256 using nearest-neighbor,
  crisp blocky edges (no blur, no anti-aliasing)
- Clean transparent alpha background — no floor, no shadow, no text, no logo

Current pose: "idle_default" — standing calmly, neutral shy expression,
looking slightly downward, arms relaxed at sides.

Please generate this single PNG. After I confirm I like this character,
I will send 9 more follow-up messages asking for the exact same character
in 9 different poses.
```

### Step 2 — Gemini 가 1장 준 뒤, 마음에 드는 결과를 확인

- 만족스러우면 다운받아 `composeApp/src/jvmMain/resources/pet/idle_default.png` 로 저장
- 만족 안 되면 "Regenerate" 또는 같은 프롬프트 재송부
- 마음에 드는 캐릭터가 정해지기 **전에는 다음 상태로 넘어가지 말 것** (후속 이미지가 일관성을 잃음)

### Step 3 — 9번의 후속 메시지 (하나씩, 개별 메시지로)

아래 9개를 **각각 하나의 독립된 메시지** 로 순서대로 보내세요. 한 번에 리스트로 몰아서 보내면 Gemini 가 텍스트로만 답하거나 1장만 만듭니다.

| 순서 | 저장 파일명 | Gemini 에 보낼 메시지 (각각 한 번) |
|---|---|---|
| 2/10 | `idle_smile.png` | `Same exact character from the previous image, keep all design details identical. New pose: gentle shy smile, cheeks more blushed pink, eyes curved upward closed in happiness, tiny arms slightly raised. Still 256x256 transparent pixel art PNG.` |
| 3/10 | `idle_boring.png` | `Same exact character, same style. New pose: bored sleepy look, half-closed droopy eyes, head tilted to one side, one tiny arm touching the cheek. 256x256 transparent pixel art PNG.` |
| 4/10 | `idle_jumping.png` | `Same exact character, same style. New pose: mid-air jumping, both tiny arms stretched upward, eyes wide open sparkly, small motion lines around body, the bottom of the body off the ground. 256x256 transparent pixel art PNG.` |
| 5/10 | `idle_touch.png` | `Same exact character, same style. New pose: startled shocked reaction, eyes very wide with shrunken pupils, body recoiling backward, arms flinched up defensively, small exclamation mark above head. 256x256 transparent pixel art PNG.` |
| 6/10 | `idle_working_prepare.png` | `Same exact character, same style. New pose: putting on tiny round black-framed glasses, determined but slightly nervous expression, focused eyes, mouth set, ready-to-work stance. 256x256 transparent pixel art PNG.` |
| 7/10 | `idle_working.png` | `Same exact character, same style. New pose: wearing the tiny round glasses, silently concentrating, one small hand to chin in thinking pose, serious calm eyes. 256x256 transparent pixel art PNG.` |
| 8/10 | `idle_working_end.png` | `Same exact character, same style. New pose: relaxed relieved expression, wiping forehead with tiny arm, glasses slightly askew, gentle tired smile, small sigh. 256x256 transparent pixel art PNG.` |
| 9/10 | `idle_hungry.png` | `Same exact character, same style. New pose: weak tired expression, low-hanging half-lidded eyes, body slightly hunched, one tiny hand on belly, small frown, looks a bit sad. 256x256 transparent pixel art PNG.` |
| 10/10 | `idle_fed.png` | `Same exact character, same style. New pose: eyes sparkling with joy, wide grateful smile, holding a tiny rice bowl with chopsticks, happy tears in eyes, cheeks very rosy. 256x256 transparent pixel art PNG.` |

### Gemini 팁

- **반드시 한 대화창 안에서** 10개 메시지를 이어가야 캐릭터가 유지됨. 새 채팅 시작하면 캐릭터가 달라짐.
- 매번 응답에 `Same exact character from the previous image` 를 꼭 포함
- 만족 안 되면 해당 메시지에서 Regenerate 하거나 "좀 더 X 하게, 같은 캐릭터로 다시" 라고 한국어로 수정 요청 가능
- 이미지 다운로드: 이미지 우측 하단 또는 우클릭 → 다른 이름으로 저장
- 저장 시 파일명을 위 표의 값과 **정확히 일치** 시키기 (다르면 앱이 못 찾음)

### 프롬프트 변형 — "Claude 캐릭터" 느낌을 더 강화하고 싶다면

위 Step 1 에서 `spark / flame motif` 부분을 아래처럼 대체할 수 있음 (단 Anthropic 공식 로고 그대로 복제 금지):

```
Head top has a coral-orange pointed spark shape — a stylized simplified
sparkle / small flame, original design (do NOT copy any existing company
logo or trademark). The spark is solid, outlined in dark brown.
```

---

## 2순위 — Retro Diffusion (진짜 픽셀 아트)

retrodiffusion.ai → 가입 → "Character" 또는 "Sprite" 모드 선택. 캐릭터 일관성 기능 내장.

### 공통 negative prompt (모든 생성에 공통)

```
text, logo, watermark, signature, border, frame, background, realistic,
3d, blurry, anti-aliased, soft, smooth gradient, multiple characters
```

### 상태별 프롬프트 (키워드 중심)

| 파일명 | 프롬프트 |
|---|---|
| `idle_default.png` | `chibi blob creature, peach orange body, shy big eyes, standing calmly, pixel art sprite, 32x32, transparent background` |
| `idle_smile.png` | `chibi blob creature, peach orange body, shy smile, blushing cheeks, closed happy eyes, pixel art sprite, 32x32, transparent background` |
| `idle_boring.png` | `chibi blob creature, peach orange body, bored sleepy, half-closed eyes, tilted head, pixel art sprite, 32x32, transparent background` |
| `idle_jumping.png` | `chibi blob creature, peach orange body, mid-air jumping, arms raised, wide eyes, motion lines, pixel art sprite, 32x32, transparent background` |
| `idle_touch.png` | `chibi blob creature, peach orange body, startled shocked, wide eyes, recoiling backward, exclamation mark, pixel art sprite, 32x32, transparent background` |
| `idle_working_prepare.png` | `chibi blob creature, peach orange body, wearing round glasses, determined focused, getting ready, pixel art sprite, 32x32, transparent background` |
| `idle_working.png` | `chibi blob creature, peach orange body, round glasses, concentrating, hand on chin, thinking, pixel art sprite, 32x32, transparent background` |
| `idle_working_end.png` | `chibi blob creature, peach orange body, round glasses askew, relieved smile, wiping forehead, pixel art sprite, 32x32, transparent background` |
| `idle_hungry.png` | `chibi blob creature, peach orange body, weak tired, half-lidded eyes, hand on belly, slight frown, pixel art sprite, 32x32, transparent background` |
| `idle_fed.png` | `chibi blob creature, peach orange body, sparkling eyes, wide grateful smile, holding rice bowl, happy tears, pixel art sprite, 32x32, transparent background` |

### Retro Diffusion 팁

- "Seed" 값을 고정해 같은 캐릭터 유지
- "Style Reference" 에 첫 생성물을 업로드하면 나머지 9장이 같은 캐릭터로 나옴

---

## 3순위 — Bing Image Creator (DALL-E 3, 무료)

copilot.microsoft.com/images/create → 마이크로소프트 계정으로 로그인.

**한계**: "pixel art" 로 요청해도 실제로는 anti-aliased soft pixel 이 나옴. 데스크톱 펫 크기(~128px) 에서는 시각적으로 큰 차이가 없어서 허용 범위.

프롬프트는 위 Gemini 섹션을 그대로 사용 가능. "Step 1" 을 첫 프롬프트로 전체 복사해 넣으면 됨.

---

## 트레이 아이콘 (선택, SPEC-026 에서 교체)

```
16x16 pixel art icon of the same chibi blob creature, front-facing smile,
one single tile, transparent background, optimized for macOS menu bar
```

파일: `composeApp/src/jvmMain/resources/pet/tray_icon.png`

---

## 생성 후 확인

1. 10개 PNG 를 지정 경로에 배치
2. `./gradlew :composeApp:run` 실행
3. 펫이 Canvas 동그라미 대신 생성한 픽셀 아트로 렌더되면 성공
4. 일부만 배치해도 해당 상태에서만 이미지 노출, 나머지는 Canvas fallback
