package org.isihop.fr.perTranslateClient;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerTranslateServiceMain{

	public static void main(String[] args) {
                SpringApplication application = new SpringApplication(PerTranslateServiceMain.class);
		application.setBannerMode(Mode.OFF);
                application.run(args);
	}

}