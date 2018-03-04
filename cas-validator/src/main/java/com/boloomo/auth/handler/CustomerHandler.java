package com.boloomo.auth.handler;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.FailedLoginException;

import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import com.boloomo.cas.utils.MD5;


public class CustomerHandler extends AbstractPreAndPostProcessingAuthenticationHandler  {

	public CustomerHandler(String name, ServicesManager servicesManager,
			PrincipalFactory principalFactory, Integer order) {
		super(name, servicesManager, principalFactory, order);
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof UsernamePasswordCredential;
	}

	@Override
	protected HandlerResult doAuthentication(Credential credential)
			throws GeneralSecurityException, PreventedException {
		UsernamePasswordCredential usernamePasswordCredentia = (UsernamePasswordCredential) credential;

        //获取传递过来的用户名和密码
        String username = usernamePasswordCredentia.getUsername();
        String password1 = usernamePasswordCredentia.getPassword();
        String password = MD5.md5(password1);

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            //直接是原生的数据库配置啊
            String url = "jdbc:mysql://192.168.129.69:3306/boloomodb_test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false";
            String user = "blm";
            String pass = "boloomodb";

            conn = DriverManager.getConnection(url,user, pass);

            //查询语句
            String sql = "SELECT * FROM casuser WHERE username =? AND password = ?";
            PreparedStatement  ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                //存放数据到里面
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("username", rs.getString("username"));
                result.put("password", rs.getString("password"));
                result.put("department", rs.getString("department"));
                result.put("company", rs.getString("company"));
                result.put("phone", rs.getString("phone"));
                result.put("email", rs.getString("email"));
                //允许登录，并且通过this.principalFactory.createPrincipal来返回用户属性
                return createHandlerResult(credential, this.principalFactory.createPrincipal(username, result), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      //当是admin用户的情况，直接就登录了，
        if(username.startsWith("admin")) {
            //直接返回去了
            return createHandlerResult(credential, this.principalFactory.createPrincipal(username, Collections.emptyMap()), null);
        }else if (username.startsWith("lock")) {
            //用户锁定
            throw new AccountLockedException();
        } else if (username.startsWith("disable")) {
            //用户禁用
            throw new AccountDisabledException();
        } else if (username.startsWith("invali")) {
            //禁止登录该工作站登录
            throw new InvalidLoginLocationException();
        } else if (username.startsWith("passorwd")) {
            //密码错误
            throw new FailedLoginException();
        } else if (username.startsWith("account")) {
            //账号错误
            throw new AccountLockedException();
        }
        return null;
		
	}

}
