package edu.otensoft.incident.api.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_incident")
public class Incident extends BaseEntity<Long> {
    
    private static final long serialVersionUID = 1L;

    @NonNull
    private String name;

    @NonNull
    private String description;

    private LocalDateTime closedAt;

    // TODO add valid or audit?
    // TODO ignore in construct
    // todo criar status
    // private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;
    // delete virtual
    //private boolean deleted;

    public Incident(){}

    public Incident(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

}
