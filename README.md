# SysDesignOLA3 — Commit Stage CI (Java 21 + Maven + Docker to GHCR)

This repository is my OLA3 hand-in for the bachelor course **Udvikling af store systemer**.

The main goal of the assignment is to demonstrate a **Commit Stage CI pipeline** with quality gates (build, unit tests, static analysis, coverage), artifact generation (including an SBOM), Docker image build, and publication to **GitHub Container Registry (GHCR)**.

---

## Application overview

The application is intentionally small and focused. It implements pricing logic for a fictional subscription setup with:

- Subscription plans (e.g., BASIC / STANDARD / PREMIUM)
- VAT calculation
- Discounts and promo codes
- Guardrails and validation (e.g., allowed month ranges)

The application is used mainly as a realistic target for:
- unit tests
- coverage enforcement (JaCoCo)
- static analysis (Checkstyle + SpotBugs)
- reproducible builds and artifact outputs

---

## Tech stack

- **Java:** Eclipse Temurin 21
- **Build tool:** Maven (wrapper)
- **Testing:** JUnit 5
- **Coverage:** JaCoCo (coverage thresholds enforced)
- **Static analysis:** Checkstyle + SpotBugs
- **SBOM:** CycloneDX (JSON)
- **CI/CD:** GitHub Actions
- **Container:** Docker (multi-stage build)
- **Registry:** GitHub Container Registry (GHCR)

--- 

## Local setup

### Prerequisites
- Java 21 (Eclipse Temurin recommended)
- Docker Desktop (for container build/run)

### Verify (same as CI)
```bash
./mvnw -B -ntp verify
````
## Build JAR
```bash
./mvnw -B -ntp package
```

## Run Locally 
```bash
java -jar target/sysdesignola3-1.0.0.jar
```

--- 

## Docker

The repository contains a multi-stage **Dockerfile**:

1) **Build stage** uses `maven:3.9.9-eclipse-temurin-21` to compile and package the application
2) **Runtime stage** uses `eclipse-temurin:21-jre` to run the built JAR

This keeps the final image smaller and avoids shipping Maven tooling in production.

### Build image
```bash
docker build -t sysdesignola3:local .
```

## Run image
```bash
docker run --rm sysdesignola3:local
```

--- 

## Commit Stage CI (GitHub Actions)

The pipeline is designed as a **Commit Stage CI** and runs automatically on:

- **Pull requests** targeting `main`
- **Push** to `main`

High-level flow:
1. Checkout repository
2. Setup Java 21 (Temurin)
3. Run `mvn verify` (compile + unit tests + coverage gate + static analysis + SBOM generation)
4. Upload build artifacts and reports
5. Build Docker image on all runs
6. Push Docker image to GHCR **only on push to `main`**

Workflow file:
- `.github/workflows/commit-ci.yml`

--- 

## Quality gates

The commit stage enforces the following quality gates:

- **Unit tests (JUnit 5):** must pass
- **Coverage (JaCoCo):** minimum thresholds are enforced; the build fails if coverage drops below the configured baseline
- **Style (Checkstyle):** build fails on Checkstyle violations
- **Static analysis (SpotBugs):** build fails when configured thresholds are exceeded
- **SBOM generation (CycloneDX):** SBOM is generated during the build

This ensures that changes are validated early and consistently before they are allowed onto `main`.

--- 

## Artifacts produced

A successful workflow run uploads artifacts in GitHub Actions.

Primary artifact:
- **JAR:** `target/*.jar`

Reports and metadata:
- **JaCoCo report:** `target/site/jacoco/`
- **SpotBugs report:** `target/spotbugsXml.xml`
- **Checkstyle report:** `target/checkstyle-result.xml`
- **SBOM (CycloneDX):** `target/bom.json`

These can be downloaded from the **Artifacts** section on the workflow run page.

--- 

## GHCR container publishing

On **push to `main`**, the pipeline publishes the Docker image to **GitHub Container Registry (GHCR)**.

Image naming pattern:
- `ghcr.io/<owner>/<repo>:latest`
- `ghcr.io/<owner>/<repo>:sha-<commit_sha>`

The package can be verified from:
- GitHub profile → **Packages**
- (or) repository view if packages are enabled and visible

---

## Branch rules and PR gating (evidence)

A ruleset is applied to `main` that:
- requires a **Pull Request** before merging
- blocks **force push**
- restricts **deletes**
- requires CI checks to pass before merge

Evidence screenshots:

### PR failing (red)
![PR failing](/evidence/failed_pr.png)

### Checks passing (green)
![Checks passing](/evidence/checks_pass.png)

### Merge enabled
![Merge enabled](/evidence/merge_ready.png)

--- 

## Reproducing the red/green PR demonstration

To reproduce the behavior shown in the screenshots:

1. Create a feature branch and introduce a deliberate quality gate violation (for example, a Checkstyle violation such as an unused import).
2. Push the branch and open a Pull Request into `main`.
3. Observe CI failing and merge being blocked by the ruleset.
4. Fix the violation, push again, and observe CI turning green and merge becoming available.

--- 




