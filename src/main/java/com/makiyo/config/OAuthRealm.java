package com.makiyo.config;

import com.makiyo.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuthRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuthToken;
    }

    /**
     * 授权（验证权限时调用）
     * @param principalCollection
     * @return info
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //TODO查询用户权限列表
        //TODO把权限列表添加到info对象
        return info;
    }

    /**
     * 认证（登录时调用）
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //TODO 从令牌中获取userId,然后检测该账户是否被冻结
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo();
        //TODO 往info对象中添加用户信息,Token字符串
        return info;
    }
}
