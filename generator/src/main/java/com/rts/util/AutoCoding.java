package com.rts.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class AutoCoding {
    public static void main(String[] args) {
        // 定义 要生成的表名
        String table = "pms_product_order";
        // 定义其他模块名称
        String module = "product";
        // 获取项目父目录
        String parent_path = System.getProperty("user.dir");
        // 定义entity目录
        String entity_path = parent_path + "/entity/src/main/java/com/rts/entity";
        // 定义其他模块目录
        String other_path = parent_path + "/" + module + "/src/main";
        String mapper_path = other_path + "/java/com/rts/mapper";
        String xml_path = other_path + "/resources/com/rts/mapper";
        String service_path = other_path + "/java/com/rts/service";
        String service_impl_path = other_path + "/java/com/rts/service/impl";
        String controller_path = other_path + "/java/com/rts/controller";
        // 定义生成器对象
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 设置所有者
        gc.setAuthor("rts");
        // 每次生成后不打开目录
        gc.setOpen(false);
        // 设置service接口名字去掉I
        gc.setServiceName("%sService");
        generator.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://192.168.204.121:3307/xiyue?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true");
        dsc.setDbType(DbType.MYSQL);
        dsc.setUsername("root");
        dsc.setPassword("root");
        generator.setDataSource(dsc);
        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.rts");
        // 自定义各模块路径
        Map<String, String> pathInfo = new HashMap<>();
        pathInfo.put("entity_path",entity_path);
        pathInfo.put("mapper_path",mapper_path);
        pathInfo.put("xml_path",xml_path);
        pathInfo.put("service_path",service_path);
        pathInfo.put("service_impl_path",service_impl_path);
        pathInfo.put("controller_path",controller_path);
        pc.setPathInfo(pathInfo);
        generator.setPackageInfo(pc);
        // 策略配置
        StrategyConfig sc = new StrategyConfig();
        // 生成的表名
        sc.setInclude(table);
        sc.setRestControllerStyle(true);
        sc.setEntityLombokModel(true);
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setControllerMappingHyphenStyle(false);
        generator.setStrategy(sc);
        // 模板配置
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        // 执行代码生成
        generator.execute();

    }
}
