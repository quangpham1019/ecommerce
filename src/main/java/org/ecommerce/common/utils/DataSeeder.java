package org.ecommerce.common.utils;

import org.ecommerce.user.domain.model.*;
import org.ecommerce.user.infrastructure.repository.jpa.RoleRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.ecommerce.user.infrastructure.repository.jpa.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) {

        seedRoles();
        seedUsers();
        seedUserRoles();

    }

    private void seedRoles() {

        if (roleRepository.count() == 0) { // Avoid duplicate inserts
            roleRepository.saveAll(Arrays.asList(
                    new Role("ADMIN", "Administrator role with all permissions"),
                    new Role("USER", "User role with standard permissions"),
                    new Role("MODERATOR", "Moderator role with limited permissions")
            ));
        }
    }

    private void seedUsers() {

        if (userRepository.count() == 0) {
            String[] names = {"jack", "kim", "mike", "darcy", "korin"};

            for (String name : names) {
                userRepository.save(new User(name, name+"Password", new Email(name+"@gmail.com")));
            }
        }

    }

    private void seedUserRoles() {

        if (userRoleRepository.count() == 0) {
            List<User> userList = userRepository.findAll();
            List<Role> roleList = roleRepository.findAll();

            for (int i = 0; i < userList.size(); i++) {

                int roleNum = i <= 2 ? 0 : i <= 5 ? 1 : 2;
                userRoleRepository.save(new UserRole(userList.get(i), roleList.get(roleNum), UserRoleStatus.ACTIVE));
            }
        }
    }
}
