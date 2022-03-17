package EjemploChat;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GroupChat extends javax.swing.JFrame {

    static String user;
    static int limite = 1000;
    public static ArrayList<User> Users = new ArrayList<User>();
    public static ArrayList<String> privChats = new ArrayList<String>();
    public static ArrayList<ClienteChat> privClients = new ArrayList<ClienteChat>();
    static Dato[] Datos;
    static String panel = "";
    static boolean emptyP = true;

    public GroupChat() {
        initComponents();
        this.setResizable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        ChatPanel = new javax.swing.JEditorPane();
        SendMsj = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        MsjField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        UsersListPanel = new javax.swing.JEditorPane();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        UserNameField = new javax.swing.JTextField();
        UserButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ChatPanel.setEditable(false);
        ChatPanel.setContentType("text/html"); // NOI18N
        ChatPanel.setText("");
        ChatPanel.setToolTipText("");
        jScrollPane1.setViewportView(ChatPanel);

        SendMsj.setText("Send Message");
        SendMsj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMsjActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel2.setText("Group chat");

        MsjField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MsjFieldActionPerformed(evt);
            }
        });

        UsersListPanel.setEditable(false);
        UsersListPanel.setContentType("text/html"); // NOI18N
        jScrollPane2.setViewportView(UsersListPanel);

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel3.setText("Users connected");

        UserNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserNameFieldActionPerformed(evt);
            }
        });

        UserButton.setText("Send Private Message");
        UserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("User's name");

        jButton1.setText("Salir del chat");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(MsjField, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(SendMsj))
                                    .addComponent(jScrollPane1)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(202, 202, 202)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(95, 95, 95)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(69, 69, 69))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(UserButton)
                            .addComponent(jSeparator1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(UserNameField))
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SendMsj)
                    .addComponent(MsjField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82))
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(UserNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UserButton)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

        public static void sesion(boolean op){
            try {
            MulticastSocket s = new MulticastSocket(4000);//El socket escuchará el puerto 8000
            s.setReuseAddress(true);
            s.setTimeToLive(255);//TTL=255 
            String dir = "230.1.1.1";
            int ptoD = 4000;//Se transmitirá a este puerto
            InetAddress dst = null;
            try {//Resolver la dir IP
                dst = InetAddress.getByName(dir);
            } catch (UnknownHostException u) {
                System.err.println("La dir. no es válida");
                System.exit(1);
            }//catch
            s.joinGroup(dst);//Unirse a la dirección de grupo
            System.out.println("Servicio de mensajeria unido al grupo:" + dir + "...enviando mensaje");
            //<--- get and send message--------->
            String msj= "";
            if (op){
                msj="<inicio>" + user;
            }
            else 
                msj="<fin>"+ user;
            byte[] b = msj.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, dst, ptoD);
            s.send(p);
            System.out.println("Mensaje enviado: " + msj);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
        }
    private void SendMsjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMsjActionPerformed
        // TODO add your handling code here:
        //<------------Send a message------------->
        try {
            MulticastSocket s = new MulticastSocket(4000);//El socket escuchará el puerto 8000
            s.setReuseAddress(true);
            s.setTimeToLive(255);//TTL=255 
            String dir = "230.1.1.1";
            int ptoD = 4000;//Se transmitirá a este puerto
            InetAddress dst = null;
            try {//Resolver la dir IP
                dst = InetAddress.getByName(dir);
            } catch (UnknownHostException u) {
                System.err.println("La dir. no es válida");
                System.exit(1);
            }//catch
            s.joinGroup(dst);//Unirse a la dirección de grupo
            System.out.println("Servicio de mensajeria unido al grupo:" + dir + "...enviando mensaje");
            //<--- get and send message--------->
            String msj = "<" + user + ">" + "<msj>" + MsjField.getText();
            byte[] b = msj.getBytes();
            DatagramPacket p = new DatagramPacket(b, b.length, dst, ptoD);
            s.send(p);
            System.out.println("Mensaje enviado: " + msj);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//GEN-LAST:event_SendMsjActionPerformed

    private void MsjFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MsjFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MsjFieldActionPerformed

    private void UserNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UserNameFieldActionPerformed

    private void UserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserButtonActionPerformed
        String privU = UserNameField.getText();
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).user.equals(privU)) {
                ClienteChat c = new ClienteChat();
                c.UserName.setText(privU);
                c.Me = user;
                c.setVisible(true);
                privClients.add(c);
                privChats.add(privU);
            }
        }
    }//GEN-LAST:event_UserButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
sesion(false);
System.exit(0);// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GroupChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GroupChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GroupChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GroupChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GroupChat().setVisible(true);
                
            }
        });

        try {
            MulticastSocket s = new MulticastSocket(4000);//El socket escuchará el puerto 8000
            s.setReuseAddress(true);
            s.setTimeToLive(255);//TTL=255 
            String dir = "230.1.1.1";
            int ptoD = 4000;//Se transmitirá a este puerto
            InetAddress dst = null;
            try {//Resolver la dir IP
                dst = InetAddress.getByName(dir);
            } catch (UnknownHostException u) {
                System.err.println("La dir. no es válida");
                System.exit(1);
            }//catch
            s.joinGroup(dst);//Unirse a la dirección de grupo
            System.out.println("Servicio unido al grupo:" + dir + "...generando anuncios");

            //get user's name
            user = JOptionPane.showInputDialog(ChatPanel, "Ingresa tu nombre");
            //<-----Save my name into the user's table------------->
            User u = new User(user, s.getInterface(), s.getPort());
            Users.add(u);
            sesion(true);
            //<------Keep listenig messages-------------->
            for (;;) {
                //Receive other user's name or messages
                DatagramPacket p1 = new DatagramPacket(new byte[1000], 1000);
                s.receive(p1);
                String datos = new String(p1.getData(), 0, p1.getLength());

                //System.out.println("Mensaje recibido: " + datos);
                int port = p1.getPort();
                InetAddress IP = p1.getAddress();
                System.out.println("Datagrama recibido desde:" + IP + ":" + port);

                //<----------------Type of message--------------------------->
                //If is a messaje or an advertisement of file 
                if (Pattern.matches("<([^>]*)><([^>]*)><([^>]*)>(.*)", datos)) {
                    Pattern patron = Pattern.compile("<([^>]*)><([^>]*)><([^>]*)>(.*)");
                    Matcher matcher = patron.matcher(datos);
                    // Hace que Matcher busque las cadenas entre "<>".
                    matcher.find();
                    if (matcher.group(3).equals(user)) {
                        if (!privChats.contains(matcher.group(1))) {
                            privChats.add(matcher.group(1));
                            ClienteChat c = new ClienteChat();
                            c.UserName.setText(matcher.group(1));
                            c.Me = user;
                            c.setVisible(true);
                            //c.setTitle("Mi usario"+user);
                            c.Mostrar(matcher.group(1), matcher.group(4));
                            privClients.add(c);
                        } else {
                            System.out.println("Chat abierto ya");
                            for (ClienteChat privIterator : privClients) {
                                if (privIterator.UserName.getText().equals(matcher.group(1))) {
                                    System.out.println(privIterator.UserName.getText());
                                    System.out.println(privIterator.UserName.getText());
                                    privIterator.Mostrar(matcher.group(1), matcher.group(4));

                                }
                            }
                        }
                    }
                } else if (Pattern.matches("<([^>]*)><([^>]*)>(.*)", datos)) {

                    Pattern patron = Pattern.compile("<([^>]*)><([^>]*)>(.*)");
                    Matcher matcher = patron.matcher(datos);
                    // Hace que Matcher busque las cadenas entre "<>".
                    matcher.find();
                    String UserCo = matcher.group(1);//User´s name
                    String TypeMsj = matcher.group(2);//Type of messaje <msj> or <file>

                    UserTable(UserCo, IP, port);//Call to user table to save new users

                    if (TypeMsj.equals("msj")) {
                        //System.out.println("Llegó un mensaje");
                        Mostrar(UserCo, matcher.group(3), false);//Call function that will put the message FLAG 0 means its a message
                    } else {
                        System.out.println("Se recibirá archivo");

                    }

                }
                else {
                     Pattern patron = Pattern.compile("<([^>]*)>(.*)");
                    Matcher matcher = patron.matcher(datos);
                    // Hace que Matcher busque las cadenas entre "<>".
                    matcher.find();
                    String UserCo = matcher.group(2);//User´s name
                    if(matcher.group(1).equals("inicio")){
                     Mostrar(UserCo,"Ha entrado al chat",false);

                    }
                    else if(matcher.group(1).equals("fin"))
                     Mostrar(UserCo,"Ha salido del chat",false);
                     for(int i=0; i < Users.size();i++){
                         if (Users.get(i).user.equals(matcher.group(2))){
                             Users.remove(i);
                         }
                     }
                }
                

            }
        } catch (HeadlessException | IOException e) {
        }//catch
    }

//<-----------------Update the list of users---------------------->
    public static void UserTable(String user, InetAddress IP, int port) {
        String table = "";
        boolean exist = false;
        //Check if user exists 
        for (int i = 0; i < Users.size(); i++) {
            String UserLista = Users.get(i).user;
            if (UserLista.equals(user)) {//If user exists ignore it
                System.out.println("Usuario:" + user + " existente en la lista");
                exist = true;
            }
        }
        if (exist == false)//Save new user
        {
            User u = new User(user, IP, port);
            Users.add(u);
            //System.out.println("Usuario:"+user+" agregado en la lista");
        }
        for (int i = 0; i < Users.size(); i++) {
            String UserinList = Users.get(i).user;
            table = table + "<tr><td>" + UserinList + "</td></tr>";
            //System.out.println("Usuario HTML:"+table);
        }
        //System.out.println("Usuarios HTML:"+table);
        UsersListPanel.setText("<table>" + table + "</table>"); 
    }

    public static void Mostrar(String UserName, String Msj, boolean t) {
        String msjHTML = "";
        String msjCom = "";

        if (t == false)//If its a message
        {
            //Check is it has faces like :D, :v, :/ , :s , :3, <3
            if (Pattern.matches("[^:]+(:D|:v|:c|:s|:o|<3)", Msj)) {
                //System.out.println("Hay un emoji al final del mensaje");
                Pattern patron = Pattern.compile("([^:]*)(:D|:v|:c|:s|:o|<3)");
                Matcher matcher = patron.matcher(Msj);
                matcher.find();
                String texto = matcher.group(1);//Get only the text
                String emoji = matcher.group(2);//Get only the emoji
                System.out.println("Mensaje:" + texto + " Emoji:" + emoji);
                //<-----------------------Check what kind of emoji is------------------>
                if (emoji.equals(":D") || texto.equals(":D")) {
                    msjHTML = msjHTML + "<img src=https://i.pinimg.com/originals/36/df/46/36df46dc53773f15ad5935379f5bae7e.gif "
                            + "width=\"30\" height=\"30\">";
                } else if (emoji.equals(":v")) {
                    msjHTML = msjHTML + "<img src=\"file:\\\\\\D:\\DESCARGAS\\Chat\\Archivos enviables\\pacman.gif\" "
                            + "width=\"30\" height=\"30\">";
                } else if (emoji.equals(":c")) {
                    msjHTML = msjHTML + "<img src=\"file:\\\\\\D:\\DESCARGAS\\Chat\\Archivos enviables\\triste.png\" "
                            + "width=\"30\" height=\"30\">";
                } else if (emoji.equals(":s")) {
                    msjHTML = msjHTML + "<img src=\"file:\\\\\\D:\\DESCARGAS\\Chat\\Archivos enviables\\borracho.png\" "
                            + "width=\"30\" height=\"30\">";
                } else if (emoji.equals(":o")) {
                    msjHTML = msjHTML + "<img src=\"file:\\\\\\D:\\DESCARGAS\\Chat\\Archivos enviables\\feo.gif\" "
                            + "width=\"30\" height=\"30\">";
                } else if (emoji.equals("<3")) {
                    msjHTML = msjHTML + "<img src=\"file:\\\\\\D:\\DESCARGAS\\Chat\\Archivos enviables\\love.gif\" "
                            + "width=\"30\" height=\"30\">";
                }
                msjCom = "<tr><td>" + UserName + " dice: " + texto + msjHTML + "</td></tr>";
            }//if
            else //If the message doesn't have emoji
            {
                //System.out.println("Mensaje sin emoji");
                msjCom = "<tr><td>" + UserName + "  :  " + Msj + "</td></tr>";
            }
        }//if
        else//if its a file
        {
            //System.out.println("Se enviará un archivo");
            if (Pattern.matches("[^:]+(.png|.jpg|.jpeg|.gif|.PNG)", Msj))//If it's an image 
            {
                //System.out.println("El archivo es una imagen");
                msjHTML = msjHTML + "<img src=\"/home/alfa/NetBeansProjects/Chat/" + Msj + "\""
                        + " width=\"200\" height=\"200\">";
                System.out.println(msjHTML);
                msjCom = "<tr><td>" + UserName + " envió una imagen: <br>" + msjHTML + "</td></tr>";
                System.out.println(msjCom);

            } else//For any other kind of file 
            {
                System.out.println("Es otro tipo de archivo");
                msjCom = "<tr><td>" + UserName + " envió un archivo: " + Msj + "</td></tr>";
            }
        }//else 

        //Once it proceses the messages show them in the interface 
        panel = panel + msjCom;
        //System.out.println("Salió del if, linea HTML: "+panel);
        ChatPanel.setText(panel);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JEditorPane ChatPanel;
    private javax.swing.JTextField MsjField;
    private javax.swing.JButton SendMsj;
    private javax.swing.JButton UserButton;
    public javax.swing.JTextField UserNameField;
    public static javax.swing.JEditorPane UsersListPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
