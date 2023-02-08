package com.catalog.infrastructure.video.persistence;

import com.catalog.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;


public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

   @Query("""
           select new com.catalog.domain.video.VideoPreview(
                v.id as id,
                v.title as title,
                v.description as description,
                v.createdAt as createdAt,
                v.updatedAt as updatedAt
           )
           from Video v
                join v.castMembers members
                join v.categories categories
                join v.genres genres
           where
                ( :terms is null or UPPER(v.title) like :terms )
           and
                ( :castMembers is null or members.id.castMemberId )
           and
                ( :categories is null or categories.id.categoryId )
           and
                ( :genres is null or genres.id.genresId )
           """)
    Page<VideoPreview> findAll(
            @Param("terms") String terms,
            @Param("castMembers") Set<String> castMembers,
            @Param("categories") Set<String> categories,
            @Param("genres") Set<String> genres,
            PageRequest page);
}
