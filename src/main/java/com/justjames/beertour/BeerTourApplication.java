package com.justjames.beertour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.justjames.beertour.security.PublicFilter;
import com.justjames.beertour.security.SecurityFilter;
import com.justjames.beertour.security.SimpleCorsFilter;
import com.justjames.beertour.security.TokenRealm;
import com.justjames.beertour.security.UserRealm;

@SpringBootApplication(exclude=JerseyAutoConfiguration.class)
public class BeerTourApplication extends SpringBootServletInitializer {
	
	private Log log = LogFactory.getLog(BeerTourApplication.class);
	
	@Value("${sslEnabled}")
	private String sslEnabledStr;
	
    public static void main(String[] args) {
        SpringApplication.run(BeerTourApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	return application.sources(applicationClass);
    }

    private static Class<BeerTourApplication> applicationClass = BeerTourApplication.class;
   
    
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
        filterChainDefinitionMapping.put("/login?", "ssl, token");
        filterChainDefinitionMapping.put("/logout?", "ssl, token");
        filterChainDefinitionMapping.put("/users/*/adminEdit*", "ssl, token");
        filterChainDefinitionMapping.put("/users/signup?", "ssl, public");
        filterChainDefinitionMapping.put("/users/", "ssl, token");
        filterChainDefinitionMapping.put("/users?", "ssl, token");
        filterChainDefinitionMapping.put("/beers?", "ssl, token");
        filterChainDefinitionMapping.put("/beers/", "ssl, token");
        filterChainDefinitionMapping.put("/beerlists/**/*", "ssl, token");
        filterChainDefinitionMapping.put("/beers/browse?", "public");
        filterChainDefinitionMapping.put("/health?", "public");
        filterChainDefinitionMapping.put("/*", "public");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);

        Map<String, Filter> filters = new LinkedHashMap<String,Filter>();
        
        SslFilter ssl = new SslFilter();
        ssl.setEnabled(enableSSL());
        log.info("HTTPS port=" + ssl.getPort() + ", SSL filter enabled=" + ssl.isEnabled());
        
        filters.put("token", new SecurityFilter());
        filters.put("public", new PublicFilter());
        filters.put("ssl", ssl);
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
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.init();
        return userRealm;
    }

    @Bean(name = "tokenRealm")
    public TokenRealm tokenRealm() {
        TokenRealm tokenRealm = new TokenRealm();
        tokenRealm.init();
        return tokenRealm;
    }
    
    private boolean enableSSL() {
    	log.debug("sslEnabledStr=" + sslEnabledStr);
    	if (sslEnabledStr.equalsIgnoreCase("true")) {
    		return true;
    	} else {
    		return false;
    	}
    }


}
