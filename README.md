# GitHub Activity CLI

A simple Java CLI application that fetches and displays recent public GitHub activity for any user using the GitHub REST API.

---

## Features

- Fetch recent public GitHub activity
- Display multiple event types:
  - PushEvent
  - WatchEvent
  - PullRequestEvent
  - IssuesEvent
  - ForkEvent
  - CreateEvent
  - DeleteEvent
- Accept username from terminal arguments
- Optional limit for number of events
- Error handling for invalid users and API failures
- Fat JAR packaging with Maven

---

## Technologies Used

- Java 17+
- Maven
- Java HttpClient
- Jackson Databind
- GitHub REST API

---

## Project Structure

```text
src/
└── main/
    └── java/
        └── org/
            └── example/
                └── GitHubActivity.java
```

---

## Build the Project

Run:

```bash
mvn clean package
```

This generates:

```text
target/github-cli-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## Run the Application

### Basic Usage

```bash
java -jar target/github-cli-1.0-SNAPSHOT-jar-with-dependencies.jar torvalds
```

### With Event Limit

```bash
java -jar target/github-cli-1.0-SNAPSHOT-jar-with-dependencies.jar torvalds 5
```

---

## Example Output

```text
Output (Showing up to 5 events):

- Pushed to torvalds/linux on branch master
- Starred torvalds/subsurface
- Opened a pull request in torvalds/linux
- Created branch in torvalds/test-repo
- Forked torvalds/linux to user/linux-fork
```

---

## Create a Global Terminal Alias (Windows)

### 1. Create Folder

```text
C:\cli-tools
```

### 2. Create File

```text
github-activity.bat
```

### 3. Add This Content

```bat
@echo off
java -jar "D:\Training Programs\Spring-tutorial\github-cli\target\github-cli-1.0-SNAPSHOT-jar-with-dependencies.jar" %*
```

### 4. Add Folder to PATH

Add:

```text
C:\cli-tools
```

to Windows Environment Variables → Path.

### 5. Restart Terminal

Now run globally:

```bash
github-activity torvalds 5
```

---

## API Used

GitHub Public Events API:

```text
https://api.github.com/users/{username}/events/public
```

---

## Notes

- Only public GitHub activity is returned.
- Some PushEvent payloads no longer include commit details due to GitHub API changes.
- The application uses defensive JSON parsing with Jackson `JsonNode`.

---

## Future Improvements

- Colored terminal output
- GitHub authentication token support
- Interactive CLI mode
- Filtering by event type
- Pagination support
- Better formatting and timestamps
- Native executable with GraalVM

---

## License

MIT
