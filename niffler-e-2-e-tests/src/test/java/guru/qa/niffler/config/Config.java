package guru.qa.niffler.config;

public interface Config {
    String docker = "docker";
    String env = "test.env";

    static Config getInstance() {
        return docker.equals(System.getProperty(env))
                ? DockerConfig.instance
                : LocalConfig.instance;
    }

    String frontUrl();

    String spendUrl();

    String dbHost();

    default int dbPort() {
        return 5432;
    }
}
