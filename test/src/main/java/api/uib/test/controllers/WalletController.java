package api.uib.test.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.uib.test.entities.Wallet;
import api.uib.test.repositories.WalletRepository;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getWalletByUser(@PathVariable("userId") Long userId) {
        Optional<Wallet> wallet = walletRepository.findByUserId(userId);
        if (wallet.isPresent()) {
            return ResponseEntity.ok(wallet.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        }

    }
}
