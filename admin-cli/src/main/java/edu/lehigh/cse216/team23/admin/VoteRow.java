package edu.lehigh.cse216.team23.admin;

public class VoteRow implements RowData {
    private final int voteId;
    private final int userId;
    private final int postId;
    private final int upDown;

    public VoteRow(int voteId, int userId, int postId, int upDown) {
        this.voteId = voteId;
        this.userId = userId;
        this.postId = postId;
        this.upDown = upDown;
    }

    @Override
    public int getId() {
        return voteId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public int getUpDown() {
        return upDown;
    }

    @Override
    public String toString() {
        return "VoteRow [voteId=" + voteId + ", userId=" + userId + ", postId=" + postId + ", upDown=" + upDown + "]";
    }
}

