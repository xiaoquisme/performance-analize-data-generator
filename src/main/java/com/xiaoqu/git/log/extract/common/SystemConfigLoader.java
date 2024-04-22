package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemConfigLoader {
    public static SystemConfig config;
    static  {
        Yaml yaml = new Yaml(new Constructor(SystemConfig.class));
        InputStream resourceAsStream = SystemConfigLoader.class.getClassLoader().getResourceAsStream("config.yaml");
        config =  yaml.load(resourceAsStream);
    }
    public static HikariConfig getHikariConfig() {
        return config.db;
    }
}
