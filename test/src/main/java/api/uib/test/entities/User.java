package api.uib.test.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import api.uib.test.entities.IdentificationDocument;
import api.uib.test.entities.IdentificationType;
import api.uib.test.entities.Role;
import api.uib.test.entities.Wallet;
import api.uib.test.entities.WalletType;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    private String resetToken;
    
    @Transient
    private WalletType walletType; // This won't be saved in the database

    
    @Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", fullname=" + fullname
				+ ", email=" + email + ", roles=" + roles
				+ "]";
	}



	@Column(name = "fullname", nullable = false)
    private String fullname;
	
	@Column(name = "phoneNbr", nullable = false)
    private String phoneNbr;
    
    @Column(name = "email", nullable = true)
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
   
    
    
    @Enumerated(EnumType.STRING)
    private IdentificationType identificationType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<IdentificationDocument> documents;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;
    
    
    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;





    
    // Getters and Setters

	public Long getId() {
		return id;
	}
	
    public User() {}
 
	public User(Long id, String username, String password, String fullname, String email, Set<Role> roles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.roles = roles;
	}



	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getPhoneNbr() {
		return phoneNbr;
	}

	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}

	public IdentificationType getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(IdentificationType identificationType) {
		this.identificationType = identificationType;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public WalletType getWalletType() {
	    return walletType;
	}

	public void setWalletType(WalletType walletType) {
	    this.walletType = walletType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	
		 
}
