package io.github.badpop.mari.lib.http.monitoring;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IOLogStatus {
    OK("success"),
    KO("failure");

    private final String label;
}
