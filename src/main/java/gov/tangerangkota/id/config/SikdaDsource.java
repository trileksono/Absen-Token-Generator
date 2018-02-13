package gov.tangerangkota.id.config;


import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by manjaro on 06/02/18.
 */
@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(basePackages = "gov.tangerangkota.id.sikda.repo", entityManagerFactoryRef = "sikdaEntityManager",
    transactionManagerRef = "sikdaTransactionManager")
public class SikdaDsource {

  @Autowired
  private Environment env;

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean sikdaEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(sikdaDataSource());
    em.setPackagesToScan("gov.tangerangkota.id.sikda.entity");

    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", env.getProperty("mysql.dialect"));
    properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Primary
  @Bean
  public DataSource sikdaDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(env.getProperty("mysql.driverClassName"));
    dataSource.setJdbcUrl(env.getProperty("sikda.url"));
    dataSource.setUsername(env.getProperty("sikda.username"));
    dataSource.setPassword(env.getProperty("sikda.password"));
    dataSource.setMaximumPoolSize(20);
    dataSource.setMinimumIdle(1);
    dataSource.setIdleTimeout(60000);
    dataSource.setMaxLifetime(120000);
    return dataSource;
  }

  @Primary
  @Bean
  public PlatformTransactionManager sikdaTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(sikdaEntityManager().getObject());
    return transactionManager;
  }
}
