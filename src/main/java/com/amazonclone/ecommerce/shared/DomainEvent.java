package com.amazonclone.ecommerce.shared;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}

