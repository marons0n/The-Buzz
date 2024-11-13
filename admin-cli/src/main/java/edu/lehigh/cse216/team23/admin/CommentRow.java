package edu.lehigh.cse216.team23.admin;

public class CommentRow implements RowData {
    private final int commentId;
    private final int userId;
    private final int postId;
    private final String message;

    public CommentRow(int commentId, int userId, int postId, String message) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.message = message;
    }

    @Override
    public int getId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CommentRow [commentId=" + commentId + ", userId=" + userId + ", postId=" + postId + ", message=" + message + "]";
    }
}
