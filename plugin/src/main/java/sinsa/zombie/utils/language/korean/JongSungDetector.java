package sinsa.zombie.utils.language.korean;

public interface JongSungDetector {

	boolean canHandle(String str);

	int getJongSungType(String str);

}
