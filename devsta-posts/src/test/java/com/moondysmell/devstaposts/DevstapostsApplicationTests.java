package com.moondysmell.devstaposts;

import com.moondysmell.devstaposts.domain.document.User;
import com.moondysmell.devstaposts.domain.dto.PostsSaveRequestDto;
import com.moondysmell.devstaposts.repository.PostsRepository;
import com.moondysmell.devstaposts.repository.UserRepository;
import com.moondysmell.devstaposts.service.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DevstapostsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private PostsService postsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostsRepository postsRepository;

	private List<User> writer;

	@BeforeEach
	private void init(){
		this.writer = userRepository.findAll();
	}

	@Test
	public void save() {
		User user = User.builder().userId(writer.get(0).getUserId()).nickName(writer.get(0).getNickName()).build();
		PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder().contents("성공했음좋겠다").build();
		postsService.post(postsSaveRequestDto, user);

	}
}
