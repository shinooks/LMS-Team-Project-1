package com.sesac.backend.assignment.domain;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AssignSubmit {

    @Id
    @GeneratedValue
    private UUID assignSubmitId;

    // TODO
}
