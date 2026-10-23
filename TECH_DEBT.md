# Technical debt

Temporary workarounds to remove once their root cause is fixed.

## TD-1 - Maven 3.10 breaks `maven-git-versioning-extension` (Oct 2026)

### Problem

- GitHub runner images (`ubuntu-latest` / `ubuntu-24.04`) now ship **Maven 3.10.0** (previously 3.9.x).
- Maven 3.10 **no longer provides `maven-shared-utils`**. `me.qoomon:maven-git-versioning-extension` (declared in `.mvn/extensions.xml`, version 9.12.1 = latest at the time) still relies on it.
- Every `mvn` call using the runner Maven fails at project scan with:
  `NoClassDefFoundError: org/apache/maven/shared/utils/logging/MessageUtils`.
- `./mvnw` is not affected (wrapper = Maven 3.9.9).
- Impacted: integration tests (the orchestrator launches `mvn sonar:sonar` on the test project), `tag_release.yml` (`mvn`), and the GitHub-managed "Automatic Dependency Submission" workflow.
- Same extension and same risk in `creedengo-java` and `creedengo-python`.

### Workarounds in place

| Where | Workaround |
|---|---|
| `.github/workflows/build.yml`, `build-jdk17.yml`, `dependency-submission.yml` | Step `Use Maven wrapper distribution as "mvn"`: puts the wrapper Maven (3.9.x) first in `PATH`. `Check Maven version` step prints the version used. |
| `.github/workflows/tag_release.yml` | `mvn` replaced by `./mvnw`. |
| `src/it/test-projects/creedengo-php-plugin-test-project/.mvn/maven.config` | Empty file: makes Maven treat the test project as its own root so it does not load the root `.mvn/extensions.xml` (the test project does not need the extension). Must stay **empty** (no `#` comments, not verified as supported). |
| `.github/workflows/dependency-submission.yml` | Replaces the GitHub-managed "Automatic Dependency Submission". Requires in repository settings: **Dependency graph = enabled**, **Automatic dependency submission = disabled**. |

### Related fix (not Maven-3.10 related)

Bumping `creedengo-integration-test` to 0.7.0 pulls a `sonar-orchestrator` needing `commons-lang3` >= 3.18 (`ObjectUtils.getIfNull(T, T)`). An older copy bundled in `sonar-php-plugin` shadowed it on the failsafe classpath. Fixed with `classpathDependencyExcludes` for `org.sonarsource.php:sonar-php-plugin` in the `maven-failsafe-plugin` configuration of `pom.xml`. Keep.

### Actions when the extension supports Maven 3.10

1. Check for a release of `maven-git-versioning-extension` newer than 9.12.1 that no longer needs `maven-shared-utils` (upstream issue/PR to open or follow: https://github.com/qoomon/maven-git-versioning-extension). Bump the version in `.mvn/extensions.xml` (Dependabot may propose it).
2. Remove the `Use Maven wrapper distribution as "mvn"` and `Check Maven version` steps from the three workflows.
3. Restore `mvn` in `tag_release.yml` only if needed (`./mvnw` is fine to keep).
4. Delete `src/it/test-projects/creedengo-php-plugin-test-project/.mvn/maven.config` and check the integration tests still pass.
5. Decide whether to keep `dependency-submission.yml` or go back to the GitHub-managed "Automatic dependency submission" (then delete the workflow and re-enable the setting).
6. Optionally upgrade the Maven wrapper (`.mvn/wrapper/maven-wrapper.properties`) to 3.10.x and fix any stricter POM validation errors.
7. Apply the same cleanup in `creedengo-java` and `creedengo-python`.
8. Delete this section.

### References

- Maven 3.10.0 release notes: https://maven.apache.org/docs/3.10.0/release-notes.html
- Runner image picking up Maven 3.10.0: https://github.com/actions/runner-images/issues/14883
- Fix of the extension on an unofficial fork (shows the required change): https://github.com/MahiFX/maven-git-versioning-extension/pull/52
