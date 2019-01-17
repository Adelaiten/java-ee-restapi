import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Comment;
import models.Note;
import models.User;
import org.hibernate.Session;
import servletHelpers.ServletHelper;

import javax.persistence.*;
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
    private ServletHelper servletHelper = new ServletHelper();

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

    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String requestJSON = servletHelper.parseRequest(request);
        Comment comment = getComment(requestJSON);
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(comment);
        transaction.commit();
        em.close();
        response.setHeader("Content-type", "application/json");
        response.getWriter().write("{persist successful}");
    }

    private Comment getComment(String requestJSON) {
        Gson gson = new Gson();
        Comment comment = gson.fromJson(requestJSON, Comment.class);
        Note note = new Note();
        note.setNoteId(comment.getNote_id());
        comment.setNote(note);
        User user = new User();
        user.setId(comment.getUser_id());
        comment.setUser(user);
        return comment;
    }

    protected  void doPut(HttpServletRequest request,HttpServletResponse response) throws IOException{
        doPost(request, response);
    }
}
