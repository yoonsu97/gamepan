package com.gamepan.gameboard.domain.like.service;

import com.gamepan.gameboard.domain.like.entity.Like;
import com.gamepan.gameboard.domain.like.repository.LikeRepository;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.post.repository.PostRepository;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    // 내가 좋아요 한 게시글 페이지네이션
    @Transactional
    public Page<Post> findLikedPostsPage(Long userId, Pageable pageable) {
        return likeRepository.findLikedPosts(userId, pageable);
    }

    @Transactional
    public ToggleResult toggleLike(Long postId, User user) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        var existing = likeRepository.findByPost_IdAndUser_Id(postId, user.getId());

        boolean liked;
        if (existing.isPresent()) {
            // 좋아요 취소
            likeRepository.deleteByPost_IdAndUser_Id(postId, user.getId());
            postRepository.decrementLikeCount(postId);
            liked = false;

            // 화면 일관성용 필드 보정(선택)
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        } else {
            try {
                likeRepository.save(Like.builder().post(post).user(user).build());
                postRepository.incrementLikeCount(postId);
                liked = true;

                // 화면 일관성용 필드 보정(선택)
                post.setLikeCount(post.getLikeCount() + 1);
            } catch (DataIntegrityViolationException e) {
                // 동시성으로 인해 이미 다른 트랜잭션이 저장했을 수 있음
                liked = true;
            }
        }

        return new ToggleResult(liked, post.getLikeCount());
    }

    public record ToggleResult(boolean liked, long likeCount) {}
}
