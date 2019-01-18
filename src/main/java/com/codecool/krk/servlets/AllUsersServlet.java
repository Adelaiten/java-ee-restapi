import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Comment;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
        List<User> allUsers = getUsers();
        Type usersType = new TypeToken<List<User>>(){}.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(allUsers, usersType);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletResponse response,  HttpServletRequest request) throws ServletException, IOException {
        EntityManager entityManager = emf.createEntityManager();


    }

    private List<User> getUsers() {
        EntityManager em = emf.createEntityManager();
        String hql = "FROM Comments";
        em.getTransaction().begin();
        Query query = em.createQuery(hql, Comment.class);
        em.close();
        return (List<User>) query.getResultList();
    }
}
