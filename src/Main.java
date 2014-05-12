import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dustin on 5/12/14.
 */
public class Main extends JFrame {
    private JTextField textField;
    private JButton button;
    private SwingWorker<Void, Void> worker;
    public Main(){
        super("Server");
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loop();
                return null;
            }
        };
    }

    private void loop() {
        try {
            ServerSocket server = new ServerSocket(5050,10);
            System.out.println("waiting for connection");
            Socket connection = server.accept();
            System.out.println("connected");
            final ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(getComponent(0),"Connection Established!");
                    button.setEnabled(true);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                output.writeObject(textField.getText());
                                JOptionPane.showMessageDialog(getComponent(0),"Sent" + textField.getText());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                }
            });
            System.out.println("Loop executed");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void  main(String[] args){
        final Main main = new Main();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                main.createGui();
                main.setVisible(true);
            }
        });
        main.startWorker();
    }

    private void startWorker() {
        worker.execute();
    }


    private void createGui() {
        setSize(new Dimension(500,500));
        textField = new JTextField("Enter Stuff here");
        button = new JButton("Send");
        setLayout(new GridLayout(2,0));
        add(textField);
        add(button);
        button.setEnabled(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
