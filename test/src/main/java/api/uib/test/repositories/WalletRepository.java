package api.uib.test.repositories;

import api.uib.test.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    // This allows us to find the wallet by user ID (assuming 1 wallet per user)
    Optional<Wallet> findByUserId(Long userId);
}
