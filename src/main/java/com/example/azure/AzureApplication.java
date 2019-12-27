package com.example.azure;

import com.example.azure.cron.ICronExecutable;
import com.example.azure.persistence.models.ScheduledTaskModel;
import com.example.azure.persistence.repository.ProductRepository;
import com.example.azure.persistence.repository.ScheduledTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Date;
import java.util.List;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
public class AzureApplication {

	private static final Logger LOG = LoggerFactory.getLogger(AzureApplication.class);

	@Autowired
	private ScheduledTaskRepository scheduledTaskRepository;

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(AzureApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Pbkdf2PasswordEncoder();
	}

	@Scheduled(fixedRate = 5000)
	public void scheduledJob() {
		final List<ScheduledTaskModel> scheduledTaskModels = scheduledTaskRepository.findAll();
		for (ScheduledTaskModel scheduledTaskModel : scheduledTaskModels) {
			try {
				if (scheduledTaskModel.getRecurrenceMillis() < (System.currentTimeMillis() - scheduledTaskModel.getLatestExecutionTime().toInstant().toEpochMilli())) {
					LOG.info("task {} start", scheduledTaskModel.getId());
					Class<?> scheduledTaskClass = Class.forName(scheduledTaskModel.getScheduledTaskClass());
					((ICronExecutable) ReflectUtils.newInstance(scheduledTaskClass)).execute();
					scheduledTaskModel.setLatestExecutionTime(new Date());
					scheduledTaskRepository.save(scheduledTaskModel);
					LOG.info("task {}, {} successfully finished", scheduledTaskModel.getId(), scheduledTaskModel.getScheduledTaskClass());
				}
			} catch (Exception e) {
				LOG.info("task {} finished with error, {}", scheduledTaskModel.getId(), e);
			}
		}
	}
}
