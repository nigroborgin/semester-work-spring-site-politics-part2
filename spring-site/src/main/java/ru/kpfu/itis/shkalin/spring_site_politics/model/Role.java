package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role_of_user")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Role(Integer id) {
        this.id = id;
    }

    public Role(String name) {
        this.name = name;
    }
}
