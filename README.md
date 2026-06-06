# RK Control Center

RK Control Center is a Kotlin Multiplatform + Compose Multiplatform app for tracking Rune Knight resources, zeny, daily tasks, and rune craft planning for Ragnarok LATAM gameplay.

The project currently targets Android and Web. Most application logic and UI lives in the shared module so both targets can reuse the same dashboard experience.

## Features

- Multi-profile character tracking with active profile selection.
- Zeny wallet tracking per profile.
- Rune material inventory with icon-backed stock cards.
- Bazar price inputs for farm materials.
- Rune stock tracking for Berkana, Thurisaz, Luxanima, Othila, Nauthiz, and Wyrd.
- Reactive craft planner that calculates missing materials from the desired rune quantity.
- Estimated zeny cost for missing craft materials based on saved bazar prices.
- Custom farm and instance schedule persisted per profile.
- Activity rows for instances, map farms, build tests, target monsters, route notes, and expected loot.
- Daily completion checks with a reset action for the active profile.
- Compose resource images for rune and material icons.
- Native vector icons for common controls such as add, delete, check, reset, map, instance, and zeny total.

## Tech Stack

- Kotlin Multiplatform
- Compose Multiplatform
- Compose Material 3
- Kotlinx Serialization
- Kotlinx Datetime
- Kotlin/Wasm and Kotlin/JS browser targets
- Android application target

## Project Structure

```text
androidApp/
  Android entry point and application packaging.

webApp/
  Web entry point for JS and Wasm browser targets.

shared/
  Shared Compose UI, domain logic, models, storage abstraction, and resources.
```

Important shared files:

```text
shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/App.kt
  Main Compose dashboard.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/domain/MatrizCraft.kt
  Rune recipes and craft cost calculation.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/domain/CronogramaData.kt
  Reusable farm, instance, and test activity definitions.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/domain/Models.kt
  Serializable app state models.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/data/StorageManager.kt
  JSON persistence facade used by the app.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/ui/components/
  Reusable Compose components, farm activity rows, and vector icons.

shared/src/commonMain/kotlin/org/zera/rkcontrolcenter/ui/theme/Cores.kt
  Shared color tokens and route category colors.

shared/src/commonMain/composeResources/drawable/
  WebP icons used by the inventory and rune cards.
```

## Running The App

On Windows:

```powershell
.\gradlew.bat :androidApp:assembleDebug
.\gradlew.bat :webApp:wasmJsBrowserDevelopmentRun
.\gradlew.bat :webApp:jsBrowserDevelopmentRun
```

On macOS or Linux:

```bash
./gradlew :androidApp:assembleDebug
./gradlew :webApp:wasmJsBrowserDevelopmentRun
./gradlew :webApp:jsBrowserDevelopmentRun
```

Use the Wasm target for modern browsers. Use the JS target when browser compatibility is more important than runtime performance.

## Verification

Useful Gradle checks:

```powershell
.\gradlew.bat :shared:compileKotlinMetadata
.\gradlew.bat :shared:wasmJsTest
.\gradlew.bat :shared:jsTest
.\gradlew.bat :shared:testAndroidHostTest
```

Use `./gradlew` instead of `.\gradlew.bat` on macOS or Linux.

## Persistence

The app stores `PainelData` as JSON through `StorageManager`.

Web targets persist data in browser `localStorage`.

Android storage is currently a placeholder implementation and does not persist data yet. The expected next step is to back `StoragePlatform` with SharedPreferences or DataStore.

## Development Notes

- Keep shared UI in `shared/src/commonMain` unless a platform-specific API is required.
- Add new rune recipes in `MatrizCraft`.
- Add reusable farm or instance activity definitions in `CronogramaData.kt`.
- Add new persisted fields to the serializable models before wiring them into UI state.
- Put reusable UI controls in `ui/components`.
- Put shared palette and category colors in `ui/theme/Cores.kt`.
- Register new image assets under `shared/src/commonMain/composeResources/drawable`.
- Custom schedule entries are stored on `Personagem.atividadesCustom`; completion state is stored in `checksDiarios`.

## Known Gaps

- Android persistence is not implemented yet.
- Some platform template files, such as `Greeting.kt`, are still present and can be removed when no longer useful.
- Automated tests for craft calculation and persistence migration are not yet in place.
