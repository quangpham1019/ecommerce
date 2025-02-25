package org.ecommerce.common.utils;

import org.ecommerce.user.domain.model.*;
import org.ecommerce.user.domain.model.enums.RolePermissionStatus;
import org.ecommerce.user.domain.model.enums.UserRoleStatus;
import org.ecommerce.user.domain.model.value_objects.Email;
import org.ecommerce.user.infrastructure.repository.jpa.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.sql.Date;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AddressRepository addressRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserProfileRepository userProfileRepository;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, AddressRepository addressRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository, UserAddressRepository userAddressRepository, UserProfileRepository userProfileRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.addressRepository = addressRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userAddressRepository = userAddressRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void run(String... args) {

        seedRoles();
        seedUsers();
        seedUserRoles_Hardcode_TwentyUsers();

        seedPermissions();
        seedRolePermissions();

        seedAddresses();
        seedUserAddresses();

        seedUserProfiles();
    }


    private void seedUsers() {
        if (userRepository.count() == 0) { // Avoid duplicate inserts
            userRepository.saveAll(Arrays.asList(
                    new User("john_doe", "SecurePass123!", new Email("john.doe@example.com")),
                    new User("jane_smith", "StrongPass456!", new Email("jane.smith@example.com")),
                    new User("michael_j", "Pass9876#", new Email("michael.johnson@example.com")),
                    new User("emily_d", "EmilyP@ssw0rd!", new Email("emily.davis@example.com")),
                    new User("daniel_b", "Daniel123Secure!", new Email("daniel.brown@example.com")),
                    new User("sophia_w", "Sophia789@", new Email("sophia.wilson@example.com")),
                    new User("ethan_m", "EthanP@ssword!", new Email("ethan.miller@example.com")),
                    new User("olivia_t", "Olivia_Secure789!", new Email("olivia.taylor@example.com")),
                    new User("liam_k", "LiamPass321!", new Email("liam.king@example.com")),
                    new User("ava_h", "Ava_Hunter#Pass!", new Email("ava.hall@example.com")),
                    new User("noah_r", "Noah98765Pass!", new Email("noah.robinson@example.com")),
                    new User("isabella_c", "IsaC@SecurePass!", new Email("isabella.carter@example.com")),
                    new User("mason_g", "MasonGreatPass!", new Email("mason.green@example.com")),
                    new User("mia_p", "MiaSuperSecure123!", new Email("mia.parker@example.com")),
                    new User("logan_s", "Logan_Pass4321!", new Email("logan.stewart@example.com")),
                    new User("charlotte_b", "Charlotte789#", new Email("charlotte.baker@example.com")),
                    new User("lucas_m", "Lucas_SecretPass!", new Email("lucas.martin@example.com")),
                    new User("amelia_t", "AmeliaPass432!", new Email("amelia.turner@example.com")),
                    new User("william_f", "WillF@SuperPass!", new Email("william.foster@example.com")),
                    new User("harper_d", "HarperD@789Pass!", new Email("harper.dixon@example.com"))
            ));
        }
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
    private void seedAddresses() {
        if (addressRepository.count() == 0) { // Avoid duplicate inserts
            addressRepository.saveAll(Arrays.asList(
                    new Address("123 Main St", "New York", "NY", "10001", "USA"),
                    new Address("456 Elm St", "Los Angeles", "CA", "90001", "USA"),
                    new Address("789 Oak St", "Chicago", "IL", "60601", "USA"),
                    new Address("101 Pine St", "San Francisco", "CA", "94101", "USA"),
                    new Address("202 Maple Ave", "Boston", "MA", "02108", "USA"),
                    new Address("303 Birch Rd", "Seattle", "WA", "98101", "USA"),
                    new Address("404 Cedar Ln", "Houston", "TX", "77001", "USA"),
                    new Address("505 Walnut Dr", "Denver", "CO", "80201", "USA"),
                    new Address("606 Aspen Ct", "Phoenix", "AZ", "85001", "USA"),
                    new Address("707 Cherry St", "Miami", "FL", "33101", "USA")
            ));
        }
    }
    private void seedPermissions() {
        if (permissionRepository.count() == 0) { // Avoid duplicate inserts
            permissionRepository.saveAll(Arrays.asList(
                    new Permission("READ_PRODUCTS", "Allows viewing products"),
                    new Permission("CREATE_ORDER", "Allows placing new orders"),
                    new Permission("MANAGE_USERS", "Allows modifying user details"),
                    new Permission("UPDATE_PRODUCT", "Allows updating product details"),
                    new Permission("DELETE_ORDER", "Allows deleting orders"),
                    new Permission("VIEW_REPORTS", "Allows accessing business reports"),
                    new Permission("MANAGE_INVENTORY", "Allows modifying stock levels")
            ));
        }
    }
    private void seedUserProfiles() {
        if (userProfileRepository.count() == 0) { // Avoid duplicate inserts
            // Fetch existing users (20 users)
            List<User> users = userRepository.findAll();

            // Hardcoded UserProfiles for 20 users, with varied fields
            userProfileRepository.saveAll(Arrays.asList(
                    new UserProfile("John", "Doe", new Date(95, 4, 21), "Male", users.get(0), true),
                    new UserProfile("Alice", "Smith", new Date(90, 11, 15), "Female", users.get(1), true),
                    new UserProfile("Chris", "Johnson", new Date(88, 2, 10), "Male", users.get(2), true),
                    new UserProfile("Eve", "Brown", new Date(92, 7, 5), "Female", users.get(3), true),
                    new UserProfile("Grace", "Davis", new Date(85, 0, 22), "Female", users.get(4), true),
                    new UserProfile("Ivy", "Evans", new Date(93, 3, 13), "Female", users.get(5), true),
                    new UserProfile("Liam", "Wilson", new Date(89, 8, 3), "Male", users.get(6), true),
                    new UserProfile("Noah", "Harris", new Date(91, 6, 18), "Male", users.get(7), true),
                    new UserProfile("Paul", "Clark", new Date(87, 5, 26), "Male", users.get(8), true),
                    new UserProfile("Rita", "Lewis", new Date(93, 2, 12), "Female", users.get(9), true),
                    new UserProfile("Tina", "Young", new Date(86, 10, 29), "Female", users.get(10), true),
                    new UserProfile("Victor", "White", new Date(84, 11, 14), "Male", users.get(11), true),
                    new UserProfile("Xander", "Adams", new Date(94, 6, 2), "Male", users.get(12), true),
                    new UserProfile("Zara", "Scott", new Date(85, 1, 9), "Female", users.get(13), true),
                    new UserProfile("Bella", "Moore", new Date(89, 3, 27), "Female", users.get(14), true),
                    new UserProfile("Dylan", "Martinez", new Date(92, 8, 21), "Male", users.get(15), true),
                    new UserProfile("Felix", "Perez", new Date(90, 0, 17), "Male", users.get(16), true),
                    new UserProfile("Holly", "Ramirez", new Date(86, 11, 11), "Female", users.get(17), true),
                    new UserProfile("Jackie", "Lee", new Date(95, 4, 8), "Female", users.get(18), true),
                    new UserProfile("Lena", "Walker", new Date(87, 7, 3), "Female", users.get(19), true)
            ));
        }
    }


    private void seedUserRoles_Random_TwentyUsers() {
        if (userRoleRepository.count() == 0) { // Avoid duplicate inserts
            List<User> users = userRepository.findAll(); // Fetch existing users
            List<Role> roles = roleRepository.findAll(); // Fetch existing roles

            if (users.isEmpty() || roles.isEmpty()) {
                throw new IllegalStateException("Users or Roles must be seeded before assigning UserRoles.");
            }
            if (users.size() < 20) {
                throw new IllegalStateException("There must be at least 20 users.");
            }
            if (roles.size() < 3) {
                throw new IllegalStateException("There must be at least 3 roles.");
            }

            List<UserRole> userRoles = new ArrayList<>();

            Random random = new Random();
            UserRoleStatus[] statuses = UserRoleStatus.values();

            for (User user : users) {
                // Assign 1 to 2 random roles per user
                Collections.shuffle(roles);
                int rolesToAssign = random.nextInt(2) + 1; // Assign at least 1 role

                for (int i = 0; i < rolesToAssign; i++) {
                    Role role = roles.get(i);
                    UserRoleStatus status = statuses[random.nextInt(statuses.length)];

                    userRoles.add(new UserRole(user, role, status));
                }
            }

            userRoleRepository.saveAll(userRoles);
        }
    }
    private void seedUserRoles_Hardcode_TwentyUsers() {
        if (userRoleRepository.count() == 0) { // Avoid duplicate inserts
            // Fetch existing users and roles
            List<User> users = userRepository.findAll();
            List<Role> roles = roleRepository.findAll();

            if (users.isEmpty() || roles.isEmpty()) {
                throw new IllegalStateException("Users or Roles must be seeded before assigning UserRoles.");
            }
            if (users.size() < 20) {
                throw new IllegalStateException("There must be at least 20 users.");
            }
            if (roles.size() < 3) {
                throw new IllegalStateException("There must be at least 3 roles.");
            }

            // Hardcode UserRole assignments
            List<UserRole> userRoles = new ArrayList<>();

            // Assign roles manually to existing users with multiple roles for some
            userRoles.add(new UserRole(users.get(0), roles.get(0), UserRoleStatus.ACTIVE));  // User 1, ADMIN
            userRoles.add(new UserRole(users.get(0), roles.get(1), UserRoleStatus.ACTIVE));  // User 1, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(1), roles.get(1), UserRoleStatus.PENDING)); // User 2, USER
            userRoles.add(new UserRole(users.get(1), roles.get(2), UserRoleStatus.PENDING)); // User 2, MODERATOR (Multiple roles)

            userRoles.add(new UserRole(users.get(2), roles.get(2), UserRoleStatus.INACTIVE)); // User 3, MODERATOR
            userRoles.add(new UserRole(users.get(2), roles.get(1), UserRoleStatus.INACTIVE)); // User 3, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(3), roles.get(0), UserRoleStatus.ACTIVE));  // User 4, ADMIN
            userRoles.add(new UserRole(users.get(3), roles.get(1), UserRoleStatus.ACTIVE));  // User 4, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(4), roles.get(1), UserRoleStatus.ACTIVE));  // User 5, USER

            userRoles.add(new UserRole(users.get(5), roles.get(2), UserRoleStatus.PENDING)); // User 6, MODERATOR
            userRoles.add(new UserRole(users.get(5), roles.get(1), UserRoleStatus.PENDING)); // User 6, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(6), roles.get(1), UserRoleStatus.INACTIVE)); // User 7, USER

            userRoles.add(new UserRole(users.get(7), roles.get(0), UserRoleStatus.ACTIVE));  // User 8, ADMIN

            userRoles.add(new UserRole(users.get(8), roles.get(2), UserRoleStatus.PENDING)); // User 9, MODERATOR
            userRoles.add(new UserRole(users.get(8), roles.get(1), UserRoleStatus.PENDING)); // User 9, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(9), roles.get(1), UserRoleStatus.INACTIVE)); // User 10, USER

            userRoles.add(new UserRole(users.get(10), roles.get(0), UserRoleStatus.ACTIVE)); // User 11, ADMIN

            userRoles.add(new UserRole(users.get(11), roles.get(1), UserRoleStatus.PENDING)); // User 12, USER

            userRoles.add(new UserRole(users.get(12), roles.get(2), UserRoleStatus.INACTIVE)); // User 13, MODERATOR

            userRoles.add(new UserRole(users.get(13), roles.get(0), UserRoleStatus.ACTIVE)); // User 14, ADMIN
            userRoles.add(new UserRole(users.get(13), roles.get(1), UserRoleStatus.ACTIVE)); // User 14, USER (Multiple roles)

            userRoles.add(new UserRole(users.get(14), roles.get(1), UserRoleStatus.PENDING)); // User 15, USER

            userRoles.add(new UserRole(users.get(15), roles.get(2), UserRoleStatus.INACTIVE)); // User 16, MODERATOR
            userRoles.add(new UserRole(users.get(15), roles.get(0), UserRoleStatus.ACTIVE)); // User 16, ADMIN (Multiple roles)

            userRoles.add(new UserRole(users.get(16), roles.get(1), UserRoleStatus.ACTIVE)); // User 17, USER

            userRoles.add(new UserRole(users.get(17), roles.get(2), UserRoleStatus.PENDING)); // User 18, MODERATOR

            userRoles.add(new UserRole(users.get(18), roles.get(0), UserRoleStatus.INACTIVE)); // User 19, ADMIN

            userRoles.add(new UserRole(users.get(19), roles.get(1), UserRoleStatus.ACTIVE)); // User 20, USER

            // Save all UserRole entities to the repository
            userRoleRepository.saveAll(userRoles);
        }
    }
    private void seedRolePermissions() {
        if (rolePermissionRepository.count() == 0) { // Avoid duplicate inserts
            // Fetch existing roles
            List<Role> roles = roleRepository.findAll();

            // Fetch existing permissions
            List<Permission> permissions = permissionRepository.findAll();

            // Hardcoded status for RolePermission
            RolePermissionStatus grantedStatus = RolePermissionStatus.GRANTED;
            RolePermissionStatus revokedStatus = RolePermissionStatus.REVOKED;

            // Assign permissions to roles
            rolePermissionRepository.saveAll(Arrays.asList(
                    // Admin Role Permissions
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("READ_PRODUCTS")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("ADMIN")).findFirst().get(), grantedStatus),
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("CREATE_ORDER")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("ADMIN")).findFirst().get(), grantedStatus),
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("MANAGE_USERS")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("ADMIN")).findFirst().get(), grantedStatus),
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("UPDATE_PRODUCT")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("ADMIN")).findFirst().get(), revokedStatus),

                    // User Role Permissions
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("READ_PRODUCTS")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("USER")).findFirst().get(), grantedStatus),
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("CREATE_ORDER")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("USER")).findFirst().get(), grantedStatus),

                    // Moderator Role Permissions
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("READ_PRODUCTS")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("MODERATOR")).findFirst().get(), grantedStatus),
                    new RolePermission(permissions.stream().filter(p -> p.getPermissionName().equals("UPDATE_PRODUCT")).findFirst().get(), roles.stream().filter(r -> r.getRoleName().equals("MODERATOR")).findFirst().get(), grantedStatus)
            ));
        }
    }
    private void seedUserAddresses() {
        if (userAddressRepository.count() == 0) { // Avoid duplicate inserts
            // Fetch existing users (20 users)
            List<User> users = userRepository.findAll();

            // Fetch existing addresses (10 addresses)
            List<Address> addresses = addressRepository.findAll();

            // Hardcoded user-address associations with random data for shipping/billing
            userAddressRepository.saveAll(Arrays.asList(
                    // User 1 addresses
                    new UserAddress(users.get(0), "John Doe", addresses.get(0), "123-456-7890", true, true), // Both shipping and billing

                    // User 2 addresses
                    new UserAddress(users.get(1), "Alice Smith", addresses.get(1), "234-567-8901", true, false), // Shipping
                    new UserAddress(users.get(1), "Bob Smith", addresses.get(2), "432-765-5432", false, true), // Billing

                    // User 3 addresses
                    new UserAddress(users.get(2), "Chris Johnson", addresses.get(3), "345-678-9012", true, false), // Shipping
                    new UserAddress(users.get(2), "Dana Johnson", addresses.get(4), "543-876-6543", false, true), // Billing

                    // User 4 addresses
                    new UserAddress(users.get(3), "Eve Brown", addresses.get(5), "456-789-0123", true, false), // Shipping
                    new UserAddress(users.get(3), "Frank Brown", addresses.get(6), "654-987-7654", false, true), // Billing

                    // User 5 addresses
                    new UserAddress(users.get(4), "Grace Davis", addresses.get(7), "567-890-1234", true, false), // Shipping
                    new UserAddress(users.get(4), "Harry Davis", addresses.get(8), "765-098-8765", false, true), // Billing

                    // User 6 addresses
                    new UserAddress(users.get(5), "Ivy Evans", addresses.get(9), "678-901-2345", true, true), // Both shipping and billing

                    // User 7 addresses
                    new UserAddress(users.get(6), "Liam Wilson", addresses.get(0), "789-012-3456", true, false), // Shipping
                    new UserAddress(users.get(6), "Mona Wilson", addresses.get(1), "987-123-6543", false, true), // Billing

                    // User 8 addresses
                    new UserAddress(users.get(7), "Noah Harris", addresses.get(2), "890-123-4567", true, false), // Shipping
                    new UserAddress(users.get(7), "Olivia Harris", addresses.get(3), "098-234-7654", false, true), // Billing

                    // User 9 addresses
                    new UserAddress(users.get(8), "Paul Clark", addresses.get(4), "901-234-5678", true, false), // Shipping
                    new UserAddress(users.get(8), "Quincy Clark", addresses.get(5), "109-345-8765", false, true), // Billing

                    // User 10 addresses
                    new UserAddress(users.get(9), "Rita Lewis", addresses.get(6), "012-345-6789", true, true), // Both shipping and billing

                    // User 11 addresses
                    new UserAddress(users.get(10), "Tina Young", addresses.get(7), "123-321-4567", true, false), // Shipping
                    new UserAddress(users.get(10), "Ursula Young", addresses.get(8), "321-654-8765", false, true), // Billing

                    // User 12 addresses
                    new UserAddress(users.get(11), "Victor White", addresses.get(9), "234-543-7654", true, true), // Both shipping and billing

                    // User 13 addresses
                    new UserAddress(users.get(12), "Xander Adams", addresses.get(0), "345-654-8765", true, false), // Shipping
                    new UserAddress(users.get(12), "Yara Adams", addresses.get(1), "543-765-2109", false, true), // Billing

                    // User 14 addresses
                    new UserAddress(users.get(13), "Zara Scott", addresses.get(2), "456-765-4321", true, false), // Shipping
                    new UserAddress(users.get(13), "Aaron Scott", addresses.get(3), "654-987-3456", false, true), // Billing

                    // User 15 addresses
                    new UserAddress(users.get(14), "Bella Moore", addresses.get(4), "567-876-5432", true, false), // Shipping
                    new UserAddress(users.get(14), "Carlos Moore", addresses.get(5), "765-321-0987", false, true), // Billing

                    // User 16 addresses
                    new UserAddress(users.get(15), "Dylan Martinez", addresses.get(6), "678-987-6543", true, true), // Both shipping and billing

                    // User 17 addresses
                    new UserAddress(users.get(16), "Felix Perez", addresses.get(7), "789-321-7654", true, false), // Shipping
                    new UserAddress(users.get(16), "Gina Perez", addresses.get(8), "987-234-5678", false, true), // Billing

                    // User 18 addresses
                    new UserAddress(users.get(17), "Holly Ramirez", addresses.get(9), "890-432-1098", true, true), // Both shipping and billing

                    // User 19 addresses
                    new UserAddress(users.get(18), "Jackie Lee", addresses.get(0), "901-543-7654", true, false), // Shipping
                    new UserAddress(users.get(18), "Kylie Lee", addresses.get(1), "109-654-8765", false, true), // Billing

                    // User 20 addresses
                    new UserAddress(users.get(19), "Lena Walker", addresses.get(2), "012-654-9876", true, false), // Shipping
                    new UserAddress(users.get(19), "Mason Walker", addresses.get(3), "210-765-4321", false, true) // Billing
            ));
        }
    }


}
