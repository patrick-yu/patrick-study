package patrick.study.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mframework.batch.BatchAspect;
import mframework.batch.BatchReturn;
import mframework.config.EnvConfig;
import mframework.log.LogUtils;

/**
 * Batch 설정
 *
 * @author patrick
 *
 */
@Aspect
@Component
public class BatchConfig {

	@Autowired
	private EnvConfig config;

	@Autowired
	private LogUtils logUtils;

	@Pointcut("execution(mframework.batch.BatchReturn *..*.*(..)) && @annotation(mframework.batch.BatchJob)")
	public void batchAspectPointcut() {}

	@Around(value="batchAspectPointcut()")
	public BatchReturn logAround(ProceedingJoinPoint pjp) {
		return BatchAspect.logAround(pjp, config, logUtils);
	}


}
