package com.rokyinfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokyinfo.domain.SysUser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PdmApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy filterChain;

	private MockMvc mvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(this.context).addFilters(this.filterChain).build();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void everythingIsSecuredByDefault() throws Exception {
		this.mvc.perform(get("/").accept(MediaTypes.HAL_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
		this.mvc.perform(get("/users").accept(MediaTypes.HAL_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
		this.mvc.perform(get("/users/1").accept(MediaTypes.HAL_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());

	}

	@Test
	@Ignore
	public void accessingRootUriPossibleWithUserAccount() throws Exception {
		String header = "Basic " + new String(Base64.encode("pdm_client:rokyinfo".getBytes()));
		this.mvc.perform(
				get("/").accept(MediaTypes.HAL_JSON).header("Authorization", header))
				.andExpect(
						header().string("Content-Type", MediaTypes.HAL_JSON.toString()))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void useAppSecretsPlusUserAccountToGetBearerToken() throws Exception {
		String header = "Basic " + new String(Base64.encode("pdm_client:rokyinfo".getBytes()));
		MvcResult result = this.mvc
				.perform(post("/oauth/token").header("Authorization", header)
						.param("grant_type", "password").param("scope", "read")
						.param("username", "admin").param("password", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Object accessToken = this.objectMapper
				.readValue(result.getResponse().getContentAsString(), Map.class)
				.get("access_token");
		MvcResult flightsAction = this.mvc
				.perform(get("/users/1").accept(MediaTypes.HAL_JSON)
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(header().string("Content-Type",
						MediaTypes.HAL_JSON.toString() + ";charset=UTF-8"))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		SysUser mSysUser = this.objectMapper.readValue(
				flightsAction.getResponse().getContentAsString(), SysUser.class);

		assertThat(mSysUser.getUsername()).isEqualTo("admin");
	}


}
