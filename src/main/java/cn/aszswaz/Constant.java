package cn.aszswaz;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public class Constant {
    private static Executor threadPool;
    private static Properties config;

    public static void init() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        int cpu = runtime.availableProcessors();
        log.info("available processors: {}", cpu);
        threadPool = Executors.newFixedThreadPool(cpu << 1);

        config = new Properties();
        config.load(new InputStreamReader(
                Objects.requireNonNull(Constant.class.getClassLoader().getResourceAsStream("mail.properties")),
                StandardCharsets.UTF_8
        ));
    }

    public static Executor getThreadPool() {
        return threadPool;
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}
