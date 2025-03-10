package com.sesac.backend.usermgmt.ldap;

public enum LdapGroupPrefix {
    S("ou=학생,ou=사용자"),
    P("ou=교수,ou=사용자"),
    E("ou=교직원,ou=사용자");

    private final String baseDn;

    LdapGroupPrefix(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getBaseDn() {
        return baseDn;
    }

}