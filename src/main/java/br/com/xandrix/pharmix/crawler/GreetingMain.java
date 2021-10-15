package br.com.xandrix.pharmix.crawler;

import javax.inject.Inject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class GreetingMain implements QuarkusApplication {

    @Inject
    GreetingService service;
    
    public static void main(String... args) {
        Quarkus.run(GreetingMain.class, args);
    }

    @Override
    public int run(String... args) {

        if(args.length>0) {
            System.out.println(service.greeting(String.join(" ", args)));
        } else {
            System.out.println(service.greeting("commando"));
        }

        return 0;
    }


}
