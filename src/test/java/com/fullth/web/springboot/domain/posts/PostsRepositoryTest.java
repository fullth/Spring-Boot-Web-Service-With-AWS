package com.fullth.web.springboot.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";

        /** 별도의 설정 없이 @SpringBootTest 사용 시 H2 Database를 자동으로 실행함 */
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("test@naver.com")
                .build());

        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        Assertions.assertEquals(posts.getTitle(), title);
        Assertions.assertEquals(posts.getContent(), content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2022, 2, 5, 0, 0, 0);

        postsRepository.save(Posts.builder()
                        .title("tittle")
                        .content("content")
                        .author("fullth")
                        .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        LocalDateTime createdDate = posts.getCreatedDate();
        LocalDateTime modifiedDate = posts.getModifiedDate();

        System.out.println(">>>>> createdDate=" + createdDate);
        System.out.println(">>>>> modifiedDate=" + modifiedDate);
    }
}