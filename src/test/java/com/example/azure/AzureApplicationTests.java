package com.example.azure;

import com.example.azure.persistence.models.*;
import com.example.azure.persistence.repository.UserActivationRequestRepository;
import com.example.azure.persistence.repository.UserPasswordChangeRequestRepository;
import com.example.azure.service.UserService;
import com.example.azure.web.TestConfig;
import com.example.azure.web.TestContext;
import com.example.azure.web.UsersController;
import com.example.azure.web.cilent.UserServiceClient;
import com.example.azure.web.data.LoginData;
import com.example.azure.web.data.RegisterUserData;
import com.example.azure.web.data.ResetUserPasswordData;
import com.example.azure.web.responses.Responses;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = "server_port=8080" )

@EnableFeignClients
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AzureApplicationTests {

	public static final String REGISTER_USER_ID = "registerUser.id";
	public static final String REGISTER_USER_DATA = "registerUser.data";

	@Autowired
	private TestContext testContext;
	@Autowired
	private UserService userService;
	@Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private UserActivationRequestRepository userActivationRequestRepository;
	@Autowired
	private UserPasswordChangeRequestRepository userPasswordChangeRequestRepository;

	@Test
	@Order(0)
	public void test001_RegisterUser() {

		final RegisterUserData registerUserData = RegisterUserData.builder()
				.email(String.format("test%s@gmail.com", RandomStringUtils.randomNumeric(5)))
				.firstName("testFirstName")
				.lastName("testLastName")
				.password("123456").build();
		testContext.addAttribute(REGISTER_USER_DATA, registerUserData);

		final ResponseEntity<?> registerUserDataResponse = userServiceClient.usersRegister(registerUserData);
		assertThat(registerUserDataResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(registerUserDataResponse.getBody()).isEqualTo(Responses.created(UsersController.USER_SUCCESSFULLY_REGISTERED).getBody());

		final UserModel userModel = testContext.getAttribute(TestConfig.EMAIL_SERVICE_ACTIVATE_REGISTER_USER_MODEL);
		assertThat(userModel.getFirstName()).isEqualTo(registerUserData.getFirstName());
		assertThat(userModel.getEmail()).isEqualTo(registerUserData.getEmail());
		assertThat(userModel.getLastName()).isEqualTo(registerUserData.getLastName());
		assertThat(userModel.getUserStatus()).isEqualTo(UserStatusEnum.CREATED);
		assertThat(userModel.getPassword()).isNotEqualTo(registerUserData.getPassword());

		final UserActivationRequestModel userActivationRequestModel = testContext.getAttribute(TestConfig.EMAIL_SERVICE_ACTIVATE_USER_ACTIVATION_REQUEST_MODEL);
		assertThat(userActivationRequestModel.getUserId()).isEqualTo(userModel.getId());
		assertThat(userActivationRequestModel.getRequestStatus()).isEqualTo(RequestStatusEnum.OPEN);

		assertThat(userServiceClient.getUsers().stream().map(UserModel::getId).collect(Collectors.toList())).contains(userModel.getId());

		testContext.addAttribute(REGISTER_USER_ID, userModel.getId());
	}

	@Test
	@Order(1)
	public void test002_RegisterUserIsNotAbleToLogin() {

		final RegisterUserData registerUserData = getRegisterUserData();

		assertThrows(feign.FeignException.Unauthorized.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				userServiceClient.usersLogin(registerUserData.getEmail(), registerUserData.getPassword());
			}
		});
	}

	@Test
	@Order(2)
	public void test003_ActivateUser() {
		final UserActivationRequestModel userActivationRequestModel = testContext.getAttribute(TestConfig.EMAIL_SERVICE_ACTIVATE_USER_ACTIVATION_REQUEST_MODEL);
		userServiceClient.usersActivate(userActivationRequestModel.getId());
		final UserModel user = getRegisterUserModel();
		assertThat(user.getUserStatus()).isEqualTo(UserStatusEnum.ACTIVE);
		final UserActivationRequestModel refreshedUserActivationRequestModel = userActivationRequestRepository.findById(userActivationRequestModel.getId()).orElse(userActivationRequestModel);
		assertThat(refreshedUserActivationRequestModel.getRequestStatus()).isEqualTo(RequestStatusEnum.CLOSED);
	}

	@Test
	@Order(3)
	public void test004_ActivatedUserIsAbleToLogin() {
		final RegisterUserData registerUserData = getRegisterUserData();
		final ResponseEntity<LoginData> loginDataResponse = userServiceClient.usersLogin(registerUserData.getEmail(), registerUserData.getPassword());
		assertThat(loginDataResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(loginDataResponse.getBody().getAccessToken()).isNotNull();
		assertThat(loginDataResponse.getBody().getUserId()).isEqualTo(getRegisterUserModel().getId());
	}

	@Test
	@Order(3)
	public void test005_ResetUserPasswordInitiate() {
		final ResponseEntity<?> passwordResetInitiateResponse = userServiceClient.usersPasswordResetInitiate(getRegisterUserData().getEmail());
		assertThat(passwordResetInitiateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		final UserModel userModel = testContext.getAttribute(TestConfig.EMAIL_SERVICE_RESET_USER_MODEL);
		assertThat(userModel).isNotNull();
		assertThat(userModel.getId()).isEqualTo(getRegisterUserModel().getId());
		final UserPasswordChangeRequestModel userPasswordChangeRequestModel = getUserResetPasswordRequest();
		assertThat(userPasswordChangeRequestModel.getUsedId()).isEqualTo(userModel.getId());
		assertThat(userPasswordChangeRequestModel.getRequestStatus()).isEqualTo(RequestStatusEnum.OPEN);
	}

	@Test
	@Order(4)
	public void test005_ResetUserPasswordComplete() {
		final UserPasswordChangeRequestModel userResetPasswordRequest = getUserResetPasswordRequest();
		final ResetUserPasswordData resetPasswordData = ResetUserPasswordData.builder().password("Qwerty123").build();
		userServiceClient.usersPasswordResetSubmit(userResetPasswordRequest.getId(), resetPasswordData);
		assertThrows(feign.FeignException.Unauthorized.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				userServiceClient.usersLogin(getRegisterUserData().getEmail(), getRegisterUserData().getPassword());
			}
		});
		final ResponseEntity<LoginData> loginWithNewPasswordResponse = userServiceClient.usersLogin(getRegisterUserData().getEmail(), resetPasswordData.getPassword());
		assertThat(loginWithNewPasswordResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getUserResetPasswordRequest().getRequestStatus()).isEqualTo(RequestStatusEnum.CLOSED);
	}

	private UserModel getRegisterUserModel() {
		final String registerUserId = testContext.getAttribute(REGISTER_USER_ID);
		return userService.getUserForId(registerUserId);
	}

	private RegisterUserData getRegisterUserData() {
		return testContext.getAttribute(REGISTER_USER_DATA);
	}

	private UserPasswordChangeRequestModel getUserResetPasswordRequest() {
		final UserPasswordChangeRequestModel userPasswordChangeRequestModel = testContext.getAttribute(TestConfig.EMAIL_SERVICE_RESET_ACTIVATION_REQUEST_MODEL);
		return userPasswordChangeRequestRepository.findById(userPasswordChangeRequestModel.getId()).orElse(userPasswordChangeRequestModel);
	}
}
