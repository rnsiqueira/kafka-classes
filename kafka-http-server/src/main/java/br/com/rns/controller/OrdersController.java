package br.com.rns.controller;

import br.com.rns.service.GenerateOrder;
import br.com.rns.service.GenerateReport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class OrdersController {

    public static void main(String[] args) throws Exception {
        var server = new Server(8085);

        var context = new ServletContextHandler();
        context.setContextPath("/api");
        context.addServlet(new ServletHolder(new GenerateOrder()), "/order");
        context.addServlet(new ServletHolder(new GenerateReport()), "/report");

        server.setHandler(context);
        server.start();
        server.join();
    }


}
