package edu.sjsu.cmpe275.cartpool.dto;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String screenName;

	@Column(unique = true)
	private String nickName;

	@Column(unique = true)
	private String email;

	private String password;

	private boolean isProfileCompleted = false;

	private boolean isVerified = false;

	private String poolId;

	@Column(unique = true)
	private String uid;

	@Embedded
	private Address address;

	private int creditScore = 0;

	private String role;

	private int validationCode;

	public User() {
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public boolean isProfileCompleted() {
		return isProfileCompleted;
	}

	public void setProfileCompleted(boolean profileCompleted) {
		isProfileCompleted = profileCompleted;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean verified) {
		isVerified = verified;
	}

	public int getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(int validationCode) {
		this.validationCode = validationCode;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", screenName='" + screenName + '\'' + ", nickName='" + nickName + '\''
				+ ", email='" + email + '\'' + ", password='" + password + '\'' + ", verified=" + isVerified
				+ ", poolId='" + poolId + '\'' + ", uid='" + uid + '\'' + ", address=" + address + ", creditScore="
				+ creditScore + ", role='" + role + '\'' + ", profileCompleted='" + isProfileCompleted + '\'' + '}';
	}
}
