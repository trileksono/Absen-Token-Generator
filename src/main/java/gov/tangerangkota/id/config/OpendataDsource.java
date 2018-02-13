package gov.tangerangkota.id.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by manjaro on 07/02/18.
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(basePackages = "gov.tangerangkota.id.opendata.repo", // ini nama folder interface DAO
    entityManagerFactoryRef = "opendataEntityManager",
    transactionManagerRef = "opendataTransactionManager")
public class OpendataDsource {

  @Autowired
  private Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean opendataEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(opendataDataSource());
    em.setPackagesToScan("gov.tangerangkota.id.opendata.entity"); //ini nama folder file entity atau model

    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", env.getProperty("mysql.dialect"));
    properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Bean
  public DataSource opendataDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(env.getProperty("mysql.driverClassName"));
    dataSource.setJdbcUrl(env.getProperty("opendata.url"));
    dataSource.setUsername(env.getProperty("opendata.username"));
    dataSource.setPassword(env.getProperty("opendata.password"));
    dataSource.setMaximumPoolSize(20);
    dataSource.setMinimumIdle(1);
    dataSource.setIdleTimeout(60000);
    dataSource.setMaxLifetime(120000);
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager opendataTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(opendataEntityManager().getObject());
    return transactionManager;
  }
}
