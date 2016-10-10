package de.cronn.jira.sync.domain;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JiraRemoteLink extends JiraIdResource {

	private JiraRemoteLinkObject object;

	public JiraRemoteLink() {
	}

	public JiraRemoteLink(URL url) {
		this.object = new JiraRemoteLinkObject(url);
	}

	public JiraRemoteLink(String url) {
		this(toUrl(url));
	}

	private static URL toUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Illegal url: " + url, e);
		}
	}

	public JiraRemoteLinkObject getObject() {
		return object;
	}

	public void setObject(JiraRemoteLinkObject object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", getId())
			.append("object", object)
			.toString();
	}

}
