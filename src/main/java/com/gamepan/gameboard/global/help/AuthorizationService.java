package com.gamepan.gameboard.global.help;

import com.gamepan.gameboard.domain.board.entity.Board;
import com.gamepan.gameboard.domain.comment.entity.Comment;
import com.gamepan.gameboard.domain.post.entity.Post;
import com.gamepan.gameboard.domain.report.entity.Report;
import com.gamepan.gameboard.domain.user.entity.Role;
import com.gamepan.gameboard.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


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

    private void forbid(String msg) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, msg);
    }

    public void hasUserPermission(User user, User currentUser, String msg) {
        if (!isAdmin(currentUser) && !isAuthor(user, currentUser)) {
            forbid(msg);
        }
    }

    public void hasPostPermission(Post post, User currentUser, String msg) {
        if (!isAdmin(currentUser) && !isAuthor(post.getUser(), currentUser)) {
            forbid(msg);
        }
    }

    public void hasBoardPermission(User currentUser, String msg) {
        if (!isAdmin(currentUser)) {
            forbid(msg);
        }
    }

    public void hasCommentPermission(Comment comment, User currentUser, String msg) {
        if (!isAdmin(currentUser) && !isAuthor(comment.getUser(), currentUser)) {
            forbid(msg);
        }
    }

    public void hasReportPermission( User currentUser, String msg) {
        if (!isAdmin(currentUser)) {
            forbid(msg);
        }
    }
}
