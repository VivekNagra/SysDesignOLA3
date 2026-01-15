# SysDesignOLA3 – Commit Stage CI (Large Systems)

This repository is my solution for the **E25 Large Systems – Study Point Assignment (10 SP): Continuous Integration**.

The goal is a **commit stage** pipeline that runs fast quality gates (build, unit tests, static analysis, coverage) and produces **artifacts + SBOM**.

## What the pipeline does

### On Pull Requests to `main`
- Runs: build + unit tests + Checkstyle + SpotBugs + JaCoCo coverage + SBOM
- Builds Docker image (validates Dockerfile)
- Uploads artifacts to the workflow run (JAR, SBOM, reports)
- **Does NOT push** Docker image (PR code is not trusted yet)

### On Push to `main`
- Runs the same quality gates again
- Builds and **pushes Docker image** to GitHub Container Registry (GHCR)
- Uploads artifacts again (JAR, SBOM, reports)

## Where outputs are produced (locally and in CI)
- JAR: `target/*.jar`
- Coverage report: `target/site/jacoco/index.html`
- Checkstyle report: `target/checkstyle-result.xml`
- SpotBugs report: `target/spotbugsXml.xml`
- SBOM (CycloneDX): `target/bom.json`

## How to run locally
```bash
# Run the commit-stage checks (same as CI)
./mvnw -B -ntp verify

# Run the app (simple CLI)
./mvnw -q -DskipTests package
java -jar target/sysdesignola3-1.0.0.jar
