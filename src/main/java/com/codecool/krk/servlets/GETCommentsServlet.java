import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Comment;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(urlPatterns = {"/comments"})
public class CommentsServlet extends HttpServlet {
    EntityManagerFactory entityManagerFactory = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = entityManagerFactory.createEntityManager();
//        List<Comment> comments = em.createNamedQuery("allCommentsQuery", Comment.class).getResultList();
        String hql = "FROM Comments";
        em.getTransaction().begin();
        Query query = em.createQuery(hql, Comment.class);
        List<Comment> comments =(List<Comment>) query.getResultList();
        Type commentsType = new TypeToken<List<Comment>>(){}.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(comments, commentsType);
        response.getWriter().write(json);
        em.close();
    }
}
