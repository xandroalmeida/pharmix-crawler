package br.com.xandrix.pharmix.crawler;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.java.Log;

@QuarkusMain
@Log
public class GreetingMain implements QuarkusApplication {

    @Inject
    JobManager jobManager;
    
    public static void main(String... args) {
    	try {
        Quarkus.run(GreetingMain.class, args);
    	} catch (Throwable e) {
			log.severe(e.getMessage());
			System.exit(-1);
		}
    }

    @Override
    public int run(String... args) {
    	jobManager.run();
        return 0;
    }

}
