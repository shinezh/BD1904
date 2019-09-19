/**
 * @fileName: BeanFactory
 * @author: orange
 * @date: 2019/7/3 16:13
 * @description: bean工厂
 * @version: 1.0
 */

package cn.shine.framework;


import cn.shine.service.StudentService;
import cn.shine.service.impl.StudentServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BeanFactory {

	/**
	 * 通过工厂获取服务层对象
	 *
	 * @return 服务对象
	 */
	public static StudentService getStuService() {
		final StudentService stuService = new StudentServiceImpl();
		//创建业务层的代理对象
		StudentService proxyStu = (StudentService) Proxy.newProxyInstance(stuService.getClass().getClassLoader(), stuService.getClass().getInterfaces(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				try {
					//开启事务
					TranscationManager.start();
					//执行操作
					Object reValue = method.invoke(stuService, args);
					//提交事务
					TranscationManager.commitTrans();
					//返回结果
					return reValue;
				} catch (Exception e) {
					//回滚事务
					TranscationManager.rollBack();
					//处理异常
					throw new RuntimeException("工厂异常" + e.getMessage());
				} finally {
					//释放资源
					TranscationManager.releaseAll();
				}
			}
		});
		return proxyStu;
	}

}