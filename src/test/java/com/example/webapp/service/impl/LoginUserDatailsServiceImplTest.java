package com.example.webapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.webapp.entity.Authentication;
import com.example.webapp.entity.LoginUser;
import com.example.webapp.repository.AuthenticationMapper;

@ExtendWith(MockitoExtension.class)
class LoginUserDatailsServiceImplTest {

	@Mock
	private AuthenticationMapper authenticationMapper;

	@InjectMocks
	private LoginUserDatailsServiceImpl service;

	/*
	 * loadUserByUsernameメソッドテスト
	 */
	@Nested
	class loadUserByUsernameTest {
		/**
		 * 試験対象：loadUserByUsernameメソッド
		 * 試験内容：
		 * ・戻り値とmapperに与えた値が同値となること
		 * ・権限：ADMINのユーザー→権限がADMINとUSERを保持すること
		 * ・例外が発生しないこと
		 * @throws InvocationTargetException 
		 * @throws IllegalAccessException 
		 */
		@Test
		@DisplayName("loadUserByUsername_正常系(ADMINの場合)")
		void returnLoginUser_ADMIN() {
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"),
					new SimpleGrantedAuthority("USER"));
			LoginUser loginUser = new LoginUser("username", "password", authorities);
			/**
			 * mockセット
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);

			/*
			 * 試験
			 */
			UserDetails resultUser = service.loadUserByUsername("username");
			assertDoesNotThrow(() -> service.loadUserByUsername("username"));
			assertAll(
					() -> assertEquals(loginUser.getUsername(), resultUser.getUsername()),
					() -> assertEquals(loginUser.getPassword(), resultUser.getPassword()),
					() -> assertEquals(loginUser.getAuthorities(), resultUser.getAuthorities()));
		}

		/**
		 * 試験対象：loadUserByUsernameメソッド
		 * 試験内容：
		 * ・戻り値とmapperに与えた値が同値となること
		 * ・権限：USERのユーザー→権限がUSERのみを保持すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("loadUserByUsername_正常系(USERの場合)")
		void returnLoginUser_USER() {
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
			LoginUser loginUser = new LoginUser("username", "password", authorities);
			/**
			 * mockセット
			 */
			Authentication auth = new Authentication("username", "password", "USER");
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);

			/*
			 * 試験
			 */
			UserDetails resultUser = service.loadUserByUsername("username");
			assertDoesNotThrow(() -> service.loadUserByUsername("username"));
			assertAll(
					() -> assertEquals(loginUser.getUsername(), resultUser.getUsername()),
					() -> assertEquals(loginUser.getPassword(), resultUser.getPassword()),
					() -> assertEquals(loginUser.getAuthorities(), resultUser.getAuthorities()));
		}

		/**
		 * 試験対象：loadUserByUsernameメソッド
		 * 試験内容：
		 * ・対象ユーザーが存在しないこと
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("loadUserByUsername_異常系")
		void notFindUser() {
			/**
			 * mockセット
			 */
			Authentication auth = null;
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);

			/*
			 * 試験
			 */
			assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("username"));
			try {
				service.loadUserByUsername("username");
			} catch (UsernameNotFoundException e) {
				assertEquals("username => 指定しているユーザー名は存在しません", e.getMessage());
			}
		}

	}

}
