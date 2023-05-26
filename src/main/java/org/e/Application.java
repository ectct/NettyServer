package org.e;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan(value = "org.e.mapper")
public class Application implements CommandLineRunner {

    @Autowired
    private NettyServerThread nettyServerThread;
    @Autowired
    private HttpNettyServerThread httpNettyServerThread;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServerThread.start();
        httpNettyServerThread.start();
    }
}
@Component
class NettyServerThread extends Thread {
    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run() {
        try {
            nettyServer.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

@Component
class HttpNettyServerThread extends Thread {
    @Autowired
    private HttpNettyServer nettyServer;

    @Override
    public void run() {
        try {
            nettyServer.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}