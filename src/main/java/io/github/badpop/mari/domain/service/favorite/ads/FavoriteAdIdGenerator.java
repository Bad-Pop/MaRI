package io.github.badpop.mari.domain.service.favorite.ads;

import java.net.URI;
import java.time.LocalDateTime;

import static io.vavr.API.Try;
import static java.util.UUID.randomUUID;

public interface FavoriteAdIdGenerator {

    static String generateFavoriteAdId(String adUrl) {
        return Try(() -> new URI(adUrl))
                .map(URI::getHost)
                .map(String::toLowerCase)
                .map(adDomain -> randomUUID() + "__" + adDomain + "__" + LocalDateTime.now())
                .getOrElse(randomUUID() + "__" + LocalDateTime.now());
    }
}
