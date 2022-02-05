package com.fullth.web.springboot.controller;

import com.fullth.web.springboot.domain.posts.Posts;
import com.fullth.web.springboot.domain.posts.PostsRepository;
import com.fullth.web.springboot.dto.PostsSaveRequestDto;
import com.fullth.web.springboot.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @WebMvcTest를 사용할 경우 JPA 기능이 작동하지 않아, 아래와 같이 테스트해야 함.
 * HelloControllerTest 처럼 이후 버전은 문제 없음.
 * */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    /**
     * tearDown(): 가장 마지막에 수행되는 메서드
     * <blockquote>
     * setUp()의 반대개념. <br>
     * ex.) 네트워크 연결 종료, DB 연결 종료 등
     * </blockquote>
     * */
    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("fullth")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(responseEntity.getBody() > 0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertEquals(all.get(0).getTitle(), title);
        Assertions.assertEquals(all.get(0).getContent(), content);
    }

    @Test
    public void Posts_수정된다() throws Exception {
        //given
        Posts savePosts = postsRepository.save(Posts.builder()
                        .title("수정 전 타이틀")
                        .content("수정 전 본문")
                        .author("fullth")
                        .build());

        Long updateId = savePosts.getId();
        String expectedTitle = "수정 후 타이틀";
        String expectedContent = "수정 후 본문";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(responseEntity.getBody() > 0L);

        List<Posts> all = postsRepository.findAll();
        Assertions.assertEquals(all.get(0).getTitle(), expectedTitle);
        Assertions.assertEquals(all.get(0).getContent(), expectedContent);
    }
}