import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Comment;
import models.User;
import servletHelpers.ServletHelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(urlPatterns = {"/user"})
public class AllUsersServlet extends HttpServlet {
    private EntityManagerFactory emf = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        String hql = "FROM Users";
        em.getTransaction().begin();
        Query query = em.createQuery(hql, User.class);
        List<User> allUsers = (List<User>) query.getResultList();
        Type usersType = new TypeToken<List<User>>(){}.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(allUsers, usersType);
        response.setHeader("Content-type", "application/json");
        response.getWriter().print(json);
        em.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        ServletHelper servletHelper = new ServletHelper();
        String requestJSON = servletHelper.parseRequest(request);
        Gson gson = new Gson();
        User user = gson.fromJson(requestJSON, User.class);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user);
        transaction.commit();
        em.close();
        response.setHeader("Content-type", "application/json");
        response.getWriter().write("{persist successful}");

    }

    protected  void doPut(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
        doPost(request, response);
    }

}
