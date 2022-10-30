package com.example.gfbf;

public class RecyclearModel {
    String name,age,bio,height,sex,passion,propose,ProfileImageUri;

    public RecyclearModel() {

    }

    public RecyclearModel(String name, String age, String bio, String height, String sex, String passion, String propose, String profileImageUri) {
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.height = height;
        this.sex = sex;
        this.passion = passion;
        this.propose = propose;
        ProfileImageUri = profileImageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassion() {
        return passion;
    }

    public void setPassion(String passion) {
        this.passion = passion;
    }

    public String getPropose() {
        return propose;
    }

    public void setPropose(String propose) {
        this.propose = propose;
    }

    public String getProfileImageUri() {
        return ProfileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        ProfileImageUri = profileImageUri;
    }
}
