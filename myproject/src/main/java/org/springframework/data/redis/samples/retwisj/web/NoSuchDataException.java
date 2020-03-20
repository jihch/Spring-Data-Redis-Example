package org.springframework.data.redis.samples.retwisj.web;

public class NoSuchDataException extends RuntimeException {

	private final String data;
	private final boolean userRelated;

	public NoSuchDataException(String data, boolean userRelated) {

		super("Invalid data " + data);
		this.data = data;
		this.userRelated = userRelated;
	}

	/**
	 * Returns the name.
	 *
	 * @return Returns the name
	 */
	public String getData() {
		return data;
	}

	public boolean isPost() {
		return !userRelated;
	}
	
}
