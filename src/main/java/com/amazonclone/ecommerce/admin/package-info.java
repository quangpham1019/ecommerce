@org.springframework.modulith.ApplicationModule(
        displayName = "Admin",
        allowedDependencies = {
                "catalog",
                "inventory",
                "ordering",
                "shipping",
                "identity",
                "shared"
        }
)
package com.amazonclone.ecommerce.admin;

