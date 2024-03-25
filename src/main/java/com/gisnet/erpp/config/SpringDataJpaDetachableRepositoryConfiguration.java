package com.gisnet.erpp.config;
import com.gisnet.erpp.ErppApplication;
import io.springlets.data.jpa.repository.support.DetachableJpaRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * = SpringDataJpaDetachableRepositoryConfiguration
 TODO Auto-generated class documentation
 *
 */
@Configuration
@EnableJpaRepositories(repositoryBaseClass = DetachableJpaRepositoryImpl.class, basePackageClasses = ErppApplication.class)
public class SpringDataJpaDetachableRepositoryConfiguration {
}
