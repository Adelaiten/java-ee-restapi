import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
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

@WebServlet(name="Notes_Servlet", urlPatterns = {"/notes/*"})
public class NotesServlet extends HttpServlet {

    EntityManagerFactory emf = SingletonEntityManagerFactory.getInstance();
    EntityManager em = emf.createEntityManager();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String urlString = request.getRequestURI();

        urlString = urlString.replace("notes", "");
        urlString = urlString.replace("/", "");

        System.out.println("Url string:" + urlString);

        if(urlString.isEmpty()) {

            List<Note> notesList = (List<Note>) em.createNamedQuery("allNotesQuery", Note.class).getResultList();

            em.close();

            Type collectionType = new TypeToken<ArrayList<Note>>(){}.getType();

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            String json = gson.toJson(notesList,collectionType);

            response.getWriter().write(json);

        } else {

            int noteNumber = Integer.parseInt(urlString);
            System.out.println(noteNumber);
            Note note = em.find(Note.class, noteNumber);

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            String json = gson.toJson(note);

            response.getWriter().write(json);        }
    }

}
