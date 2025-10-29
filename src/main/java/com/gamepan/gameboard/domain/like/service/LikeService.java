package com.gamepan.gameboard.domain.like.service;

import com.gamepan.gameboard.domain.like.entity.Like;
import com.gamepan.gameboard.domain.like.repository.LikeRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public ToggleResult toggleLike(Long postId, User user) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "게시글을 찾을 수 없습니다."));

        var existing = likeRepository.findByPost_IdAndUser_Id(postId, user.getId());

        boolean liked;
        if (existing.isPresent()) {
            // 이미 좋아요 → 취소
            likeRepository.delete(existing.get());
            liked = false;
        } else {
            // 아직 안 눌렀음 → 추가
            likeRepository.save(Like.builder().post(post).user(user).build());
            liked = true;
        }

        long cnt = likeRepository.countByPost_Id(postId);
        post.setLikeCount((int) cnt);

        return new ToggleResult(liked, cnt);
    }

    public record ToggleResult(boolean liked, long likeCount) {}
}
