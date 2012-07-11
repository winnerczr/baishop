package junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试基类（有事务）
 * @author guozq
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"classpath:spring-dao.xml","classpath:spring-beans.xml"})
public class BaseTransactionalTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	
}
