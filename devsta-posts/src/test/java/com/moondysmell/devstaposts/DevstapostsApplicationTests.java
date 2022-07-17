package com.moondysmell.devstaposts;

import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.repository.PostsRepository;
import com.moondysmell.devstaposts.service.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
class DevstapostsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private PostsService postsService;

	@Autowired
	private PostsRepository postsRepository;

//	@BeforeEach
//	private void init(){
//		this.writer = userRepository.findAll();
//	}

	@Test
	public void save() {

		PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder().userId("향긔1").contents("성공했음좋겠다")
				.pictureUrl(null).build();
		postsService.save(postsSaveRequestDto);

	}
}
