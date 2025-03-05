package io.github.badpop.mari.application.domain.patch;

public record UpdateOperation(String op, String path, String value) {
}
