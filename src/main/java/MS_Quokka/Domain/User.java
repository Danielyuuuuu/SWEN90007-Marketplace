package MS_Quokka.Domain;

import MS_Quokka.Mapper.SellerGroupMapper;
import MS_Quokka.Mapper.UserMapper;
import MS_Quokka.Utils.Role;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class User extends DomainObject {
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String shippingAddress;
    private Role role;
    private SellerGroup sellerGroup;
    private boolean isEditing = false;

    public User(String id) {
        super(id);
        email = null;
        firstname = null;
        lastname = null;
        password = null;
        shippingAddress = null;
        role = null;
        sellerGroup = null;
    }

    public User(String id, String email, String firstname, String lastname, String password, String shippingAddress, Role role, SellerGroup sellerGroup) {
        super(id);
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.shippingAddress = shippingAddress;
        this.role = role;
        this.sellerGroup = sellerGroup;
    }

    private void reload() {
        if (!isEditing){
            User fullUser = new UserMapper().readOne(getId());
            email = fullUser.getEmail();
            firstname = fullUser.getFirstname();
            lastname = fullUser.getLastname();
            password = fullUser.getPassword();
            shippingAddress = fullUser.getShippingAddress();
            role = fullUser.getRole();
            sellerGroup = fullUser.getSellerGroup();
        }

    }

    public String getEmail() {
        if (email == null) {
            reload();
        }
        return email;
    }

    public String getFirstname() {
        if (firstname == null) {
            reload();
        }
        return firstname;
    }

    public String getLastname() {
        if (lastname == null) {
            reload();
        }
        return lastname;
    }

    public String getPassword() {
        if (password == null) {
            reload();
        }
        return password;
    }

    public Role getRole() {
        if (role == null) {
            reload();
        }
        return role;
    }

    public String getShippingAddress() {
        if (shippingAddress == null) {
            reload();
        }
        return shippingAddress;
    }

    public SellerGroup getSellerGroup() {
        if (sellerGroup == null) {
            reload();
        }
        return sellerGroup;
    }

    public void setIsEditing(){
        this.isEditing = true;
    }

    public void setIsNotEditing(){
        this.isEditing = false;
    }

    public boolean getIsEditing(){
        return this.isEditing;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        encoder.setEncodeHashAsBase64(false);
        this.password = encoder.encode(password);
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
