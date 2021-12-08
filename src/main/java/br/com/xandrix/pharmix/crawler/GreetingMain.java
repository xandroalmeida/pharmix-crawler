package br.com.xandrix.pharmix.crawler;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class GreetingMain implements QuarkusApplication {

    @Inject
    JobManager jobManager;
    
    public static void main(String... args) {
        Quarkus.run(GreetingMain.class, args);
    }

    @Override
    public int run(String... args) {
    	jobManager.run();
        return 0;
    }

}
