import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import connections.SingletonEntityManagerFactory;
import models.Note;
import models.User;

import javax.persistence.*;
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

    //Retrive data (JSON) from DB and show on page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityManager em = emf.createEntityManager();

        String urlString = parseURL(request);

        String json;

        if(urlString.isEmpty()) {

            json = createJSONforAllNotes(response);

        } else {
            int noteNumber = Integer.parseInt(urlString);

            json = createJSONforSingleNote(response, noteNumber);

        }

        response.getWriter().write(json);

        em.close();

    }

    //create json and send object to DB
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String json = request.getReader().readLine();

        Note newNote = createNoteFromJSON(json);

        saveNoteToDB(newNote);

        response.setHeader("Content-type", "application/json");
        response.getWriter().write("{'created':'successfully'}");
    }

    //Update data and save to DB
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = request.getReader().readLine();

        Note updatedNote = createNoteFromJSON(json);

        updateNoteInTheDB(updatedNote);

        response.setHeader("Content-type", "application/json");
        response.getWriter().write("{'updated':'successfully'}");
    }


    //Delete data from DB
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityManager em = emf.createEntityManager();

        int noteId = Integer.parseInt(request.getRequestURI());

        Note note = em.find(Note.class, noteId);
        em.remove(note);
        response.getWriter().write(String.format("{removed id=%d", noteId));

        em.close();
    }


    //doGet
    private String createJSONforAllNotes(HttpServletResponse response) throws IOException {
        EntityManager em = emf.createEntityManager();

        List<Note> notesList = (List<Note>) em.createNamedQuery("allNotesQuery", Note.class).getResultList();

        Type collectionType = new TypeToken<ArrayList<Note>>(){}.getType();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(notesList,collectionType);

        em.close();

        return json;

    }

    private String createJSONforSingleNote(HttpServletResponse response, int noteNumber) throws IOException {
        EntityManager em = emf.createEntityManager();

        Note note = em.find(Note.class, noteNumber);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(note);

        em.close();

        return json;
    }

    //doPost
    private void saveNoteToDB(Note newNote){
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.persist(newNote);
        transaction.commit();

        em.close();
    }

    //doPut
    private void updateNoteInTheDB(Note updatedNote) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.merge(updatedNote);
        transaction.commit();

        em.close();
    }

    //general helpers
    private String parseURL(HttpServletRequest request){
        String urlString = request.getRequestURI();

        urlString = urlString.replace("notes", "");
        urlString = urlString.replace("/", "");

        return urlString;
    }

    private Note createNoteFromJSON(String json){
        Gson gson = new Gson();

        Note newNote = gson.fromJson(json, Note.class);

        int userId = newNote.getUserId();

        User user = new User();

        user.setId(userId);

        newNote.setUser(user);

        return newNote;
    }
}
