package app.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name="venue_ID")
    Integer venueID;
    String name;
    Integer capacity;
    String note;
}

