package marshmallow.config;

import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.config.yaml.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class Configuration {

    private final ClassLoader classLoader;
    private final String fileName;
    private YamlConfiguration fileConfiguration;
    private File folder;
    private File configFile;

    public Configuration(Marshmallow marshmallow, File folder, String name) {
        this.fileName = name;
        this.folder = folder;
        this.configFile = new File(folder, name);
        this.fileConfiguration = new YamlConfiguration(name);

        classLoader = marshmallow.getClass().getClassLoader();
    }

    public YamlConfiguration getConfig() {
        if (fileConfiguration == null) {
            reload();
        }
        return fileConfiguration;
    }

    public void reload() {
        if (configFile == null) {
            if (folder == null) {
                throw new IllegalStateException();
            }
            configFile = new File(folder, fileName);
        }
        fileConfiguration = new YamlConfiguration(fileName);
    }

    private void save() {
        saveResource(fileName, true);
    }

    public void saveDefaultConfig() {
        if (!exists()) {
            saveResource(fileName, false);
        }
    }

    private void saveResource(String path, boolean replace) {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("Path cannot be null or empty.");
        }

        path = path.replace('\\', '/');
        InputStream in = getResource(path);

        if (in == null) {
            throw new IllegalArgumentException("The resource '" + path + "' cannot be found in " + fileName);
        }

        File outFile = new File(folder, path);
        int lastIndex = path.lastIndexOf('/');
        File outDir = new File(folder, path.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                log.warn("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException e) {
            log.warn("Could not save " + outFile.getName() + " to " + outFile, e);
        }
    }


    private InputStream getResource(String fileName) {
        if (fileName == null || fileName.equals("")) {
            throw new IllegalArgumentException("Filename cannot be null or empty.");
        }

        try {
            URL url = classLoader.getResource(fileName);

            if (url == null) return null;

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);

            return connection.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    private boolean exists() {
        return configFile != null && configFile.exists() && configFile.isFile();
    }
}
