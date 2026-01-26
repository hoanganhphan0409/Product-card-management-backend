package Main.service;

import Main.dto.AuthResponse;
import Main.dto.LoginRequest;
import Main.dto.RegisterRequest;
import Main.model.User;
import Main.exception.DuplicateResourceException;
import Main.exception.InvalidCredentialsException;
import Main.exception.ResourceNotFoundException;
import Main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email đã được sử dụng: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());
        response.setMessage("Đăng ký thành công");

        return response;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email hoặc mật khẩu không chính xác");
        }

        String token = jwtService.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setMessage("Đăng nhập thành công");

        return response;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản với email: " + email));

        return "Link khôi phục mật khẩu đã được gửi đến email: " + email;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với email: " + email));
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public String getEmailFromToken(String token) {
        return jwtService.getEmailFromToken(token);
    }
}
