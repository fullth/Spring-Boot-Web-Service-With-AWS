package com.fullth.web.springboot.service;

import com.fullth.web.springboot.domain.posts.Posts;
import com.fullth.web.springboot.domain.posts.PostsRepository;
import com.fullth.web.springboot.dto.PostsResponseDto;
import com.fullth.web.springboot.dto.PostsSaveRequestDto;
import com.fullth.web.springboot.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        /** <S extends T> S save(S entity); */
        return postsRepository.save(requestDto.toEntity())
                                                .getId();
    }

    /**
     * JPA의 영속성 컨텍스트가 유지된 상태에서(트랜잭션 안에서 데이터베이안의 데이터를 가져오면)
     * 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영.
     * 즉, Entity 객체의 값만 변경하면 별도로 Update쿼리를 날릴 필요가 없음
     * Ref.) dirty checking
     * */
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다. id= " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다. id= " + id));

        return new PostsResponseDto(entity);
    }
}
