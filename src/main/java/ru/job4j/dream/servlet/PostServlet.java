package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 22.03.2021.
 */
public class PostServlet extends HttpServlet {
    final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("posts", PsqlStore.instOf().findAllPosts());
        req.getRequestDispatcher("post/posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStore.instOf().save(
                new Post(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        req.getParameter("description"),
                        LocalDate.parse(req.getParameter("created"), DATE_TIME_FORMATTER))
        );
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
