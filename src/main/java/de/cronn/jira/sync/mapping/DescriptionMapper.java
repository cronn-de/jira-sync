package de.cronn.jira.sync.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.cronn.jira.sync.domain.JiraIssue;
import de.cronn.jira.sync.domain.JiraIssueFields;

public final class DescriptionMapper {

	private static final Pattern PANEL_PATTERN = Pattern.compile("\\{panel[^\\\\}]*\\}.*?\\{panel\\}\\s*", Pattern.DOTALL);
	private static final Pattern PANEL_START_PATTERN = Pattern.compile("\\{(panel[^\\\\}]*)\\}");

	private DescriptionMapper() {
	}

	public static String mapSourceDescription(JiraIssue sourceIssue) {
		String sourceDescription = getDescription(sourceIssue);
		return mapSourceDescription(sourceDescription);
	}

	public static String getDescription(JiraIssue sourceIssue) {
		JiraIssueFields fields = sourceIssue.getFields();
		Assert.notNull(fields, "fields must not be null");
		return normalizeDescription(fields.getDescription());
	}

	private static String normalizeDescription(String description) {
		if (description == null) {
			return null;
		}
		return description.replaceAll("\\r\\n", "\n").trim();
	}

	public static String mapSourceDescription(String sourceDescription) {
		if (StringUtils.isEmpty(sourceDescription)) {
			return "";
		} else {
			String normalizedSourceDescription = normalizeDescription(sourceDescription);
			String escapedSourceDescription = PANEL_START_PATTERN.matcher(normalizedSourceDescription).replaceAll("\\\\{$1\\\\}");
			return "{panel:title=Original description|titleBGColor=#DDD|bgColor=#EEE}\n" + escapedSourceDescription + "\n{panel}\n\n";
		}
	}

	public static String mapTargetDescription(JiraIssue sourceIssue, JiraIssue targetIssue) {
		String sourceDescription = getDescription(sourceIssue);
		String targetDescription = getDescription(targetIssue);
		return mapTargetDescription(sourceDescription, targetDescription);
	}

	public static String mapTargetDescription(String sourceDescription, String targetDescription) {
		if (StringUtils.isEmpty(sourceDescription) && StringUtils.isEmpty(targetDescription)) {
			return null;
		}

		if (targetDescription == null) {
			targetDescription = "";
		}

		if (sourceDescription == null) {
			sourceDescription = "";
		}

		targetDescription = targetDescription.replaceFirst(Pattern.quote(sourceDescription), "");
		Matcher panelMatcher = PANEL_PATTERN.matcher(targetDescription);
		String mappedSourceDescription = mapSourceDescription(sourceDescription);
		if (panelMatcher.find()) {
			targetDescription = panelMatcher.replaceFirst(Matcher.quoteReplacement(mappedSourceDescription));
		} else {
			targetDescription = mappedSourceDescription + targetDescription;
		}

		return targetDescription.trim();
	}
}
