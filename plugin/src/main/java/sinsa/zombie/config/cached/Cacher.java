package sinsa.zombie.config.cached;

public interface Cacher {

	Object toCache(Object object);
	Object revertCache(Object object);

}
