package tacos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacos.dao.UserRepository;
import tacos.domain.User;

@Service
@Primary
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userRepo.findByUsername(username);
        if (null != user) {
            return user;
        }

        throw new UsernameNotFoundException("User '" + username + "' not found");
    }
}
