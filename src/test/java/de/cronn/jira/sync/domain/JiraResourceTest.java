package de.cronn.jira.sync.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class JiraResourceTest {

	private static class TestJiraResource extends JiraResource {
		private static final long serialVersionUID = 1L;
	}

	@Test
	public void testToString() {
		JiraResource resource = new TestJiraResource();
		resource.setSelf("self");
		assertThat(resource).hasToString("JiraResourceTest.TestJiraResource[self=self]");
	}

}