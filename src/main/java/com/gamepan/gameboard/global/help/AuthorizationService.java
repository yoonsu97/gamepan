package com.gamepan.gameboard.global.help;

import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import com.gamepan.gameboard.global.exception.BusinessException;
import com.gamepan.gameboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorizationService {

    private boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    private boolean isAuthor(User Author, User currentUser) {
        if (Author == null || currentUser == null) return false;
        return Author.getId() != null && Author.getId().equals(currentUser.getId());
    }

    private void exception(ErrorCode error) {
        throw new BusinessException(error);
    }

    public void AdminHasUserPermission( User currentUser, ErrorCode error) {
        if (!isAdmin(currentUser)) {
            exception(error);
        }
    }


    public void hasPostPermission(Post post, User currentUser, ErrorCode error) {
        if (!isAdmin(currentUser) && !isAuthor(post.getUser(), currentUser)) {
            exception(error);
        }
    }

    public void hasBoardPermission(User currentUser, ErrorCode error) {
        if (!isAdmin(currentUser)) {
            exception(error);
        }
    }

    public void hasCommentPermission(Comment comment, User currentUser, ErrorCode error) {
        if (!isAdmin(currentUser) && !isAuthor(comment.getUser(), currentUser)) {
            exception(error);
        }
    }

    public void hasReportPermission( User currentUser, ErrorCode error) {
        if (!isAdmin(currentUser)) {
            exception(error);
        }
    }
}
