import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SingletonEntityManagerFactory {
    private static EntityManagerFactory entityManagerFactory = null;

    public static EntityManagerFactory getInstance() {
        if(entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("szkolna_17");
        }
        return entityManagerFactory;

    }

    private SingletonEntityManagerFactory() {
    }
}
