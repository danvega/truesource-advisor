# TrueSource Spring Enterprise patch demo

A small Spring Boot order service that shows how to take an enterprise-only Spring fix with Application Advisor, without leaving the release line you run.

## The scenario

Spring Boot 3.5 left open-source support in June 2026. The last open-source releases are Boot 3.5.16 and Spring Framework 6.2.19.

On August 20, 2026, Spring published a batch of Spring Framework advisories. For the 6.2 line, the fixed version is **6.2.20, Enterprise Support Only**. One example is [CVE-2026-59281](https://spring.io/security/cve-2026-59281). Spring Enterprise supplies that fix. Application Advisor applies it to the build.

This app does not use the features those advisories describe. The demo shows how a fix gets into an application, not an exploit.

## Branches

| Branch | Boot | Framework | Purpose |
| --- | --- | --- | --- |
| `patch-before` | 3.5.16 | 6.2.19 | Start here. The last open-source 3.5 release |
| `patch-after` | 3.5.17 | 6.2.20 | The result of `advisor patch apply` |

Only `pom.xml` differs between the two branches. The source and tests are the same.

## Prerequisites

- JDK 21. `.sdkmanrc` selects it, so run `sdk env` in the project directory.
- Maven 3.9 or later.
- [Application Advisor](https://knowledge.broadcom.com/external/article/454737/upgrading-spring-boot-applications-using.html) CLI. The demo was recorded with 1.6.7.
- A Spring Enterprise entitlement. Put your credentials in `~/.m2/settings.xml` under a server with the ID `spring-enterprise`. Never commit them.

## Run the demo

Start from `patch-before` on a new branch:

```bash
git switch patch-before
git switch -c my-patch-run
```

Check the baseline:

```bash
mvn dependency:tree -Dincludes=org.springframework:spring-webmvc
mvn clean verify
```

Apply the patch updates:

```bash
advisor patch apply
```

Advisor reads the build and moves each dependency to its latest patch release on the same line. That includes enterprise releases from the `spring-enterprise` repository. It edits `pom.xml` in place. This takes a few minutes.

Review the change:

```bash
git diff -- pom.xml
mvn dependency:tree -Dincludes=org.springframework:spring-webmvc
cat ~/.m2/repository/org/springframework/spring-webmvc/6.2.20/_remote.repositories
```

The last command shows which repository Maven downloaded the artifact from. Look for `spring-enterprise`.

Test it and run it:

```bash
mvn clean verify
java -jar target/orders-demo.jar
```

In a second terminal:

```bash
curl -i http://127.0.0.1:18080/api/orders/1001
curl -i http://127.0.0.1:18080/api/orders/9999
```

You should get a 200 with `{"id":"1001","total":"42.00","currency":"USD"}`, then a 404. Stop the app with Ctrl+C.

## What changed

The recorded run on October 6, 2026 reported 33 dependency changes in one file:

| Dependency | Before | After |
| --- | --- | --- |
| Spring Boot | 3.5.16 | 3.5.17 |
| Spring Framework | 6.2.19 | 6.2.20 |
| Tomcat | 10.1.55 | 10.1.60 |
| Jackson | 2.21.4 | 2.21.7 |
| Logback | 1.5.34 | 1.5.38 |

Advisor changed the parent version. It also added a `dependencyManagement` block that pins Tomcat, Jackson and SLF4J. The Spring artifacts came from `spring-enterprise`. Libraries like Tomcat and Jackson are public releases from Maven Central.

`advisor patch apply` is general dependency maintenance, not a vulnerability scan. A later run may pick newer versions as new releases ship.

## References

- [Spring security advisories](https://spring.io/security)
- [Spring Boot support timeline](https://spring.io/projects/spring-boot#support)
- [How Advisor uses enterprise releases](https://knowledge.broadcom.com/external/article/454519/how-tanzu-spring-application-advisor-inc.html)
- [TrueSource by Broadcom](https://www.broadcom.com/company/news/articles/vmware-explore/speed-is-the-problem-quality-early-access-truesource-broadcom)
