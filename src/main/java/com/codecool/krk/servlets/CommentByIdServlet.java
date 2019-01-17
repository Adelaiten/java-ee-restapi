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
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(comment);
        transaction.commit();
        response.getWriter().write(String.format("{removed id=%d", id));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestJSON = servletHelper.parseRequest(request);
        Comment comment = getComment(requestJSON);
        EntityManager em = entityManagerFactory.createEntityManager();
        postComment(response, comment, em);
    }

    private Comment getComment(String requestJSON) {
        boolean exist = requestJSON.contains("\"id\"");
        if (exist) {
            String[] arr = requestJSON.split(",",2);
            requestJSON = arr[1];
            requestJSON = "{" + requestJSON;
            System.out.println(requestJSON);
        }
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

    private void postComment(HttpServletResponse response, Comment comment, EntityManager em) throws IOException {
        em.clear();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(comment);
        transaction.commit();
        em.close();
        response.getWriter().write("{persist successful}");
    }

    protected  void doPut(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        String requestJSON = servletHelper.parseRequest(request);
        int id = getId(request);
        Comment newComment = getComment(requestJSON);
        newComment.setId(id);
        EntityManager em = entityManagerFactory.createEntityManager();
        Comment oldComment = em.find(Comment.class, id);
        if(oldComment == null){
            postComment(response, newComment, em);
        } else {
            putComment(response, newComment, em);
        }
    }

    private void putComment(HttpServletResponse response, Comment newComment, EntityManager em) throws IOException {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(newComment);
        transaction.commit();
        em.close();
        response.getWriter().print("{edit successful}");
    }
}
