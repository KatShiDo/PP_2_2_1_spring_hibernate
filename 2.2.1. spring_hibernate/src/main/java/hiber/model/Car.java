package hiber.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private int series;

    @OneToOne(mappedBy = "car")
    private User user;
}
