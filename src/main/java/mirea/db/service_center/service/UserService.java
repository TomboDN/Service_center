package mirea.db.service_center.service;

import lombok.RequiredArgsConstructor;
import mirea.db.service_center.exception.UserAlreadyExistsException;
import mirea.db.service_center.model.Customer;
import mirea.db.service_center.model.User;
import mirea.db.service_center.repository.CustomerRepository;
import mirea.db.service_center.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void register(User user) {
        if (checkIfUserExists(user.getUsername())) {
            try {
                throw new UserAlreadyExistsException("User already exists for this username");
            } catch (UserAlreadyExistsException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        } else {
            user.setRole("customer");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    public void updateCustomerData(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }


}