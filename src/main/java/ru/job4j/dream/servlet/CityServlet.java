package ru.job4j.dream.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.CityStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 19.04.2021.
 */
public class CityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        if (id == null) {
            List<City> cites = (List<City>) CityStore.instOf().findAll();
            String json = mapper.writeValueAsString(cites);
            writer.println(json);
        } else {
            City city = (City) CityStore.instOf().findById(Integer.parseInt(id));
            String json = mapper.writeValueAsString(city);
            writer.println(json);
        }
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CityStore.instOf().save(new City(0, req.getParameter("name")));
        resp.sendRedirect(req.getContextPath() + "/");

    }
}
