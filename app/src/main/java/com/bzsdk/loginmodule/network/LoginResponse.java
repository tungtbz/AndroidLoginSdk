package com.bzsdk.loginmodule.network;

public class LoginResponse extends BaseResponse {
    public String accessToken;
    public String refreshToken;
    public String expireIn;
}
