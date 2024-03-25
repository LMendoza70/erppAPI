package com.gisnet.erpp.security;


import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;


@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return userName != null ? userName : "auditor";
    }
}
