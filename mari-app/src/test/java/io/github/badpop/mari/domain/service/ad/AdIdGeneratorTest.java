package io.github.badpop.mari.domain.service.ad;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdIdGeneratorTest {

  @Test
  void should_create_id_with_domain() {
    val url = "https://www.mari.fr/annonce/123456789";
    val actual = AdIdGenerator.generateAdId(url);
    assertThat(actual).contains("www.mari.fr");
  }

  @Test
  void should_create_id_without_domain() {
    val malformedUrl = "http/://www.mari.fr/annonce/123456789";
    val actual = AdIdGenerator.generateAdId(malformedUrl);
    assertThat(actual.split("__")).hasSize(2);
  }
}
