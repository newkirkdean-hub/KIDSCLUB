# Database migrations for KIDSCLUB

- Place one-off initialization SQL in db/docker-entrypoint-initdb.d/ (runs on first container start).
- Place subsequent SQL migration files in db/migrations/ (use a naming convention like V001__create_<table>.sql).
- To apply new migrations to a running DB, you can:
  - run the SQL manually with `psql`, or
  - add migration tooling (Flyway/Liquibase) later.
