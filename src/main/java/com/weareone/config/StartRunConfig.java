package com.weareone.config;

import com.weareone.common.Constans;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化任务
 * Created by wang.linqiao on 2016/11/9.
 */
@Configuration
public class StartRunConfig implements CommandLineRunner{

    @Override
    public void run(String... strings) throws Exception {
        Constans.init();
    }
}
