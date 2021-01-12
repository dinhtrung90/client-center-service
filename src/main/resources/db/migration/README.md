# File name format:

```
V<timestamp>__name-of-the-migration.sql

```

Notice the double underscore character. It's a must

# To generate a new file

- CD to this directory
- Run the following command to create the file:

```bash
touch "src/main/resources/db/migration/V$(date +'%Y%m%d%H%M%S')__name-of-the-migration.sql"
```

# Explain

When the API server starts, all SQL files in this folder will be executed in the order of file name.
Every file is only run once (e.g. in the next start, it won't be run)
