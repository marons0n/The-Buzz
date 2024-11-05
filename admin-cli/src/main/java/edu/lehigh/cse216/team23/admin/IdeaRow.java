package edu.lehigh.cse216.team23.admin;

public class IdeaRow implements RowData {
    private final int mId;
    private final String mSubject;
    private final String mMessage;
    private final int mVotes;

    public IdeaRow(int mId, String mSubject, String mMessage, int mVotes) {
        this.mId = mId;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.mVotes = mVotes;
    }

    @Override
    public int getId() {
        return mId;
    }

    public String getSubject() {
        return mSubject;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getVotes() {
        return mVotes;
    }

    @Override
    public String toString() {
        return "IdeaRow [id=" + mId + ", subject=" + mSubject + ", message=" + mMessage + ", votes=" + mVotes + "]";
    }
}

