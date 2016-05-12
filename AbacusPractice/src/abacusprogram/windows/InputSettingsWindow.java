package abacusprogram.windows;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import abacusprogram.quiz.question.Range;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created on 3/29/16.
 */
public class InputSettingsWindow extends JFrame {

    public final int LEVEL_PANE = 0;

    private JTabbedPane tabbedPane;
    private JPanel[] subPanels;
    private JLabel[] labels;


    public InputSettingsWindow(){
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        setUpButtons();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        setVisible(true);

    }

    public void setUpButtonWithXML() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        Document doc = docBuilder.parse(getClass().getResourceAsStream("src/buttons.xml"));

        // Get a list of all <book> elements in the document
        NodeList bookNodes = doc.getElementsByTagName("pset");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element)bookNodes.item(i); // <book> element
            System.out.println("BOOK " + (i+1));
            String isbn = bookElement.getAttribute("ISBN"); // <book> attribute
            System.out.println("\tISBN:\t" + isbn);

            // Get the child elements <title> of <book>, only one
            NodeList titleNodes = bookElement.getElementsByTagName("title");
            Element titleElement = (Element)titleNodes.item(0);
            System.out.println("\tTitle:\t" + titleElement.getTextContent());

            // Get the child elements <author> of <book>, one or more
            NodeList authorNodes = bookElement.getElementsByTagName("author");
            for (int author = 0; author < authorNodes.getLength(); author++) {
                Element authorElement = (Element)authorNodes.item(author);
                System.out.println("\tAuthor:\t" + authorElement.getTextContent());
            }
        }

    }


    //
    public void setUpButtons()
    {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("resources/inputOptionsData.txt"))
            );
            String line = null;

            while((line = reader.readLine()) != null)
            {
                if(line.contains("//")) continue;
                //A1 {
                if(line.contains("{"))
                {

                    //set up subpanels and tabbed Pane
                    JPanel levelPanel = new JPanel();

                    //subpanels are the (time, numOfNumPerQuestion
                    int numOfSubPanels = 6;
                    levelPanel.setLayout(new GridLayout(0, numOfSubPanels));
                    subPanels = new JPanel[numOfSubPanels];
                    labels = new JLabel[numOfSubPanels];

                    for (int i = 0; i < numOfSubPanels; i++) {
                        subPanels[i] = new JPanel();

                        labels[i] = new JLabel("---");
                        subPanels[i].add(labels[i]);

                        levelPanel.add(subPanels[i]);
                    }
                    tabbedPane.add(line.substring(0, line.indexOf("{")), levelPanel);


                    //---Set up buttons in the panel under on tab

                    //TODO: where i left off....
                    Range[] ranges;
                    boolean showRanges;
                    int[] numOfProblems;
                    boolean showNumOfProblems;
                    double[] times;
                    boolean showTimes;





                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }


    }


    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InputSettingsWindow();
            }
        });
    }
}
