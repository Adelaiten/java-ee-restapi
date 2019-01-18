import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import connections.*;
import models.Comment;
import models.Note;
import models.User;
import org.hibernate.Transaction;
import servletHelpers.ServletHelper;

import javax.persistence.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;


@WebServlet(urlPatterns = {"/user/*"})
public class UserByIdServlet extends HttpServlet {
    private EntityManagerFactory emf = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
        int userId = Integer.parseInt(url.replace("/user/", ""));

        String json = getUserJsonById(userId, request, response);
        response.setHeader("Content-type", "application/json");
        response.getWriter().print(json);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager entityManager = emf.createEntityManager();
        ServletHelper servletHelper = new ServletHelper();
        String json = servletHelper.parseRequest(request);
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        EntityManager entityManager = emf.createEntityManager();
        ServletHelper servletHelper = new ServletHelper();
        String json = servletHelper.parseRequest(request);
        User userFromRequest = getUserByJson(json);
        String url = request.getRequestURI();
        int userId = Integer.parseInt(url.replace("/user/", ""));
        User oldUser = entityManager.find(User.class, userId);
        if(oldUser == null) {
            System.out.println("BUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUULKAAAAAAAAAAAAAAAA");
            postUserWhenNull(response, entityManager, userFromRequest);
        }else {
            userFromRequest.setId(userId);
            updateUser(response, entityManager, userFromRequest);
        }



    }


    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager entityManager = emf.createEntityManager();
        String url = request.getRequestURI();
        int userId = Integer.parseInt(url.replace("/user/", ""));
        User user = entityManager.find(User.class, userId);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(user);
        transaction.commit();
    }


    private String getUserJsonById(int id, HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException  {
        EntityManager entityManager = emf.createEntityManager();
        User user = entityManager.find(User.class, id);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        user.setNotesIds();
        return gson.toJson(user);
    }




    private void updateUser(HttpServletResponse response, EntityManager entityManager, User user) throws IOException {
        System.out.println("CHLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEb");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(user);

        transaction.commit();
        entityManager.close();
        response.getWriter().print("{edit successful}");
    }


    private void postUserWhenNull(HttpServletResponse response, EntityManager entityManager, User user) { //uzyc response'a
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        entityManager.close();
    }

    private User getUserByJson(String requestJSON) {
        boolean exist = requestJSON.contains("\"id\"");
        if (exist) {
            String[] arr = requestJSON.split(",",2);
            requestJSON = arr[1];
            requestJSON = "{" + requestJSON;
            System.out.println(requestJSON);
        }
        Gson gson = new Gson();
        User user = gson.fromJson(requestJSON, User.class);
        return user;
    }
}
