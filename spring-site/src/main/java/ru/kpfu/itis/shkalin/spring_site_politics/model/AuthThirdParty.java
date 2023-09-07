package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "third_party_auth")
public class AuthThirdParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_service_id")
    private AuthName authName;

    @Column(name = "token")
    private String token;

    @Column(name = "id_in_service")
    private String idInService;
}
