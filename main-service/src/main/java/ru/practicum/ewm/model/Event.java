package ru.practicum.ewm.model;

import lombok.*;
import ru.practicum.ewm.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "events", schema = "public")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false, length = 120)
    private String title;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private Long confirmedRequests;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    private Integer participantLimit;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(nullable = false, name = "created_On")
    private LocalDateTime createdOn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    private Long views;

    private LocalDateTime publishedOn;

    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @Column(nullable = false)
    private Boolean requestModeration;

    @Transient
    private final String datePattern = Pattern.DATE;

    @Transient
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

}