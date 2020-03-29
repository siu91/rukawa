package org.siu.rukawa.datasource.test.service;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.siu.rukawa.datasource.core.anotation.DataSource;
import org.siu.rukawa.datasource.test.jooq.tables.Users;
import org.siu.rukawa.datasource.test.param.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @Author Siu
 * @Date 2020/3/29 9:31
 * @Version 0.0.1
 */
@Slf4j
@Service
@DataSource("primary")
public class UserService {

    @Resource
    DSLContext dsl;

    private static final Random random = new Random();

    @DataSource("#header.ds")
    public void getDsFromHeader() {
        dsl.insertInto(Users.USERS, Users.USERS.ID, Users.USERS.NAME, Users.USERS.AGE, Users.USERS.PASS)
                .values(random.nextLong(), "siu", 29, "pass").execute();
    }

    @DataSource("#session.ds")
    public void getDsFromSession() {
        dsl.insertInto(Users.USERS, Users.USERS.ID, Users.USERS.NAME, Users.USERS.AGE, Users.USERS.PASS)
                .values(random.nextLong(), "siu", 29, "pass").execute();
    }

    @DataSource("#param.ds")
    public void getDsFromParam(Param param) {
        log.info("ds:[{}]", param.getDs());
        dsl.insertInto(Users.USERS, Users.USERS.ID, Users.USERS.NAME, Users.USERS.AGE, Users.USERS.PASS)
                .values(random.nextLong(), "siu", 29, "pass").execute();
    }


}
