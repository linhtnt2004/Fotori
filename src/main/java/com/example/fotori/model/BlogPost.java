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
@Table(name = "blogs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(unique = true, nullable = false)
    String slug;

    @Column(columnDefinition = "TEXT")
    String excerpt;

    @Column(columnDefinition = "LONGTEXT")
    String content;

    String coverImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    BlogCategory category;

    @ElementCollection
    @CollectionTable(name = "blog_tags", joinColumns = @JoinColumn(name = "blog_id"))
    @Column(name = "tag")
    List<String> tags;

    Boolean featured;

    Integer likes;
}