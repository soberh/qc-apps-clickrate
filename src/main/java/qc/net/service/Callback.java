package qc.net.service;

/**
 * 
 * @author dragon
 * 
 */
public interface Callback<T> {
	void call(T result);
}
