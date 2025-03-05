package io.github.badpop.mari.application.domain.ad.model;

import io.vavr.control.Option;

import java.util.UUID;

public record Ad(UUID id,
                 String name,
                 String url,
                 double price,
                 AdType type,
                 Option<String> description,
                 Option<String> remarks,
                 Option<String> address) {
}
