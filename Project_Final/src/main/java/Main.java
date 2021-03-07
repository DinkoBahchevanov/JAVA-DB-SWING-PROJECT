import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("project");
        EntityManager em = emf.createEntityManager();
        Engine engine = new Engine(em);

        engine.run();
    }
}
