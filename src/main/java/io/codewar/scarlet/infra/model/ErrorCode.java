package io.codewar.scarlet.infra.model;

public record ErrorCode(int code, String msg) {

    public final static ErrorCode Success = new ErrorCode(0, "success");
    public final static ErrorCode BadRequest = new ErrorCode(400, "该请求不符合规范");
    public final static ErrorCode Unauthorized = new ErrorCode(401, "用户还未登录,请先登录");
    public final static ErrorCode PermissionDenied = new ErrorCode(403, "权限不足,请联系管理员");

}
