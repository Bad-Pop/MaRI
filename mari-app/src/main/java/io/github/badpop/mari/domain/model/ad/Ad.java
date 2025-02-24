package io.github.badpop.mari.domain.model.ad;

import io.vavr.control.Option;

public record Ad(String id,
                 String name,
                 String url,
                 AdType type,
                 Double price,
                 Option<String> description,
                 Option<String> remarks,
                 Option<String> address,
                 Option<Double> pricePerSquareMeter) {
}
