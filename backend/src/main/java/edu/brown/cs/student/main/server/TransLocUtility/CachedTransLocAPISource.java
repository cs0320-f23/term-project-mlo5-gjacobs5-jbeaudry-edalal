package edu.brown.cs.student.main.server.TransLocUtility;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class CachedTransLocAPISource implements APISource {

  private final APISource wrappedSource;
  private final LoadingCache<String, List<BUSRoute>> cacheRoutes;
  private final LoadingCache<String, List<BUSStops>> cacheRouteStops;
  private final LoadingCache<String, List<BUSRouteStopMapping>> cacheRouteStopMappings;

  public CachedTransLocAPISource(APISource toWrap, int maxEntries, int minutesToExpire) {
    this.wrappedSource = toWrap;

    this.cacheRoutes =
        CacheBuilder.newBuilder()
            .maximumSize(maxEntries)
            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @NotNull
                  @Override
                  public List<BUSRoute> load(@NotNull String key) throws ShuttleDataException {
                    return wrappedSource.getRoutes();
                  }
                });

    this.cacheRouteStops =
        CacheBuilder.newBuilder()
            .maximumSize(maxEntries)
            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @NotNull
                  @Override
                  public List<BUSStops> load(@NotNull String key) throws ShuttleDataException {
                    return wrappedSource.getStops();
                  }
                });

    this.cacheRouteStopMappings =
        CacheBuilder.newBuilder()
            .maximumSize(maxEntries)
            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @NotNull
                  @Override
                  public List<BUSRouteStopMapping> load(@NotNull String key)
                      throws ShuttleDataException {
                    return wrappedSource.getRouteStopMappings();
                  }
                });
  }

  @Override
  public List<BUSRoute> getRoutes() throws ShuttleDataException {
    try {
      return cacheRoutes.get("routes");
    } catch (ExecutionException e) {
      throw new ShuttleDataException(e.getMessage());
    }
  }

  @Override
  public List<BUSStops> getStops() throws ShuttleDataException {
    try {
      return cacheRouteStops.get("stops");
    } catch (ExecutionException e) {
      throw new ShuttleDataException(e.getMessage());
    }
  }

  @Override
  public List<BUSRouteStopMapping> getRouteStopMappings() throws ShuttleDataException {
    try {
      return cacheRouteStopMappings.get("routeStopMappings");
    } catch (ExecutionException e) {
      throw new ShuttleDataException(e.getMessage());
    }
  }
}
