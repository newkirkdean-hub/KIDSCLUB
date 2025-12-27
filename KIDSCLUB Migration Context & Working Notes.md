
Version: 0.1  
Last updated: 2025-12-26  
Author: Dean <newkirkdean@gmail.com> (GitHub: newkirkdean-hub)

Change log:
- 2025-12-26 — initial v0.1 — added ALL_CAPS convention and clarified Git branches

## Purpose / top-level overview
- "KIDSCLUB" is a small, local network Java/FX program that reads/writes a Microsoft Access DB for managing customer records, invoices, time clock, inventory, sales, maintenance, and simple reporting.[...]

## Overall goal 
- To migrate "KIDSCLUB" to a local network web service + PostgreSQL while keeping user functionality and appearance as close to the same as is current. Migrate corp2 into same source files as the rest[...]
- Target cutover June, 2026
- there should not be any down time during cutover as testing will continue until we have all the bugs fixed.
- browser support list (IE, Google Chrome, Brave).

# KIDSCLUB Migration Context & Working Notes

Schema files location and notes:
- All database schema files, migration SQL, and related README or import scripts will be stored under the repository path `db/schema/`.
- Each subsystem gets its own subfolder, for example:
  - `db/schema/copr/` — COPR (current COPR Postgres schema and README)
  - `db/schema/counterfx/` — COUNTERFX schema files
  - `db/schema/members/`, `db/schema/sales/`, etc. for other domains
- File naming convention: use `<subsystem>_schema.sql` for DDL, and `README.md` alongside it to document mapping notes and import guidance.
- Files are committed to the `master` branch for easy retrieval. If you prefer them in a feature branch during development, mention the branch name.
- Current files present:
  - `db/schema/copr/copr_schema.sql`
  - `db/schema/copr/README.md`


## Names
- Top-level program will be referred to as KIDSCLUB; this is the all-inclusive program of the two branches COUNTERFX and CORP2.
- COUNTERFX and CORP2 will be referred to as branches (Git branches); I renamed two branches under the master Git branch. For reference, when I ask you to look at a file in COUNTERFX, that means look in the git branch titled COUNTERFX.
- Other folders under the COUNTERFX branch will be referred to as sub-folder. 
Example, "please read the file Games.FXML in sub-folder GAMES.
- Most of this Naming is for the owners needs so he can communicate to you. 

## Where the Access DB lives (non-secret pointer)
- "Microsoft Access Driver (*.accdb)"
- Access Database Engine 2016
- File path (local/dev): /**-NET-DRIVE/clubdb
- Shared location on local computer in our office, backup copies are on owners home computer and owner will provide table descriptions in text format.

## How to run the existing app (short)
- JDK version: “JDK 25.0.1”
- Build command: `./ANT`
- Run command:
    • java -jar Local-Jar-Files/corp2/dist/corp2.jar
    • java -jar Local-Jar-Files/counterFX/dist/counterFX.jar
- Testing, Owner has complete version at home for design and testing. This is how any future testing will be done.


## Important source files & entry points
- Main class: COUNTERFX and CORP2 (two programs; COUNTERFX is mostly dependent on JavaFX and CORP2 uses Swing)
- Data access: src/.../AccessDataRepository.java (reads .mdb via UCanAccess)
- UI / forms: There are two main forms in COUNTERFX called "counter" and "Main". CORP2's main form is "Corpform_Main2".

## High-level data model
- Number of tables: 38
- Most important tables: Members, Inventory, Timeclock, Sales, Emails, Member Mail.
- Tables themselves do not maintain referential integrety, The code will keep this. The only referenatial integrety is in the
    • Members Table
    • MembersDetail Table
- Referenatial integrity will be explored more when we get to that issue.

## Table-level notes (short per-table summary)
- Table name: Members
