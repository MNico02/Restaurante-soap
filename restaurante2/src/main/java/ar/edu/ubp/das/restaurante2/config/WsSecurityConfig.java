package ar.edu.ubp.das.restaurante2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import java.util.Collections;
import java.util.List;
@EnableWs
@Configuration
public class WsSecurityConfig implements WsConfigurer {
    @Value("${ws.security.username}")
    private String username;
    @Value("${ws.security.password}")
    private String password;
    @Bean
    public Wss4jSecurityInterceptor securityInterceptor() {
        Wss4jSecurityInterceptor si = new Wss4jSecurityInterceptor();
        si.setValidationActions(WSHandlerConstants.USERNAME_TOKEN);
        si.setValidationCallbackHandler(passwordValidationCallbackHandler());
        return si;
    }
    @Bean
    public SimplePasswordValidationCallbackHandler
    passwordValidationCallbackHandler() {
        SimplePasswordValidationCallbackHandler cb = new
                SimplePasswordValidationCallbackHandler();
        cb.setUsersMap(Collections.singletonMap(username, password));
        return cb;
    }
    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor());
    }
}

