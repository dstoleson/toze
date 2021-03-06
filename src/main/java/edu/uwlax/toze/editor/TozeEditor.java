package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.editor.SpecificationTreeModel.SpecificationNode;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.persist.SpecificationReader;
import edu.uwlax.toze.persist.SpecificationWriter;
import edu.uwlax.toze.persist.TozeJaxbContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

import static edu.uwlax.toze.editor.TozeNotificationKey.*;

/**
 * Main UI class for the TOZE Editor. Displays a file/specification tree
 * navigator on the left, a tabbed specification editor on the right, a tool
 * palette on the bottom left under the navigator, and a status console on the
 * bottom right under the specification editor.
 * <p/>
 * //TODO Possibly break UI components into own classes to simplify and
 * for re-use possibilities.
 *
 * @author David Stoleson
 */
public class TozeEditor extends javax.swing.JFrame implements Observer
{
    // keep track of untitled documents
    // make each one unique
    private int untitledCount = 1;

    private boolean isClosingDocument = false;

    private final ResourceBundle uiBundle;

    // Editor Tabs
    private JTabbedPane specificationTabPanel;
    private JTree specificationTree;
    private JList specialCharsList;
    private JList errorsList;

    // Maps Editor Tabs to the Controller controller the specification inside of it
    private final HashMap<Component, SpecificationController> tabControllers = new HashMap<Component, SpecificationController>();
    private final SpecificationTreeModel treeModel = new SpecificationTreeModel(new DefaultMutableTreeNode("ROOT"));

    public TozeEditor()
    {
        uiBundle = ResourceBundle.getBundle("edu.uwlax.toze.editor.toze");

        /*
         * Handle Mac OS menu bars
         */
        OSType osType = getOperatingSystemType();
        System.out.printf("OS = %s\n", osType);

        if (getOperatingSystemType() == OSType.MacOS)
            {
            /*
             * use the apple menu bar at the top of the screen
             * instead of putting the menu on the window frame
             */

            // @TODO - can't do this until CMD-Q can be intercepted
            // and handled like quit() instead of just quitting
            // the app automatically
            // System.setProperty("apple.laf.useScreenMenuBar", "true");
            // System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TozeEditor");
            }

        initComponents();

        // init and cache the JAXB context on startup
        // slight performance / user experience improvement when
        // open / saving documents
        TozeJaxbContext.getTozeJaxbContext();
    }

    private TreeModel getTreeModel()
    {
        return treeModel;
    }

    private void initComponents()
    {
        EditorMouseAdaptor mouseAdaptor = new EditorMouseAdaptor();

        JSplitPane editorSplitPane = new JSplitPane();
        JSplitPane leftSplitPane = new JSplitPane();
        JSplitPane rightSplitPane = new JSplitPane();

        JScrollPane specificationTreeScrollPane = new JScrollPane();
        specificationTree = new JTree();
        specificationTree.addTreeSelectionListener(new EditorTreeSelectionListener());

        specificationTabPanel = new JTabbedPane();
        specificationTabPanel.addChangeListener(new EditorTabbedPaneListener());
        JTabbedPane paletteTabPanel = new JTabbedPane();

        JScrollPane specialCharsScrollPane = new JScrollPane();
        specialCharsList = new JList(TozeCharMap.getAllChars().toArray());
        specialCharsList.setCellRenderer(new SpecialCharListCellRenderer());
        specialCharsList.setFont(TozeFontMap.getFont());
        specialCharsList.addMouseListener(mouseAdaptor);

        JScrollPane errorScrollPane = new JScrollPane();
        errorsList = new JList();
        errorsList.setCellRenderer(new ErrorListCellRenderer());
        errorsList.setFont(TozeFontMap.getFont());
        errorsList.addMouseListener(mouseAdaptor);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        leftSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setDividerLocation(400);
        leftSplitPane.setTopComponent(specificationTreeScrollPane);
        leftSplitPane.setBottomComponent(paletteTabPanel);

        // give the specification tree priority on resize
        // instead of the palettes
        leftSplitPane.setResizeWeight(1);

        rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setDividerLocation(400);
        rightSplitPane.setLeftComponent(specificationTabPanel);
        rightSplitPane.setRightComponent(errorScrollPane);

        // give the document priority on resize
        // instead of the error list
        rightSplitPane.setResizeWeight(1);

        editorSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        editorSplitPane.setDividerLocation(250);
        editorSplitPane.setLeftComponent(leftSplitPane);
        editorSplitPane.setRightComponent(rightSplitPane);


        specificationTree.setModel(this.getTreeModel());
        specificationTree.setRootVisible(false);
        specificationTree.setShowsRootHandles(true);

        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
        specificationTree.setCellRenderer(treeCellRenderer);

        specificationTreeScrollPane.setViewportView(specificationTree);
        specialCharsScrollPane.setViewportView(specialCharsList);
        errorScrollPane.setViewportView(errorsList);

        paletteTabPanel.addTab(uiBundle.getString("specialCharsTab.title"), specialCharsScrollPane);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu();
        fileMenu.setText(uiBundle.getString("fileMenu.title"));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItem;

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.newSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                newSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.openSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                openSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.saveSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                saveSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.saveAsSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                saveAsSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.printSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                printSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.exportJpeg.title"));
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                exportSpecificationAsJpeg();
            }
        }
        );

        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.exportLatex.title"));
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                exportSpecificationAsLatex();
            }
        }
        );

        fileMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.closeSpecification.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                closeSelectedSpecifications();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.quit.title"));
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                quit();
            }
        }
        );
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu(uiBundle.getString("editMenu.title"));
        editMenu.setMnemonic(KeyEvent.VK_E);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.cut.title"));
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                cut();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.copy.title"));
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                copy();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.paste.title"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paste();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("editMenu.font.increase"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                increaseFont();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("editMenu.font.decrease"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                decreaseFont();
            }
        }
        );
        editMenu.add(menuItem);

        menuBar.add(editMenu);

        JMenu specificationMenu = new JMenu(uiBundle.getString("specificationMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.checkSyntax.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentSpecificationController().parseSpecification(false);
            }
        }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.clearErrors.title"));
        menuItem.addActionListener(new ActionListener()
                                   {
                                       @Override
                                       public void actionPerformed(ActionEvent e)
                                       {
                                           currentSpecificationController().clearErrors();
                                       }
                                   }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAbbreviationDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAbbreviation(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        JMenu addAxiomaticDefMenu = new JMenu();
        addAxiomaticDefMenu.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAxiomaticType(specController.getSpecification(), true);
            }
        }
        );

        addAxiomaticDefMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAxiomaticType(specController.getSpecification(), false);
            }
        }
        );
        addAxiomaticDefMenu.add(menuItem);

        specificationMenu.add(addAxiomaticDefMenu);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addBasicTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addBasicType(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addClass.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addClass();
            }
        }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addFreeTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addFreeType(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        JMenu addGenericDefMenu = new JMenu();
        addGenericDefMenu.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addGenericType(true);
            }
        }
        );
        addGenericDefMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addGenericType(false);
            }
        }
        );
        addGenericDefMenu.add(menuItem);

        specificationMenu.add(addGenericDefMenu);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addSpecificationPredicate();
            }
        }
        );
        specificationMenu.add(menuItem);

        menuBar.add(specificationMenu);
        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                          .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                          .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        )
        );

        pack();

        // set up application closing behavior
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
                          {
                              @Override
                              public void windowClosing(WindowEvent e)
                              {
                                  TozeEditor.this.quit();
                              }
                          });
    }

    private void quit()
    {
        List<SpecificationDocument> allSpecifications = allSpecifications();
        boolean hasEditedDocuments = hasEditedDocuments(allSpecifications);
        String applicationQuitTextKey = hasEditedDocuments ? "application.quit.edited.message" : "application.quit.message";
        int dialogOption = hasEditedDocuments ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION;

        int result = JOptionPane.showConfirmDialog(this,
                                                   uiBundle.getString(applicationQuitTextKey),
                                                   uiBundle.getString("application.quit.title"),
                                                   dialogOption);

        // if there are no edited documents and the user wants to quite, don't check, close
        // if there are no edited documents and the user doesn't want to quit, don't check, don't close
        // if there are edited documents and the user doesn't want to quit, don't check, don't close
        // if there are edited documents and the user wants to quit without saving, don't check, close
        // if there are edited documents and the user wants to quit with saving, check, close
        boolean shouldCheck = (hasEditedDocuments && (result == JOptionPane.YES_OPTION));
        boolean shouldClose = ((result == JOptionPane.YES_OPTION)
                                      || (hasEditedDocuments && (result == JOptionPane.NO_OPTION)));

        // close the application, checking unsaved specifications if needed
        // if the number of closed specifications does not match the number of
        // actually closed specifications, don't quit because the user cancelled
        if (shouldClose)
            {
            int closedSpecifications = closeSpecifications(allSpecifications, shouldCheck);
            if (closedSpecifications == allSpecifications.size())
                {
                System.exit(0);
                }
            }

    }

    private void cut()
    {
        currentSpecificationController().cut();
    }

    private void copy()
    {
        currentSpecificationController().copy();
    }

    private void paste()
    {
        currentSpecificationController().paste();
    }

    private SpecificationController currentSpecificationController()
    {
        Component tab = specificationTabPanel.getSelectedComponent();
        return tabControllers.get(tab);
    }

    private void newSpecification()
    {
        Specification specification = new Specification();
        openSpecificationTab(specification, new File(uiBundle.getString("file.untitled") + untitledCount++));

    }

    private void openSpecification()
    {
        FileDialog fileDialog = new FileDialog(this, uiBundle.getString("fileDialog.openSpecification.title"), FileDialog.LOAD);
        fileDialog.setMultipleMode(true);
        fileDialog.show();

        File[] selectedFiles = fileDialog.getFiles();

        if (selectedFiles != null)
            {
            List<File> selectedFileList = Arrays.asList(selectedFiles);
            for (File specificationFile : selectedFileList)
                {
                // see if the file is already open
                Component tab = null;

                for (Map.Entry<Component, SpecificationController> entry : tabControllers.entrySet())
                    {
                    if (specificationFile.equals(entry.getValue().getSpecificationDocument().getFile()))
                        {
                        tab = entry.getKey();
                        break;
                        }
                    }


                if (tab != null)
                    {
                    // the file is already open, select the corresponding tab
                    specificationTabPanel.setSelectedComponent(tab);
                    }
                else
                    {
                    // open the file in a new tab
                    try
                        {
                        InputStream inputStream = new FileInputStream(specificationFile);
                        SpecificationReader specReader = new SpecificationReader(inputStream);
                        Specification specification = specReader.read();
                        inputStream.close();

                        openSpecificationTab(specification, specificationFile);

                        }
                    catch (Exception e)
                        {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                                      uiBundle.getString("fileWarning.problemOpening.message") + ": " + specificationFile.getName(),
                                                      uiBundle.getString("fileWarning.problemOpening.title"),
                                                      JOptionPane.WARNING_MESSAGE
                        );
                        }
                    }
                }
            }
    }

    private void openSpecificationTab(Specification specification, File specificationFile)
    {
        SpecificationDocument specificationDocument = new SpecificationDocument(specificationFile, specification);
        treeModel.addSpecificationDocument(specificationDocument);

        SpecificationController controller = new SpecificationController(specificationDocument);
        SpecificationView specView = new SpecificationView(specification, controller);
        controller.setSpecificationView(specView);

        specView.setLayout(new TozeLayout());
        specView.requestRebuild();

        JPanel panel = new JPanel(false);
        final JScrollPane specScroller = new JScrollPane();
        specScroller.getViewport().add(specView, null);
        panel.add(specScroller, null);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(specScroller);

        controller.setScrollPane(specScroller);

        // scroll to the top of the document after
        // everything has been set up properly
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                specScroller.getVerticalScrollBar().setValue(0);
            }
        });
        specificationTabPanel.addTab(specificationDocument.getFile().getName(), specScroller);
        specificationTabPanel.setSelectedComponent(specScroller);

        // map the tab to the controller
        tabControllers.put(specScroller, controller);

        controller.addObserver(this);
        controller.addObserver(treeModel);
        specificationDocument.addObserver(treeModel);
        controller.parseSpecification(false);
    }

    private void saveSpecification()
    {
        SpecificationController specificationController = currentSpecificationController();
        SpecificationDocument specificationDocument = specificationController.getSpecificationDocument();
        saveSpecification(specificationDocument);
    }

    private boolean saveSpecification(SpecificationDocument specificationDocument)
    {
       return saveSpecification(specificationDocument, false);
    }

    private boolean saveSpecification(SpecificationDocument specificationDocument, boolean rename)
    {
        Specification specification = specificationDocument.getSpecification();
        File specificationFile = specificationDocument.getFile();
        boolean savedSpecification = false;

        // if this is a new file, or the file is being renamed (save as...)
        if (rename || specificationFile.getName().startsWith(uiBundle.getString("file.untitled")))
            {
            specificationFile = fileForSaving();
            }

        // if a file was selected, or already existed
        if (specificationFile != null)
            {
            savedSpecification = writeSpecificationToFile(specification, specificationFile);
            }

        // the file was written successfully, update the UI
        if (savedSpecification)
            {
            specificationDocument.setEdited(false);
            specificationDocument.setFile(specificationFile);

            // make the tab title the new file name
            int selectedTabIndex = specificationTabPanel.getSelectedIndex();
            specificationTabPanel.setTitleAt(selectedTabIndex, specificationFile.getName());
            }


        return savedSpecification;
    }

    private boolean saveAsSpecification()
    {
        SpecificationController specificationController = currentSpecificationController();
        SpecificationDocument specificationDocument = specificationController.getSpecificationDocument();

        boolean specificationSaved = saveSpecification(specificationDocument, true);

        return specificationSaved;
    }


    private File fileForSaving()
    {
        FileDialog fileDialog = new FileDialog(this, uiBundle.getString("fileDialog.saveSpecification.title"), FileDialog.SAVE);
        fileDialog.show();
        File specificationFile = null;

        if (fileDialog.getFile() != null)
            {
            specificationFile = new File(fileDialog.getDirectory() + fileDialog.getFile());
            }
        return specificationFile;
    }

    private void exportSpecificationAsLatex()
    {
        String filename = "";
        FileDialog fd = new FileDialog(this, uiBundle.getString("fileDialog.exportLatex.title"), FileDialog.SAVE);
        fd.show();

        if (fd.getFile() != null)
            {
            filename = fd.getDirectory() + fd.getFile() + ".tex";
            Specification specification = currentSpecificationController().getSpecification();

            String latex = TozeLatexExporter.getLatex(specification);

            try
                {
                File latexFile = new File(filename);
                FileWriter writer = new FileWriter(latexFile);
                writer.write(latex);
                writer.close();
                }
            catch (IOException e)
                {
                e.printStackTrace();
                }
            }
    }

    private void exportSpecificationAsJpeg()
    {
        String filename = "";
        FileDialog fd = new FileDialog(this, uiBundle.getString("fileDialog.exportJpeg.title"), FileDialog.SAVE);
        fd.show();

        if (fd.getFile() != null)
            {
            filename = fd.getDirectory() + fd.getFile() + ".jpg";
            SpecificationView specificationView = currentSpecificationController().getSpecificationView();
            Dimension d = specificationView.getPreferredSize();
            BufferedImage img = (BufferedImage) specificationView.createImage(d.width, d.height);
            specificationView.paint(img.getGraphics());
            try
                {
                ImageIO.write(img, "jpeg", new File(filename));
                }
            catch (Exception e2)
                {
                System.out.println(e2.toString());
                }
            }
    }

    private void printSpecification()
    {
        // assume printing will occur
        int printPanelOption = JOptionPane.OK_OPTION;

        // check to see that the page fits
        // it not warn user and they can print at a smaller scale
        SpecificationController specController = currentSpecificationController();
        SpecificationView specificationView = specController.getSpecificationView();
        Dimension d = specificationView.getPreferredSize();

        double scale = 1.0;

        boolean shouldPrint = PrintUtilities.checkPageWidth(d.width);
        String printMessage = "";

        if (!shouldPrint)
            {
            // scale to 75% if doesn't fully fit
            scale = 0.75;
            printMessage = uiBundle.getString("print.warning1");
            }

        // check it again
        shouldPrint = PrintUtilities.checkPageWidth((int)(d.getWidth() * scale));

        if (!shouldPrint)
            {
            printMessage = uiBundle.getString("print.warning2");
            }


        // if it was scaled or still doesn't fit ask the user
        if (scale != 1.0 || !shouldPrint)
            {
                printPanelOption = JOptionPane.showConfirmDialog(new JFrame(),
                                                  printMessage,
                                                  uiBundle.getString("print.title"),
                                                  JOptionPane.OK_CANCEL_OPTION);
            }

        if (printPanelOption == JOptionPane.YES_OPTION)
            {
            BufferedImage img = (BufferedImage) specificationView.createImage(d.width, d.height);
            List<Integer> pageBreaks = calcPageBreaks(specificationView);
            specificationView.paint(img.getGraphics());

            try
                {
                PrintUtilities.printImage(img, pageBreaks, scale);
                }
            catch (PrinterException e)
                {
                System.out.println(e);
                Object[] options = {uiBundle.getString("print.error.ok")};
                JOptionPane.showOptionDialog(new JFrame(),
                                             uiBundle.getString("print.error.message"),
                                             uiBundle.getString("print.error.title"),
                                             JOptionPane.PLAIN_MESSAGE,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,
                                             options,
                                             options[0]);
                }
            }
    }

    private List<Integer> calcPageBreaks(final Container c)
    {
        ArrayList<Integer> pageBreaks = new ArrayList<Integer>();

        int numSpecParagraphs = c.getComponentCount();

        for (int i = 0; i < numSpecParagraphs; i++)
            {
            Component specChild = c.getComponent(i);

            if (specChild instanceof ParagraphView)
                {
                // special handling of class view sub-paragraphs
                // sub paragraphs are relative to the class paragraph
                if (specChild instanceof ClassView)
                    {
                    int classHeightOffset = specChild.getY();
                    int numClassParagraphs = ((ClassView) specChild).getComponentCount();

                    for (int j = 0; j < numClassParagraphs; j++)
                        {
                        Component classChild = ((ClassView) specChild).getComponent(j);
                        if (classChild instanceof ParagraphView)
                            {
                            pageBreaks.add(classChild.getY() + classChild.getHeight() + classHeightOffset);
                            }
                        }
                    }

                pageBreaks.add(specChild.getY() + specChild.getHeight());
                }
            }

        Collections.sort(pageBreaks);

        return pageBreaks;
    }

    private boolean writeSpecificationToFile(Specification specification, File specificationFile)
    {
        boolean wroteFile = false;

        try
            {
            OutputStream outputStream = new FileOutputStream(specificationFile);
            SpecificationWriter specWriter = new SpecificationWriter(outputStream);
            specWriter.write(specification);
            outputStream.close();
            wroteFile = true;
            }
        catch (Exception e)
            {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                                          uiBundle.getString("fileWarning.problemSaving.message") + ": " + specificationFile.getName(),
                                          uiBundle.getString("fileWarning.problemSaving.title"),
                                          JOptionPane.WARNING_MESSAGE
            );
            }
        return wroteFile;
    }

    private List<SpecificationDocument> allSpecifications()
    {
        List<SpecificationDocument> allSpecifications = new ArrayList<SpecificationDocument>();

        for (SpecificationController specificationController : tabControllers.values())
            {
            allSpecifications.add(specificationController.getSpecificationDocument());
            }

        return allSpecifications;
    }

    private List<SpecificationDocument> selectedSpecifications()
    {
        TreePath[] selectedPaths = specificationTree.getSelectionPaths();
        List<SpecificationDocument> selectedSpecifications = new ArrayList<SpecificationDocument>();

        if (selectedPaths != null)
            {
            List<TreePath> treePathList = Arrays.asList(selectedPaths);

            for (TreePath treePath : treePathList)
                {
                if (treePath.getPathCount() == 2) // the number of path items in selected domain node
                    {
                    SpecificationNode specificationNode = (SpecificationNode) treePath.getLastPathComponent();
                    SpecificationDocument specificationDocument = specificationNode.getSpecificationDocument();
                    selectedSpecifications.add(specificationDocument);
                    }
                }
            }
        return selectedSpecifications;
    }

    private boolean hasEditedDocuments(List<SpecificationDocument> specificationDocuments)
    {
        boolean hasEditedDocuments = false;

        for (SpecificationDocument specificationDocument : specificationDocuments)
            {
            if (specificationDocument.isEdited())
                {
                hasEditedDocuments = true;
                break;
                }
            }
        return hasEditedDocuments;
    }

    private void closeSelectedSpecifications()
    {
        List<SpecificationDocument> selectedSpecifications = selectedSpecifications();

        if (selectedSpecifications.isEmpty())
            {
            return;
            }

        boolean hasEditedDocuments = hasEditedDocuments(selectedSpecifications);

        String closeSelectedTextKey = hasEditedDocuments ? "fileWarning.closeSpecification.edited.message" : "fileWarning.closeSpecification.message";
        int dialogOption = hasEditedDocuments ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION;

        int result = JOptionPane.showConfirmDialog(this,
                                                   uiBundle.getString(closeSelectedTextKey),
                                                   uiBundle.getString("fileWarning.closeSpecification.confirm"),
                                                   dialogOption);

        // if there are no edited documents and the user wants to close them, don't check, close
        // if there are no edited documents and the user doesn't want to close them, don't check, don't close
        // if there are edited documents and the user doesn't want to close, don't check, don't close
        // if there are edited documents and the user wants to close without saving, don't check, close
        // if there are edited documents and the user wants to close with saving, check, close
        boolean shouldCheck = (hasEditedDocuments && (result == JOptionPane.YES_OPTION));
        boolean shouldClose = ((result == JOptionPane.YES_OPTION)
                || (hasEditedDocuments && (result == JOptionPane.NO_OPTION)));

        if (shouldClose)
            {
            closeSpecifications(selectedSpecifications, shouldCheck);
            }
    }

    private int closeSpecifications(List<SpecificationDocument> specificationDocuments, boolean checkForEdited)
    {
        int closedSpecifications = 0;

        isClosingDocument = true;

        boolean canceled = false;

        Iterator<SpecificationDocument> iterator = specificationDocuments.iterator();

        while (!canceled && iterator.hasNext())
            {
            SpecificationDocument specificationDocument = iterator.next();

            if (checkForEdited && specificationDocument.isEdited())
                {
                // if checkForEdited and document is edited, ask to save it and then close
                // if not saved, cancel the whole operation
                // if use doesn't want to save it, close anyway
                String editedMessage = MessageFormat.format(uiBundle.getString("fileWarning.closedEditedSpecification.message"),
                                                            specificationDocument.getFile().getName());
                int closeState = JOptionPane.showConfirmDialog(this,
                                                               editedMessage,
                                                               uiBundle.getString("fileWarning.closedEditedSpecification.confirm"),
                                                               JOptionPane.YES_NO_CANCEL_OPTION);
                switch (closeState)
                    {
                    case JOptionPane.YES_OPTION:
                        boolean saved = saveSpecification(specificationDocument);

                        if (saved)
                            {
                            closeSpecification(specificationDocument);
                            closedSpecifications++;
                            }

                        // if the file couldn't be save, fail and ignore
                        // the remaining specifications to be close
                        canceled = !saved;

                        break;
                    case JOptionPane.NO_OPTION:
                        closeSpecification(specificationDocument);
                        closedSpecifications++;
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        canceled = true;
                    }
                }
            else
                {
                // blindly close the document because the user didn't want to save anything
                closeSpecification(specificationDocument);
                closedSpecifications++;
                }
            }

        isClosingDocument = false;
        
        return closedSpecifications;
    }

    private void closeSpecification(SpecificationDocument specificationDocument)
    {
        treeModel.removeSpecification(specificationDocument);
        int tabIndex = specificationTabPanel.indexOfTab(specificationDocument.getFile().getName());

        if (tabIndex >= 0)
            {
            Component tab = specificationTabPanel.getComponentAt(tabIndex);
            specificationTabPanel.removeTabAt(tabIndex);
            tabControllers.remove(tab);
            }
        updateErrors(null);
    }

    public void insertSymbol(String symbol)
    {
        SpecificationController selectedController = currentSpecificationController();

        selectedController.insertSymbol(symbol);
    }

    public void changeFont(int amount)
    {
        int fontSize = TozeFontMap.getDefaultFontSize();

        if (fontSize + amount >= 2)
            {
            TozeFontMap.setDefaultFontSize(fontSize + amount);
            for (SpecificationController specController : tabControllers.values())
                {
                SpecificationView specificationView = specController.getSpecificationView();
                specificationView.revalidate();
                specificationView.repaint();
                }
            }
    }

    public void increaseFont()
    {
        changeFont(2);
    }

    public void decreaseFont()
    {
        changeFont(-2);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable()
                                        {
                                            public void run()
                                            {
                                                new TozeEditor().setVisible(true);
                                            }
                                        }
        );
    }

    /**
     * Display any errors in the current document, a 'no errors' message if there
     * are no errors in the current document, display nothing if there is no
     * current document.
     *
     * @param errors Not empty, errors -> display them
     *               Empty, no errors -> display no errors message
     *               Null, no document -> display nothing
     */
    private void updateErrors(List errors)
    {
        Object[] errorArray = null;

        if (errors != null)
            {
            if (!errors.isEmpty())
                {
                // display the errors
                errorArray = errors.toArray();
                }
            else
                {
                // display that there are no errors in the current document
                errorArray = new String[]{uiBundle.getString("errorList.noErrors")};
                }
            }
        else
            {
            // no current document, thus no errors
            errorArray = new String[]{};
            }

        errorsList.setListData(errorArray);
    }

    public void update(Observable o, Object arg)
    {
        if (o instanceof SpecificationController)
            {
            TozeNotificationType notification =
                    (TozeNotificationType)((HashMap) arg).get(KEY_NOTIFICATION_TYPE);
            switch (notification)
                {
                case ERRORS:
                    updateErrors(((SpecificationController) o).getSpecification().getErrors());
                }
            }
    }

    private class EditorMouseAdaptor extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getSource() == specialCharsList)
                {
                if (e.getClickCount() == 2)
                    {
                    int index = specialCharsList.locationToIndex(e.getPoint());
                    TozeCharMap charMap = (TozeCharMap) specialCharsList.getModel().getElementAt(index);
                    TozeEditor.this.insertSymbol(charMap.getTozeChar());
                    }
                }
            else if (e.getSource() == errorsList)
                {
                if (errorsList.getSelectedValue() instanceof TozeToken)
                    {
                    TozeToken errorToken = (TozeToken) errorsList.getSelectedValue();
                    SpecificationController specificationController = currentSpecificationController();

                    if (specificationController != null)
                        {
                            specificationController.selectError(errorToken);
                        }
                    }
                }
        }
    }


    /**
     * Implement JTabbedPane ChangeListener interface
     */
    private class EditorTabbedPaneListener implements ChangeListener
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            SpecificationController specificationController = currentSpecificationController();

            if (specificationController != null)
                {
                updateErrors(specificationController.getSpecification().getErrors());
                }
        }
    }

    private class EditorTreeSelectionListener implements TreeSelectionListener
    {
        @Override
        public void valueChanged(TreeSelectionEvent e)
        {
            // get the selected tree path
            TreePath treePath = e.getPath();

            // if a new docoument is selected, need to switch the tabs
            SpecificationDocument documentToSelect = null;

            // get the selected object node (might be the same as the specification document node)
            Object selectedObject = treePath.getLastPathComponent();

            if (selectedObject instanceof SpecificationNode)
                {
                documentToSelect = ((SpecificationNode) selectedObject).getSpecificationDocument();
                }

            // get the specification document node
            SpecificationNode selectedRoot = (SpecificationNode)treePath.getPathComponent(1);
            SpecificationController currentController = currentSpecificationController();
            SpecificationDocument currentSpecificationDocument = currentController.getSpecificationDocument();

            if (selectedRoot.getSpecificationDocument() != currentSpecificationDocument)
                {
                documentToSelect = selectedRoot.getSpecificationDocument();
                }

            if (documentToSelect != null)
                {
                // determine which tab belongs to the specification
                // based on the file name (same as the tab title)
                // and switch to that tab
                int tabIndex = specificationTabPanel.indexOfTab(documentToSelect.getFile().getName());
                specificationTabPanel.setSelectedIndex(tabIndex);
                }

            // highlight and move to the selected object
            // in the currently display specification
            SpecObject specObject = ((SpecificationTreeModel.SpecObjectNode)selectedObject).getSpecObject();
            currentSpecificationController().selectParagraph(specObject);
        }
    }

    /*
     * Various types of operating systems
     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    }

    /*
     * Return the OSType corresponding to the current operation system.
     */
    public static OSType getOperatingSystemType() {
        OSType detectedOS = null;

        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0))
            {
            detectedOS = OSType.MacOS;
            }
        else if (OS.indexOf("win") >= 0)
            {
            detectedOS = OSType.Windows;
            }
        else if (OS.indexOf("nux") >= 0)
            {
            detectedOS = OSType.Linux;
            }
        else
            {
            detectedOS = OSType.Other;
            }

    return detectedOS;
    }

}
