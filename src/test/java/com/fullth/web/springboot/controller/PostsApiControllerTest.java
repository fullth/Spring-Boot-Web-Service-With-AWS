package com.fullth.web.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullth.web.springboot.domain.posts.Posts;
import com.fullth.web.springboot.domain.posts.PostsRepository;
import com.fullth.web.springboot.dto.PostsSaveRequestDto;
import com.fullth.web.springboot.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest를 사용할 경우 JPA 기능이 작동하지 않아, 아래와 같이 테스트해야 함.
 * HelloControllerTest 처럼 이후 버전은 문제 없음.
 * */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
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
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertEquals(all.get(0).getTitle(), title);
        Assertions.assertEquals(all.get(0).getContent(), content);
    }

    @Test
    @WithMockUser(roles = "USER")
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

        //when
        mockMvc.perform(put("/api/v1/posts/" + updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertEquals(all.get(0).getTitle(), expectedTitle);
        Assertions.assertEquals(all.get(0).getContent(), expectedContent);
    }

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
}