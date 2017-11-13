package com.tendyron.routewifi.appmanager.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by Neo on 2017/1/9.
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 2){
            throw new IllegalArgumentException("必须指定两个参数");
        }
        int port = Integer.parseInt(args[0]);
        String path = args[1];
//        String warPath = args[1];

        Server server = new Server(port);
        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath("/");
        webApp.setResourceBase(path);// .setWar(warPath);

        server.setHandler(webApp);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
