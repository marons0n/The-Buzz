package edu.lehigh.cse216.team23.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MockDatabase extends Database {
    private Map<Integer, RowData> ideas;
    private Map<String, UserRowData> users;
    private Map<Integer, CommentRowData> comments;
    private int nextIdeaId = 1;
    private int nextCommentId = 1;

    public MockDatabase() {
        ideas = new HashMap<>();
        users = new HashMap<>();
        comments = new HashMap<>();
    }

    @Override
    public RowData selectOne(int id) {
        return ideas.get(id);
    }

    @Override
    public ArrayList<RowData> selectAll() {
        return new ArrayList<>(ideas.values());
    }

    @Override
    public int insertRow(String message, int userId) {
        RowData newIdea = new RowData(nextIdeaId, message, 0, userId, 1);
        ideas.put(nextIdeaId, newIdea);
        nextIdeaId++;
        return 1;
    }

    @Override
    public int updateOne(int id, String message, int visible) {
        if (ideas.containsKey(id)) {
            RowData oldIdea = ideas.get(id);
            RowData updatedIdea = new RowData(id, message, oldIdea.mVotes(), oldIdea.userId(), visible);
            ideas.put(id, updatedIdea);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteRow(int id) {
        return ideas.remove(id) != null ? 1 : 0;
    }

    @Override
    public UserRowData selectOneUser(String id) {
        return users.get(id);
    }

    @Override
    public ArrayList<UserRowData> selectAllUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public int insertUserRow(String name, String email, String genderIdentity, String sexualOrientation) {
        String id = String.valueOf(users.size() + 1);
        UserRowData newUser = new UserRowData(id, name, email, genderIdentity, sexualOrientation, 0);
        users.put(id, newUser);
        return 1;
    }

    @Override
    public int updateOneUser(String id, String name, String email, int restricted) {
        if (users.containsKey(id)) {
            UserRowData oldUser = users.get(id);
            UserRowData updatedUser = new UserRowData(id, name, email, oldUser.uGender_identity(), oldUser.uSexual_orientation(), restricted);
            users.put(id, updatedUser);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteUserRow(int id) {
        return users.remove(String.valueOf(id)) != null ? 1 : 0;
    }

    @Override
    public ArrayList<CommentRowData> selectAllComments() {
        return new ArrayList<>(comments.values());
    }

    @Override
    public int insertComment(int userId, int postId, String message) {
        CommentRowData newComment = new CommentRowData(nextCommentId, userId, postId, message);
        comments.put(nextCommentId, newComment);
        nextCommentId++;
        return 1;
    }

    @Override
    public int deleteComment(int id) {
        return comments.remove(id) != null ? 1 : 0;
    }

    // Add other necessary methods and override them as needed for testing
}