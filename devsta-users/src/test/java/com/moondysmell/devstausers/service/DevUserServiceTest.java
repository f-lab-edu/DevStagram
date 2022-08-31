package com.moondysmell.devstausers.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.moondysmell.devstausers.domain.document.DevUser;
import com.moondysmell.devstausers.domain.dto.UserDetailDto;
import com.moondysmell.devstausers.repository.DevUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(TestConfig.class)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@EnableAutoConfiguration
@DataMongoTest
class DevUserServiceTest {

    @InjectMocks
    private DevUserService userService;

    @MockBean
    private DevUserRepository devUserRepository;

    @Autowired
    @Qualifier("testMongoTemplate")
    private MongoTemplate mongoTemplate;


    DevUserServiceTest() {
    }

    @Test
    @DisplayName("mongoTemplate 커넥션 확인(find 쿼리)")
    public void findOneTest() {
        Query query = new Query(Criteria.where("email").is("momo@gmail.com"));
        List<DevUser> target = mongoTemplate.find(query, DevUser.class);
        Assertions.assertEquals(1, target.size());
        Assertions.assertEquals("김모모", target.get(0).getName());
    }

    @Test
    @DisplayName("mongoTemplate insert 테스트")
    public void insertTest() {
        UserDetailDto dto = UserDetailDto.builder()
                .name("kiki")
                .nickname("kiki")
                .password("kiki1234")
                .pictureUrl(null)
                .createdDt(null)
                .description(null)
                .email("kiki@gmail.com")
                .github(null)
                .blog(null)
                .tags(null)
                .provider("app")
                .build();
        DevUser newUser = new DevUser();
        newUser.ofDetail(dto);
        DevUser savedUser = mongoTemplate.insert(newUser, "DevUser");

        Query query = new Query(Criteria.where("email").is("kiki@gmail.com"));
        DevUser target = mongoTemplate.findOne(query, DevUser.class);
        Assertions.assertEquals(dto.getName(), target.getName());


    }

    @Test
    void findUserByEmail() {
//        //given
//        DevUser devUser = DevUser.builder()
//                .name("mock유저1")
//                .age(20)
//                .build();
//        List<Member> members = new ArrayList<>();
//        members.add(member1);
//
//        given(memberRepository.findAll()).willReturn(members);

        //given
        String targetEmail = "momo@gmail.com";
        DevUser result = userService.findUserByEmail(targetEmail);
        assertEquals(targetEmail, result.getEmail());
        assertEquals("김모모", result.getName());
    }


    @Test
    void checkExistUser() {
//        Query query = new Query(Criteria.where("email").is("dada@gmail.com"));
//        DevUser targetUser = mongoTemplateLocal.findOne(query, DevUser.class);

    }

    @Test
    void findAllUserByEmail() {
    }

    @Test
    void findAllUserByNickname() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updatePw() {
    }

    @Test
    void saveUser() {
    }

    @Test
    void updateProfile() {
    }
}