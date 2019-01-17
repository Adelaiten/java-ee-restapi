import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connections.SingletonEntityManagerFactory;
import models.Comment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/comments/*"})
public class CommentByIdServlet extends HttpServlet {
    private EntityManagerFactory entityManagerFactory = SingletonEntityManagerFactory.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String path = request.getRequestURI().replace("/comments/", "");
        System.out.println("path ="+path);
        int id = Integer.parseInt(path);
        sendCommentById(response, id);
    }

    private void sendCommentById(HttpServletResponse response, int id) throws IOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Comment comment = em.find(Comment.class, id);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        comment.setIds();
        String json = gson.toJson(comment);
        response.getWriter().write(json);
    }
}
