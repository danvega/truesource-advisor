# TrueSource Spring video demo

One Spring Boot order service, with its before and after states on Git branches. Run Maven and Broadcom Application Advisor directly in the terminal during the recording.

## Branches

| Branch | What it shows |
| --- | --- |
| `main` | Starting application: Boot 3.5.0 |
| `codex/advisor-before` | Same starting application, ready for a live Advisor run |
| `codex/advisor-after` | Saved result produced by `advisor patch apply`: Boot 3.5.17 |
| `codex/commercial-before` | Historical commercial example: Boot 3.3.15 |
| `codex/commercial-after` | Same application using commercial hotfix Boot 3.3.15.1 |

Every branch has `pom.xml` and `src/` at the repository root. Application source and tests stay the same; dependency changes live in the POM. There is no remote configured.

## Prerequisites

Use Java 17 or 21, Maven, and installed Application Advisor. The rehearsal used Java 21 and Advisor 1.6.7. On this Mac, select Java 21 in your terminal with:

```bash
sdk use java 21.0.6-oracle
java -version
mvn -version
advisor --version
```

Maven uses your existing external `~/.m2/settings.xml`. Its server ID must match `spring-enterprise` in the POM. Keep repository credentials outside this repo and off screen.

## Live Advisor walkthrough

Start a new branch for each take; choose a new name when repeating the recording. Git will preserve the saved before and after branches.

```bash
cd ~/Downloads/truesource-advisor
git switch codex/advisor-before
git switch -c codex/recording-take-1
mvn clean verify
advisor build-config get
advisor upgrade-plan get
advisor patch apply
git diff -- pom.xml
mvn clean verify
java -jar target/orders-demo-0.0.1-SNAPSHOT.jar --server.port=18080
```

`upgrade-plan get` shows the available major-version path. `patch apply` performs the patch-level dependency changes demonstrated here. The major-version plan is not applied in this walkthrough. The Advisor catalog can change, so a later run may select different versions.

In another terminal:

```bash
curl -i http://127.0.0.1:18080/api/orders/1001
curl -i http://127.0.0.1:18080/api/orders/9999
```

Expect 200 with `{"id":"1001","total":"42.00","currency":"USD"}` and 404 for the unknown order. Stop the server with Ctrl C.

To preserve the take after reviewing its diff:

```bash
git add pom.xml
git commit -m "Apply Application Advisor dependency patches"
```

Advisor's generated reports live in ignored `.advisor/`. Build output lives in ignored `target/`. Keep useful sanitized recording evidence in `docs/` if you want to commit it.

## Inspect the saved Advisor result

```bash
git diff codex/advisor-before..codex/advisor-after -- pom.xml
git switch codex/advisor-after
mvn clean verify
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.1:tree '-Dincludes=org.springframework:*,org.apache.tomcat.embed:*'
```

The saved rehearsal changed Boot 3.5.0 to 3.5.17, Framework 6.2.7 to 6.2.20, and Tomcat 10.1.41 to 10.1.60. Advisor reported 41 dependency updates in one POM, including an Awaitility change from 4.3.0 to 4.2.2. Review the complete diff: this is a broader dependency patching example, and every reported update is not necessarily a version increase or security fix.

## Historical commercial hotfix walkthrough

This is a separate example of consuming a commercial fix. Its parent version was changed explicitly; it is not the result of the Advisor run above.

```bash
git switch codex/commercial-before
mvn clean verify
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.1:tree '-Dincludes=org.springframework:*,org.apache.tomcat.embed:*'
git diff codex/commercial-before..codex/commercial-after -- pom.xml
git switch codex/commercial-after
mvn clean verify
mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.1:tree '-Dincludes=org.springframework:*,org.apache.tomcat.embed:*'
java -jar target/orders-demo-0.0.1-SNAPSHOT.jar --server.port=18080
```

Use the same two curl requests. Always rebuild after switching branches before running the JAR; `target/` does not switch with Git.

The only POM change between the commercial branches is Boot 3.3.15 to 3.3.15.1. Framework moves from 6.1.22 to 6.1.23; Tomcat stays at 10.1.44. This is a documented September 2025 hotfix associated with CVE-2025-41248 and CVE-2025-41249, used as a historical demonstration rather than a current production version recommendation. The sample does not use the affected generic method-security configuration, so it demonstrates commercial artifact consumption and tested application behavior.

## Evidence

`docs/evidence/` contains sanitized results from the initial rehearsal, including dependency trees, test output, Advisor changes, and commercial repository-origin records. The original rehearsal used Boot 3.5.16; the new run selected 3.5.17. Files prefixed with `repo-` capture the current branch-based rehearsal. Other log paths and diff labels refer to the earlier folder layout. `docs/evidence/repo-rehearsal.json` records the subsequent checks in this branch-based repo.

The two HTTP tests check the expected order response and the unknown-order 404. They do not establish every application behavior or reproduce a security exploit. Fresh Maven resolution established enterprise sourcing for the commercial parent, BOM and selected Framework artifacts; it did not verify SLSA provenance.

For the security context of the Advisor example, Tomcat 10.1.41 is in the affected version range for CVE-2025-48988 and 10.1.60 is beyond its published fix. This service has no multipart upload endpoint, so dependency version exposure alone does not demonstrate application exploitability.

See [the recording outline](docs/recording.md) for the screen sequence and narration.

## References

- [Advisor upgrade workflow](https://knowledge.broadcom.com/external/article/454737/upgrading-spring-boot-applications-using.html)
- [Advisor dependency patching](https://knowledge.broadcom.com/external/article/453533/managing-spring-boot-transitive-dependen.html)
- [Published commercial hotfix](https://spring.io/blog/2025/09/15/spring-framework-and-spring-security-fixes-for-CVE-2025-41249-and-CVE-2025-41248/)
- [Tomcat security advisories](https://tomcat.apache.org/security-10.html)
- [TrueSource article and customer next step](https://www.broadcom.com/company/news/articles/vmware-explore/speed-is-the-problem-quality-early-access-truesource-broadcom)
