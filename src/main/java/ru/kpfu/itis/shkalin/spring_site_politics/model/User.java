package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "picture_url")
    private String pictureUrl;

//    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
//    private List<Post> posts;
//
//    public void addPost(Post post) {
//        if (posts == null) {
//            posts = new ArrayList<>();
//        }
//        posts.add(post);
//        post.setUser(this);
//    }
}
