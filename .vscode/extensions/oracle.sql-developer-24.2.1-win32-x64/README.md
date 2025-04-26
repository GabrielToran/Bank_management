# Oracle SQL Developer Extension for VS Code
<!-- Markdown reference: https://www.markdownguide.org/ -->
## About the Oracle SQL Developer extension for VS Code

The de-facto query, development, and administration tool for Oracle Database professionals, is now available as an extension in the world's most popular IDE. 

The Oracle SQL Developer Extension for VS Code provides the ability to execute your SQL queries and scripts, perform PL/SQL development, and interact with your database schema objects. Developed over decades with input from more than 6 million users worldwide, SQL Developer brings the features today's developers expect when working with the world's best database.

Over the next few quarters, we will be rapidly introducing new features from the desktop tool, and the  SQL Developer Extension for VS Code will become the new SQL Developer.


## Getting started

Install SQL Developer for VS Code by clicking the `Install` button above.

### Complete Feature List

#### Connectivity
Everything required to connect to your database is included in the extension. If you have a TNSNames.ORA file you can use that to define your database connections.

Easily point and click through creating connections using an EZCONNECT/BASIC connection list of properties, or reference a TNS entry, or use your Oracle Cloud Autonomous Database Wallet.ZIP files.

![connection dialog](https://www.oracle.com/a/tech/img/connect1.png)
*Quickly test your connection via the Test button.*

- Named connections
- Create, save, test, and clone connections
- Preferences for location of TNSNames.ora and auto-execute startup SQL scripts

Once connected, right-click to open a Worksheet. For any code editor, use the connection selector in the bottom right hand corner to switch to a different database.

If a connection is interrupted, you'll be prompted to automatically reconnect.

We take security extremely seriously, all of your saved connection passwords are securely stored in private Java wallets.

Connections defined in SQL Developer for VS Code, will also be available to independent installs of Oracle SQLcl on your machine.


#### Schema navigation
Your connection can be expanded to display a tree representation of your schema, and of the other schemas in your database. 

To open an object, simply click on it. You can also drag and drop items to a code editor to have the desired SQL or DML code generated. 

An alternative navigation aid is via the VS Code breadcrumbs. SQL Developer will present the contents of your database as a virtual file system. Simply use the drop down controls to change up the schema, type of object, or object you wish to browse. 

![breadcrumbs](https://www.oracle.com/a/tech/img/breadcrumb1.png)
*Easily navigate your schema via the breadcrumbs.*

- Objects Supported
    - Tables
    - Views
    - Indexes
    - Packages
    - Procedures
    - Functions
    - Operators
    - Queues
    - Queues Tables
    - Triggers
    - Types
    - Sequences
    - Materialized Views
    - Materialized View Logs
    - Synonyms
    - Public Synonyms
    - Database Links
    - Public Database Links
    - Directories
    - Editions
    - Application Express
    - XML DB Repository
    - DBMS Jobs
    - Recycle Bin

- Object Browser features
    - 300+ Context menu operations such as 'Disable all foreign key constraints' for a selected TABLE or 'Make unusable' for a selected INDEX
    - Drag and drop to editor to insert name or generated code (INSERT, UPDATE, DELETE, SELECT)
    - Detailed reports for each object, for example list of partitions on a TABLE or constraints on a VIEW
    - DDL generation for the object on the SQL page
    - Additionally, for Tables & Views
      - Insert, Update, Delete rows via Data Grid
      - Upload / download files to and from BLOBs/CLOBs

#### SQL Worksheet
The worksheet is where most of your work as an Oracle Database professional will take place. Running ad hoc queries or even complex SQL scripts can easily be achieved using our productivity features such as completion insight, SQL History, and our code snippets.

![code insight](https://www.oracle.com/a/tech/img/insight1.png)
*Automatic insight or invoke on demand via ctrl+spacebar.*

Existing SQL Developer users will find their familiar keyboard shortcuts intact, including ctrl+Enter for executing a statement and ctrl+Up arrow for recalling the previous item from your SQL History.

- Execute SQL statements
- Execute scripts
- Execute via integrated SQLcl (command line interface)
- Explain Plan
  - ALL
  - Basic
  - Serial
  - Typical
 - SQL History / SQL Recall
 - SQL Formatter
 - SQL Parser, detect problems in your code
 - Code completion
    - Oracle command syntax
    - Data Dictionary
    - Editor contents
- Go To Declaration, open dictionary objects directly from editor
- Peek Declaration, get the PL/SQL definition for program at cursor
- Access to advanced SQLcl scripting Commands, including
        - CTAS
        - INFO
        - DDL
        - OERR
        - CODESCAN
        - SODA

![script output engine](https://www.oracle.com/a/tech/img/script-engine1.png)
*Easily query and generate output files using your favorite SQL Developer scripting commands.*


#### Data Grids
Browsing data and/or your query results becomes a breeze with our intuitive and friendly spreadsheet-style displays. 

Invoke our Single Record View to easily browse one record at a time using our vertical, index-card like viewer.

 ![single record view](https://www.oracle.com/a/tech/img/single-record-view1.png)
*Vertical vs horizontal display in our Single Record View.*

Intuitively drag and drop columns around to achieve the desired display, or simply double-click toggle your way through apply SORTS on your columns. 

When your results are ready to share, simply right-click to EXPORT them to the desired format. 

- Easily browse and interact with SQL query results and contents of TABLES and VIEWS
- Single Record View
- Optimized column sizing based on data and column headers
- Reorder and/or hide columns
- Multi-column sorts
- Double-click column to toggle sort ASC, DESC, off, column sort indicators
- Count Rows
- Export
  -  CSV
  -  Delimited
  -  Fixed width
  -  HTML
  -  INSERT
  -  JSON
  -  JSON (pretty)
  -  SQL*Loader
  -  Text
  -  XML
- BLOB/CLOB/JSON contents preview 

![blob preview](https://www.oracle.com/a/tech/img/blob1.png)
*Inspect the contents of your LOB and JSON columns.*


#### PL/SQL Code Editor
Whether you're working directly with PL/SQL programs in your database, or working from your local workspace, SQL Developer makes it easy to develop your stored procedures, functions, packages, triggers, and more. 

As you're coding, problems will be immediately detected via our PL/SQL and SQL Parser. Compiler errors are also automatically displayed as you save your work to the database.

When it's time to test or execute your program, SQL Developer provides an interactive panel where you can input the required parameters. The resulting PL/SQL anonymous block can be executed immediately, or saved to a new code editor for further customization.

![plsql](https://www.oracle.com/a/tech/img/plsql-1.png)
*Execute your PL/SQL, generated scripts, see the results.*

OUT and RETURN parameters are automatically captured displayed post-execution, including your SYS_REFCURSORS.

- Compile stored procedures and PL/SQL programs
- Client error detection as you code
- Compiler error feedback
- Execute programs, view results
- Work with files or directly with database objects from your connections


#### Code Snippets
All of your favorite analytic functions, date format options, and conversion functions with examples are at your fingertips via VS Code's integrated code completion support for your active extension's code snippets. 

- Aggregate Functions
- Analytic Functions
- Character Functions
- Conversion Functions
- Date Formats
- Date/Time Functions
- Number Formats
- Numeric Functions
- Optimizer Hints
- PL/SQL Programming Techniques
- Predictive Analytics
- Pseudocolumns
- Flashback

#### Integrated Command Line Interface via Oracle SQLcl
We recognize that many developers feel more comfortable at a command or bash prompt. With the SQL Developer Extension for VS Code, no compromise is required! Connections defined in the extension can also be used to spawn command line sessions to your database.

[Oracle SQLcl](https://www.oracle.com/database/sqldeveloper/technologies/sqlcl/) is your modern command line interface for the Oracle Database. Popular features from SQL Developer such as code completion, SQL History, and generation of DDL have been implemented. In addition, Oracle's enhanced implementation of [Liquibase](https://docs.oracle.com/en/database/oracle/sql-developer-command-line/23.3/sqcug/using-liquibase.html#GUID-4CA25386-E442-4D9D-B119-C1ACE6B79539) for schema versioning is delivered in SQLcl via the liquibase (lb) command.

Spawn a CLI session on demand, or take any code editor and execute the contents immediately in a brand new, independent session, in a single click!

![connect-to-database](https://www.oracle.com/a/tech/img/sqlcl-1.png)
*Use the powerful editor, execute in the terminal, it's your choice!*

### Additional Quality of Life Features
- SQL Developer for VS Code is self-contained, with no installation pre-requisites.
- Includes Oracle SQLcl, our modern command-line interface. 
- 20+ application commands available from Command Palette
- In-place application updates will be released quarterly, using a YY.Q numbering scheme, e.g. 23.4, 24.1, 24.2
- Oracle Database customers with valid support and maintenance contracts can open Services Requests for SQL Developer Extension for VS Code via My Oracle Support

## Useful Commands
| Command  | Shortcut  |
|---|---|
| Execute SQL  | Ctrl+Enter  |
| Execute SQL as Script  | F5  |
| Execute SQL as Script in SQLcl  | Ctrl+Shift+F5  |
| Explain Plan  | F10  |
| Replace with Previous/Next item from SQL History  | Ctrl+Up / Down  |


## Quick Start Examples

Start working with your database by simply expanding the connection in the connections panel.
![connect-to-database](https://www.oracle.com/a/tech/img/connect-to-database.gif)

To write and execute SQL against your database, click the Worksheet icon.
![sql-worksheet](https://www.oracle.com/a/tech/img/sql-worksheet.gif)

Auto-complete can accelerate your development time.
![select-statement](https://www.oracle.com/a/tech/img/select-statement.gif)

We've even included SQL Snippets to help keep you in the flow.
![sql-snippets](https://www.oracle.com/a/tech/img/sql-snippets.gif)

And in case you want to revisit your earlier work, be sure to check out the
SQL History tab, located in the bottom panel.
![sql-history](https://www.oracle.com/a/tech/img/sql-history.gif)

Working with your database objects is easy too. You're just a click away from
inspecting and gathering important details.
![object-manipulation](https://www.oracle.com/a/tech/img/object-manipulation.gif)

### Supported Locales
The extension is available in multiple languages: de, en, es, fr, it, ja, ko, pt-BR, zh-CN, zh-TW


### Bookmarks

- [Learn SQL @ Live SQL](https://livesql.oracle.com)
- [License](https://www.oracle.com/downloads/licenses/oracle-free-license.html)
- Official community [forums](https://forums.oracle.com/ords/apexds/domain/dev-community/category/sqldev-for-vscode)
<!-- Release notes, and enhancement requests will need links -->

<!-- Forums for enhancement requests -->
<!-- Maybe add in github > Issues for enhancement requests? -->

## Support

Support documentation for Oracle SQL Developer for VS Code can be found [here](https://docs.oracle.com/en/database/oracle/sql-developer-vscode/23.4/sqdnx/index.html).

Customers with My Oracle Support established for their Oracle Database can create Service Requests for official product [support](https://support.oracle.com/portal/), including filing bugs or submitting enhancement requests. 

## Data and telemetry

In order to continuously improve our products, Oracle is interested in learning about product usage. To that end, automated reports can occasionally be sent to Oracle describing the product features in use. No personally identifiable information will be sent and the report will not affect performance. You can review Oracle's [privacy policy](https://www.oracle.com/legal/privacy/privacy-policy.html).

## License

Oracle SQL Developer for VS Code is distributed free, under the [Free Use Terms
and Conditions](https://www.oracle.com/downloads/licenses/oracle-free-license.html)
(FUTC) License.

<!-- Do we need to mention the licenses that any open-source packages/libraries use?  -->
