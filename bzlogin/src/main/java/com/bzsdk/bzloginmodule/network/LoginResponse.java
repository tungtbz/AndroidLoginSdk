package com.bzsdk.bzloginmodule.network;
import com.rofi.core.network.BaseResponse;

public class LoginResponse extends BaseResponse {
    public String accessToken;
    public String refreshToken;
    public String expireIn;
}
