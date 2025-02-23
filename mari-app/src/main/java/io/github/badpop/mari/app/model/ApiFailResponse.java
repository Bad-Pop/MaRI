package io.github.badpop.mari.app.model;

import io.github.badpop.mari.domain.control.MariFail;
import io.vavr.collection.Seq;

public record ApiFailResponse(Seq<ApiFail> failures) {

    public record ApiFail(String code, String message) {

        public static ApiFail from(MariFail fail) {
            return new ApiFail(fail.code(), fail.message());
        }
    }
}
