package ch.hftm;

import ch.hftm.entity.Blog;
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
            Blog blog1 = new Blog("Hallo Welt", "Dies ist der erste Blogeintrag.");
            Blog blog2 = new Blog("Frühlingsgefühle", "Der Frühling ist endlich da, Zeit für neue Anfänge!");
            Blog blog3 = new Blog("Reiseabenteuer", "Ich habe eine spannende Reise durch Europa unternommen.");
            Blog blog4 = new Blog("Kochtipps", "Heute teile ich einige meiner Lieblingsrezepte mit euch.");
            Blog blog5 = new Blog("Technologie-Trends", "Ein Blick auf die neuesten Entwicklungen in der Technologie.");

            blogRepository.persist(blog1);
            blogRepository.persist(blog2);
            blogRepository.persist(blog3);
            blogRepository.persist(blog4);
            blogRepository.persist(blog5);
        }
    }
}
