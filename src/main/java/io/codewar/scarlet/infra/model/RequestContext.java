package io.codewar.scarlet.infra.model;

public class RequestContext {
    private static final ThreadLocal<AuthorizedUser> userHolder = new ThreadLocal<>();

    public static AuthorizedUser setUser(AuthorizedUser user) {
        userHolder.set(user);
        return user;
    }

    public static AuthorizedUser getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}
