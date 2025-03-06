package io.github.badpop.mari.application.domain.ad.control;

import io.vavr.control.Option;

import java.time.LocalDateTime;

public record AdSharingParameters(boolean expires, Option<LocalDateTime> expireAt) {
}
