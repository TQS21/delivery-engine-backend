package ua.tqs21.deliveryengine;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ua.tqs21.deliveryengine.models.UserRole;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

@SpringBootApplication
@EnableAutoConfiguration
public class DeliveryEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryEngineApplication.class, args);
	}

	@Transactional
	@Bean
	public CommandLineRunner demo(UserRoleRepository roleRepository) {
		return (args) -> {
			roleRepository.save(new UserRole("ADMIN"));
			roleRepository.save(new UserRole("RIDER"));
			roleRepository.save(new UserRole("SERVICE_OWNER"));
		};
	}

}
