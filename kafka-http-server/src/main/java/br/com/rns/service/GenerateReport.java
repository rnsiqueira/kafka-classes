package br.com.rns.service;

import br.com.rns.model.CorrelationId;
import br.com.rnsiquera.service.KafkaDispatcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateReport extends HttpServlet {

    private KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher();

    @Override
    public void destroy() {
        super.destroy();
        try {
            batchDispatcher.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            batchDispatcher.send("send_message_to_all_users", "user_generate_reading_report", "user_generate_reading_report", new CorrelationId(GenerateReport.class.getSimpleName()));
        } catch (ExecutionException e) {
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Generate Report to All Users");

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("Generantion all reports");
    }
}
