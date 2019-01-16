
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void populateDb(EntityManager em) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date birthDate1 = Calendar.getInstance().getTime();
//        try {
//            birthDate1 = sdf.parse("1997-07-21");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        User user = new User();
//        user.setName("karol");
//        user.setNick("notarian");
//        user.setSurname("trzaska");
//        user.setEmail("karoltrzaska19@gmail.com");
//
//        Comment comment = new Comment();
//
//        comment.setContent("hahaha");
//        comment.setUser(user);
//        comment.setDate(birthDate1);
//        Note note = new Note();
//        note.setContent("hehehe");
//        note.setDate(birthDate1);
//
//
//        note.setTitle("Title");
//        note.setUser(user);
//        comment.setNote(note);
//        EntityTransaction transaction = em.getTransaction();
//        transaction.begin();
//        em.persist(note);
//        em.persist(comment);
//
//        em.persist(user);
//
//
//        transaction.commit();
    }

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("szkolna_17");
        EntityManager em = emf.createEntityManager();

        populateDb(em);
        em.clear(); //clear hibernate cache - force next statements to read data from db

        em.close();
        emf.close();

    }
}
