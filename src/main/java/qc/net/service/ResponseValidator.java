/**
 * 
 */
package qc.net.service;

/**
 * 验证连接返回的信息是否符合指定的要求
 * 
 * @author dragon
 * 
 */
public interface ResponseValidator {
	boolean validate(Object response);
}
