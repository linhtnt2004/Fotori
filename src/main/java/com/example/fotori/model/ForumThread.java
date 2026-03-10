package com.example.fotori.model;

import com.example.fotori.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forum_threads")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForumThread extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    ForumCategory category;

    @ElementCollection
    @CollectionTable(name = "forum_thread_tags",
        joinColumns = @JoinColumn(name = "thread_id"))
    @Column(name = "tag")
    List<String> tags;

    Integer likes;

}