package org.ecommerce.common.utils.seeders;

import org.ecommerce.product.domain.model.*;
import org.ecommerce.product.infrastructure.repository.jpa.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductContextDataSeeder implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;
    private ProductRepository productRepository;
    private ProductReviewRepository productReviewRepository;
    private ProductVariantRepository productVariantRepository;
    private WarehouseRepository warehouseRepository;

    public ProductContextDataSeeder(CategoryRepository categoryRepository, InventoryRepository inventoryRepository, ProductRepository productRepository, ProductReviewRepository productReviewRepository, ProductVariantRepository productVariantRepository, WarehouseRepository warehouseRepository) {
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.productReviewRepository = productReviewRepository;
        this.productVariantRepository = productVariantRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        seedCategories();
        seedProducts();

        seedProductVariants();
        seedProductReviews();

        seedWarehouses();
        seedInventory();
    }

    private void seedCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Electronics", "Devices, gadgets, and technology products"));
        categories.add(new Category("Computers", "Laptops, desktops, and computer accessories"));
        categories.add(new Category("Mobile Phones", "Smartphones and mobile accessories"));
        categories.add(new Category("Accessories", "Various accessories for tech and daily use"));
        categories.add(new Category("Gaming", "Gaming hardware, accessories, and peripherals"));
        categories.add(new Category("Home Appliances", "Electronics for home and kitchen use"));
        categories.add(new Category("Fitness", "Health and fitness-related gadgets and gear"));

        categoryRepository.saveAll(categories);
    }
    private void seedProducts() {
        List<Category> categories = categoryRepository.findAll();

        // Categories
        Category electronics = categories.get(0);
        Category computers = categories.get(1);
        Category mobilePhones = categories.get(2);
        Category accessories = categories.get(3);
        Category gaming = categories.get(4);
        Category homeAppliances = categories.get(5);
        Category fitness = categories.get(6);

        List<ProductVariant> productVariants = new ArrayList<>();

        List<Product> products = Arrays.asList(
                new Product(
                        "Laptop Pro",
                        "High-performance laptop for professionals",
                        new ProductVariant("Laptop Pro 16GB", "16GB RAM, 512GB SSD", 1099.99, "laptop.jpg", null),
                        Set.of(electronics, computers)
                ),

                new Product(
                        "Smartphone X",
                        "Latest generation smartphone with advanced camera",
                        new ProductVariant("Smartphone X 128GB", "128GB Storage, 6GB RAM", 699.99, "phone.jpg", null),
                        Set.of(electronics, mobilePhones)
                ),

                new Product(
                        "Gaming Mouse Ultra",
                        "Precision gaming mouse with customizable buttons",
                        new ProductVariant("Gaming Mouse RGB", "RGB Lighting edition", 59.99, "mouse.jpg", null),
                        Set.of(gaming, accessories)
                ),

                new Product(
                        "Wireless Earbuds Pro",
                        "Noise-canceling earbuds with premium sound quality",
                        new ProductVariant("Earbuds Pro", "Noise-canceling edition", 129.99, "earbuds.jpg", null),
                        Set.of(accessories, electronics)
                ),

                new Product(
                        "4K Smart TV",
                        "55-inch UHD TV with HDR support",
                        new ProductVariant("Smart TV 55-inch", "Ultra HD with HDR10+", 499.99, "tv.jpg", null),
                        Set.of(electronics, homeAppliances)
                ),

                new Product(
                        "Fitness Tracker",
                        "Track your steps, heart rate, and workouts",
                        new ProductVariant("Fitness Tracker Pro", "Advanced heart rate monitoring", 79.99, "tracker.jpg", null),
                        Set.of(fitness, electronics)
                ),

                new Product(
                        "Bluetooth Speaker",
                        "Portable speaker with deep bass",
                        new ProductVariant("Speaker Bass+", "Water-resistant, 10-hour battery life", 59.99, "speaker.jpg", null),
                        Set.of(accessories, electronics)
                ),

                new Product(
                        "Mechanical Gaming Keyboard",
                        "Tactile switches and RGB lighting",
                        new ProductVariant("Keyboard RGB", "Customizable lighting", 89.99, "keyboard.jpg", null),
                        Set.of(gaming, computers)
                ),

                new Product(
                        "Robot Vacuum Cleaner",
                        "Smart home cleaning solution",
                        new ProductVariant("Vacuum AutoClean", "AI-powered navigation", 249.99, "vacuum.jpg", null),
                        Set.of(homeAppliances, electronics)
                ),

                new Product(
                        "Smartwatch Series 5",
                        "Advanced health tracking and notifications",
                        new ProductVariant("Smartwatch Premium", "GPS & LTE model", 299.99, "watch.jpg", null),
                        Set.of(fitness, mobilePhones)
                ),

                new Product(
                        "Gaming Headset",
                        "Surround sound with noise cancellation",
                        new ProductVariant("Headset Pro", "7.1 surround sound", 149.99, "headset.jpg", null),
                        Set.of(gaming, accessories)
                ),

                new Product(
                        "DSLR Camera",
                        "Professional photography camera",
                        new ProductVariant("Camera 24MP", "24MP sensor with 4K video", 799.99, "camera.jpg", null),
                        Set.of(electronics, accessories)
                ),

                new Product(
                        "Home Security Camera",
                        "Smart surveillance with motion detection",
                        new ProductVariant("Security Cam", "1080p, night vision", 99.99, "securitycam.jpg", null),
                        Set.of(homeAppliances, electronics)
                ),

                new Product(
                        "Electric Scooter",
                        "Eco-friendly commuting solution",
                        new ProductVariant("Scooter Max", "25-mile range, 15mph top speed", 399.99, "scooter.jpg", null),
                        Set.of(electronics, fitness)
                ),

                new Product(
                        "Portable Projector",
                        "Compact projector for movies and presentations",
                        new ProductVariant("Projector Mini", "HD resolution, built-in speakers", 149.99, "projector.jpg", null),
                        Set.of(electronics, homeAppliances)
                )
        );

        for (Product product : products) {
            productVariants.add(product.getPrimaryVariant());
        }
        productVariantRepository.saveAll(productVariants);
        productRepository.saveAll(products);

        productVariants.clear();
        for (Product product : products) {
            ProductVariant curProductVariant = product.getPrimaryVariant();
            curProductVariant.setProduct(product);
            productVariants.add(product.getPrimaryVariant());
        }

        productVariantRepository.saveAll(productVariants);
    }

    private void seedProductVariants() {
        List<Product> products = productRepository.findAll(); // Fetch all products from the repository

        // Ensure you have the products in the correct order (use product names or another identifier)
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getName, product -> product)); // Map products by name

        List<ProductVariant> productVariants = Arrays.asList(
                // Laptop Pro variants
                new ProductVariant("Laptop Pro 8GB", "8GB RAM, 256GB SSD", 999.99, "laptop-8gb.jpg", productMap.get("Laptop Pro")),
                new ProductVariant("Laptop Pro 32GB", "32GB RAM, 1TB SSD", 1399.99, "laptop-32gb.jpg", productMap.get("Laptop Pro")),

                // Smartphone X variants
                new ProductVariant("Smartphone X 64GB", "64GB Storage, 4GB RAM", 599.99, "phone-64gb.jpg", productMap.get("Smartphone X")),
                new ProductVariant("Smartphone X 256GB", "256GB Storage, 8GB RAM", 799.99, "phone-256gb.jpg", productMap.get("Smartphone X")),

                // Gaming Mouse Ultra variants
                new ProductVariant("Gaming Mouse RGB Elite", "RGB Lighting, Wireless", 89.99, "mouse-elite.jpg", productMap.get("Gaming Mouse Ultra")),
                new ProductVariant("Gaming Mouse Pro", "Customizable buttons, Wired", 69.99, "mouse-pro.jpg", productMap.get("Gaming Mouse Ultra")),

                // Wireless Earbuds Pro variants
                new ProductVariant("Earbuds Pro Black", "Noise-canceling, Black edition", 129.99, "earbuds-black.jpg", productMap.get("Wireless Earbuds Pro")),
                new ProductVariant("Earbuds Pro White", "Noise-canceling, White edition", 129.99, "earbuds-white.jpg", productMap.get("Wireless Earbuds Pro")),

                // 4K Smart TV variants
                new ProductVariant("Smart TV 65-inch", "Ultra HD, HDR10+ with Alexa", 799.99, "tv-65.jpg", productMap.get("4K Smart TV")),
                new ProductVariant("Smart TV 75-inch", "Ultra HD, HDR10+ with Google Assistant", 999.99, "tv-75.jpg", productMap.get("4K Smart TV")),

                // Fitness Tracker variants
                new ProductVariant("Fitness Tracker Lite", "Basic heart rate monitoring", 49.99, "tracker-lite.jpg", productMap.get("Fitness Tracker")),
                new ProductVariant("Fitness Tracker Advanced", "Advanced heart rate and sleep monitoring", 99.99, "tracker-advanced.jpg", productMap.get("Fitness Tracker")),

                // Bluetooth Speaker variants
                new ProductVariant("Speaker Bass+ Pro", "Extended range, Deep Bass", 89.99, "speaker-pro.jpg", productMap.get("Bluetooth Speaker")),
                new ProductVariant("Speaker Bass+ Mini", "Compact size, Deep Bass", 49.99, "speaker-mini.jpg", productMap.get("Bluetooth Speaker")),

                // Mechanical Gaming Keyboard variants
                new ProductVariant("Keyboard RGB Elite", "Tactile switches, Advanced RGB", 119.99, "keyboard-elite.jpg", productMap.get("Mechanical Gaming Keyboard")),
                new ProductVariant("Keyboard RGB Compact", "Compact design, RGB", 69.99, "keyboard-compact.jpg", productMap.get("Mechanical Gaming Keyboard")),

                // Robot Vacuum Cleaner variants
                new ProductVariant("Vacuum AutoClean Max", "Enhanced suction power, Larger bin", 299.99, "vacuum-max.jpg", productMap.get("Robot Vacuum Cleaner")),
                new ProductVariant("Vacuum AutoClean Mini", "Compact design, Standard suction", 199.99, "vacuum-mini.jpg", productMap.get("Robot Vacuum Cleaner")),

                // Smartwatch Series 5 variants
                new ProductVariant("Smartwatch Basic", "GPS, Standard model", 199.99, "watch-basic.jpg", productMap.get("Smartwatch Series 5")),
                new ProductVariant("Smartwatch Sport", "GPS & LTE, Sport band", 349.99, "watch-sport.jpg", productMap.get("Smartwatch Series 5")),

                // Gaming Headset variants
                new ProductVariant("Headset Pro Wireless", "7.1 surround sound, Wireless", 179.99, "headset-wireless.jpg", productMap.get("Gaming Headset")),
                new ProductVariant("Headset Pro Wired", "7.1 surround sound, Wired", 149.99, "headset-wired.jpg", productMap.get("Gaming Headset")),

                // DSLR Camera variants
                new ProductVariant("Camera 18MP", "18MP sensor with Full HD video", 699.99, "camera-18mp.jpg", productMap.get("DSLR Camera")),
                new ProductVariant("Camera 32MP", "32MP sensor with 4K video", 999.99, "camera-32mp.jpg", productMap.get("DSLR Camera")),

                // Home Security Camera variants
                new ProductVariant("Security Cam Wireless", "Wireless, Night Vision", 149.99, "securitycam-wireless.jpg", productMap.get("Home Security Camera")),
                new ProductVariant("Security Cam Wired", "Wired, 4K video", 199.99, "securitycam-wired.jpg", productMap.get("Home Security Camera")),

                // Electric Scooter variants
                new ProductVariant("Scooter Max Pro", "30-mile range, 18mph top speed", 499.99, "scooter-pro.jpg", productMap.get("Electric Scooter")),
                new ProductVariant("Scooter Mini", "15-mile range, 12mph top speed", 299.99, "scooter-mini.jpg", productMap.get("Electric Scooter")),

                // Portable Projector variants
                new ProductVariant("Projector Mini Plus", "Full HD, Longer battery", 179.99, "projector-plus.jpg", productMap.get("Portable Projector")),
                new ProductVariant("Projector Ultra", "4K resolution, Larger screen", 299.99, "projector-ultra.jpg", productMap.get("Portable Projector"))
        );

        // Save all product variants
        productVariantRepository.saveAll(productVariants);
    }

    private void seedProductReviews() {
        List<ProductReview> productReviews = Arrays.asList(
                // Laptop Pro 16GB
                new ProductReview("Great performance", "This laptop runs everything smoothly, great for productivity!", productVariantRepository.findById(1L).get(), 1L),
                new ProductReview("Not bad, but overpriced", "The laptop is good, but I feel it’s overpriced for the specs.", productVariantRepository.findById(1L).get(), 2L),

                // Smartphone X 128GB
                new ProductReview("Amazing phone", "Great phone with awesome performance, no lag at all!", productVariantRepository.findById(2L).get(), 3L),
                new ProductReview("Disappointing battery life", "Battery life isn’t as good as I expected, needs improvement.", productVariantRepository.findById(2L).get(), 4L),

                // Gaming Mouse RGB
                new ProductReview("Great for gaming", "Super responsive mouse, perfect for fast-paced games.", productVariantRepository.findById(3L).get(), 5L),
                new ProductReview("Okay, but the buttons are too clicky", "I like the design, but the buttons feel a bit too loud for me.", productVariantRepository.findById(3L).get(), 6L),

                // Earbuds Pro
                new ProductReview("Excellent sound", "The sound quality is superb, and they fit perfectly.", productVariantRepository.findById(4L).get(), 7L),
                new ProductReview("Mediocre battery life", "The sound is great, but the battery doesn't last long enough.", productVariantRepository.findById(4L).get(), 8L),

                // Smart TV 55-inch
                new ProductReview("Great picture quality", "The picture quality on this TV is amazing, perfect for watching movies.", productVariantRepository.findById(5L).get(), 9L),
                new ProductReview("Average sound", "The sound could be better, but the picture quality is awesome.", productVariantRepository.findById(5L).get(), 10L),

                // Fitness Tracker Pro
                new ProductReview("Perfect for workouts", "This fitness tracker is very accurate for tracking my steps and heart rate.", productVariantRepository.findById(6L).get(), 11L),
                new ProductReview("Could use more features", "It's a good tracker, but I wish it had more features like GPS.", productVariantRepository.findById(6L).get(), 12L),

                // Speaker Bass+
                new ProductReview("Great sound", "The sound is very deep and clear, perfect for parties.", productVariantRepository.findById(7L).get(), 13L),
                new ProductReview("Too bulky", "The speaker is great but too big to carry around.", productVariantRepository.findById(7L).get(), 14L),

                // Keyboard RGB
                new ProductReview("Love the RGB", "The RGB lighting is vibrant and customizable. Great for gaming.", productVariantRepository.findById(8L).get(), 15L),
                new ProductReview("A bit too noisy", "The keys are a bit too clicky for my taste, but it works well otherwise.", productVariantRepository.findById(8L).get(), 16L),

                // Vacuum AutoClean
                new ProductReview("Super convenient", "This vacuum cleans my house effortlessly. A real time-saver.", productVariantRepository.findById(9L).get(), 17L),
                new ProductReview("Doesn't clean corners well", "It misses some spots and doesn't reach into corners.", productVariantRepository.findById(9L).get(), 18L),

                // Smartwatch Premium
                new ProductReview("Fantastic smartwatch", "Great features and excellent battery life.", productVariantRepository.findById(10L).get(), 19L),
                new ProductReview("Lacks some features", "It's a good watch, but it lacks some features found in other smartwatches.", productVariantRepository.findById(10L).get(), 20L),

                // Headset Pro
                new ProductReview("Comfortable and great sound", "The headset is very comfortable, and the sound quality is fantastic.", productVariantRepository.findById(11L).get(), 1L),
                new ProductReview("Too tight", "The headphones are a bit too tight on my head after long use.", productVariantRepository.findById(11L).get(), 2L),

                // Camera 24MP
                new ProductReview("Great camera", "The camera captures sharp and clear images, love it for photography.", productVariantRepository.findById(12L).get(), 3L),
                new ProductReview("Overpriced", "The camera is good, but there are cheaper options that perform similarly.", productVariantRepository.findById(12L).get(), 4L),

                // Security Cam
                new ProductReview("Very reliable", "This security camera works perfectly and keeps me updated in real-time.", productVariantRepository.findById(13L).get(), 5L),
                new ProductReview("Could be better", "The video quality is decent, but the app could be improved.", productVariantRepository.findById(13L).get(), 6L),

                // Scooter Max
                new ProductReview("Perfect for commuting", "Love how fast and easy it is to get around the city with this scooter.", productVariantRepository.findById(14L).get(), 7L),
                new ProductReview("Not as durable", "The scooter works, but it doesn’t feel as durable as I expected.", productVariantRepository.findById(14L).get(), 8L),

                // Projector Mini
                new ProductReview("Great value for the price", "The projector is a steal for the price, works perfectly for home use.", productVariantRepository.findById(15L).get(), 9L),
                new ProductReview("Poor resolution", "The resolution is lower than I expected, but it works fine for casual use.", productVariantRepository.findById(15L).get(), 10L),
                // Laptop Pro 8GB (16L)
                new ProductReview("Decent for work", "Good laptop for light work, but struggles with heavy multitasking.", productVariantRepository.findById(16L).get(), 1L),
                new ProductReview("Good for the price", "It works well for basic tasks, but not suitable for gaming or video editing.", productVariantRepository.findById(16L).get(), 2L),

                // Laptop Pro 32GB (17L)
                new ProductReview("Powerful machine", "This laptop is a beast! Handles everything I throw at it with ease.", productVariantRepository.findById(17L).get(), 3L),
                new ProductReview("Too expensive", "It's great, but the price is just too high for the specs it offers.", productVariantRepository.findById(17L).get(), 4L),

                // Smartphone X 64GB (18L)
                new ProductReview("Great budget phone", "Excellent phone for the price, performs well with most apps.", productVariantRepository.findById(18L).get(), 5L),
                new ProductReview("Not enough storage", "I quickly ran out of storage. Should have gone for a higher capacity.", productVariantRepository.findById(18L).get(), 6L),

                // Smartphone X 256GB (19L)
                new ProductReview("Perfect for power users", "The storage is massive, and the phone is very fast!", productVariantRepository.findById(19L).get(), 7L),
                new ProductReview("Battery life issues", "The phone is great, but the battery drains too quickly.", productVariantRepository.findById(19L).get(), 8L),

                // Gaming Mouse RGB Elite (20L)
                new ProductReview("Best gaming mouse ever", "The responsiveness and customization options are top-notch!", productVariantRepository.findById(20L).get(), 9L),
                new ProductReview("Not worth the hype", "The mouse looks great but didn't meet my expectations in terms of performance.", productVariantRepository.findById(20L).get(), 10L),

                // Gaming Mouse Pro (21L)
                new ProductReview("Great for pro gamers", "The mouse works perfectly for competitive gaming, very precise.", productVariantRepository.findById(21L).get(), 11L),
                new ProductReview("A bit too heavy", "The mouse is a bit on the heavier side, which isn't great for long gaming sessions.", productVariantRepository.findById(21L).get(), 12L),

                // Earbuds Pro Black (22L)
                new ProductReview("Amazing sound quality", "These are the best earbuds I’ve ever had, amazing bass and clarity.", productVariantRepository.findById(22L).get(), 13L),
                new ProductReview("The fit is uncomfortable", "The sound is great, but they hurt my ears after a while.", productVariantRepository.findById(22L).get(), 14L),

                // Earbuds Pro White (23L)
                new ProductReview("Great for workouts", "The earbuds stay in place and have a solid sound profile.", productVariantRepository.findById(23L).get(), 15L),
                new ProductReview("Not very durable", "The earbuds feel fragile and seem like they’ll break easily.", productVariantRepository.findById(23L).get(), 16L),

                // Smart TV 65-inch (24L)
                new ProductReview("Great for movies", "The TV has an incredible picture quality, perfect for movie nights.", productVariantRepository.findById(24L).get(), 17L),
                new ProductReview("The remote is confusing", "The TV is awesome, but the remote control is hard to use and not very intuitive.", productVariantRepository.findById(24L).get(), 18L),

                // Smart TV 75-inch (25L)
                new ProductReview("Massive TV, great for the living room", "The 75-inch size is perfect for a large living room. Picture is amazing.", productVariantRepository.findById(25L).get(), 19L),
                new ProductReview("Poor sound quality", "The picture is great, but the built-in speakers are not loud enough.", productVariantRepository.findById(25L).get(), 20L),

                // Fitness Tracker Lite (26L)
                new ProductReview("Good for basic tracking", "Works well for basic step counting and heart rate tracking, but lacks advanced features.", productVariantRepository.findById(26L).get(), 1L),
                new ProductReview("Not very accurate", "The step counter seems off, not very reliable for serious fitness tracking.", productVariantRepository.findById(26L).get(), 2L),

                // Fitness Tracker Advanced (27L)
                new ProductReview("Perfect for fitness enthusiasts", "This tracker is perfect for anyone serious about their workouts. Very accurate.", productVariantRepository.findById(27L).get(), 3L),
                new ProductReview("Hard to sync", "It works well but has issues syncing with my phone sometimes.", productVariantRepository.findById(27L).get(), 4L),

                // Speaker Bass+ Pro (28L)
                new ProductReview("Incredible bass", "The sound is booming, and it’s perfect for parties.", productVariantRepository.findById(28L).get(), 5L),
                new ProductReview("Too large to move", "The speaker has great sound but is too big to move around easily.", productVariantRepository.findById(28L).get(), 6L),

                // Speaker Bass+ Mini (29L)
                new ProductReview("Perfect size and sound", "This speaker is small but packs a punch. Great for casual listening.", productVariantRepository.findById(29L).get(), 7L),
                new ProductReview("Could be louder", "The speaker is good, but I wish it was louder for outdoor use.", productVariantRepository.findById(29L).get(), 8L),

                // Keyboard RGB Elite (30L)
                new ProductReview("Great keyboard for gaming", "Love the RGB and the feel of the keys. Great for both typing and gaming.", productVariantRepository.findById(30L).get(), 9L),
                new ProductReview("Key switches are too loud", "The keys are too clicky and noisy for my workspace.", productVariantRepository.findById(30L).get(), 10L),

                // Keyboard RGB Compact (31L)
                new ProductReview("Great for portability", "I love the compact size. It's easy to take with me to work or gaming events.", productVariantRepository.findById(31L).get(), 11L),
                new ProductReview("Not enough key spacing", "The keys feel too cramped, which affects my typing speed.", productVariantRepository.findById(31L).get(), 12L),

                // Vacuum AutoClean Max (32L)
                new ProductReview("Fantastic for big spaces", "This vacuum can clean large spaces very quickly and efficiently.", productVariantRepository.findById(32L).get(), 13L),
                new ProductReview("Doesn't pick up everything", "It misses some dirt on the edges of the room, not as thorough as expected.", productVariantRepository.findById(32L).get(), 14L),

                // Vacuum AutoClean Mini (33L)
                new ProductReview("Great for small apartments", "This vacuum is perfect for my small apartment. It’s quick and efficient.", productVariantRepository.findById(33L).get(), 15L),
                new ProductReview("A bit weak", "The suction power isn’t as strong as I thought it would be.", productVariantRepository.findById(33L).get(), 16L),

                // Smartwatch Basic (34L)
                new ProductReview("Good for basic features", "If you just need time and step counting, this watch is perfect.", productVariantRepository.findById(34L).get(), 17L),
                new ProductReview("Missing key features", "No heart rate monitor or GPS, which are essential for fitness tracking.", productVariantRepository.findById(34L).get(), 18L),

                // Smartwatch Sport (35L)
                new ProductReview("Great for fitness", "This smartwatch has everything I need for tracking workouts and steps.", productVariantRepository.findById(35L).get(), 19L),
                new ProductReview("Battery drains too fast", "The battery doesn’t last long enough, I need to charge it daily.", productVariantRepository.findById(35L).get(), 20L),

                // Headset Pro Wireless (36L)
                new ProductReview("Great for gaming", "The wireless connection is flawless and the sound is great for gaming.", productVariantRepository.findById(36L).get(), 1L),
                new ProductReview("Discomfort after long use", "The headset is a bit uncomfortable for extended use, but the sound is great.", productVariantRepository.findById(36L).get(), 2L),

                // Headset Pro Wired (37L)
                new ProductReview("Good value for money", "The sound quality is good for a wired headset, and it’s durable.", productVariantRepository.findById(37L).get(), 3L),
                new ProductReview("Not very flexible", "The wires are too stiff, which makes it uncomfortable to move around.", productVariantRepository.findById(37L).get(), 4L),

                // Camera 18MP (38L)
                new ProductReview("Decent for casual shots", "The camera is good for casual use, but struggles in low light.", productVariantRepository.findById(38L).get(), 5L),
                new ProductReview("Not professional level", "It’s okay, but the quality isn’t up to professional standards.", productVariantRepository.findById(38L).get(), 6L),

                // Camera 32MP (39L)
                new ProductReview("Great resolution", "This camera captures fantastic details, love it for landscapes.", productVariantRepository.findById(39L).get(), 7L),
                new ProductReview("Too bulky", "It’s a bit too bulky for my liking, but the quality is hard to beat.", productVariantRepository.findById(39L).get(), 8L),

                // Security Cam Wireless (40L)
                new ProductReview("Very reliable", "Great wireless security cam, no lag and very clear picture.", productVariantRepository.findById(40L).get(), 9L),
                new ProductReview("Needs better motion detection", "The motion detection is slow and sometimes doesn’t trigger alerts on time.", productVariantRepository.findById(40L).get(), 10L),

                // Security Cam Wired (41L)
                new ProductReview("Stable connection", "The wired connection is very stable and provides good video quality.", productVariantRepository.findById(41L).get(), 11L),
                new ProductReview("Wires are hard to manage", "The wires are a hassle to set up, but the cam works great once it’s set up.", productVariantRepository.findById(41L).get(), 12L),

                // Scooter Max Pro (42L)
                new ProductReview("Fast and efficient", "This scooter is fast and makes commuting so much easier.", productVariantRepository.findById(42L).get(), 13L),
                new ProductReview("Heavy", "The scooter is a bit heavy for me, but it works well.", productVariantRepository.findById(42L).get(), 14L),

                // Scooter Mini (43L)
                new ProductReview("Super portable", "Great scooter for the city, easy to carry and store.", productVariantRepository.findById(43L).get(), 15L),
                new ProductReview("Not for rough terrain", "The scooter works well on smooth roads, but struggles on rough terrain.", productVariantRepository.findById(43L).get(), 16L),

                // Projector Mini Plus (44L)
                new ProductReview("Good value", "Great little projector for the price, perfect for movie nights.", productVariantRepository.findById(44L).get(), 17L),
                new ProductReview("Needs better resolution", "The resolution could be better, especially for bigger screens.", productVariantRepository.findById(44L).get(), 18L),

                // Projector Ultra HD (45L)
                new ProductReview("Amazing quality", "The picture quality is stunning, perfect for home theater.", productVariantRepository.findById(45L).get(), 19L),
                new ProductReview("Very expensive", "It’s great, but very expensive for a projector.", productVariantRepository.findById(45L).get(), 20L)
        );

        productReviewRepository.saveAll(productReviews);
    }

    private void seedWarehouses() {
        List<Warehouse> warehouses = List.of(
                new Warehouse("New York Warehouse", "123 Main St, NY"),
                new Warehouse("California Warehouse", "456 West St, CA"),
                new Warehouse("Texas Warehouse", "789 South St, TX"),
                new Warehouse("Florida Warehouse", "101 Ocean Dr, FL"),
                new Warehouse("Chicago Warehouse", "222 Lake St, IL"),
                new Warehouse("Seattle Warehouse", "333 Pine St, WA"),
                new Warehouse("Denver Warehouse", "444 Mountain Rd, CO")
        );
        warehouseRepository.saveAll(warehouses);
    }

    private void seedInventory() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        List<ProductVariant> productVariants = productVariantRepository.findAll();

        // Hardcoded inventory for each product variant across 1 to 3 warehouses, with quantities from 0 to 100
        List<Inventory> inventoryList = Arrays.asList(
                // Product Variant 1 (Laptop Pro 16GB)
                new Inventory(50, warehouses.get(0), productVariants.get(0)), // Warehouse 1
                new Inventory(30, warehouses.get(1), productVariants.get(0)), // Warehouse 2

                // Product Variant 2 (Smartphone X 128GB)
                new Inventory(80, warehouses.get(0), productVariants.get(1)), // Warehouse 1
                new Inventory(40, warehouses.get(2), productVariants.get(1)), // Warehouse 3

                // Product Variant 3 (Gaming Mouse RGB)
                new Inventory(60, warehouses.get(1), productVariants.get(2)), // Warehouse 2
                new Inventory(20, warehouses.get(3), productVariants.get(2)), // Warehouse 4

                // Product Variant 4 (Earbuds Pro)
                new Inventory(0, warehouses.get(4), productVariants.get(3)), // Warehouse 5

                // Product Variant 5 (Smart TV 55-inch)
                new Inventory(70, warehouses.get(0), productVariants.get(4)), // Warehouse 1
                new Inventory(10, warehouses.get(2), productVariants.get(4)), // Warehouse 3
                new Inventory(15, warehouses.get(5), productVariants.get(4)), // Warehouse 6

                // Product Variant 6 (Fitness Tracker Pro)
                new Inventory(55, warehouses.get(3), productVariants.get(5)), // Warehouse 4

                // Product Variant 7 (Speaker Bass+)
                new Inventory(40, warehouses.get(1), productVariants.get(6)), // Warehouse 2
                new Inventory(10, warehouses.get(4), productVariants.get(6)), // Warehouse 5

                // Product Variant 8 (Keyboard RGB)
                new Inventory(30, warehouses.get(2), productVariants.get(7)), // Warehouse 3
                new Inventory(60, warehouses.get(6), productVariants.get(7)), // Warehouse 7

                // Product Variant 9 (Vacuum AutoClean)
                new Inventory(90, warehouses.get(0), productVariants.get(8)), // Warehouse 1
                new Inventory(50, warehouses.get(5), productVariants.get(8)), // Warehouse 6

                // Product Variant 10 (Smartwatch Premium)
                new Inventory(30, warehouses.get(3), productVariants.get(9)), // Warehouse 4
                new Inventory(25, warehouses.get(1), productVariants.get(9)), // Warehouse 2

                // Product Variant 11 (Headset Pro)
                new Inventory(0, warehouses.get(6), productVariants.get(10)), // Warehouse 7

                // Product Variant 12 (Camera 24MP)
                new Inventory(40, warehouses.get(1), productVariants.get(11)), // Warehouse 2
                new Inventory(20, warehouses.get(5), productVariants.get(11)), // Warehouse 6

                // Product Variant 13 (Security Cam)
                new Inventory(30, warehouses.get(2), productVariants.get(12)), // Warehouse 3
                new Inventory(50, warehouses.get(6), productVariants.get(12)), // Warehouse 7

                // Product Variant 14 (Scooter Max)
                new Inventory(70, warehouses.get(0), productVariants.get(13)), // Warehouse 1

                // Product Variant 15 (Projector Mini)
                new Inventory(80, warehouses.get(5), productVariants.get(14)), // Warehouse 6

                // Product Variant 16 (Laptop Pro 8GB)
                new Inventory(65, warehouses.get(0), productVariants.get(15)), // Warehouse 1
                new Inventory(10, warehouses.get(3), productVariants.get(15)), // Warehouse 4

                // Product Variant 17 (Laptop Pro 32GB)
                new Inventory(50, warehouses.get(2), productVariants.get(16)), // Warehouse 3
                new Inventory(20, warehouses.get(5), productVariants.get(16)), // Warehouse 6

                // Product Variant 18 (Smartphone X 64GB)
                new Inventory(10, warehouses.get(0), productVariants.get(17)), // Warehouse 1

                // Product Variant 19 (Smartphone X 256GB)
                new Inventory(75, warehouses.get(1), productVariants.get(18)), // Warehouse 2

                // Product Variant 20 (Gaming Mouse RGB Elite)
                new Inventory(30, warehouses.get(3), productVariants.get(19)), // Warehouse 4
                new Inventory(60, warehouses.get(6), productVariants.get(19)), // Warehouse 7

                // Product Variant 21 (Gaming Mouse Pro)
                new Inventory(40, warehouses.get(0), productVariants.get(20)), // Warehouse 1
                new Inventory(20, warehouses.get(4), productVariants.get(20)), // Warehouse 5

                // Product Variant 22 (Earbuds Pro Black)
                new Inventory(70, warehouses.get(1), productVariants.get(21)), // Warehouse 2
                new Inventory(50, warehouses.get(6), productVariants.get(21)), // Warehouse 7

                // Product Variant 23 (Earbuds Pro White)
                new Inventory(100, warehouses.get(2), productVariants.get(22)), // Warehouse 3
                new Inventory(40, warehouses.get(3), productVariants.get(22)), // Warehouse 4

                // Product Variant 24 (Smart TV 65-inch)
                new Inventory(10, warehouses.get(0), productVariants.get(23)), // Warehouse 1
                new Inventory(15, warehouses.get(5), productVariants.get(23)), // Warehouse 6

                // Product Variant 25 (Smart TV 75-inch)
                new Inventory(60, warehouses.get(4), productVariants.get(24)), // Warehouse 5
                new Inventory(30, warehouses.get(2), productVariants.get(24)), // Warehouse 3

                // Product Variant 26 (Fitness Tracker Lite)
                new Inventory(90, warehouses.get(3), productVariants.get(25)), // Warehouse 4
                new Inventory(30, warehouses.get(1), productVariants.get(25)), // Warehouse 2

                // Product Variant 27 (Fitness Tracker Advanced)
                new Inventory(20, warehouses.get(6), productVariants.get(26)), // Warehouse 7
                new Inventory(50, warehouses.get(0), productVariants.get(26)), // Warehouse 1

                // Product Variant 28 (Speaker Bass+ Pro)
                new Inventory(40, warehouses.get(1), productVariants.get(27)), // Warehouse 2
                new Inventory(20, warehouses.get(3), productVariants.get(27)), // Warehouse 4

                // Product Variant 29 (Speaker Bass+ Mini)
                new Inventory(70, warehouses.get(4), productVariants.get(28)), // Warehouse 5
                new Inventory(50, warehouses.get(0), productVariants.get(28)), // Warehouse 1

                // Product Variant 30 (Keyboard RGB Elite)
                new Inventory(40, warehouses.get(5), productVariants.get(29)), // Warehouse 6
                new Inventory(25, warehouses.get(3), productVariants.get(29)), // Warehouse 4

                // Product Variant 31 (Keyboard RGB Compact)
                new Inventory(60, warehouses.get(2), productVariants.get(30)), // Warehouse 3
                new Inventory(15, warehouses.get(1), productVariants.get(30)), // Warehouse 2

                // Product Variant 32 (Vacuum AutoClean Max)
                new Inventory(80, warehouses.get(0), productVariants.get(31)), // Warehouse 1
                new Inventory(40, warehouses.get(6), productVariants.get(31)), // Warehouse 7

                // Product Variant 33 (Vacuum AutoClean Mini)
                new Inventory(100, warehouses.get(5), productVariants.get(32)), // Warehouse 6
                new Inventory(30, warehouses.get(1), productVariants.get(32)), // Warehouse 2

                // Product Variant 34 (Smartwatch Basic)
                new Inventory(60, warehouses.get(4), productVariants.get(33)), // Warehouse 5
                new Inventory(50, warehouses.get(2), productVariants.get(33)), // Warehouse 3

                // Product Variant 35 (Smartwatch Sport)
                new Inventory(30, warehouses.get(0), productVariants.get(34)), // Warehouse 1
                new Inventory(20, warehouses.get(3), productVariants.get(34)), // Warehouse 4

                // Product Variant 36 (Headset Pro Wireless)
                new Inventory(90, warehouses.get(1), productVariants.get(35)), // Warehouse 2
                new Inventory(10, warehouses.get(6), productVariants.get(35)), // Warehouse 7

                // Product Variant 37 (Headset Pro Wired)
                new Inventory(40, warehouses.get(5), productVariants.get(36)), // Warehouse 6
                new Inventory(50, warehouses.get(4), productVariants.get(36)), // Warehouse 5

                // Product Variant 38 (Camera 18MP)
                new Inventory(70, warehouses.get(3), productVariants.get(37)), // Warehouse 4
                new Inventory(30, warehouses.get(2), productVariants.get(37)), // Warehouse 3

                // Product Variant 39 (Camera 32MP)
                new Inventory(100, warehouses.get(1), productVariants.get(38)), // Warehouse 2
                new Inventory(40, warehouses.get(5), productVariants.get(38)), // Warehouse 6

                // Product Variant 40 (Security Cam Wireless)
                new Inventory(90, warehouses.get(0), productVariants.get(39)), // Warehouse 1
                new Inventory(50, warehouses.get(4), productVariants.get(39)), // Warehouse 5

                // Product Variant 41 (Security Cam Wired)
                new Inventory(30, warehouses.get(2), productVariants.get(40)), // Warehouse 3
                new Inventory(10, warehouses.get(6), productVariants.get(40)), // Warehouse 7

                // Product Variant 42 (Scooter Max Pro)
                new Inventory(60, warehouses.get(3), productVariants.get(41)), // Warehouse 4
                new Inventory(20, warehouses.get(1), productVariants.get(41)), // Warehouse 2

                // Product Variant 43 (Scooter Mini)
                new Inventory(70, warehouses.get(5), productVariants.get(42)), // Warehouse 6
                new Inventory(10, warehouses.get(4), productVariants.get(42)), // Warehouse 5

                // Product Variant 44 (Projector Mini Plus)
                new Inventory(60, warehouses.get(0), productVariants.get(43)), // Warehouse 1
                new Inventory(25, warehouses.get(6), productVariants.get(43)), // Warehouse 7

                // Product Variant 45 (Projector Ultra)
                new Inventory(50, warehouses.get(2), productVariants.get(44)), // Warehouse 3
                new Inventory(30, warehouses.get(5), productVariants.get(44)) // Warehouse 6
        );

        // Save all inventory entries to the repository
        inventoryRepository.saveAll(inventoryList);
    }

}

