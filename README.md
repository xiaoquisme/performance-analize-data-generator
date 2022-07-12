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