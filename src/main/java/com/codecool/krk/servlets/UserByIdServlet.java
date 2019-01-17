import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.*;
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
        getUserById(userId, request, response);

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

    
    private void getUserById(int id, HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException  {
        EntityManager entityManager = emf.createEntityManager();
        User user = entityManager.find(User.class, id);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        user.setNotesIds();
        Type listOfTestObject = new TypeToken<User>(){}.getType();
        String json = gson.toJson(user, listOfTestObject);
        response.setHeader("Content-type", "application/json");
        response.getWriter().print(json);
    }
}
