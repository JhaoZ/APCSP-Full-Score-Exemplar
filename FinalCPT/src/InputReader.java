import java.io.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/io/package-summary.html
import java.util.*;
//Source from: https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html

public class InputReader {

	static int highScore;

	public static int getHighScoreFromFile() {
		return highScore;
	}

	public static ArrayList<Question> parseTheText(File f) throws IOException {
		highScore = 0;
		ArrayList<Question> question = new ArrayList<Question>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String Testline = br.readLine();
		if (Testline == null) {
			Interface.errorFile();
			return null;
		} else {
			int numOfQ = Integer.parseInt(Testline);
			for (int i = 0; i < numOfQ; i++) {
				String qName = br.readLine();
				boolean[] ansIndex = new boolean[4];
				String[] ans = new String[4];
				Arrays.fill(ansIndex, false);
				for (int j = 0; j < 4; j++) {
					String line = br.readLine();
					StringTokenizer st = new StringTokenizer(line);
					if (st.nextToken().equals("*")) {
						ansIndex[j] = true;
					}
					if (st.hasMoreTokens()) {
						ans[j] = st.nextToken();
					} else {
						ans[j] = "";
					}
				}
				// add all questions in ArrayList q
				Question Q = new Question(qName, ans, ansIndex);
				question.add(Q);
			}
			highScore = Integer.parseInt(br.readLine());
			return question;
		}
	}

	public static void exportTheFile(File f, ArrayList<Question> q, int highS) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		pw.println(q.size());
		for (Question Q : q) {
			pw.println(Q.question);
			for (int i = 0; i < Q.answers.length; i++) {
				if (Q.correctAnswerIndex[i]) {
					pw.print("* ");
				} else {
					pw.print(". ");
				}
				if (Q.answers[i] == null) {
					pw.println("");
				} else {
					pw.println(Q.answers[i]);
				}
			}
		}
		pw.println(highS);
		System.out.println("Successful Save");
		pw.close();
		return;
	}

}
