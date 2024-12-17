# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Changed

- check SonarQube 10.7 version compatibility

### Deleted

## [2.0.0] - 2024-12-16

### Changed

- [#34](https://github.com/green-code-initiative/creedengo-php/issues/34) Add test to ensure all Rules are registered
- [#38](https://github.com/green-code-initiative/creedengo-php/pull/38) migration from ecocode to creedengo - rules renaming
- migration from ecocode to creedengo - all over the code

## [1.5.1] - 2024-07-25

### Changed

- update tag workflow to use JDK 17

## [1.5.0] - 2024-07-25

### Changed

- upgrade docker system
- [#30](https://github.com/green-code-initiative/creedengo-php/issues/30) Upgrade dependencies to latest ones - technical
  incompatibilities for SonarQube before 9.9 version
- clean unit test files (to have less other issues)

## [1.4.4] - 2024-07-19

### Changed

- [#28](https://github.com/green-code-initiative/creedengo-php/issues/28) Add support for SonarQube 10.6.0

## [1.4.3] - 2024-05-15

### Added

- [#20](https://github.com/green-code-initiative/creedengo-php/issues/20) Add support for SonarQube 10.4 "
  DownloadOnlyWhenRequired" feature

### Changed

- [#23](https://github.com/green-code-initiative/creedengo-php/issues/23) deprecation of EC22 rule for PHP (waiting for
  measurement to prove it)
- check Sonarqube 10.5.1 compatibility + update docker files and README.md

## [1.4.2] - 2024-01-12

### Changed

- [#17](https://github.com/green-code-initiative/creedengo-php/issues/17) Correction of error with deprecated EC34 rule
- Update ecocode-rules-specifications to 1.4.7

## [1.4.1] - 2024-01-06

### Added

- Add 10.3 SonarQube compatibility

### Changed

- [#9](https://github.com/green-code-initiative/creedengo-php/pull/9) Upgrade licence system and licence headers of Java
  files
- [#10](https://github.com/green-code-initiative/creedengo-php/pull/10) Adding EC35 rule : EC35 rule replaces EC34 with a
  specific use case ("file not found" specific)
- [#13](https://github.com/green-code-initiative/creedengo-php/issues/13) Add build number to manifest
- [#12](https://github.com/green-code-initiative/creedengo-php/issues/12) Fix unit tests execution with Maven
- Update ecoCode-rules-specifications to 1.4.6
- README.md upgrade : docker test environment

## [1.4.0] - 2023-08-08

### Added

- PHP rules moved from `ecoCode` repository to current repository
- [#121](https://github.com/green-code-initiative/creedengo-rules-specifications/issues/121) new PHP rule : Multiple if-else statement +
  refactoring implementation
- [#205](https://github.com/green-code-initiative/creedengo-rules-specifications/issues/205) compatibility with SonarQube 10.1

## Comparison list

[unreleased](https://github.com/green-code-initiative/creedengo-php/compare/2.0.0...HEAD)
[2.0.0](https://github.com/green-code-initiative/creedengo-php/compare/1.5.1...2.0.0)
[1.5.1](https://github.com/green-code-initiative/creedengo-php/compare/1.5.0...1.5.1)
[1.5.0](https://github.com/green-code-initiative/creedengo-php/compare/1.4.4...1.5.0)
[1.4.4](https://github.com/green-code-initiative/creedengo-php/compare/1.4.3...1.4.4)
[1.4.3](https://github.com/green-code-initiative/creedengo-php/compare/1.4.2...1.4.3)
[1.4.2](https://github.com/green-code-initiative/creedengo-php/compare/1.4.1...1.4.2)
[1.4.1](https://github.com/green-code-initiative/creedengo-php/compare/1.4.0...1.4.1)
[1.4.0](https://github.com/green-code-initiative/creedengo-php/releases/tag/1.4.0)
