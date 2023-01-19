package patrick.study.config;

import java.util.Collections;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;

/**
 * DB 설정
 *
 * @author patrick
 *
 */
@Configuration
@MapperScan(
		basePackages = {"patrick", "bnet", "mframework", "egovframework"},
		annotationClass = Mapper.class,
		sqlSessionTemplateRef = "sqlSessionBnet"
	)
@EnableTransactionManagement
public class DataSourceConfig {
	private final String MYBATIS_CONFIG = "classpath:mybatis/mybatis-config.xml";
	private final String MYBATIS_MAPPER = "classpath*:**/mybatis/**/postgresql/**/*.xml";

	@Bean(name = "hikariConfig")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean(name = {"dataSource", "egov.dataSource", "egovDataSource"})
	DataSource dataSource() {
		Log4jdbcProxyDataSource lpds = new Log4jdbcProxyDataSource(new HikariDataSource(hikariConfig()));

		Log4JdbcCustomFormatter lcf = new Log4JdbcCustomFormatter();
		lcf.setLoggingType(LoggingType.MULTI_LINE);
		lcf.setSqlPrefix("SQL:::\n\t\t");

		lpds.setLogFormatter(lcf);

		return lpds;
	}

	@Bean
	TransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	TransactionInterceptor txAdvice() {
		NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
		RuleBasedTransactionAttribute txAttribute = new RuleBasedTransactionAttribute();

		txAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		txAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();
		txMethods.put("*", txAttribute);
		txAttributeSource.setNameMap(txMethods);

		return new TransactionInterceptor(txManager(), txAttributeSource);
	}

	@Bean
	Advisor transactionAdviceAdvisor() {
		AspectJExpressionPointcut serviceMethod = new AspectJExpressionPointcut();
		serviceMethod.setExpression("execution(* *..service.*Service.*(..))");
		return new DefaultPointcutAdvisor(serviceMethod, txAdvice());
	}

	@Bean(name = "sqlSessionFactory")
	SqlSessionFactory sqlSessionFactory() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setConfigLocation(resolver.getResource(MYBATIS_CONFIG));
		Resource[] resources = resolver.getResources(MYBATIS_MAPPER);
		factoryBean.setMapperLocations(resources);
		return factoryBean.getObject();
	}

	@Bean(name = "sqlSessionBnet")
	SqlSession sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}

}
