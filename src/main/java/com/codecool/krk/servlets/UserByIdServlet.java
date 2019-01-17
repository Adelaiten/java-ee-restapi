import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.*;
import models.User;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import javax.persistence.EntityTransaction;


@WebServlet(urlPatterns = {"/user/*"})
public class UserByIdServlet extends HttpServlet {
    private EntityManagerFactory emf = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
        int userId = Integer.parseInt(url.replace("/user/", ""));
        getUserById(userId, request, response);

    }

    protected void doPost(HttpServletResponse response,  HttpServletRequest request) throws ServletException, IOException {
        EntityManager entityManager = emf.createEntityManager();


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
