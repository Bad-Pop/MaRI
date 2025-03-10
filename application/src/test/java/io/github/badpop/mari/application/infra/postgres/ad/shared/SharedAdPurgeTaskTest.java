package io.github.badpop.mari.application.infra.postgres.ad.shared;

import io.github.badpop.mari.application.generators.AdEntityGenerator;
import io.github.badpop.mari.application.DatabaseCleaner;
import io.github.badpop.mari.application.generators.SharedAdEntityGenerator;
import io.github.badpop.mari.application.generators.UserEntityGenerator;
import io.github.badpop.mari.application.infra.postgres.ad.AdRepository;
import io.github.badpop.mari.application.infra.postgres.user.UserRepository;
import io.github.badpop.mari.libraries.tests.WithSharedPostgres;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@QuarkusTest
@WithSharedPostgres
class SharedAdPurgeTaskTest {

  @Inject
  DatabaseCleaner dbCleaner;

  @Inject
  UserRepository userRepository;

  @Inject
  AdRepository adRepository;

  @Inject
  SharedAdRepository sharedAdRepository;

  @Inject
  SharedAdPurgeTask task;

  @AfterEach
  @Transactional
  void tearDown() {
    dbCleaner.cleanAllTables();
  }

  @Test
  @TestTransaction
  void should_purge_expired_shared_ads() {
    val userEntity = UserEntityGenerator.generate("userId");
    val adEntity = AdEntityGenerator.generate(UUID.randomUUID(), userEntity);
    val adEntityTwo = AdEntityGenerator.generate(UUID.randomUUID(), userEntity);
    val expiredSharedAdEntity = SharedAdEntityGenerator.generate(UUID.randomUUID(), adEntity, userEntity, true, LocalDateTime.now().minusDays(1));
    val notExpiredSharedAdEntity = SharedAdEntityGenerator.generate(UUID.randomUUID(), adEntityTwo, userEntity, true, LocalDateTime.now().plusDays(1));

    userRepository.persist(userEntity);
    adRepository.persist(adEntity);
    adRepository.persist(adEntityTwo);
    sharedAdRepository.persist(expiredSharedAdEntity);
    sharedAdRepository.persist(notExpiredSharedAdEntity);

    assertThatNoException().isThrownBy(() -> task.purgeExpiredSharedAds());

    val actual = sharedAdRepository.count();
    assertThat(actual).isEqualTo(1);

    val lastSharedAd = sharedAdRepository.find("id", notExpiredSharedAdEntity.getId()).firstResult();
    assertThat(lastSharedAd).isEqualTo(notExpiredSharedAdEntity);
  }
}
