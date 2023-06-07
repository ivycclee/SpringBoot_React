package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import app.model.Entrant;
import app.model.Venue;
import app.repository.EntrantRepository;
import app.repository.VenueRepository;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class MyService {
    @Autowired
    private EntrantRepository erepo;
    @Autowired
    private VenueRepository vrepo;

    public List<Entrant> search(Date start, Date end, String country) {
        return erepo.findByDateOfFinalBetweenAndArtistCountryContainingIgnoreCaseOrderByTotalPointsDesc(start, end, country);
    }

    public Entrant findEntrantById(Long id) {
        return erepo.findById(id).get();
    }

    public Venue findVenueById(Long id) {
        return vrepo.findById(id).get();
    }

    public void updateEntrant(Entrant e) {
        erepo.save(e);
    }

    public List<Integer> getAllYears() {
        return erepo.getAllYears();
    }

    public List<String> getAllSections() {
        return erepo.getAllSections();
    }

    public List<Entrant> getBySectionAndYear(String section, int year) {
        return erepo.findBySectionAndYear(section, year);
    }

}
