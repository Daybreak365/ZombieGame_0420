package sinsa.zombie.config.cached;

public interface Node {

	String getPath();
	Object getDefault();
	boolean hasCacher();
	Cacher getCacher();
	String[] getComments();

}
