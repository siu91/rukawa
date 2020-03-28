package org.siu.rukawa.datasource.autoconfigure;

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.SystemProperties;
import org.siu.rukawa.RukawaBanner;
import org.siu.rukawa.datasource.support.P6SpyMessageFormat;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 应用启动时自动配置p6spy
 *
 * @Author Siu
 * @Date 2020/2/22 16:48
 * @Version 0.0.1
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(P6SpyDriver.class)
public class RukawaApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        RukawaApplicationRunner.printBanner();
        RukawaApplicationRunner.p6spyReload();
    }

    public static void printBanner() {
        RukawaBanner.printBanner();
    }

    public static void p6spyReload() {
        Map<String, String> defaults = P6SpyOptions.getActiveInstance().getDefaults();
        defaults.put(SystemProperties.P6SPY_PREFIX.concat("appender"), "com.p6spy.engine.spy.appender.Slf4JLogger");
        defaults.put(SystemProperties.P6SPY_PREFIX.concat("logMessageFormat"), P6SpyMessageFormat.class.getName());
        defaults.put(SystemProperties.P6SPY_PREFIX.concat("customLogMessageFormat"), "executionTime:%(executionTime)| 执行sql:%(sqlSingleLine)");
        P6SpyOptions.getActiveInstance().load(defaults);
        P6ModuleManager.getInstance().reload();
    }
}

