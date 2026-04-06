package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.application.auth.OAuthUserInfo;

public interface OAuthPort {

    OAuthUserInfo getUserInfo(String authorizationCode);
}
