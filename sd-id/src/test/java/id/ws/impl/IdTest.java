package id.ws.impl;

import org.junit.*;

public class IdTest {

	SDIdImpl id;

	@Before
	public void setUp() {
		id = new SDIdImpl();
	}

	@After
	public void tearDown() {
		id = null;
	}

	public User getUserFromUsername(String userId) {
		return id.findUser(userId);
	}

}