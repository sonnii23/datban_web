package helper;

import java.util.logging.Logger;

import org.eclipse.serializer.reference.LazyReferenceManager;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import database.EclipseStore;

@WebListener
public class EclipseStoreStart implements ServletContextListener {

	private static final Logger log = Logger.getLogger(EclipseStoreStart.class.getName());
	
	// wird bei Start des Servers aufgerufen
    public void contextInitialized(ServletContextEvent sce) {
    	
    	EclipseStore.storageManager();
    	log.info("Anwendung gestartet.");
    }

    public void contextDestroyed(ServletContextEvent sce) {
    	EclipseStore.storageManager().shutdown();
    	
    	// warten, bis Threads beendet sind
    	while (EclipseStore.storageManager().isActive()) {
    		try {
    			Thread.sleep(100);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	log.info("Anwendung heruntergefahren.");
    }
}
