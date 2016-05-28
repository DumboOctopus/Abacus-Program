package abacusprogram.windows;

import abacusprogram.quiz.QuizListener;
import abacusprogram.quiz.question.Operation;
import abacusprogram.quiz.question.Range;
import abacusprogram.quiz.question.RequiredQuestion;
import abacusprogram.quiz.QuizManager;
import abacusprogram.quiz.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


/**
 * Created on 2/20/16.
 */
public class AbacusPracticeWindow extends JFrame implements QuizListener {

    private JLabel mainLabel;
    private JButton mainButton;
    private JLabel questionNumberLabel;

    private JMenuBar menuBar;
    private Settings settings;

    private boolean quizStarted = false;

    public AbacusPracticeWindow() throws Exception {
        super("Abacus PRACTICE ");
        settings = new Settings(10, new Range(1, 20), 7, 5, 1, 100, Operation.ADDITION, new RequiredQuestion[0]);
        Image i = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/mainBackground.jpg"));
        setContentPane(new ContainerWithBackground(i));

        setLayout(null);
        setUpMainMenu2();
        setUpMainButton();
        setUpLabels();



        setVisible(true);
        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setUpMainMenu()
    {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu submenu = new JMenu("Quiz Settings");
        JMenuItem item;

        item = new JMenuItem("Number Of Problems ");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setNumberOfQuestions(promptInteger(
                        "Set Number Of Problems",
                        "How many problems (Question 1, question 2) should the text have?",
                        settings.getNumberOfQuestions()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Range Of Problems ");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setLowerRange(promptInteger(
                        "Set Lower Range",
                        "What is the Lowest Number Possible?",
                        settings.getLowerRange()
                ));
                settings.setUpperRange(promptInteger(
                        "Set Upper Range",
                        "What is the Highest Number Possible?",
                        settings.getUpperRange()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Set Number Of Numbers Per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setNumberOfNumbersPerQuestions(promptInteger(
                        "Set Number Of Numbers per Question",
                        "In each question, a series of numbers pop up. How many of these numbers should pop up?",
                        settings.getNumberOfNumbersPerQuestions()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setTimePerQuestion(promptDouble(
                        "Set Time per Question",
                        "In each question, a series of numbers pop up. How long should it stay there?",
                        settings.getTimePerQuestion()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time to Answer");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setTimeToAnswer(promptInteger(
                        "Set Time to Answer",
                        "At the end of each set of numbers, how long should the child have to fill in their answer",
                        settings.getTimeToAnswer()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Operation");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = Operation.values();
                Operation operation = (Operation)JOptionPane.showInputDialog(
                        AbacusPracticeWindow.this,
                        "What operation is the quiz using",
                        "Choose Operation",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[settings.getOperation().ordinal()]);
                settings.setOperation(operation);
            }
        });
        submenu.add(item);

        menuBar.add(submenu);

        submenu = new JMenu("Display Settings");

        item = new JMenuItem("Font Size");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setFontSize(promptInteger(
                        "Set Font Size",
                        "How big should the numbers be?",
                        settings.getFontSize()
                ));
                mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));
                mainLabel.setSize(950, settings.getFontSize() + 20);

            }
        });
        submenu.add(item);


        menuBar.add(submenu);

        item = new JMenuItem("Report a Bug");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(AbacusPracticeWindow.this,"Please email: neilp0101@gmail.com");
            }
        });
        menuBar.add(item);
    }

    public void setUpMainMenu2()
    {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);


        JMenu submenu = new JMenu("Chapter");
        JMenuItem item;

        try {
            ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();
            BufferedReader reader;
            reader = new BufferedReader(
                    new InputStreamReader(
                            resourceLoader.getResourceAsStream("resources/chapterData.txt"),
                            "UTF-8"
                    )
            );

            ButtonGroup buttonGroup = new ButtonGroup();
            String line;

            JMenu currSubMenu = submenu;
            while((line = reader.readLine()) != null)
            {
                if(line.contains("//"))
                {
                    continue;
                }
                if(line.contains("{"))
                {
                    currSubMenu = new JMenu(line.replace("{",""));
                    submenu.add(currSubMenu);
                    continue;
                }else if(line.contains("}"))
                {
                    currSubMenu = submenu;
                    continue;
                }
                item = generateFromLine(line);
                buttonGroup.add(item);
                currSubMenu.add(item);
            }
            reader.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        menuBar.add(submenu);


        submenu = new JMenu("Display Settings");

        item = new JMenuItem("Font Size");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setFontSize(promptInteger(
                        "Set Font Size",
                        "How big should the numbers be?",
                        settings.getFontSize()
                ));
                mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));
                mainLabel.setBounds(0, 250 - settings.getFontSize()/2, 1000, settings.getFontSize());
            }
        });
        submenu.add(item);


        menuBar.add(submenu);

        item = new JMenuItem("Report a Bug");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                String message = "mailto:neilp0101@gmail.com?subject=Bug%20Dectected%20For%20AbacusProgram&body=Please%20Place%20Description%20Here";
                URI uri = URI.create(message);
                try {
                    desktop.mail(uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        menuBar.add(item);


        //custom settings
        submenu = new JMenu("Manual Set");

        item = new JMenuItem("Number Of Problems ");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setNumberOfQuestions(promptInteger(
                        "Set Number Of Problems",
                        "How many problems (Question 1, question 2) should the text have?",
                        settings.getNumberOfQuestions()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Range Of Problems ");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setLowerRange(promptInteger(
                        "Set Lower Range",
                        "What is the Lowest Number Possible?",
                        settings.getLowerRange()
                ));
                settings.setUpperRange(promptInteger(
                        "Set Upper Range",
                        "What is the Highest Number Possible?",
                        settings.getUpperRange()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Set Number Of Numbers Per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setNumberOfNumbersPerQuestions(promptInteger(
                        "Set Number Of Numbers per Question",
                        "In each question, a series of numbers pop up. How many of these numbers should pop up?",
                        settings.getNumberOfNumbersPerQuestions()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setTimePerQuestion(promptDouble(
                        "Set Time per Question",
                        "In each question, a series of numbers pop up. How long should it stay there?",
                        settings.getTimePerQuestion()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time to Answer");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setTimeToAnswer(promptInteger(
                        "Set Time to Answer",
                        "At the end of each set of numbers, how long should the child have to fill in their answer",
                        settings.getTimeToAnswer()
                ));
            }
        });
        submenu.add(item);

        item = new JMenuItem("Operation");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = Operation.values();
                Operation operation = (Operation)JOptionPane.showInputDialog(
                        AbacusPracticeWindow.this,
                        "What operation is the quiz using",
                        "Choose Operation",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[settings.getOperation().ordinal()]);
                settings.setOperation(operation);
            }
        });
        submenu.add(item);

        menuBar.add(submenu);

        submenu = new JMenu("Display Settings");

        item = new JMenuItem("Font Size");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setFontSize(promptInteger(
                        "Set Font Size",
                        "How big should the numbers be?",
                        settings.getFontSize()
                ));
                mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));
                mainLabel.setSize(950, settings.getFontSize() + 20);

            }
        });
        submenu.add(item);

    }
    public SettingsModifierButton generateFromLine(String line)
    {
        String[] tokens = line.split(", ");
        String chapterName = tokens[0];
        int numQues = Integer.parseInt(tokens[1]);
        int lowerRange = Integer.parseInt(tokens[2]);
        int upperRange = Integer.parseInt(tokens[3]);
        int numOfNumbersPerQuestion = Integer.parseInt(tokens[4]);
        int timeToAnswer = Integer.parseInt(tokens[5]);
        double timePerQues = Double.parseDouble(tokens[6]);
        Operation operation = Operation.parseOperation(tokens[7]);
        RequiredQuestion[] requiredNumbers = new RequiredQuestion[tokens.length - 8];

        for (int i = 8; i < tokens.length; i++) {
            requiredNumbers[i-8] =new RequiredQuestion( Integer.parseInt(tokens[i]));
            System.out.println(requiredNumbers[i-8]);
        }

        return new SettingsModifierButton(
                chapterName,
                settings,
                new Settings(
                        numQues,
                        new Range(lowerRange,upperRange),
                        numOfNumbersPerQuestion,
                        timeToAnswer,
                        timePerQues,
                        settings.getFontSize(),
                        operation,
                        requiredNumbers
                )
        );
    }

    //----------
    public void setUpMainButton()
    {
        mainButton = new JButton("Start Quiz");
        mainButton.setBounds(0, 0, 100, 50);
        mainButton.addActionListener(new ActionListener() {
            private QuizManager qm;

            @Override
            public void actionPerformed(ActionEvent e) {

                if(!quizStarted) {
                    qm = new QuizManager(AbacusPracticeWindow.this);
                    quizStarted = true;

                    mainButton.setText("Abort Quiz");
                } else
                {
                    qm.cancelQuiz();
                    quizStarted = false;
                    mainButton.setText("Start Quiz");
                    mainLabel.setText("[number]");
                }
            }
        });
        add(mainButton);
    }
    public void setUpLabels()
    {
        mainLabel = new JLabel("[number]", SwingConstants.CENTER);
        mainLabel.setBounds(0, 200, 1000, 100);
        mainLabel.setOpaque(true);
        mainLabel.setBackground(new Color(242, 242, 242));
        mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));
        add(mainLabel);

        questionNumberLabel = new JLabel("", SwingConstants.CENTER);
        questionNumberLabel.setBounds(500, 20, 100, 40);
        add(questionNumberLabel);
    }

    //==============PROMPTING==================//
    public int promptInteger(String title, String message, int previousValue)
    {
        int tmp = 0;
        String s = (String) JOptionPane.showInputDialog(
                this,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                previousValue);
        if(s == null) return previousValue;
        try {
            tmp = (Integer.parseInt(s));

        } catch(NumberFormatException e)
        {
            return promptInteger(title, message, previousValue); //maybe user might troll...
        }

        return tmp;
    }

    public double promptDouble(String title, String message, double previousValue)
    {
        double tmp = 0;
        String s = (String) JOptionPane.showInputDialog(
                this,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                previousValue);
        if(s == null) return previousValue;
        try {
            tmp = (Double.parseDouble(s));

        } catch(NumberFormatException e)
        {
            return promptDouble(title, message, previousValue); //maybe user might troll...
        }

        return tmp;
    }

    //================================Listeners

    @Override
    public void onQuizFinish() {
        quizStarted = false;
        mainButton.setText("Start Quiz");
    }


    //===================================GETTERS=======================//


    public JLabel getQuestionNumberLabel() {
        return questionNumberLabel;
    }


    public JLabel getMainLabel() {
        return mainLabel;
    }

    public Settings getSettings() {
        return settings;
    }


    //=================================RUNNING============================//
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new AbacusPracticeWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
