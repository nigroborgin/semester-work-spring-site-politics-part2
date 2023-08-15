package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "author_of_post")
    private String authorOfPost;

    @Column(name = "text")
    private String text;

    @Column(name = "type")
    private String type;

//    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
//    private List<Comment> comments;
//
//    public void addComment(Comment comment) {
//        if (comments == null) {
//            comments = new ArrayList<>();
//        }
//        if (!comments.contains(comment)) {
//            comments.add(comment);
//            comment.setPost(this);
//        }
//    }

}
