# Oracle SQL Developer for VS Code Changelog

## 24.2.1 August 2024

### Issues Fixed

- Support comma separated aliases in tnsnames.ora
- Incorrect paths computed for TNS folder on Windows
- TNS Connect Identifier based connections do not work on 24.2.0
- Handle quoted paths for TNS folder and startup script on Windows
- Select statement generated for partitioned tables includes list of partitions

## 24.2.0 July 2024

### New Features

- Support Oracle Database 23ai Duality Views
- Support rich editing of JSON database values
- JSON Data Guide diagram for Duality View

### Issues Fixed

- Make it clear that preference for TNS location, accepts folder only
- Rename VS Code language id from oracle_sql to oracle-sql
- Set DDL Insert Option Default to OFF
- Accept Command performs an unnecessary commit
- Include synonym option is not showing proper synonym name
- Preferences should be set in one execution request against the session
- Drag and drop table to worksheet doesn't include schema name in generated SQL statements
- Cancel long running queries correctly
- Drop option in materialized view logs not passing name properly
- Export Table data panel need to refresh after choosing JSON format
- Connection form won't work after modifying port number
- Open new worksheet for DDL Viewers for objects, action panel, runner viewer
- Column name not displayed in Add column action and Apex objects in confirmation dialog
- Fix problems with sys_context values being overwritten/erased
- Add support for displaying nested cursors
- Unable to browse packages - resultSet undefined error
- Remove sql-developer prefix from snippets
- SQL query calling function returning refcursor breaks grid and script output
- Export as INSERT ignores table name
- Handle CONNECT, DISCONNECT, EXIT and QUIT commands in worksheet
- Unable to browse other schemas objects (not using DBA views)
- PL/SQL runner doesn't instantiate CHAR inputs correctly
- Unable to browse packages in 11gR2
- Grid cell display is eating/hiding whitespace characters

## 24.1.2 June 2024

### Issues Fixed

- Ensure tnsnames.ora location exists before passing to SQLcl
- Extension failing to activate when untitled file open
- Startup script not being executed for worksheet sessions

## 24.1.1: May 2024

### New Features

- Support dedicated connection per worksheet.

  Previously each worksheet for a given connection used a single database session, meaning that only one of the worksheets could perform work at a time. 
  
  With a dedicated database session per worksheet, each worksheet can work independently, which gives a better user experience, but uses additional database sessions.

  This behavior can be disabled by changing 
  `Settings > Extensions > Oracle SQL Developer Extension for VSCode > Session per attached worksheet` to `off`. 
  
  The default and recommended value is `on`.

- Fill in default package/package body when opening undefined objects
- Toggle action to swap between package specification and body
- Data grid should use the text editor (monospace) font
- Grid Export, Add 'Clipboard' option

### Issues Fixed

- Handle new-line characters in grids
- Executing query with bind or substitution, make 'apply' button active
- Explain plan diagram navigator could be navigated only left and right
- Support cancellation while cloning sessions
- Row header should be pinned in the datagrid
- SODA command doesn't work in worksheet or embedded SQLcl
- Task cancellation not working for long running query executed as a statement
- Remove/make optional the background drop of type when compilin
- Grids, disabled buttons when no row is selected
- Disable buttons once action has been initiated
- Duplicated table aliases showing in autocomplete
- Filters on timestamp data not working correctly
- Count rows fails with 'missing right parenthesis'
- Make connection dialog buttons disabled once any action has been initiated
- Child reports not activating unless you click on row number in parent grid

## 24.1.0: April 2024

### New Features

- Explain Plan Diagram
- Enable lists of objects and schemas to be filtered in object navigator
- Support PL/SQL files opening in the PL/SQL editor
- Filter SQL History
- Allow users to modify DDL Preferences
- Support copying script output to clipboard
- Support copying rows using keyboard (ctrl+c)

### Issues Fixed

- Preview in single record view showing incorrect data
- Spool command not writing to correct directory
- Timestamps showing differently between Grid and Script output
- View PL/SQL not working for triggers
- Sorting icon is not cleared when query is re-executed
- Support generating DDL for apex applications and pages
- Support generating DDL for database links and public database links
- Numbers are not right-aligned in grids 
- Support non-ASCII passwords
- Support Generating DDL for Materialized View Log 
- Issues with PL/SQL Runner when code has syntax errors
- Incorrect data types for some procedure arguments
- Execute in SQLcl fails with emoji in the connection name on Windows platforms
- Support confirmation dialog in object actions
- Support PL/SQL Runner and drag & drop code generation for triggers
- Support sorting SQL History columns
- Support opening package body when package declaration missing and vice versa

## 23.4.2: March 2024

### New Features

- Support proxy user database connections

### Issues Fixed

- Ensure database character sets such as WE8ISO8859P9 are supported
- Deleting connection does not remove connection folder
- Query results grid not honoring session NLS parameters
- Missing connection status for custom JDBC connection
- Support Linux ARM 64
- Problems removing multiple filters from grid

## 23.4.1: February 2024

### Issues Fixed

- Grid cell updates are not being accepted when inserting new rows
- Unable to scroll while script output is being rendered
- State is lost when switching between database object viewers
- Reclaim heap memory at end of executing SQL script
- Client must always report if it encounters an error
- Restrict PL/SQL editor commands to PL/SQL editors
- Substitution variable prefixed with whitespace does not work
- Address issue with file permissions for user accounts belonging to a Windows domain
- Detect conflicting extensions and warn user
- Datagrid single record view is showing wrong record when filters are applied
- Enable COMMIT and ROLLBACK commands to be mapped to keyboard shortcuts
- Unable to browse packages without bodies
- Binds are not stored between executions in a session
- Fetch 1000 navigator tree objects at a time
- Pop up problems panel when PLSQL object compilation fails
- Poor performance parsing and executing PL/SQL program
- Grid does not handle result set with duplicate column names
- Default data grid display columns to 'Best Fit'
- Data grid best-fit only partially works for tables with many columns

## 23.4.0: January 2024

- Initial release