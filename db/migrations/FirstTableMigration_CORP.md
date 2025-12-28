# FirstTableMigration_CORP

This document records the initial COPR table migration and how to run the local Postgres test instance for KIDSCLUB.

Files added for the first-table migration (location in repo):
- `db/schema/copr/copr_schema.sql` — canonical DDL for COPR tables (reference)
- `db/schema/copr/README.md` — mapping notes and choices made
- `db/docker-entrypoint-initdb.d/001_create_copr.sql` — initialization SQL that creates the three COPR tables on container startup
- `db/migrations/README.md` — migration guidance for adding future migration files
- `docker-compose.yml` — docker-compose to run Postgres locally for development
- `.env.example` — example environment variables for the Postgres container

Quickstart (local development)
1. Copy `.env.example` to `.env` and update `POSTGRES_PASSWORD` if desired.
2. Start Postgres:
   - `docker-compose up -d`
3. Connect to the database (example using psql):
   - `psql "host=localhost port=$POSTGRES_PORT dbname=$POSTGRES_DB user=$POSTGRES_USER"`
4. The COPR tables are created automatically the first time the container initializes.  
   - Tables: `copr_changers`, `copr_paidouts`, `copr_sales`.

Adding more tables / next migrations
- Add new SQL migration files to `db/migrations/` using a clear versioned filename (e.g., `V002__create_members.sql`) or add one-off init scripts to `db/docker-entrypoint-initdb.d/` if you want them applied at first initialization only.
- To apply migrations to an already running DB, either run the SQL with `psql` or add a migration tool (Flyway, Liquibase) later and commit its config.

Notes and recommendations
- Current default Postgres credentials are in `.env.example`. Change the password before using in any shared environment.
- Files are intentionally committed to `master` for easy retrieval during the migration. If you prefer to stage work in feature branches, create branches like `migrate/copr-schema`.
- If you want me to add Flyway and make migrations idempotent and versioned, I can add that next.

Contact / owner info
- Author: Dean <newkirkdean@gmail.com> (GitHub: newkirkdean-hub)

---