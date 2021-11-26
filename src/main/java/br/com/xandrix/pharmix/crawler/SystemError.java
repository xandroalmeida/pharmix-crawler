package br.com.xandrix.pharmix.crawler;

public class SystemError extends RuntimeException {

    public SystemError(Exception e) {
        super(e);
    }
    
}
