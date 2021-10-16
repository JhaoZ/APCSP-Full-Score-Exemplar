import java.awt.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html
import java.io.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/io/package-summary.html
import java.util.*;
//Source from: https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
import javax.swing.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html
import java.awt.event.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/java/awt/event/package-summary.html
import javax.swing.event.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/javax/swing/event/package-summary.html
import javax.swing.filechooser.*;
//Source from: https://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html

public class Interface {

	static JFrame frame;
	static JPanel mainPanel;
	static File file;
	static String directory;
	static ArrayList<Question> questions;
	static boolean hasSaved;
	static int currentQIndex;
	static int highScore;

	static JTextField nameOfQ;

	// Label on top
	static JLabel QLabelPos;

	// Individual Answers
	static JCheckBox check1;
	static JCheckBox check2;
	static JCheckBox check3;
	static JCheckBox check4;

	static JTextField field1;
	static JTextField field2;
	static JTextField field3;
	static JTextField field4;

	static JLabel previousScore;

	public static void main(String[] args) {
		init();
	}

	public static void init() {
		highScore = 0;
		questions = new ArrayList<Question>();
		questions.add(new Question("", new String[4], new boolean[4]));
		directory = "";
		currentQIndex = 0;
		file = null;
		QLabelPos = new JLabel("Load a new test file or start a new one to create a new quiz. Press play to play you quiz. Check the buttons on each questions to make that the answers.", SwingConstants.CENTER);
		hasSaved = false;
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		frame.setContentPane(mainPanel);
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		frame.setLocationRelativeTo(null);
		menu.add(fileMenu);
		JMenuItem newFile = new JMenuItem("New");
		newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				questions = new ArrayList<Question>();
				questions.add(new Question("", new String[4], new boolean[4]));				
				file = null;
				directory = "";
				highScore = 0;
				currentQIndex = 0;
				updateMaker();
			}
		});
		JMenuItem loadFile = new JMenuItem("Load");
		JFileChooser chooser = new JFileChooser();
		loadFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Files with .test extension", "test");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(chooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("File Choose: " + chooser.getSelectedFile().getName());
					directory = chooser.getSelectedFile().getAbsolutePath();
					System.out.println("Directory: " + directory);
					file = chooser.getSelectedFile().getAbsoluteFile();
					try {
						questions = InputReader.parseTheText(file);
						System.out.println(questions);
					} catch (IOException e) {
						System.out.println("File Invalid.");
						e.printStackTrace();
					}
					highScore = InputReader.getHighScoreFromFile();
					updateMaker();
				}
			}
		});
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (file != null) {
					try {
						InputReader.exportTheFile(file, questions, highScore);
					} catch (IOException e) {
						System.out.println("Save Failed.");
						e.printStackTrace();
					}
				} else {
					int saveFileVal = chooser.showSaveDialog(mainPanel);
					if (saveFileVal == JFileChooser.APPROVE_OPTION) {
						System.out.println("Save file name: " + chooser.getSelectedFile().getName());
						File saved = new File(chooser.getSelectedFile().getAbsolutePath() + ".test");
						try {
							saved.getParentFile().mkdirs();
							saved.createNewFile();
						} catch (IOException e) {
							System.out.println("Saving File Errors.");
							e.printStackTrace();
						}
						System.out.println(saved.toString());
						file = saved;
						try {
							InputReader.exportTheFile(file, questions, highScore);
						} catch (IOException e) {
							System.out.println("File Save As error.");
							e.printStackTrace();
						}
					}
				}

			}
		});
		JMenuItem saveAs = new JMenuItem("Save as");
		saveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int saveFileVal = chooser.showSaveDialog(mainPanel);
				if (saveFileVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Save file name: " + chooser.getSelectedFile().getName());
					File saved = new File(chooser.getSelectedFile().getAbsolutePath() + ".test");
					try {
						saved.getParentFile().mkdirs();
						saved.createNewFile();
					} catch (IOException e) {
						System.out.println("Saving File Errors.");
						e.printStackTrace();
					}
					System.out.println(saved.toString());
					file = saved;
					try {
						InputReader.exportTheFile(file, questions, highScore);
					} catch (IOException e) {
						System.out.println("File Save As error.");
						e.printStackTrace();
					}
				}
			}
		});
		// Add all components to the file menu
		fileMenu.add(newFile);
		fileMenu.add(loadFile);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		frame.setJMenuBar(menu);
		BorderLayout bl = new BorderLayout(0, 0);
		// Set up the main panel

		mainPanel.setLayout(bl);
		JButton goLeft = new JButton("Left");
		JButton goRight = new JButton("Right");
		goLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentQIndex == 0) {
					currentQIndex = questions.size() - 1;
				} else {
					currentQIndex--;
				}
				updateMaker();
			}
		});
		goRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentQIndex == questions.size() - 1) {
					currentQIndex = 0;
				} else {
					currentQIndex++;
				}
				updateMaker();
			}
		});
		mainPanel.add(goLeft, BorderLayout.WEST);
		mainPanel.add(goRight, BorderLayout.EAST);

		mainPanel.add(QLabelPos, BorderLayout.NORTH);
		JPanel QMakingPanel = new JPanel();
		mainPanel.add(QMakingPanel, BorderLayout.CENTER);
		QMakingPanel.setLayout(new BorderLayout(0, 0));
		JPanel questionName = new JPanel();
		QMakingPanel.add(questionName, BorderLayout.NORTH);
		JLabel qName = new JLabel("Question: ");
		nameOfQ = new JTextField("Put your Question here.");
		nameOfQ.setColumns(30);
		nameOfQ.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateQName();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateQName();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateQName();
			}

		});
		questionName.add(qName);
		questionName.add(nameOfQ);

		// The four question panels
		JPanel Q1 = new JPanel();
		JPanel Q2 = new JPanel();
		JPanel Q3 = new JPanel();
		JPanel Q4 = new JPanel();

		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
		// Init all panels

		int fieldLength = 40;

		answerPanel.add(Q1);
		check1 = new JCheckBox("Correct Answer");
		check1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateCheckBoxes(1, check1.isSelected());
			}
		});
		field1 = new JTextField();
		field1.setColumns(fieldLength);
		field1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateAnswer(1);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAnswer(1);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAnswer(1);
			}

		});
		Q1.add(check1);
		Q1.add(field1);

		answerPanel.add(Q2);
		check2 = new JCheckBox("Correct Answer");
		check2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateCheckBoxes(2, check2.isSelected());
			}
		});
		field2 = new JTextField();
		field2.setColumns(fieldLength);
		field2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateAnswer(2);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAnswer(2);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAnswer(2);
			}

		});
		Q2.add(check2);
		Q2.add(field2);
		answerPanel.add(Q3);
		check3 = new JCheckBox("Correct Answer");
		check3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateCheckBoxes(3, check3.isSelected());
			}
		});
		field3 = new JTextField();
		field3.setColumns(fieldLength);
		field3.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateAnswer(3);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAnswer(3);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAnswer(3);
			}
		});
		Q3.add(check3);
		Q3.add(field3);

		answerPanel.add(Q4);
		check4 = new JCheckBox("Correct Answer");
		check4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateCheckBoxes(4, check4.isSelected());
			}
		});
		field4 = new JTextField();
		field4.setColumns(fieldLength);
		field4.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateAnswer(4);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateAnswer(4);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateAnswer(4);
			}
		});
		Q4.add(check4);
		Q4.add(field4);
		QMakingPanel.add(answerPanel, BorderLayout.CENTER);
		JPanel managePanel = new JPanel();
		QMakingPanel.add(managePanel, BorderLayout.SOUTH);
		// Play the Quiz
		JButton play = new JButton("Play the quiz!");
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean canPlay = true;
				for (Question q : questions) {
					if (!q.correctAnswerIndex[0] && !q.correctAnswerIndex[1] && !q.correctAnswerIndex[2]
							&& !q.correctAnswerIndex[3]) {
						canPlay = false;
					}
				}
				if (canPlay) {
					
					PlayGame.playGame(questions, highScore);
				} else {
					JOptionPane.showMessageDialog(frame, "You have to make sure all questions all answers before playing,");
				}
			}
		});

		JPanel qMenu = new JPanel();
		JButton addQ = new JButton("Add Question");
		addQ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				questions.add(currentQIndex + 1, new Question("", new String[4], new boolean[4]));
				currentQIndex++;
				updateMaker();

			}
		});
		JButton deleteQ = new JButton("Delete Question");
		deleteQ.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (questions.size() == 1) {
					JOptionPane.showMessageDialog(mainPanel, "You only have one question, you cannot delete.");
				} else {
					if (currentQIndex == 0) {
						questions.remove(currentQIndex);
						updateMaker();
					} else {
						questions.remove(currentQIndex);
						currentQIndex--;
						updateMaker();
					}
				}
			}
		});
		qMenu.add(addQ);
		qMenu.add(deleteQ);

		answerPanel.add(qMenu);
		previousScore = new JLabel("Previous HighScore:" + highScore + "%", SwingConstants.CENTER);
		qMenu.add(previousScore);
		mainPanel.add(play, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.pack();
	}
	
	public static void errorFile() {
		JOptionPane.showMessageDialog(mainPanel, "File Invalid");
	}

	public static void updateMaker() {
		Question q = questions.get(currentQIndex);
		nameOfQ.setText(q.question);
		field1.setText(q.answers[0]);
		field2.setText(q.answers[1]);
		field3.setText(q.answers[2]);
		field4.setText(q.answers[3]);
		if (q.correctAnswerIndex[0]) {
			check1.setSelected(true);
		} else {
			check1.setSelected(false);
		}
		if (q.correctAnswerIndex[1]) {
			check2.setSelected(true);
		} else {
			check2.setSelected(false);
		}
		if (q.correctAnswerIndex[2]) {
			check3.setSelected(true);
		} else {
			check3.setSelected(false);
		}
		if (q.correctAnswerIndex[3]) {
			check4.setSelected(true);
		} else {
			check4.setSelected(false);
		}
		QLabelPos.setText("Question " + (currentQIndex + 1) + " out of " + questions.size() + ".");
		previousScore.setText("Previous HighScore:" + highScore + "%");
	}

	public static void updateAnswer(int fieldNum) {
		if (fieldNum == 1) {
			questions.get(currentQIndex).answers[fieldNum - 1] = field1.getText();
		} else if (fieldNum == 2) {
			questions.get(currentQIndex).answers[fieldNum - 1] = field2.getText();
		} else if (fieldNum == 3) {
			questions.get(currentQIndex).answers[fieldNum - 1] = field3.getText();
		} else if (fieldNum == 4) {
			questions.get(currentQIndex).answers[fieldNum - 1] = field4.getText();
		}
	}

	public static void updateQName() {
		questions.get(currentQIndex).question = nameOfQ.getText();
	}

	public static void updateCheckBoxes(int boxNum, boolean on) {
		if (boxNum == 1) {
			if (on) {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = true;
			} else {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = false;
			}
		}

		if (boxNum == 2) {
			if (on) {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = true;
			} else {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = false;
			}
		}

		if (boxNum == 3) {
			if (on) {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = true;
			} else {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = false;
			}
		}

		if (boxNum == 4) {
			if (on) {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = true;
			} else {
				questions.get(currentQIndex).correctAnswerIndex[boxNum - 1] = false;
			}
		}
	}

}
