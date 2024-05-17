package io.codewar.scarlet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("io.codewar.scarlet.dao")
@ComponentScan(value = { "io.codewar" })
public class ScarletApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScarletApplication.class, args);
    }
}
