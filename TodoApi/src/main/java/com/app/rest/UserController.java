package com.app.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.UserDTO;
import com.app.entity.Users;
import com.app.exception.TodoException;
import com.app.request.UserSigninRequest;
import com.app.request.UserSignupRequest;
import com.app.response.ApiResponse;
import com.app.services.CustomUserDetails;
import com.app.services.IUserService;
import com.app.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/user")
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private IUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/public/signup")
	public ResponseEntity<ApiResponse<?>> userSignUp(@Valid @RequestBody UserSignupRequest request,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new TodoException("Invalid User Input", HttpStatus.BAD_REQUEST);
		}

		Optional<Users> findUser = userService.findByEmail(request.getEmail());

		if (findUser.isPresent()) {
			throw new TodoException("Email Already Exists", HttpStatus.CONFLICT);
		}

		Users user = new Users();
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user = userService.create(user);

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("role", "user");
		claims.put("userId", user.getId());

		String token = jwtUtil.generateToken(user.getEmail(), claims);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("token", token);
		responseData.put("user", new UserDTO(user.getId(), user.getName(), user.getEmail()));

		return ResponseEntity.ok(new ApiResponse<>("success", "Account created Successfully", responseData));
	}

	@PostMapping("/public/signin")
	public ResponseEntity<ApiResponse<?>> userSignIn(@Valid @RequestBody UserSigninRequest request,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new TodoException("Invalid User Input", HttpStatus.BAD_REQUEST);
		}

		Users user = userService.findByEmail(request.getEmail())
				.orElseThrow(() -> new TodoException("Email Not Found", HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new TodoException("Invalid Password", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("role", "user");
		claims.put("userId", user.getId());

		String token = jwtUtil.generateToken(user.getEmail(), claims);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("token", token);
		responseData.put("user", new UserDTO(user.getId(), user.getName(), user.getEmail()));

		return ResponseEntity.ok(new ApiResponse<>("success", "Logged In Successfully", responseData));
	}

	@GetMapping("/secure/profile")
	public ResponseEntity<ApiResponse<?>> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Users user = customUserDetails.getUser();
		return ResponseEntity.ok(new ApiResponse<>("success", "Profile Data", user));

	}

}
