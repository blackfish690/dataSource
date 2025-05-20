package com.sky.skydemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkyDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        System.out.println("test");
    }

    @Test
    void test2() {
        System.out.println("冲突22222");
    }

}
