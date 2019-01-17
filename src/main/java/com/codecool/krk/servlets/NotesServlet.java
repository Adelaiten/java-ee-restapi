import models.Note;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="Notes_Servlet", urlPatterns = {"/notes/"})
public class NotesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlString = request.getRequestURI();
        urlString = urlString.replace("notes", "");
        urlString = urlString.replace("/", "");


        if(request.getRequestURI().isEmpty()){
            response.getWriter().write("<html><body>ALL NOTES</body></html>");
        } else if(request.getRequestURI().matches("/notes/")) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("szkolna_17");
            String string = new String();

            EntityManager em = emf.createEntityManager();

            Note note = em.find(Note.class, urlString);
            response.getWriter().write("<html><body>" +
                    "Id: " + note.getNoteId() + "<br>" +
                    "Author: " + note.getUser().getName() + "<br>" +
                    "Title: " + note.getTitle() + "<br>" +
                    "Date: " + note.getDate() + "<br>" +
                    "Content: " + note.getContent() + "<br>" +
                    "THE URL IS: " + request.getRequestURI() + "<br>" +
                    "</body></html>");
        }
        }

}
