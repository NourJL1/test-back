package api.uib.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import api.uib.test.entities.User;
import api.uib.test.entities.WalletStatus;
import api.uib.test.entities.WalletType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount = 0.0; // Initialize with default value
    private String currency = "USD"; // Default currency
    @Enumerated(EnumType.STRING)
    private WalletStatus status = WalletStatus.PENDING; // Default to PENDING

    
    @Enumerated(EnumType.STRING)
    private WalletType walletType ; // Default wallet type

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;
    
    public String getOwnerFullName() {
        return user != null ? user.getFullname() : "";
    }
    // Default constructor (required by JPA)
    public Wallet() {
    }
    
    // Constructor with user
    public Wallet(User user) {
        this.user = user;
        // Other fields are initialized with default values above
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }


    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}