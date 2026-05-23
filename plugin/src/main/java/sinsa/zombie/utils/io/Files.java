package sinsa.zombie.utils.io;

import sinsa.zombie.utils.logging.LogType;
import sinsa.zombie.utils.logging.Logger;

import java.io.File;
import java.io.IOException;

public class Files {

	private static final Logger logger = Logger.getLogger(Files.class.getName());
	private static final File mainDirectory = new File("plugins/ZombieGame");

	static {
		if (!mainDirectory.exists()) {
			mainDirectory.mkdirs();
		}
	}

	private Files() {}

	public static File getFile(String path, boolean createIfAbsent) {
		final File file = new File(mainDirectory.getPath() + "/" + path);
		try {
			if (!file.exists() && createIfAbsent) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}

			return file;
		} catch (IOException e) {
			logger.log(LogType.ERROR, file.getPath() + " 파일을 생성하지 못했습니다.");
			return file;
		}
	}

	public static File getDirectory(String path, boolean createIfAbsent) {
		final File directory = new File(mainDirectory.getPath() + "/" + path);
		if (!directory.exists() && createIfAbsent) {
			directory.mkdirs();
		}
		return directory;
	}

}
