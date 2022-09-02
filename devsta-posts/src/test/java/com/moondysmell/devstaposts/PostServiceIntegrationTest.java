package com.moondysmell.devstaposts;

import com.moondysmell.devstaposts.repository.PostsRepository;
import com.moondysmell.devstaposts.service.PostsService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PostServiceIntegrationTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;




}
