# Logging

Lib with [logback.xml](./main/resources/logback.xml) with best practices. Add as dependency in your
project and use it.

Then, you do not need to create your own `logback.xml`.

## How to Use

```xml
<dependency>
  <groupId>com.lib.java</groupId>
  <artifactId>logging</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Configuration

Configuration can be made using environment variables:

- `LOG_ROOT_APPENDER_REF` _(optional)_: default root log appender.
  - default value: `STDOUT-JSON`.
  - available values: `STDOUT-JSON`, `STDOUT-TEXT`.
- `LOG_ROOT_LEVEL` _(optional)_: default level for root logger.
  - default value: `WARN`.
  - available values: `TRACE`, `DEBUG`, `INFO`, `WARN`  ou `ERROR`.
- `LOG_TEXT_PATTERN` _(optional)_: define the pattern for text log records.
