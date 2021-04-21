package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 31.03.2021.
 */
public class UploadPhotoCandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        req.setAttribute("id", id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("candidate/photoUpload.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String photo = req.getParameter("photo");

        if (photo != null) {
            String name = req.getParameter("name");
            int id_city = Integer.parseInt(req.getParameter("id_city"));
            Candidate candidate = new Candidate(id, name, photo, id_city);
            CsqlStore.instOf().save(candidate);
            File file = new File("c:\\images\\" + photo);
            file.delete();
        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletContext servletContext = this.getServletConfig().getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List<FileItem> items = upload.parseRequest(req);
                File folder = new File("c:\\images\\");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                StringBuilder sb;
                for (FileItem item : items) {
                    sb = new StringBuilder();
                    if (!item.isFormField()) {
                        sb.append(folder);
                        sb.append(File.separator);
                        sb.append(id);
                        sb.append(".");
                        sb.append(item.getName().split("\\.")[1]);
                        File file = new File(sb.toString());
                        try (FileOutputStream out = new FileOutputStream(file)) {
                            out.write(item.getInputStream().readAllBytes());
                        }
                        Candidate candidate = (Candidate) CsqlStore.instOf().findById(id);
                        candidate.setPhoto(id + "." + item.getName().split("\\.")[1]);
                        CsqlStore.instOf().save(candidate);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }
}
