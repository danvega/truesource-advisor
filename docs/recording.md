# Long-form video: from an advisory to a tested application update

Audience: application and platform teams. The main screen demo is a live Application Advisor run. The commercial hotfix is a short, separate example of supported remediation.

## 1. Animate the problem

Show an application pulling in Spring, its dependencies, and its runtime. Introduce an advisory beside a dependency, then show the work it creates: identify the version, find a supported update, change the build, review the diff, test the application, and prepare the release.

Suggested narration: “A security advisory doesn't stop at the library. Someone still has to work out what the application uses, find the right update, and get that change through testing. That's the part I want to walk through.”

## 2. Define TrueSource before the terminal

Use a simple portfolio graphic: Spring Enterprise, Trusted Artifacts, and Data Services. Explain TrueSource as Broadcom's portfolio of commercially supported open-source software. Narrow the walkthrough to Spring Enterprise and Application Advisor.

Suggested narration: “TrueSource brings Broadcom's open-source offerings together under one enterprise standard. For this walkthrough, I'm focusing on the Spring side: supported software and the tooling that helps application teams put updates to work.”

The portfolio positioning is based on the provided product brief. Confirm final brand wording before publication. Keep quantitative coverage and build-assurance claims out of this hands-on demonstration unless their scope and evidence have been established.

## 3. Establish the application baseline

Open `pom.xml`, the order controller, and the two HTTP tests. Show Boot 3.5.0 on `advisor-before`, then create a new recording branch and run `mvn clean verify`.

Suggested narration: “Here's a small service with two checks for the behavior we care about in this example. Before touching the dependencies, I want to know the starting point works.”

## 4. Run Application Advisor visibly

Run `advisor build-config get`, then `advisor upgrade-plan get`. Briefly explain the major-version plan. Run `advisor patch apply` for the change being demonstrated. Leave the actual Advisor output visible, then open `git diff -- pom.xml` in the terminal or IDE.

Suggested narration: “Advisor can show the larger upgrade path. For this take, I'm applying dependency patches on the current Boot release line. Let's look at exactly what it changed.”

Show the parent version and dependency-management changes. Explain that this run changes multiple libraries. It should not be described as a CVE-only commercial patch. If useful, show `.advisor/patch-upgrade-report.json` in the IDE after checking it for credentials and local account information.

## 5. Test the result

Run `mvn clean verify`, launch the JAR directly, and make the two curl requests from another terminal. Show the successful response and the 404. Stop the server before switching branches.

Suggested narration: “The changes are visible and reviewable. These checks still pass, and the service returns the same responses. A real application would need its own test suite and release process here.”

## 6. Show a commercial fix as a separate example

Show the September 2025 Spring security announcement. Label the segment “Historical commercial hotfix example.” Switch between `commercial-before` and `commercial-after`, show the one-line parent diff, and show Framework 6.1.22 changing to 6.1.23 in the dependency trees. Run the tests on the after branch. The README has all commands.

Suggested narration: “Separately, here's a published example of a commercial hotfix. The Boot release line stays at 3.3, while its managed Framework version changes. I'm consuming that fix from the enterprise repository and testing the application against it.”

Show sanitized `docs/evidence/enterprise-access.json` if repository sourcing matters to the story. This application does not reproduce the advisory's affected method-security setup; do not describe it as an exploit being fixed. Repository-origin records establish sourcing, rather than SLSA verification.

## 7. Close on the customer next step and logo

Suggested narration: “For application and platform teams, the useful question is how a supported update becomes a reviewed, tested application change. That's the workflow we've looked at here.”

Show the customer next-step link from the README, then finish on an approved TrueSource by Broadcom logo asset supplied by the brand team.
