package edu.ut.softlab.rate.model;


import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;

@Entity(name = "User")
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public User(){
		super();
	}


	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String uid;
	

	@Column(name="uname", length = 45)
	private String uname;

	@Column(name="email", length = 45)
	private String email;

	@Column(name="telephone", length = 45)
	private String telephone;

	@Column(name="password")
	private String password;

	@Column(name="loginDate")
	@Temporal(TemporalType.DATE)
	private Date loginDate;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Subscribe> subscribes;


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Favorite> favorites;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Device> devices;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Feedback> feedbacks;

	@Column(name="status")
	private Boolean status = false;

	@Column(name="validatecode", length = 225)
	private String validateCode;

	@Column(name = "favorite_revision")
	private Integer favoriteRevision = 0;

	@Column(name = "subscribe_revision")
	private Integer subscribeRevision = 0;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "code_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date codeExpiration;

	@Column(name = "avatar_revision")
	private Integer avatarRevision = 0;

	public Integer getAvatarRevision() {
		return avatarRevision;
	}

	public void setAvatarRevision(Integer avatarRevision) {
		this.avatarRevision = avatarRevision;
	}

	public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getCodeExpiration() {
        return codeExpiration;
    }

    public void setCodeExpiration(Date codeExpiration) {
        this.codeExpiration = codeExpiration;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Set<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	public Integer getSubscribeRevision() {
		return subscribeRevision;
	}

	public void setSubscribeRevision(Integer subscribeRevision) {
		this.subscribeRevision = subscribeRevision;
	}

	public Set<Favorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(Set<Favorite> favorites) {
		this.favorites = favorites;
	}

	public Integer getFavoriteRevision() {
		return favoriteRevision;
	}

	public void setFavoriteRevision(Integer favoriteRevision) {
		this.favoriteRevision = favoriteRevision;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<Subscribe> getSubscribes() {
		return subscribes;
	}

	public void setSubscribes(Set<Subscribe> subscribes) {
		this.subscribes = subscribes;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
}
