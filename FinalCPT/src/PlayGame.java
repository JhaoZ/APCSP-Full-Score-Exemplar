import java.awt.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html
import java.awt.event.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/awt/event/package-summary.html
import java.io.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/io/package-summary.html
import java.util.*;
//Source from: https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
import javax.swing.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html


public class PlayGame {

	static boolean nextQ;

	static int curScore;

	static int currentQIndex;

	static JFrame frame;
	static JPanel panel;

	// Dynamic Components

	static JLabel qName;
	static JLabel totalQ;

	// Question and Answer

	static JCheckBox check1;
	static JCheckBox check2;
	static JCheckBox check3;
	static JCheckBox check4;

	static JLabel answer1;
	static JLabel answer2;
	static JLabel answer3;
	static JLabel answer4;

	public static void playGame(ArrayList<Question> q, int highScore) {
		currentQIndex = 0;
		frame = new JFrame();
		panel = new JPanel();
		curScore = 0;
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setContentPane(panel);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel topPanel = new JPanel();
		totalQ = new JLabel("Question #");
		qName = new JLabel("Question");
		topPanel.add(totalQ);
		topPanel.add(qName);

		panel.add(topPanel, BorderLayout.NORTH);

		JPanel questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

		check1 = new JCheckBox("1. ");
		check2 = new JCheckBox("2. ");
		check3 = new JCheckBox("3. ");
		check4 = new JCheckBox("4. ");

		answer1 = new JLabel("");
		answer2 = new JLabel("");
		answer3 = new JLabel("");
		answer4 = new JLabel("");

		JPanel Q1 = new JPanel();
		JPanel Q2 = new JPanel();
		JPanel Q3 = new JPanel();
		JPanel Q4 = new JPanel();

		Q1.add(check1);
		Q1.add(answer1);
		Q2.add(check2);
		Q2.add(answer2);
		Q3.add(check3);
		Q3.add(answer3);
		Q4.add(check4);
		Q4.add(answer4);

		questionPanel.add(Q1);
		questionPanel.add(Q2);
		questionPanel.add(Q3);
		questionPanel.add(Q4);

		updateQPanel(q);

		panel.add(questionPanel, BorderLayout.CENTER);

		ArrayList<boolean[]> listOfAns = new ArrayList<boolean[]>();

		nextQ = false;
		JButton next = new JButton("Next Question");
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean[] ans = new boolean[4];
				if (check1.isSelected()) {
					ans[0] = true;
				}
				if (check2.isSelected()) {
					ans[1] = true;
				}
				if (check3.isSelected()) {
					ans[2] = true;
				}
				if (check4.isSelected()) {
					ans[3] = true;
				}
				listOfAns.add(ans);
				currentQIndex++;
				if (currentQIndex >= q.size()) {
					curScore = getScore(listOfAns, q);
					String message = "";
					message = "Congratulations, you got " + curScore + "% of questions correct.";
					if (curScore > highScore) {
						message += "\n You got a new high score!";
						System.out.println("Current score: " + curScore);
						System.out.println(highScore);
						Interface.highScore = getScore(listOfAns, q);
					} else if (curScore == 100) {
						message += "\n You got a full score! Congratulations";
					} else {
						message += "\n Try harder and you can exceed your high score!";
					}
					JOptionPane.showMessageDialog(panel, message);
					Interface.updateMaker();
					try {
						if (Interface.file != null) {
							InputReader.exportTheFile(Interface.file, q, Interface.highScore);
						}
					} catch (IOException e1) {
						System.out.println("Fail to export");
						e1.printStackTrace();
					}
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				} else {
					updateQPanel(q);
				}
			}
		});
		panel.add(next, BorderLayout.SOUTH);
		frame.setSize(800, 400);
		frame.setVisible(true);
	}

	public static int getScore(ArrayList<boolean[]> answers, ArrayList<Question> questions) {
		int score = 0;
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).correct(answers.get(i))) {
				score++;
			}
		}
		double s = (double) score / (double) questions.size();
		s = s * 100;
		return (int) (s);
	}

	public static void updateQPanel(ArrayList<Question> question) {
		totalQ.setText("Question # " + (currentQIndex + 1) + " out of " + question.size() + ".");
		qName.setText(question.get(currentQIndex).question);

		answer1.setText(question.get(currentQIndex).answers[0]);
		answer2.setText(question.get(currentQIndex).answers[1]);
		answer3.setText(question.get(currentQIndex).answers[2]);
		answer4.setText(question.get(currentQIndex).answers[3]);

		check1.setSelected(false);
		check2.setSelected(false);
		check3.setSelected(false);
		check4.setSelected(false);
	}

}
