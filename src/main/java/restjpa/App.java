package restjpa;

import javax.persistence.EntityManager;

import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.hbm.Hbm;
import org.jooby.json.Jackson;

public class App extends Jooby {

  {

    use(new Jackson());

    use(new Hbm(Pet.class));

    use("/api/pets")
        .get("/:id", req -> {
          EntityManager em = req.require(EntityManager.class);
          return em.find(Pet.class, req.param("id").longValue());
        })
        .get("/", req -> {
          EntityManager em = req.require(EntityManager.class);
          return em.createQuery("from Pet", Pet.class)
              .getResultList();
        })
        .post("/", req -> {
          Pet pet = req.body().to(Pet.class);
          EntityManager em = req.require(EntityManager.class);
          em.persist(pet);
          return pet;
        })
        .put("/", req -> {
          Pet pet = req.body().to(Pet.class);
          EntityManager em = req.require(EntityManager.class);
          return em.merge(pet);
        }).delete("/:id", req -> {
          EntityManager em = req.require(EntityManager.class);
          em.remove(em.find(Pet.class, req.param("id").longValue()));
          return Results.noContent();
        });
  }

  public static void main(final String[] args) throws Exception {
    new App().start(args);
  }
}
