import models.Note;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@WebServlet(name="Notes_Servlet", urlPatterns = {"/notes/"})
public class NotesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlString = request.getRequestURI();

        urlString = urlString.replace("notes", "");
        urlString = urlString.replace("/", "");


        if(urlString.isEmpty()) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("szkolna_17");
            EntityManager em = emf.createEntityManager();

            List<Note> nowaLista = em.createNamedQuery("allNotesQuery", Note.class).getResultList();
            for(int i=0; i<nowaLista.size(); i++){
                System.out.println(nowaLista.get(i).getTitle());
            }
            response.getWriter().write("<html><body>it's in a console</body></html>");

        } else {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("szkolna_17");
            String string = new String();

            EntityManager em = emf.createEntityManager();

            Note note = em.find(Note.class, 1);
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
