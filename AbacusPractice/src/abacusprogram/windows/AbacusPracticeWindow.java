package abacusprogram.windows;

import abacusprogram.quiz.QuizListener;
import abacusprogram.quiz.question.Operation;
import abacusprogram.quiz.question.Range;
import abacusprogram.quiz.question.Required1ColumnSum;
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
 * TODO: write a manual for how to use this program
 * todo; write a key for the level's notation
 * e.g. has 1
 * +/- 4
 * has 6 + 5
 * todo: rewrite using layout manager
 * TOdo; spring layout in score dialog:DONE
 * Todo; ask kelly if set 5+6 required sum should be randomized; DETERMINED: no
 * e.g.
 * 5+6 could show up like 15 + 6 or like 16 + 5, order of ones digit does not matter
 * TODO; ask kelly weather -sums are used e.g: DETERMINED: no -sum
 * <p>
 * -4 + 1 -2 would have a negative number in ones calculator the whole time. SHould it always be positive.
 * <p>
 * TODO: add password to manuel set;DONE
 */
public class AbacusPracticeWindow extends JFrame implements QuizListener {

    private JLabel mainLabel;
    private JButton mainButton;
    private JLabel questionNumberLabel;

    private JMenuBar menuBar;
    private Settings settings;

    private boolean quizStarted = false;
    private boolean hasSessionPass = false;

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


    public void setUpMainMenu2() {
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
            while ((line = reader.readLine()) != null) {
                if (line.contains("//")) {
                    continue;
                }
                if (line.contains("{")) {
                    currSubMenu = new JMenu(line.replace("{", ""));
                    submenu.add(currSubMenu);
                    continue;
                } else if (line.contains("}")) {
                    currSubMenu = submenu;
                    continue;
                }
                item = generateFromLine(line);
                buttonGroup.add(item);
                currSubMenu.add(item);
            }
            reader.close();
        } catch (Exception e) {
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
                mainLabel.setBounds(0, 250 - settings.getFontSize() / 2, 1000, settings.getFontSize());
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
                promptNumberOfQuestions();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Range Of Problems ");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptLowerRange();
                promptUpperRange();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Set Number Of Numbers Per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptNumberOfNumbersPerQuestions();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time per Question");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptTimePerQuestion();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Time to Answer");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptTimeToAnswer();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Operation");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptOperation();
            }
        });
        submenu.add(item);

        item = new JMenuItem("Set All");
        item.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    showPassword();
                    settings.setNumberOfQuestions(promptInteger(
                            "Set Number Of Problems",
                            "How many problems (Question 1, question 2) should the text have?",
                            settings.getNumberOfQuestions()
                    ));
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
                    settings.setNumberOfNumbersPerQuestions(promptInteger(
                            "Set Number Of Numbers per Question",
                            "In each question, a series of numbers pop up. How many of these numbers should pop up?",
                            settings.getNumberOfNumbersPerQuestions()
                    ));
                    settings.setTimePerQuestion(promptDouble(
                            "Set Time per Question",
                            "In each question, a series of numbers pop up. How long should it stay there?",
                            settings.getTimePerQuestion()
                    ));
                    settings.setTimeToAnswer(promptInteger(
                            "Set Time to Answer",
                            "At the end of each set of numbers, how long should the child have to fill in their answer",
                            settings.getTimeToAnswer()

                    ));
                    Object[] options = Operation.values();
                    Operation operation = (Operation) JOptionPane.showInputDialog(
                            AbacusPracticeWindow.this,
                            "What operation is the quiz using",
                            "Choose Operation",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[settings.getOperation().ordinal()]);
                    settings.setOperation(operation);
                    return null;
                }
            };
            worker.execute();

        });
        submenu.add(item);

        menuBar.add(submenu);

        submenu = new JMenu("Display Settings");

        item = new JMenuItem("Font Size");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptFont();

            }
        });
        submenu.add(item);

    }



    public SettingsModifierButton generateFromLine(String line) {
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
            if (tokens[i].contains("+")) //the plus sign in between the 2 numbers
            {
                //its a required 1s digit sum
                int ind = tokens[i].indexOf("+");
                requiredNumbers[i - 8] = new Required1ColumnSum(
                        Integer.parseInt(tokens[i].substring(0, ind)),
                        Integer.parseInt(tokens[i].substring(ind + 1))
                );
            } else {
                requiredNumbers[i - 8] = new RequiredQuestion(Integer.parseInt(tokens[i]));
            }

        }


        return new SettingsModifierButton(
                chapterName,
                settings,
                new Settings(
                        numQues,
                        new Range(lowerRange, upperRange),
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
    public void setUpMainButton() {
        mainButton = new JButton("Start Quiz");
        mainButton.setBounds(0, 0, 100, 50);
        mainButton.addActionListener(new ActionListener() {
            private QuizManager qm;

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!quizStarted) {
                    qm = new QuizManager(AbacusPracticeWindow.this);
                    hasSessionPass = false;
                    quizStarted = true;

                    mainButton.setText("Abort Quiz");
                } else {
                    qm.cancelQuiz();
                    quizStarted = false;
                    mainButton.setText("Start Quiz");
                    mainLabel.setText("[number]");
                }
            }
        });
        mainButton.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                "pressed");
        add(mainButton);
    }

    public void setUpLabels() {
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
    public int promptInteger(String title, String message, int previousValue) {
        int tmp = 0;
        String s = (String) JOptionPane.showInputDialog(
                this,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                previousValue);
        if (s == null) return previousValue;
        try {
            tmp = (Integer.parseInt(s));

        } catch (NumberFormatException e) {
            return promptInteger(title, message, previousValue); //maybe user might troll...
        }

        return tmp;
    }

    public double promptDouble(String title, String message, double previousValue) {
        double tmp = 0;
        String s = (String) JOptionPane.showInputDialog(
                this,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                previousValue);
        if (s == null) return previousValue;
        try {
            tmp = (Double.parseDouble(s));

        } catch (NumberFormatException e) {
            return promptDouble(title, message, previousValue); //maybe user might troll...
        }

        return tmp;
    }

    private void promptOperation() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                Object[] options = Operation.values();
                Operation operation = (Operation) JOptionPane.showInputDialog(
                        AbacusPracticeWindow.this,
                        "What operation is the quiz using",
                        "Choose Operation",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[settings.getOperation().ordinal()]);
                settings.setOperation(operation);
                return null;
            }
        };
        worker.execute();
    }

    private void promptTimeToAnswer() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setTimeToAnswer(promptInteger(
                        "Set Time to Answer",
                        "At the end of each set of numbers, how long should the child have to fill in their answer",
                        settings.getTimeToAnswer()

                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptTimePerQuestion() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setTimePerQuestion(promptDouble(
                        "Set Time per Question",
                        "In each question, a series of numbers pop up. How long should it stay there?",
                        settings.getTimePerQuestion()
                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptNumberOfNumbersPerQuestions() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setNumberOfNumbersPerQuestions(promptInteger(
                        "Set Number Of Numbers per Question",
                        "In each question, a series of numbers pop up. How many of these numbers should pop up?",
                        settings.getNumberOfNumbersPerQuestions()
                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptUpperRange() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setUpperRange(promptInteger(
                        "Set Upper Range",
                        "What is the Highest Number Possible?",
                        settings.getUpperRange()
                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptLowerRange() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setLowerRange(promptInteger(
                        "Set Lower Range",
                        "What is the Lowest Number Possible?",
                        settings.getLowerRange()
                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptNumberOfQuestions() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                showPassword();
                settings.setNumberOfQuestions(promptInteger(
                        "Set Number Of Problems",
                        "How many problems (Question 1, question 2) should the text have?",
                        settings.getNumberOfQuestions()
                ));
                return null;
            }
        };
        worker.execute();
    }

    private void promptFont() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                settings.setFontSize(promptInteger(
                    "Set Font Size",
                    "How big should the numbers be?",
                    settings.getFontSize()
                ));
                mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));
                mainLabel.setSize(950, settings.getFontSize() + 20);
                return null;
            }
        };
        worker.execute();
    }
    //==========================HEH THIS STUFF==========================//
    public void showPassword()throws Exception
    {
        if(hasSessionPass)return;
        //CODE BY ulmangt

        JPasswordField passwordField = new JPasswordField("");
        // display nothing as the user types
        passwordField.setEchoChar( '*' );
        // set the width of the field to allow space for 20 characters
        passwordField.setColumns( 20 );

        JOptionPane.showConfirmDialog(AbacusPracticeWindow.this, passwordField, "Password", JOptionPane.OK_CANCEL_OPTION );
        //end of code by ulmangt
        passwordField.requestFocus();
        if(new String(passwordField.getPassword()).equals("prbjbpbti".replace("b", "a"))) {
            hasSessionPass = true;
            return;
        }
        JOptionPane.showMessageDialog(this, "Incorrect Password");
        throw new Exception("WRONG PASSWORD");
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
    public static void main(String[] args) {
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
