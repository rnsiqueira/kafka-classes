package br.com.rns.service;

import br.com.rns.model.CorrelationId;
import br.com.rns.model.Order;
import br.com.rnsiquera.service.KafkaDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GenerateOrder extends HttpServlet {
    private KafkaDispatcher<Order> products = new KafkaDispatcher<>();
    private KafkaDispatcher<String> informations = new KafkaDispatcher<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String email = req.getParameter("email");
        var orderId = UUID.randomUUID().toString();
        var amount = req.getParameter("amount");

        Order order = new Order(orderId, new BigDecimal(amount), email);
        try {
            products.send("products", order.getEmail(), order, new CorrelationId(GenerateOrder.class.getSimpleName()));
            informations.send("information", order.getEmail(), "New Information", new CorrelationId(GenerateOrder.class.getSimpleName()));
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
        System.out.println("Sent Order!");
        resp.getWriter().println("Order created with success!");
        resp.setStatus(HttpServletResponse.SC_OK);


    }

    @Override
    public void destroy() {
        try {
            products.close();
            informations.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
