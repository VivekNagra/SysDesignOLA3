# Java CI Template

This is a starter repo for demonstrating a modern **Commit Stage CI** with GitHub Actions.

## Features

- Java 21 + Maven build
- Unit tests with JUnit 5
- Code coverage with JaCoCo
- Static analysis with Checkstyle & SpotBugs
- SBOM generation with CycloneDX
- GitHub Actions workflow for build + quality gates
- Upload of JAR + reports as workflow artifacts

## How to Use

1. Click **Use this template** on GitHub to create your own repo.
2. Enable **branch protection** on `main` and require the CI check to pass.
3. Extend the pipeline as part of your assignment (e.g., stricter checks, Docker image, dependency scanning).
