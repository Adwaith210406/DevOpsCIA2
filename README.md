# Course Registration — DevOps Lab Assessment (Set F)

## Project Overview

This repository contains the Course Registration application used for the DevOps lab assessment. The project demonstrates a Maven multi-module build, JUnit testing, a Jenkins Declarative Pipeline, and a Kubernetes deployment using Minikube and Nginx.

> **Evidence note:** Record only commands and results that you actually ran. Do not describe a task as completed until you have verified it and saved the corresponding evidence.

## Project Structure

```text
course-registration/
├── pom.xml
├── Jenkinsfile
├── deployment.yaml
├── service.yaml
├── README.md
├── evidence/
└── course-registration-app/
    ├── pom.xml
    └── src/
        ├── main/java/com/university/
        └── test/java/com/university/
```

Some files/directories may not exist yet. Keep this structure section aligned with the files actually present in your repository.

## Requirements

- Java 17 for compiling the application.
- Maven.
- Git.
- Jenkins for running the pipeline.
- Docker and Minikube for the Kubernetes task.
- `kubectl` for interacting with the Kubernetes cluster.

## Task 1 — Git Workflow

The work should be developed using a feature branch, meaningful commits, and integration into `main`.

Useful verification commands:

```bash
git status
git branch
git log --oneline --graph --decorate -10
```

Record the branch name and the commits you actually created. Confirm the working tree is clean before submission:

```bash
git status
```

## Task 2 — Maven Multi-Module Build

The root `pom.xml` is the parent project and should use `pom` packaging. The child module is `course-registration-app`, which builds the application JAR. The parent manages shared build configuration and the JUnit 5 version.

Run from the directory containing the root `pom.xml`:

```bash
mvn clean test
mvn package
```

Verify the build results and test reports:

```bash
find course-registration-app/target/surefire-reports -maxdepth 1 -type f -name 'TEST-*.xml' -print
find course-registration-app/target -maxdepth 1 -type f -name '*.jar' -print
```

**Actual result:** _Fill in after running the commands. Do not claim success unless Maven exits successfully._

## Task 3 — Course Capacity Boundary

The registration service must reject registration when a course is already full and allow registration when exactly one seat remains. A rejected registration must not change the student's registrations or credits, or the course's enrollment count.

Focused tests should verify:
- A full course rejects a new registration.
- A course with one remaining seat accepts the registration.
- A rejected attempt leaves state unchanged.

Run the tests with:

```bash
mvn clean test
```

**Actual result:** _Record the test count and pass/fail result from Maven output._

## Task 4 — Registration Audit Log (Set F Change Request)

The registration service should maintain an in-memory audit log. Each successful registration adds one entry identifying the student and course. Rejected registrations must not add entries, and a later rejected attempt must not erase or duplicate earlier successful entries.

Focused tests should cover successful logging, rejected registration without a log entry, and preservation of earlier entries after a later failure.

Run:

```bash
mvn clean test
```

**Actual result:** _Record the test count and pass/fail result from Maven output._

## Task 5 — Jenkins Declarative Pipeline

The `Jenkinsfile` should be stored at the repository root. It should use Linux `sh` steps and run Maven from the repository root.

Expected pipeline behavior:
1. Check out the source repository.
2. Run `mvn clean test`.
3. Run `mvn package` only if the test stage succeeds.
4. Publish JUnit reports from `course-registration-app/target/surefire-reports/TEST-*.xml`.
5. Archive JAR artifacts from `course-registration-app/target/*.jar` only after a successful pipeline.

After running the Jenkins job, capture the dashboard/build result, console output, test report, and archived artifact where available.

**Actual result:** _Record whether the pipeline was run in Jenkins. If it was not run, state that clearly._

## Task 6 — Minikube and Nginx Kubernetes Deployment

The Kubernetes task uses an Nginx Deployment and a NodePort Service.

### Deployment requirements

- Image: `nginx:1.27`
- Initial replicas: `2`
- Service type: `NodePort`
- NodePort: `30080`
- Scale the Deployment to `3` replicas.
- Delete one managed Pod and verify that Kubernetes recreates it.

### Apply the manifests

Run these commands from the directory containing `deployment.yaml` and `service.yaml`:

```bash
minikube start --driver=docker
kubectl get nodes
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl rollout status deployment/nginx-deployment
kubectl get deployments
kubectl get pods -o wide
kubectl get services
```

Test Nginx using the URL printed by:

```bash
minikube service nginx-service --url
```

Use the actual URL printed by Minikube when testing with `curl`.

### Scale to three replicas

```bash
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get deployment nginx-deployment
kubectl get pods -o wide
```

### Demonstrate self-healing

First list the Pods and copy the name of one Nginx Pod:

```bash
kubectl get pods
```

Delete that actual Pod by name:

```bash
kubectl delete pod <actual-pod-name>
kubectl get pods -w
```

Press `Ctrl+C` after observing the replacement Pod, then verify:

```bash
kubectl get deployment nginx-deployment
kubectl get pods
```

The Deployment should return to three available replicas. Record only the behavior observed in your cluster.

### Save command output as evidence

After the relevant commands have succeeded:

```bash
mkdir -p evidence
kubectl get nodes > evidence/kubernetes-nodes.txt
kubectl get deployments -o wide > evidence/deployments.txt
kubectl get services > evidence/services.txt
kubectl get pods -o wide > evidence/pods.txt
kubectl describe deployment nginx-deployment > evidence/nginx-deployment-details.txt
```

These files contain command output, not screenshots. Also capture genuine screenshots showing the initial two replicas, the scaled three replicas, and the Pod replacement/self-healing behavior. Do not create evidence files from expected or invented output.

**Actual result:** _Record whether Minikube started, whether Nginx responded, and whether scaling and self-healing were observed._

## Evidence Checklist

Update this checklist as you collect real evidence:

- [ ] Git branch/commit history and clean working tree.
- [ ] Successful `mvn clean test` output and JUnit XML reports.
- [ ] Successful `mvn package` output and generated JAR.
- [ ] Tests for the course-capacity boundary.
- [ ] Tests for audit-log behavior.
- [ ] Jenkins pipeline configuration and build result, if run.
- [ ] Jenkins JUnit report and archived JAR, if produced.
- [ ] Kubernetes Deployment and Service YAML files.
- [ ] Evidence of two initial Nginx replicas.
- [ ] Evidence of three replicas after scaling.
- [ ] Evidence that a deleted Pod was replaced.
- [ ] Nginx HTTP response, if successfully tested.

## Submission Notes

- Run commands from the directory containing the root `pom.xml`, unless a command explicitly says otherwise.
- Include only genuine output and screenshots from your own environment.
- Replace all `_Actual result_` prompts with your observed results.
- Remove any checklist items that are not applicable, or leave them unchecked and explain what could not be completed.
- Before submission, review the repository:

```bash
git status
git log --oneline --graph --decorate -10
```
