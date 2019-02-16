package util;

public class ResultTest {
	String TestcaseName;
	String TestcaseComment;

	public String getTestcaseComment() {
		return TestcaseComment;
	}

	public void setTestcaseComment(String testcaseComment) {
		TestcaseComment = testcaseComment;
	}

	public String getTestcaseName() {
		return TestcaseName;
	}

	public void setTestcaseName(String testcaseName) {
		TestcaseName = testcaseName;
	}

	@Override
	public String toString() {
		return ("Test case: " + getTestcaseName() + "Test case Result" + getTestcaseComment());
	}

}
