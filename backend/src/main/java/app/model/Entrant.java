package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Entrant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="venue_ID")
    private Integer venueID;

    private String logo;

    @Column(name="host_City")
    @NotBlank(message="{ev.hostCityNull}")
    private String hostCity;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message="{ev.pastOrPresent}")
    @Column(name="date_Of_Final")
    private Date dateOfFinal;

    @Column(name="hostCountry")
    @NotBlank(message="{ev.hostCountryNull}")
    @Size(min=2, max=64, message="{ev.hostCountrySize}")
    private String hostCountry;

    @NotBlank(message="{ev.sectionNull}")
    private String section;

    @NotBlank(message="{ev.artistNull}")
    @Size(min=2, max=100, message="{ev.artistSize}")
    private String artist;

    @NotBlank(message="{ev.songNull}")
    private String song;

    @Column(name="artist_Country")
    @Size(min=2, max=64, message="{ev.artistCountrySize}")
    @NotBlank(message="{ev.artistCountryNull}")
    private String artistCountry;

    @Column(name="running_Order")
    @NotBlank(message="{ev.runningOrderNull}")
    private String runningOrder;

    @Column(name="total_Points")
    @NotNull(message="{ev.totalPointsNull}")
    @PositiveOrZero(message="{ev.totalPointsPositiveOrZero}")
    private Integer totalPoints;

    @NotBlank(message="{ev.rankNull}")
    private String rank;
    @NotBlank(message="{ev.qualifiedNull}")
    private String qualified;
}
