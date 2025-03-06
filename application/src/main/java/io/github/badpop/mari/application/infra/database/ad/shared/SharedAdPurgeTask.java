package io.github.badpop.mari.application.infra.database.ad.shared;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.time.LocalDateTime;

import static java.util.concurrent.TimeUnit.MINUTES;

@Singleton
@RequiredArgsConstructor
public class SharedAdPurgeTask {

  private static final String EXPIRE_AT_WHERE_CLAUSE = "expireAt < ?1";

  private final SharedAdRepository repository;

  @Transactional
  @Scheduled(
          identity = "PurgeExpiredSharedAdsScheduledTask",
          cron = "{mari.scheduled-task.purge-expired-shared-ad.cron}",
          delay = 3,
          delayUnit = MINUTES)
  public void purgeExpiredSharedAds() {
    Log.info("Expired shared ads purge task is starting !");

    val now = LocalDateTime.now();

    try {
      val expiredSharedAdsCount = repository.count(EXPIRE_AT_WHERE_CLAUSE, now);
      Log.infov("There is {0} expired shared ads to delete. Deleting...", expiredSharedAdsCount);

      val expiredSharedAdsDeletedCount = repository.delete(EXPIRE_AT_WHERE_CLAUSE, now);
      Log.infov("Successfully deleted {0} expired shared ads.", expiredSharedAdsDeletedCount);
    } catch (Exception e) {
      Log.error("An error occurred while trying to purge expired shared ads...");
      throw e;
    }

    Log.info("Expired shared ads purge task is terminated !");
  }
}
