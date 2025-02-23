package io.github.badpop.mari.domain.model.favorite.ads;

import io.vavr.control.Option;

public record FavoriteAd(String id,
                         String name,
                         String url,
                         FavoriteAdType type,
                         Option<String> description,
                         Option<String> remarks,
                         Option<String> address,
                         Option<Double> price,//TODO REFACTOR : MAKE REQUIRED AND AD IT TO THE SUMMARY PROJECTION
                         Option<Double> pricePerSquareMeter) {
}
