package qc.test;

import java.io.File;
import java.io.IOException;

/**
 * 获取当前路径的测试类
 * 
 * @author dragon
 * 
 */
public class TestMain {
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("rawtypes")
		Class clazz = TestMain.class;

		// 当前类的URI目录,不包括自己
		// file:/F:/.../target/classes/qc/net/
		// jar:file:/F:/.../target/qc-apps-clickrate-.0.jar!/qc/net/
		System.out.println("1) " + clazz.getResource(""));

		// 当前classpath的绝对URI路径
		// file:/F:/.../target/classes/
		// null
		System.out.println("2) " + clazz.getResource("/"));
		System.out.println("2) "
				+ Thread.currentThread().getContextClassLoader()
						.getResource(""));
		System.out.println("2) " + clazz.getClassLoader().getResource(""));

		// 启动目录
		// F:\Work\dragon\qc\qc-apps\qc-apps-clickrate
		// F:\Work\dragon\qc\qc-apps\qc-apps-clickrate\target
		System.out.println("3) " + System.getProperty("user.dir"));

		// 使用 File 提供的函数:
		// 当前文件夹：F:\Work\dragon\qc\qc-apps\qc-apps-clickrate
		System.out.println("4) " + new File("").getCanonicalPath());
		// 上级文件夹：F:\Work\dragon\qc\qc-apps
		System.out.println("5) " + new File("..").getCanonicalPath());
	}
}
