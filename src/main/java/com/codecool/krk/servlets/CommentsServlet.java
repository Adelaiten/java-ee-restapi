import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Comment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;

@WebServlet(urlPatterns = {"/comments/*"})
public class CommentsServlet extends HttpServlet {
    EntityManagerFactory entityManagerFactory = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("dupa");
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("szkolna_17");

        EntityManager em = entityManagerFactory.createEntityManager();

        Comment comment = em.find(Comment.class, 1);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        Type type = new TypeToken<Comment>() {}.getType();
        comment.setIds();
        String json = gson.toJson(comment);
        System.out.println(json);
        response.getWriter().write("<html><body> "+ json  +"</body></html>");
    }
}
