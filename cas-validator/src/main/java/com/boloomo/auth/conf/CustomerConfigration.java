package com.boloomo.auth.conf;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.boloomo.auth.handler.CustomerHandler;


public class CustomerConfigration implements AuthenticationEventExecutionPlanConfigurer{
	
	@Autowired
	@Qualifier(value="servicesManager")
	private ServicesManager servicesManager;
	
	//注册验证器
    @Bean
    public AuthenticationHandler customAuthenticationHandler() {
        //优先验证
        return new CustomerHandler("customerHandler",
                servicesManager, new DefaultPrincipalFactory(), 1);
    }

    //注册自定义认证器
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(customAuthenticationHandler());
    }

}
