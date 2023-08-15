package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
//@Builder
public class Comment {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
    private Post post;

//    @Column(name = "response_to_comment_id")
    private Integer responseToCommentId;

//    @Column(name = "text")
    private String text;

//    @Column(name = "date_time")
    private LocalDateTime dateTime;

}
