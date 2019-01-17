import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Note;
import models.User;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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

            ArrayList<Note> notesList = (ArrayList<Note>) em.createNamedQuery("allNotesQuery", Note.class).getResultList();

            Type collectionType = new TypeToken<ArrayList<Note>>(){}.getType();

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            String element = gson.toJson(notesList,collectionType);

            response.getWriter().write("<html><body>"+element+"</body></html>");

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
