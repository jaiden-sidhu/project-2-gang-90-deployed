# POS System for Sharetea

## Introduction

This is a repository that contains both the frontend and backend required to run a POS system for the sale of drinks in a store such as Sharetea. 

Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

## Setup

1. **Clone the repository**

HTTPS:
```bash
git clone https://github.com/CSCE331-Fall2025-900-911/project-2-gang-90.git
cd project-2-gang-90
```

SSH:
```bash
git clone git@github.com:CSCE331-Fall2025-900-911/project-2-gang-90.git
cd project-2-gang-90
```

---

### Database setup (Python scripts)

This project works on Python 3.13.7 for database setup scripts. However, other versions of python should work.

It is reccomended to use [pyenv](https://github.com/pyenv/pyenv) to manage Python versions:

```bash
# Install pyenv if you don't have it
curl https://pyenv.run | bash

# MacOS
brew install pyenv

# Install the required Python version
pyenv install 3.13.7

# Set local Python version for the project
pyenv local 3.13.7
```

Alternatively, ensure your system Python version is 3.13.7:

```bash
python3 --version  # Should output Python 3.13.7
```

Set up a virtual environment and install dependencies:

```bash
python3 -m venv venv
source venv/bin/activate  # On Windows use `venv\Scripts\activate`
pip install --upgrade pip
pip install -r requirements.txt
```

> **Note:** This Python environment is only needed for populating the database.

---

### Java Application Setup

This section covers how to build and run the Java portion of the POS system, which powers the frontend and backend logic of the application using **JavaFX**, **JDBC**, and **HikariCP** for efficient database connectivity.

---

#### Prerequisites
- **JDK 17 or 21 (ARM64 for Apple Silicon)**  
  Check your version:
  ```bash
  java -version
  ```
  Make sure it outputs `aarch64` (Apple Silicon) instead of `x86_64` (Intel).
- **Maven 3.8+**

---

#### 1. Configure the Database
Before running the application, update your database credentials in:

```
pos-app/src/main/resources/application.properties
```

Example configuration:
```properties
db.url=jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/gang_90_db
db.user=gang_90
db.password=your_password_here
```

> 💡 Tip: For better security, store credentials as environment variables and load them in `Database.java` instead of hardcoding.

---

#### 2. Build the Project
From the root of the repository:
```bash
cd pos-app
mvn clean install
```

If everything compiles successfully, you’ll see:
```
BUILD SUCCESS
```

The generated JAR file will be located at:
```
pos-app/target/pos-app-1.0.0.jar
```

---

#### 3. Run the Application
To run the JavaFX interface:
```bash
mvn javafx:run
```

Alternatively, if your JAR has a defined `Main-Class`:
```bash
java -jar target/pos-app-1.0.0.jar
```

---

#### 4. Understanding Maven vs. DAO Execution
Maven handles **building, compiling, testing, and launching** your project.  
It does **not** directly run any of your DAO (Data Access Object) logic.  

Your DAOs execute **within the application** when called by JavaFX controllers or service classes.

Example:
```java
ProductDAO dao = new ProductDAO();
List<Product> products = dao.listAll();  // Executes a SQL query via JDBC
tableView.getItems().setAll(products);
```

---

#### 5. Project Structure
```
pos-app/
├─ src/main/java/edu/tamu/project2/csce331/
│  ├─ App.java                 # JavaFX entry point
│  ├─ controller/...           # JavaFX controllers (UI logic)
│  ├─ dao/...                  # DAO classes (SQL operations)
│  └─ Database.java            # HikariCP configuration and connection pool
├─ src/main/resources/
│  ├─ application.properties   # DB configuration
│  ├─ *.fxml                   # JavaFX UI files
│  └─ *.css                    # JavaFX styling
├─ pom.xml
└─ target/                     # Build output (auto-generated)
```

---

#### 6. Troubleshooting

**Apple Silicon (M1/M2/M3) JavaFX Issues**
- Use JavaFX **22.0.2** or later for ARM64 compatibility.
- Clear stale caches:
  ```bash
  rm -rf ~/.openjfx/cache
  ```
- Verify your JDK is ARM-based (not running under Rosetta).

**“Package not visible” module errors**
If you encounter module access issues, update your `src/main/java/module-info.java`:
```java
module edu.tamu.project2.csce331 {
    requires java.sql;
    requires com.zaxxer.hikari;
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.tamu.project2.csce331 to javafx.fxml;
    exports edu.tamu.project2.csce331;
}
```

**Suppressing native access warnings**
In your `pom.xml`, inside the JavaFX plugin, add:
```xml
<jvmArgs>
    <jvmArg>--enable-native-access=javafx.graphics</jvmArg>
</jvmArgs>
```

---

#### 7. Git Hygiene
Add a `.gitignore` file to prevent committing build artifacts and IDE files:
```
target/
*.class
*.log
.mvn/wrapper/maven-wrapper.jar
.idea/
.vscode/
```

---

#### Summary
Maven compiles, builds, and launches the JavaFX application.  
Your DAOs handle all database logic **at runtime**—they are invoked by controllers or backend services, not directly by Maven.  
This structure keeps the project modular, testable, and production-ready.


