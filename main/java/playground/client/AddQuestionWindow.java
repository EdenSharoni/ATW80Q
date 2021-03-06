package playground.client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import playground.constants.Client;

public class AddQuestionWindow implements ActionListener {
	private ClientModel model;
	private JFrame frame;
	private JLabel title;
	private JLabel questionTitle;
	private JTextField questionTitleText;
	private JLabel questionBody;
	private JTextField questionBodyText;
	private JLabel answer;
	private JTextField answerText;
	private JLabel points;
	private JTextField pointsText;
	private JButton addQuestion;

	@Override
	public void actionPerformed(ActionEvent e) {

		model.addQuestion(questionTitleText.getText(), questionBodyText.getText(), answerText.getText(),
				pointsText.getText());
		frame.dispose();

	}

	public AddQuestionWindow(ClientModel model) {
		this.model = model;

		frame = new JFrame();
		frame.setTitle(Client.ADD_QUESTION);
		frame.setSize(500, 400);
		frame.setLayout(new GridLayout(3, 0));
		title = new JLabel(Client.ADD_QUESTION);
		title.setFont(Client.FONT_TITLE);
		frame.add(title);

		JPanel p1 = new JPanel(new GridLayout(4, 2));
		questionTitle = new JLabel(Client.QUESTION_TITLE);
		questionTitle.setFont(Client.FONT_BASIC);
		p1.add(questionTitle);
		questionTitleText = new JTextField(30);
		questionTitleText.setPreferredSize(new Dimension(30, 30));
		p1.add(questionTitleText);

		questionBody = new JLabel(Client.QUESTION_BODY);
		questionBody.setFont(Client.FONT_BASIC);
		p1.add(questionBody);
		questionBodyText = new JTextField(30);
		questionBodyText.setPreferredSize(new Dimension(30, 30));
		p1.add(questionBodyText);

		answer = new JLabel(Client.ANSWER);
		answer.setFont(Client.FONT_BASIC);
		p1.add(answer);
		answerText = new JTextField(30);
		answerText.setPreferredSize(new Dimension(30, 30));
		p1.add(answerText);

		points = new JLabel(Client.POINTS);
		points.setFont(Client.FONT_BASIC);
		p1.add(points);
		pointsText = new JTextField(30);
		pointsText.setPreferredSize(new Dimension(30, 30));
		p1.add(pointsText);

		frame.add(p1);

		JPanel p2 = new JPanel();
		addQuestion = new JButton(Client.ADD_QUESTION);
		addQuestion.setFont(Client.FONT_BASIC);
		p2.add(addQuestion);
		frame.add(p2);

		addQuestion.addActionListener(this);

		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
