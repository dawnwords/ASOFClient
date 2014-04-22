package edu.fudan.se.asof.felix;

import edu.fudan.se.asof.engine.Log;
import org.twdata.pkgscanner.ExportPackage;
import org.twdata.pkgscanner.PackageScanner;

import java.util.Collection;
import java.util.Properties;


/**
 * Because it is somehow hard to use conf/config.properties file in android, I build up properties object hard coded for now
 *
 * @author matthiasneubert
 */
public class FelixConfig {

    private Properties configuration;

    public FelixConfig(String absFilePath) {
        analyzeClassPath();
        configuration = new Properties();
        configuration.put("org.osgi.framework.storage", absFilePath + "/felix/cache");
        configuration.put("felix.cache.rootdir", absFilePath + "/felix");
        configuration.put("felix.fileinstall.dir", absFilePath + "/felix/newbundle");
        configuration.put("felix.fileinstall.debug", "1");
        configuration.put("org.osgi.framework.system.packages.extra", ANDROID_FRAMEWORK_PACKAGES_ext);
        configuration.put("felix.log.level", "4");
        configuration.put("felix.auto.deploy.action","install,start");
        configuration.put("felix.startlevel.bundle", "1");
    }

    public Properties getConfiguration() {
        return configuration;
    }

    // package scanner
    private void analyzeClassPath() {
        PackageScanner pkgScanner = new PackageScanner();
        pkgScanner.useClassLoader(PackageScanner.class.getClassLoader().getParent());

        Collection<ExportPackage> exports = pkgScanner.select(
                PackageScanner.jars(
                        PackageScanner.include("*.jar"),
                        PackageScanner.exclude("felix.jar", "package*.jar")
                ),
                PackageScanner.packages(
                        PackageScanner.include(
                                "org.*", "com.*", "javax.*", "android", "android.*", "com.android.*",
                                "dalvik.*", "java.*", "junit.*", "org.apache.*", "org.json", "org.xml.*",
                                "org.xmlpull.*", "org.w3c.*"
                        )
                )
        ).scan();
        Log.debug("HIER: " + exports.size());
        // now fill analyzedExportString
        while (exports.iterator().hasNext()) {
            Log.debug("exports: " + exports.iterator().next().getPackageName());
        }
    }

    private static final String ANDROID_FRAMEWORK_PACKAGES_ext = (
            "org.osgi.framework; version=1.4.0," +
                    "org.osgi.service.packageadmin; version=1.2.0," +
                    "org.osgi.service.startlevel; version=1.0.0," +
                    "org.osgi.service.url; version=1.0.0," +
                    "org.osgi.util.tracker," +
                    // ANDROID (here starts semicolon as separator -> Why?
                    "android; " +
                    "android.app;" +
                    "android.content;" +
                    "android.database;" +
                    "android.database.sqlite;" +
                    "android.graphics; " +
                    "android.graphics.drawable; " +
                    "android.graphics.glutils; " +
                    "android.hardware; " +
                    "android.location; " +
                    "android.media; " +
                    "android.net; " +
                    "android.opengl; " +
                    "android.os; " +
                    "android.provider; " +
                    "android.sax; " +
                    "android.speech.recognition; " +
                    "android.telephony; " +
                    "android.telephony.gsm; " +
                    "android.text; " +
                    "android.text.method; " +
                    "android.text.style; " +
                    "android.text.util; " +
                    "android.util; " +
                    "android.view; " +
                    "android.view.animation; " +
                    "android.webkit; " +
                    "android.widget; " +
                    //MAPS
                    "com.google.android.maps; " +
                    "com.google.android.xmppService; " +
                    // JAVAx
                    "javax.crypto; " +
                    "javax.crypto.interfaces; " +
                    "javax.crypto.spec; " +
                    "javax.microedition.khronos.opengles; " +
                    "javax.net; " +
                    "javax.net.ssl; " +
                    "javax.security.auth; " +
                    "javax.security.auth.callback; " +
                    "javax.security.auth.login; " +
                    "javax.security.auth.x500; " +
                    "javax.security.cert; " +
                    "javax.sound.midi; " +
                    "javax.sound.midi.spi; " +
                    "javax.sound.sampled; " +
                    "javax.sound.sampled.spi; " +
                    "javax.sql; " +
                    "javax.xml.parsers; " +
                    //JUNIT
                    "junit.extensions; " +
                    "junit.framework; " +
                    //APACHE
                    "org.apache.commons.codec; " +
                    "org.apache.commons.codec.binary; " +
                    "org.apache.commons.codec.language; " +
                    "org.apache.commons.codec.net; " +
                    "org.apache.commons.httpclient; " +
                    "org.apache.commons.httpclient.auth; " +
                    "org.apache.commons.httpclient.cookie; " +
                    "org.apache.commons.httpclient.methods; " +
                    "org.apache.commons.httpclient.methods.multipart; " +
                    "org.apache.commons.httpclient.params; " +
                    "org.apache.commons.httpclient.protocol; " +
                    "org.apache.commons.httpclient.util; " +
                    //OTHERS
                    "org.bluez; " +
                    "org.json; " +
                    "org.w3c.dom; " +
                    "org.xml.sax; " +
                    "org.xml.sax.ext; " +
                    "org.xml.sax.helpers; " +
                    // Android OS Version?? ->her ends semicolon as seperator -> Why?
                    "version=1.5.0.r3," +
                    // MY OWN
                    "edu.fudan.se.asof.engine"
    ).intern();
}
