package edu.lehigh.cse216.team23.admin;

public class UserRow implements RowData {
    private final int uId;
    private final String uName;
    private final String uEmail;
    private final String uGenderIdentity;
    private final String uSexualOrientation;

    public UserRow(int uId, String uName, String uEmail, String uGenderIdentity, String uSexualOrientation) {
        this.uId = uId;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uGenderIdentity = uGenderIdentity;
        this.uSexualOrientation = uSexualOrientation;
    }

    @Override
    public int getId() {
        return uId;
    }

    public String getName() {
        return uName;
    }

    public String getEmail() {
        return uEmail;
    }

    public String getGenderIdentity() {
        return uGenderIdentity;
    }

    public String getSexualOrientation() {
        return uSexualOrientation;
    }

    @Override
    public String toString() {
        return "UserRow [id=" + uId + ", name=" + uName + ", email=" + uEmail + ", genderIdentity=" + uGenderIdentity + ", sexualOrientation=" + uSexualOrientation + "]";
    }
}
