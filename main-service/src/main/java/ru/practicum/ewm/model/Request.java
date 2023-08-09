package ru.practicum.ewm.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "requests", schema = "public")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User requester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public Request(LocalDateTime created, Event event, User requester, RequestStatus status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}