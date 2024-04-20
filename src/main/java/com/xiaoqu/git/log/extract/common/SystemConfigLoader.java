package com.xiaoqu.git.log.extract.common;

import com.zaxxer.hikari.HikariConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class SystemConfigLoader {
    public static SystemConfig config;
    static  {
        Yaml yaml = new Yaml(new Constructor(SystemConfig.class));
        InputStream resourceAsStream = SystemConfigLoader.class.getClassLoader().getResourceAsStream("config.yaml");
        config =  yaml.load(resourceAsStream);
    }
    public static HikariConfig getHikariConfig() {
        return new HikariConfig("hikari.properties");
    }
}
