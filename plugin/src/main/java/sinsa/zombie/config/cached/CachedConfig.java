package sinsa.zombie.config.cached;

import org.bukkit.configuration.InvalidConfigurationException;
import sinsa.zombie.config.FileConfiguration;
import sinsa.zombie.utils.io.Files;
import sinsa.zombie.utils.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

public class CachedConfig<N extends Enum<N> & Node> {

	private static final Logger logger = Logger.getLogger(CachedConfig.class.getName());
	private final EnumMap<N, Cache> cache;
	private final N[] nodes;
	private final File file;
	private long lastModified;
	private final FileConfiguration config;

	public CachedConfig(final Class<N> enumClass, final String filePath) throws IOException, InvalidConfigurationException {
		this.cache = new EnumMap<>(enumClass);
		this.nodes = enumClass.getEnumConstants();
		this.file = Files.getFile(filePath, true);
		this.lastModified = Long.MIN_VALUE;
		this.config = new FileConfiguration(file);
	}

	public void update() throws IOException, InvalidConfigurationException {
		config.load();
		for (Entry<N, Cache> entry : cache.entrySet()) {
			final N node = entry.getKey();
			final Cache cache = entry.getValue();
			if (cache.isModifiedValue()) {
				if (node.hasCacher()) {
					config.set(node.getPath(), node.getCacher().revertCache(cache.getValue()));
				} else {
					config.set(node.getPath(), cache.getValue());
				}
			}
		}

		cache.clear();
		for (final N node : nodes) {
			if (node.hasCacher()) {
				final Cacher handler = node.getCacher();
				if (config.isSet(node.getPath())) {
					cache.put(node, new Cache(false, handler.toCache(config.get(node.getPath()))));
				} else {
					config.set(node.getPath(), node.getDefault());
					cache.put(node, new Cache(false, handler.toCache(node.getDefault())));
				}
			} else {
				Object value = config.get(node.getPath());
				if (value != null) {
					cache.put(node, new Cache(false, value));
				} else {
					config.set(node.getPath(), node.getDefault());
					cache.put(node, new Cache(false, node.getDefault()));
				}
			}
		}
		config.save();
		lastModified = file.lastModified();
	}

	public FileConfiguration getConfig() {
		return config;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(N node) throws IllegalStateException {
		if (lastModified != file.lastModified()) {
			try {
				update();
			} catch (IOException | InvalidConfigurationException e) {
				logger.log(Level.SEVERE, "콘피그를 다시 불러오는 도중 오류가 발생하였습니다.");
			}
		}
		return (T) cache.get(node).getValue();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(N node, Class<T> clazz) throws IllegalStateException {
		final List<?> list = get(node);
		final List<T> newList = new ArrayList<>();
		for (final Object object : list) {
			if (object != null && clazz.isAssignableFrom(object.getClass())) {
				newList.add((T) object);
			}
		}
		return newList;
	}

	public <T> Set<T> getSet(N node) throws IllegalStateException {
		return get(node);
	}

	public <T, U> Map<T, U> getMap(N node) throws IllegalStateException {
		return get(node);
	}

	public void setProperty(N node, Object value) {
		cache.put(node, new Cache(true, value));
	}

	public void updateProperty(N node) {
		cache.put(node, new Cache(true, cache.get(node).getValue()));
	}

}
