package MS_Quokka.spring;

import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.SQLException;

//Custom class to load users' details for authentication/authorization purposes
public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMapper userMapper = new UserMapper();
        User user = null;
        user = userMapper.readOneByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());

        builder.password(user.getPassword());
        builder.roles(user.getRole().toString());

        return builder.build();
    }

}