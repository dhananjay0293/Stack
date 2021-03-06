/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.user.controller;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.user.UserHelper;
import com.user.entity.User;
import com.user.exception.UserException;
import com.user.service.UserService;
import com.user.validation.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;


import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private Errors errors;

    @Mock
    private ObjectError objectError;

    @Test
    public void testGetUserDetails() {
        when(userService.getUserInfo(any(String.class)))
                .thenReturn(UserHelper.getMockUserList());
        ResponseEntity<List<User>> response = userController.getUserInfo("10");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = UserException.class)
    public void testGetUserDetailsWithEmptyId() {
        when(userService.getUserInfo(any(String.class)))
                .thenReturn(UserHelper.getMockUserList());
        ResponseEntity<List<User>> response = userController.getUserInfo("");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testCreateUser() {
        when(userService.createUser(any(User.class)))
                .thenReturn(UserHelper.getMockUser());
        ResponseEntity response = userController.createUser(UserHelper.getMockUser(),errors);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testCreateUserWithNullId() {
        when(userService.createUser(any(User.class)))
                .thenReturn(UserHelper.getMockUserWithoutId());
        List<ObjectError> objectErrors = new ArrayList<>();
        objectErrors.add(objectError);
        when(errors.getAllErrors())
                .thenReturn(objectErrors);
        ResponseEntity response = userController.createUser(UserHelper.getMockUserWithoutId(),errors);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testUpdateUser() {
        when(userService.modifyUser(any(User.class)))
                .thenReturn(UserHelper.getMockUser());
        ResponseEntity response = userController.updateUser(UserHelper.getMockUser());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testDeleteUser() {
        ResponseEntity response = userController.deleteUser("1");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
