package com.example.webapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.example.webapp.entity.Authentication;
import com.example.webapp.repository.AuthenticationMapper;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminServiceImplTest {
	@Tested
	private AdminServiceImpl service;

	@Injectable
	private AuthenticationMapper authenticationMapper;

	/*
	 * getUserListメソッドテスト
	 */
	@Nested
	class getUserList {
		/**
		 * 試験対象：getUserListメソッド_正常系
		 * 試験内容：
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("getUserList_正常系(SELECT結果有り)")
		void doConvert() {
			// select結果を用意
			Authentication auth = new Authentication("username", "password", "ADMIN");
			List<Authentication> selectedList = new ArrayList<>();
			selectedList.add(auth);

			new Expectations() {
				{
					authenticationMapper.selectUserList();
					result = selectedList;
				}
			};
			List<Authentication> resultList = service.getUserList();
			assertEquals(selectedList.get(0), resultList.get(0));

		}

	}

}
