import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Note;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="Notes_Servlet", urlPatterns = {"/notes/*"})
public class NotesServlet extends HttpServlet {

    EntityManagerFactory emf = SingletonEntityManagerFactory.getInstance();
    EntityManager em = emf.createEntityManager();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();

        String urlString = request.getRequestURI();

        urlString = urlString.replace("notes", "");
        urlString = urlString.replace("/", "");

        String json;

        if(urlString.isEmpty()) {

            json = createJSONforAllNotes(response);

        } else {

            json = createJSONforSingleNote(response, urlString);

        }

        response.getWriter().write(json);

        em.close();
    }

    private String createJSONforAllNotes(HttpServletResponse response) throws IOException {
        List<Note> notesList = (List<Note>) em.createNamedQuery("allNotesQuery", Note.class).getResultList();

        Type collectionType = new TypeToken<ArrayList<Note>>(){}.getType();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(notesList,collectionType);

        return json;

    }

    private String createJSONforSingleNote(HttpServletResponse response, String urlString) throws IOException {
        int noteNumber = Integer.parseInt(urlString);

        Note note = em.find(Note.class, noteNumber);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(note);

        return json;
    }

}
