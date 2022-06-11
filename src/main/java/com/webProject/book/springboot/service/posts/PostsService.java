package com.webProject.book.springboot.service.posts;

import com.webProject.book.springboot.domain.posts.Posts;
import com.webProject.book.springboot.domain.posts.PostsRepository;
import com.webProject.book.springboot.web.dto.PostsListResponseDto;
import com.webProject.book.springboot.web.dto.PostsResponseDto;
import com.webProject.book.springboot.web.dto.PostsSaveRequestDto;
import com.webProject.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    // 등록
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        return new PostsResponseDto(entity);
    }

    // 목록
    @Transactional(readOnly = true)  // transaction 범위유지 및 조회기능만 남김.  --> 조회속도 개선
    public List<PostsListResponseDto> findAllDesc() {

        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)  // .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());

             // postsRepository 결과로 넘어온 Posts의 stream을
             // map을 통해 PostsListResponseDto 변환후 --> List로 반환
    }

    @Transactional
    public void delete(Long id) {

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        postsRepository.delete(posts);
    }
}
