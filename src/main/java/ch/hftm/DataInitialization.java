package ch.hftm;

import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogStatus;
import ch.hftm.repository.BlogRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitialization {

    @Inject
    BlogRepository blogRepository;

    @Transactional
    public void init(@Observes StartupEvent event) {
        if (blogRepository.listAll().isEmpty()) {
            // PENDING blog
            Blog blog1 = new Blog("Hallo Welt", "Dies ist der erste Blogeintrag, der noch auf Validierung wartet.");
            
            // APPROVED blog
            Blog blog2 = new Blog("Frühlingsgefühle", "Der Frühling ist endlich da, Zeit für neue Anfänge! Die Sonne scheint länger, die Blumen blühen und die Stimmung steigt. Dieses Jahr werde ich meinen Garten neu gestalten und mehr Zeit draußen verbringen. Was sind eure Pläne für den Frühling?");
            blog2.setStatus(BlogStatus.APPROVED);
            
            // REJECTED blog
            Blog blog3 = new Blog("Schlechte Kochtipps", "Egal was ihr kocht, hauptsache viel Salz und Zucker! Mehr braucht es nicht für guten Geschmack. Gesundheit ist überbewertet!");
            blog3.setStatus(BlogStatus.REJECTED);
            blog3.setRejectionReason("Der Blog enthält potenziell gesundheitsschädliche Ratschläge.");

            blogRepository.persist(blog1);
            blogRepository.persist(blog2);
            blogRepository.persist(blog3);
        }
    }
}
