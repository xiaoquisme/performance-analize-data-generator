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
    project varchar(100),
    jira_no varchar(100),
    message varchar(1000),
    author varchar(100),
    commit_date varchar(100),
    commit_email varchar(100)
);
```