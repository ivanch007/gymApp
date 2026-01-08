package org.gym.domain.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "training_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "training_type_name", nullable = false, unique = true, updatable = false)
    private String trainingTypeName;

}
