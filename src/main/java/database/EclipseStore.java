package database;

import java.nio.file.Paths;

import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;


public class EclipseStore {

	private final static Database db = new SQLDatabase();
	private final static EmbeddedStorageManager storageManager;
	// System.getProperty("java.io.tmpdir")
	// System.getProperty("user.home")
	
	static {
		storageManager = EmbeddedStorage.start(
			    db,             // root object
			    Paths.get(System.getProperty("user.home")+"/dataea3") // storage directory
			);
		db.prepareStorage();
	}

	public static Database db() {
		return EclipseStore.db;
	}

	public static EmbeddedStorageManager storageManager() {
		return EclipseStore.storageManager;
	}

	private EclipseStore() {
	}

}
