package de.jpx3.intave.connect;

import de.jpx3.intave.resource.Resource;
import de.jpx3.intave.resource.Resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class IntaveDomains {
  private static final Resource DOMAIN_CACHE_RESOURCE = Resources.fileCache("domains");
  private static final DomainCache DOMAIN_CACHE = DOMAIN_CACHE_RESOURCE.collectLines(DomainCache.lineCollector());

  private static final Map<String, Long> HARDCODED_BASE_DOMAINS = Collections.singletonMap("intave.zkmjnic.tech", 0L);
  private static final Map<String, Long> HARDCODED_SERVICE_DOMAINS = Collections.singletonMap("service.zkmjnic.tech", 0L);

  public static void setup() {
  }

  static {
    if (!DOMAIN_CACHE.valid()) {
      DOMAIN_CACHE.override(HARDCODED_BASE_DOMAINS, HARDCODED_SERVICE_DOMAINS);
      DOMAIN_CACHE.saveTo(DOMAIN_CACHE_RESOURCE);
    }
  }

  public static String primaryServiceDomain() {
    return DOMAIN_CACHE.serviceDomain();
  }

  public static List<String> serviceDomains() {
    return DOMAIN_CACHE.serviceDomains();
  }

}
