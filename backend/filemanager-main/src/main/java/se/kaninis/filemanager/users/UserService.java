package se.kaninis.filemanager.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
