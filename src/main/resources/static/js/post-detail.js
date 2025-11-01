document.addEventListener("DOMContentLoaded", function () {
    // ===== 페이지 컨텍스트 =====
    const root    = document.getElementById("postDetail");
    const boardId = root ? root.dataset.boardId : null;
    const postId  = root ? root.dataset.postId  : null;

    // ===== 좋아요 토글 =====
    const likeButton = document.getElementById("likeButton");

    if (likeButton) {
        likeButton.addEventListener("click", async function () {
            if (!boardId || !postId) {
                console.warn("boardId/postId 누락");
                return;
            }

            try {
                likeButton.disabled = true;

                const res = await fetch(`/boards/${boardId}/posts/${postId}/like`, {
                    method: "POST"
                });

                if (res.status === 401) {
                    alert("로그인이 필요합니다.");
                    location.href = "/login";
                    return;
                }
                if (!res.ok) {
                    alert("좋아요 처리 중 오류가 발생했습니다.");
                    return;
                }

                // redirect/HTML 응답 → 새로고침으로 반영
                location.reload();
            } catch (e) {
                alert("서버 오류가 발생했습니다.");
            } finally {
                likeButton.disabled = false;
            }
        });
    }

    // ===== 댓글 인라인 수정 =====
    document.querySelectorAll(".edit-btn").forEach((button) => {
        button.addEventListener("click", function () {
            const commentItem = button.closest(".comment-item");
            if (!commentItem) return;

            const contentEl = commentItem.querySelector(".comment-content");
            if (!contentEl) return;

            const oldText   = contentEl.textContent.trim();
            const commentId = commentItem.dataset.commentId;

            // 수정폼 UI
            const textarea  = document.createElement("textarea");
            textarea.className = "form-control mb-2";
            textarea.value = oldText;

            const saveBtn = document.createElement("button");
            saveBtn.className = "btn btn-primary btn-sm me-2";
            saveBtn.textContent = "저장";

            const cancelBtn = document.createElement("button");
            cancelBtn.className = "btn btn-secondary btn-sm";
            cancelBtn.textContent = "취소";

            contentEl.style.display = "none";
            contentEl.insertAdjacentElement("afterend", textarea);
            textarea.insertAdjacentElement("afterend", saveBtn);
            saveBtn.insertAdjacentElement("afterend", cancelBtn);

            // 저장
            saveBtn.addEventListener("click", function () {
                const newContent = textarea.value.trim();
                if (!newContent) return alert("내용을 입력하세요");
                if (!boardId || !postId || !commentId) {
                    return alert("필수 식별자(boardId/postId/commentId)가 없습니다.");
                }

                fetch(`/boards/${boardId}/posts/${postId}/comments/${commentId}/edit`, {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `content=${encodeURIComponent(newContent)}`
                })
                    .then((res) => {
                        if (res.ok) {
                            contentEl.textContent = newContent;
                            textarea.remove(); saveBtn.remove(); cancelBtn.remove();
                            contentEl.style.display = "block";
                        } else {
                            alert("댓글 수정 실패");
                        }
                    })
                    .catch(() => alert("서버 오류가 발생했습니다."));
            });

            // 취소
            cancelBtn.addEventListener("click", function () {
                textarea.remove(); saveBtn.remove(); cancelBtn.remove();
                contentEl.style.display = "block";
            });
        });
    });

    // ===== textarea 자동 높이 조절 =====
    document.querySelectorAll(".comment-form textarea").forEach((textarea) => {
        textarea.style.overflow = "hidden";
        textarea.style.resize   = "none";
        textarea.style.height   = textarea.scrollHeight + "px";
        textarea.addEventListener("input", () => {
            textarea.style.height = "auto";
            textarea.style.height = textarea.scrollHeight + "px";
        });
    });
});
