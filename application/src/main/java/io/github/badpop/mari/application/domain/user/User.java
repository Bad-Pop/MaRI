package io.github.badpop.mari.application.domain.user;

public record User(String id,
                   String name,
                   String nickname,
                   String pictureUrl,
                   String email) {}
