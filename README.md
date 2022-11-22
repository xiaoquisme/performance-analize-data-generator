A git log extracter tools

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
create table jira_board (
    id bigint primary key ,
    name varchar(1000),
    `type` varchar(100)
);
create table jira_epic (
   id bigint primary key ,
   `key` varchar(100),
   link varchar(1000),
   name varchar(100),
   summary varchar(1000),
   is_done bool,
   board_id bigint
);

create table jira_issue(
   id bigint primary key,
   epic_id bigint,
   epic_key varchar(100) comment 'epic.key',
   `key` varchar(100) comment 'key',
   issue_type varchar(100) comment 'fields.issuetype.name',
   title varchar(1000) comment 'summary',
   `discription` text comment 'description',
   timetracking_spent varchar(100),
   story_pont varchar(100) comment  'customfield_10028',
   current_sprint varchar(100) comment 'customfield_10020.name',
   `status` varchar(100) comment 'status.name',
   reporter varchar(100) comment 'reporter.emailAddress',
   assignee varchar(100) comment 'assignee.emailAddress'
);
create table jira_worklog (
  id bigint primary key ,
  issue_id bigint,
  updater_name varchar(100),
  update_author varchar(100) comment 'updateAuthor.emailAddress',
  created varchar(100) comment 'created',
  timeSpent varchar(100) comment 'timeSpent'
);
```

## github open api docs
[https://docs.github.com/en/rest/commits/commits#list-commits](https://docs.github.com/en/rest/commits/commits#list-commits)

## jira docs
https://docs.atlassian.com/jira-software/REST/7.3.1/

## other notes
flink need 30 task slots