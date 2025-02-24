package io.github.badpop.mari.domain.model.ad;

public record AdSummary(String id,
                        String name,
                        String url,
                        AdType type,
                        Double price) {
}
