package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forum_replies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForumReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "thread_id")
    ForumThread thread;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @Column(columnDefinition = "TEXT")
    String content;

    Integer likes;

    Boolean accepted;

}