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
    if (!DOMAIN_CACHE.valid()) {
      DOMAIN_CACHE.override(HARDCODED_BASE_DOMAINS, HARDCODED_SERVICE_DOMAINS);
      DOMAIN_CACHE.saveTo(DOMAIN_CACHE_RESOURCE);
    }
  }

  private static long ping(String domain) {
    String url = "https://" + domain + "/connection-test.php";
    try {
      long start = System.currentTimeMillis();
      URLConnection connection = new URL(url).openConnection();
      connection.setConnectTimeout(1600);
      connection.setReadTimeout(1600);
      connection.setRequestProperty("User-Agent", "Intave/" + IntavePlugin.fullVersion());
      connection.connect();
      Scanner scanner = new Scanner(connection.getInputStream());
      String response = scanner.nextLine();
      scanner.close();
      long end = System.currentTimeMillis();
      if (response.contains("success")) {
        return end - start;
      } else {
        return Long.MAX_VALUE;
      }
    } catch (Exception e) {
      if (IntaveControl.DEBUG) {
        System.out.println("Could not connect to " + domain + " (" + url + "): " + e.getMessage());
      }
      return Long.MAX_VALUE;
    }
  }

  public static String primaryServiceDomain() {
    return DOMAIN_CACHE.serviceDomain();
  }

  public static List<String> serviceDomains() {
    return DOMAIN_CACHE.serviceDomains();
  }

}
