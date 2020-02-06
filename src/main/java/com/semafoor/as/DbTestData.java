package com.semafoor.as;

import com.semafoor.as.security.Roles;
import com.semafoor.as.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
@DependsOn("liquibase")
public class DbTestData {

    @PersistenceContext
    private EntityManager entityManager;

    private final PlatformTransactionManager transactionManager;

    private PasswordEncoder encoder;

    public DbTestData(PasswordEncoder encoder, PlatformTransactionManager transactionManager) {
        this.encoder = encoder;
        this.transactionManager = transactionManager;
    }

    @PostConstruct
    public void enterTestData() {
        TransactionStatus transaction = transactionManager.getTransaction(TransactionDefinition.withDefaults());

        log.info("Creating user test data");

        User admin = User.builder().username("admin")
                .password(encoder.encode("admin"))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .accountNonLocked(true).build();
        admin.addAuthorities(Roles.ADMIN, Roles.USER);

        User user = User.builder().username("user")
                .password(encoder.encode("user"))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .enabled(true).build();
        user.addAuthorities(Roles.USER);

        entityManager.persist(admin);
        entityManager.persist(user);

        transactionManager.commit(transaction);
    }
}
