package ch.hftm.repository;

import ch.hftm.entity.Like;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LikeRepository implements PanacheRepository<Like> {
}
