# Panorama Image Management Platform

A panorama and regular image management platform based on SpringBoot, supporting features like automatic panorama image slicing, user registration/login, image upload and sharing, etc.

**Supports displaying very large images (>200MB) (whether it displays properly depends on computer performance).**

## ⚠️ Disclaimer

This project is a personal development project and may contain unknown bugs or some known minor issues that do not affect the core functionality (the author is lazy and hasn't fully fixed them). **It is strongly not recommended for commercial use.** The author is not responsible for any data loss, system failures, or other issues resulting from the use of this project. Please only test and use it outside of production environments.

## Features

* 🖼️ Supports uploading and managing panorama images (2:1 aspect ratio) and regular images.
* 🔧 Integrates Krpano for automatic panorama image slicing.
* 👥 User registration and email verification system.
* 🔐 Login authentication and access control.
* 💾 Supports both SQLite and MySQL databases.
* 📧 Configurable SMTP email service.
* 🌐 Frontend and backend separation architecture (Thymeleaf + Ajax).
* 🖥️ Supports deployment on Windows and Linux systems.

## Tech Stack

* **Backend**: Spring Boot 2.7 + MyBatis
* **Database**: SQLite3 / MySQL 8.0
* **Frontend**: Thymeleaf templating engine + Majestic + Pintereso UI framework
* **Panorama Processing**: Krpano (Windows/Linux versions)
* **Build Tool**: Maven

## Quick Start

### System Requirements

* Java 11 or higher
* MySQL 8.0 (optional) or SQLite3
* Krpano tool (must be obtained separately)

### 1. Database Initialization

#### MySQL Configuration

```sql
CREATE DATABASE img_viewer CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Import initialization script (located in the database directory)
-- source database/img_viewer.sql
```

#### SQLite Configuration

The project already includes an SQLite database file (located at `database/img_viewer.db`), so no additional setup is required.

### 2. Path Configuration

#### Static Resource Path Configuration

When starting the application, you need to specify the static resource directory:

```bash
java -jar ./img_viewer-1.0.2.jar --spring.web.resources.static-locations="classpath:static/,file:/path/to/your/Panorama/"
```

**Note**: Use `/` instead of `\`, and only write the path up to the parent directory of the `upload` directory.

#### Database Path Configuration

In MySQL, configure the resource and upload paths:

```sql
UPDATE `img_viewer`.`config_item` SET `resource_path`="/path/to/your/Panorama/";
UPDATE `img_viewer`.`config_item` SET `upload_path`="/path/to/your/Panorama/uploadfile/";
```

**Note**: The upload path must point to the `uploadfile` directory, and be case-sensitive on Linux systems.

### 3. Krpano Configuration

Specify the installation path of Krpano:

```sql
UPDATE `img_viewer`.`config_item` SET `krpano_home`="/path/to/krpano/";
```

### 4. Database Connection Configuration (Optional)

#### MySQL Connection

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.datasource.url="jdbc:mysql://127.0.0.1:3306/img_viewer?characterEncoding=utf8&serverTimezone=UTC" \
  --spring.datasource.username="root" \
  --spring.datasource.password="your_password"
```

#### SQLite Connection

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.datasource.driver-class-name="org.sqlite.JDBC" \
  --spring.datasource.url="jdbc:sqlite:./sqlite/img_viewer.db"
```

### 5. Email Service Configuration (Optional)

Configure the SMTP email service for user registration verification:

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.mail.host="smtp.your-email-provider.com" \
  --spring.mail.username="your_username" \
  --spring.mail.password="your_password"
```

### 6. Start with Configuration Files

The project provides four preset configuration options:

| Configuration File | Log Level | Database | Default Connection Configuration |
| ------------------ | --------- | -------- | -------------------------------- |
| dev\_mysql         | DEBUG     | MySQL    | 127.0.0.1:3306, root, 12345678   |
| dev\_sqlite        | DEBUG     | SQLite   | Built-in SQLite database         |
| prod\_mysql        | INFO      | MySQL    | 127.0.0.1:3306, root, 12345678   |
| prod\_sqlite       | INFO      | SQLite   | Built-in SQLite database         |

Start with the specified configuration:

```bash
java -jar ./img_viewer-1.0.2.jar --spring.profiles.active="dev_mysql"
```

### 7. Port Configuration

By default, the application listens on port 9999. You can change the port using this parameter:

```bash
java -jar ./img_viewer-1.0.2.jar --server.port=8080
```

## Project Structure

```text
.
├── img_viewer/          # Java project source code
├── database/            # Database files
│   ├── img_viewer.sql    # MySQL initialization script
│   └── img_viewer.db     # SQLite database file
├── script/              # Data processing Python scripts
└── target/
    └── img_viewer-1.0.2.jar     # Compiled executable
```

## Advanced Configuration

More parameters can be configured in the `config_item` table of the database:

* Image compression ratio
* Deletion mode (true/false delete)
* Other system parameters

For detailed configuration, please refer to the `application.yml` file.

## Notes

1. **Path format**: Always use forward slashes `/` as path separators.
2. **Linux deployment**: Be mindful of case sensitivity in file paths.
3. **Krpano License**: Ensure you have a legal Krpano license for use.
4. **Privacy Protection**: The current version of image privacy protection is implemented only on the frontend, so be cautious with sensitive images.

## Project Status

* The current version is the frontend-backend separation version (using Thymeleaf rendering).
* A fully separated version (using Vue3 for the frontend) exists, but due to some bugs and incomplete mobile adaptation, it has not been open-sourced yet.
* The project may contain some unknown bugs and minor issues. It is not recommended for use in production environments.

## Inspiration

This project was inspired by panorama image sharing platforms such as DJI Sky City and 720 Cloud.

## License

This project is for personal use and learning only. Commercial use is prohibited without permission.

## Get Help

If you have any issues, please submit an Issue or contact the project maintainer. Please note that as this is a personal project, response times may be longer.

---

**Reminder**: Do not use this project in a production environment. The author is not responsible for any consequences from using it.
Here's the translation of your README file into English (US):

---

# Panorama Image Management Platform

A panorama and regular image management platform based on SpringBoot, supporting features like automatic panorama image slicing, user registration/login, image upload and sharing, etc.

**Supports displaying very large images (>200MB) (whether it displays properly depends on computer performance).**

## ⚠️ Disclaimer

This project is a personal development project and may contain unknown bugs or some known minor issues that do not affect the core functionality (the author is lazy and hasn't fully fixed them). **It is strongly not recommended for commercial use.** The author is not responsible for any data loss, system failures, or other issues resulting from the use of this project. Please only test and use it outside of production environments.

## Features

* 🖼️ Supports uploading and managing panorama images (2:1 aspect ratio) and regular images.
* 🔧 Integrates Krpano for automatic panorama image slicing.
* 👥 User registration and email verification system.
* 🔐 Login authentication and access control.
* 💾 Supports both SQLite and MySQL databases.
* 📧 Configurable SMTP email service.
* 🌐 Frontend and backend separation architecture (Thymeleaf + Ajax).
* 🖥️ Supports deployment on Windows and Linux systems.

## Tech Stack

* **Backend**: Spring Boot 2.7 + MyBatis
* **Database**: SQLite3 / MySQL 8.0
* **Frontend**: Thymeleaf templating engine + Majestic + Pintereso UI framework
* **Panorama Processing**: Krpano (Windows/Linux versions)
* **Build Tool**: Maven

## Quick Start

### System Requirements

* Java 11 or higher
* MySQL 8.0 (optional) or SQLite3
* Krpano tool (must be obtained separately)

### 1. Database Initialization

#### MySQL Configuration

```sql
CREATE DATABASE img_viewer CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Import initialization script (located in the database directory)
-- source database/img_viewer.sql
```

#### SQLite Configuration

The project already includes an SQLite database file (located at `database/img_viewer.db`), so no additional setup is required.

### 2. Path Configuration

#### Static Resource Path Configuration

When starting the application, you need to specify the static resource directory:

```bash
java -jar ./img_viewer-1.0.2.jar --spring.web.resources.static-locations="classpath:static/,file:/path/to/your/Panorama/"
```

**Note**: Use `/` instead of `\`, and only write the path up to the parent directory of the `upload` directory.

#### Database Path Configuration

In MySQL, configure the resource and upload paths:

```sql
UPDATE `img_viewer`.`config_item` SET `resource_path`="/path/to/your/Panorama/";
UPDATE `img_viewer`.`config_item` SET `upload_path`="/path/to/your/Panorama/uploadfile/";
```

**Note**: The upload path must point to the `uploadfile` directory, and be case-sensitive on Linux systems.

### 3. Krpano Configuration

Specify the installation path of Krpano:

```sql
UPDATE `img_viewer`.`config_item` SET `krpano_home`="/path/to/krpano/";
```

### 4. Database Connection Configuration (Optional)

#### MySQL Connection

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.datasource.url="jdbc:mysql://127.0.0.1:3306/img_viewer?characterEncoding=utf8&serverTimezone=UTC" \
  --spring.datasource.username="root" \
  --spring.datasource.password="your_password"
```

#### SQLite Connection

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.datasource.driver-class-name="org.sqlite.JDBC" \
  --spring.datasource.url="jdbc:sqlite:./sqlite/img_viewer.db"
```

### 5. Email Service Configuration (Optional)

Configure the SMTP email service for user registration verification:

```bash
java -jar ./img_viewer-1.0.2.jar \
  --spring.web.resources.static-locations="classpath:static/,file:/path/to/Panorama/" \
  --spring.mail.host="smtp.your-email-provider.com" \
  --spring.mail.username="your_username" \
  --spring.mail.password="your_password"
```

### 6. Start with Configuration Files

The project provides four preset configuration options:

| Configuration File | Log Level | Database | Default Connection Configuration |
| ------------------ | --------- | -------- | -------------------------------- |
| dev\_mysql         | DEBUG     | MySQL    | 127.0.0.1:3306, root, 12345678   |
| dev\_sqlite        | DEBUG     | SQLite   | Built-in SQLite database         |
| prod\_mysql        | INFO      | MySQL    | 127.0.0.1:3306, root, 12345678   |
| prod\_sqlite       | INFO      | SQLite   | Built-in SQLite database         |

Start with the specified configuration:

```bash
java -jar ./img_viewer-1.0.2.jar --spring.profiles.active="dev_mysql"
```

### 7. Port Configuration

By default, the application listens on port 9999. You can change the port using this parameter:

```bash
java -jar ./img_viewer-1.0.2.jar --server.port=8080
```

## Project Structure

```text
.
├── img_viewer/          # Java project source code
├── database/            # Database files
│   ├── img_viewer.sql    # MySQL initialization script
│   └── img_viewer.db     # SQLite database file
├── script/              # Data processing Python scripts
└── target/
    └── img_viewer-1.0.2.jar     # Compiled executable
```

## Advanced Configuration

More parameters can be configured in the `config_item` table of the database:

* Image compression ratio
* Deletion mode (true/false delete)
* Other system parameters

For detailed configuration, please refer to the `application.yml` file.

## Notes

1. **Path format**: Always use forward slashes `/` as path separators.
2. **Linux deployment**: Be mindful of case sensitivity in file paths.
3. **Krpano License**: Ensure you have a legal Krpano license for use.
4. **Privacy Protection**: The current version of image privacy protection is implemented only on the frontend, so be cautious with sensitive images.

## Project Status

* The current version is the frontend-backend separation version (using Thymeleaf rendering).
* A fully separated version (using Vue3 for the frontend) exists, but due to some bugs and incomplete mobile adaptation, it has not been open-sourced yet.
* The project may contain some unknown bugs and minor issues. It is not recommended for use in production environments.

## Inspiration

This project was inspired by panorama image sharing platforms such as DJI Sky City and 720 Cloud.

## License

This project is for personal use and learning only. Commercial use is prohibited without permission.

## Get Help

If you have any issues, please submit an Issue or contact the project maintainer. Please note that as this is a personal project, response times may be longer.

---

**Reminder**: Do not use this project in a production environment. The author is not responsible for any consequences from using it.
