package com.justjames.beertour.webcfg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.justjames.beertour.security.PublicFilter;
import com.justjames.beertour.security.SecurityFilter;
import com.justjames.beertour.security.SimpleCorsFilter;
import com.justjames.beertour.security.TokenRealm;
import com.justjames.beertour.security.UserRealm;


@Configuration
public class WebConfig {
	
	@Bean(name = "corsFilter") 
	public SimpleCorsFilter simpleCorsFilter() {
		return  new SimpleCorsFilter();
	}
	
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {

        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setLoginUrl(null);
        shiroFilter.setSecurityManager(securityManager());
        
        Map<String, String> filterChainDefinitionMapping = new LinkedHashMap<String, String>();
        filterChainDefinitionMapping.put("/login?", "token");
        filterChainDefinitionMapping.put("/logout?", "token");
        filterChainDefinitionMapping.put("/users/signup?", "public");
        filterChainDefinitionMapping.put("/users/", "token");
        filterChainDefinitionMapping.put("/users?", "token");
        filterChainDefinitionMapping.put("/beers?", "token");
        filterChainDefinitionMapping.put("/beerlists/**/*", "token");
        filterChainDefinitionMapping.put("/beers/browse?", "public");
        filterChainDefinitionMapping.put("/health?", "public");
        filterChainDefinitionMapping.put("/*", "public");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);

        Map<String, Filter> filters = new LinkedHashMap<String,Filter>();
        filters.put("token", new SecurityFilter());
        filters.put("public", new PublicFilter());
        shiroFilter.setFilters(filters);

        return shiroFilter;
    }

    @Bean(name="securityManager")
    public SecurityManager securityManager() {
        Collection<Realm> realms = new ArrayList<Realm>(2);
        realms.add(tokenRealm());
        realms.add(userRealm());

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealms(realms);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        return securityManager;
    }

    @Bean(name = "userRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.init();
        return userRealm;
    }

    @Bean(name = "tokenRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public TokenRealm tokenRealm() {
        TokenRealm tokenRealm = new TokenRealm();
        tokenRealm.init();
        return tokenRealm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	

}