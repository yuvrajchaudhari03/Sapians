package com.models;

public class GiveAuthorityModel {
    private Integer authorityTo;
    private String authority;
    private String authorityToAsEmail;

    public Integer getAuthorityTo() {
        return authorityTo;
    }

    public void setAuthorityTo(Integer authorityTo) {
        this.authorityTo = authorityTo;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthorityToAsEmail() {
        return authorityToAsEmail;
    }

    public void setAuthorityToAsEmail(String authorityToAsEmail) {
        this.authorityToAsEmail = authorityToAsEmail;
    }
}
