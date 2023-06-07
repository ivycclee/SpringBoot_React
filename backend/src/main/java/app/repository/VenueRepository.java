package app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import app.model.Venue;

@Repository
public interface VenueRepository extends CrudRepository<Venue, Long> {

}
