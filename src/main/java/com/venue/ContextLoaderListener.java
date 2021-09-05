package com.venue;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import java.io.InputStream;
import java.util.jar.Manifest;

/**
 * Spring context loader that listens for when context is first initialized
 * print build properties to log output at that time
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {

    public static Logger logger = Logger.getLogger(com.venue.ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        printBuildProps(event);
        super.contextInitialized(event);
    }

    /**
     * print to logger all the build properties captured in the war manifest file if available on classpath
     */
    private void printBuildProps(ServletContextEvent event) {
        try {
            InputStream inputStream = event.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            if (inputStream == null) {
                logger.warn("No war manifest file found, will not log build properties");
                return;
            }
            Manifest manifest = new Manifest(inputStream);
            logger.warn("Build Timestamp: " + manifest.getMainAttributes().getValue("Build-Time"));
            logger.warn("Project Name: " + manifest.getMainAttributes().getValue("Implementation-Title"));
            logger.warn("Version: " + manifest.getMainAttributes().getValue("Version"));
            logger.warn("Build-Number: " + manifest.getMainAttributes().getValue("Build-Number"));
            logger.warn("Build-Url: " + manifest.getMainAttributes().getValue("Build-Url"));
            logger.warn("SCM-Revision: " + manifest.getMainAttributes().getValue("SCM-Revision"));
        } catch (Exception ex) {
            logger.info("unable to obtain build info from war");
        }
    }
}
