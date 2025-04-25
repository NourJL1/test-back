package api.uib.test.repositories;

import api.uib.test.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // ✅ This is what you need!
    Optional<User> findByPhoneNbr(String phoneNbr);
    boolean existsByUsername(String username); // ✅ Add this line
   
}
