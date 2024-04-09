A git log extracter tools.

project template create from [flink](https://nightlies.apache.org/flink/flink-docs-release-1.10/dev/projectsetup/java_api_quickstart.html#gradle):
```bash
bash -c "$(curl https://flink.apache.org/q/gradle-quickstart.sh)"
```

usage:
in a git project folder exec
```bash
git log --oneline --decorate --graph > git_log.txt
```
then copy `git_log.txt` to `resource` folder.

## database setup

```sql
create database performance_analyze;
create table performance_analyze.git_log
(
    id varchar(100) primary key ,
    repo_owner varchar(100),
    project varchar(100),
    jira_no varchar(100),
    message varchar(1000),
    author varchar(100),
    commit_date varchar(100),
    commit_email varchar(100)
);

CREATE PROCEDURE create_jira_board(IN date VARCHAR(255))
BEGIN
    SET @table_name = CONCAT('jira_board_', date);
    SET @sql = CONCAT('CREATE TABLE ', @table_name, '(
    id bigint primary key ,
    name varchar(1000),
    `type` varchar(100)
)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END;

CREATE PROCEDURE create_jira_epic(IN date VARCHAR(255))
BEGIN
    SET @table_name = CONCAT('jira_epic_', date);
    SET @sql = CONCAT('CREATE TABLE ', @table_name, '(
   id bigint primary key ,
   `key` varchar(100),
   link varchar(1000),
   name varchar(100),
   summary varchar(1000),
   is_done bool,
   board_id bigint
)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END;

CREATE PROCEDURE create_jira_sprint(IN date VARCHAR(255))
BEGIN
    SET @table_name = CONCAT('jira_sprint_', date);
    SET @sql = CONCAT('CREATE TABLE ', @table_name, '(
  id bigint primary key ,
  boardId bigint,
  state varchar(100),
  name varchar(100) comment ''updateAuthor.emailAddress'',
  goal varchar(1000) comment ''created''
)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END;


CREATE PROCEDURE create_jira_issue(IN date VARCHAR(255))
BEGIN
    SET @table_name = CONCAT('jira_issue_', date);
    SET @sql = CONCAT('CREATE TABLE ', @table_name, '(
   id bigint primary key,
   epic_id bigint,
   epic_key varchar(100) comment ''epic.key'',
   `key` varchar(100) comment ''key'',
   issue_type varchar(100) comment ''fields.issuetype.name'',
   title varchar(1000) comment ''summary'',
   `discription` text comment ''description'',
   timetracking_spent varchar(100),
   story_point varchar(100) comment  ''customfield_10028'',
   current_sprint varchar(100) comment ''customfield_10020.name'',
   `status` varchar(100) comment ''status.name'',
   reporter varchar(100) comment ''reporter.emailAddress'',
   assignee varchar(100) comment ''assignee.emailAddress''
)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END;

CREATE PROCEDURE create_jira_worklog(IN date VARCHAR(255))
BEGIN
    SET @table_name = CONCAT('jira_worklog_', date);
    SET @sql = CONCAT('CREATE TABLE ', @table_name, '(
  id bigint primary key ,
  issue_id bigint,
  updater_name varchar(100),
  update_author varchar(100) comment ''updateAuthor.emailAddress'',
  created varchar(100) comment ''created'',
  time_spent varchar(100) comment ''timeSpent''
)');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END;


```

## github open api docs
[https://docs.github.com/en/rest/commits/commits#list-commits](https://docs.github.com/en/rest/commits/commits#list-commits)

## jira docs
https://docs.atlassian.com/jira-software/REST/7.3.1/

## other notes
flink need 50 task slots

## flink submit job
```bash
flink run -d git_log_extract-1.0-all.jar
```