package app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import app.model.Entrant;

import java.util.Date;
import java.util.List;

@Repository
public interface EntrantRepository extends CrudRepository<Entrant, Long> {
    public List<Entrant> findByDateOfFinalBetweenAndArtistCountryContainingIgnoreCaseOrderByTotalPointsDesc(Date start, Date end, String country);

    @Query("SELECT DISTINCT YEAR(e.dateOfFinal) FROM Entrant e ORDER BY YEAR(e.dateOfFinal) DESC")
    public List<Integer> getAllYears();

    @Query("SELECT DISTINCT e.section FROM Entrant e")
    public List<String> getAllSections();

    @Query("SELECT e from Entrant e WHERE e.section= ?1 AND YEAR(e.dateOfFinal) = ?2 ORDER BY e.totalPoints DESC")
    public List<Entrant> findBySectionAndYear(String section, int year);


}
