import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import connections.SingletonEntityManagerFactory;
import models.Comment;
import models.Note;
import models.User;
import servletHelpers.ServletHelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/comments/*"})
public class CommentByIdServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory = SingletonEntityManagerFactory.getInstance();
    private ServletHelper servletHelper = new ServletHelper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        sendCommentById(response, id);
    }

    private int getId(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/comments/", "");
        return Integer.parseInt(path);
    }

    private void sendCommentById(HttpServletResponse response, int id) throws IOException {
        EntityManager em = entityManagerFactory.createEntityManager();
        Comment comment = em.find(Comment.class, id);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        comment.setIds();
        String json = gson.toJson(comment);
        response.getWriter().write(json);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        EntityManager em = entityManagerFactory.createEntityManager();
        Comment comment = em.find(Comment.class, id);
        em.remove(comment);
        response.getWriter().write(String.format("{removed id=%d", id));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestJSON = servletHelper.parseRequest(request);
        Gson gson = new Gson();
        Comment comment = gson.fromJson(requestJSON, Comment.class);
        Note note = new Note();
        note.setNoteId(comment.getNote_id());
        comment.setNote(note);
        User user = new User();
        user.setId(comment.getUser_id());
        comment.setUser(user);
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(comment);
        transaction.commit();
        em.close();
        response.getWriter().write("{persist successful}");
    }
}
