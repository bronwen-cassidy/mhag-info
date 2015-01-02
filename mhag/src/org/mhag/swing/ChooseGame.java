/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChooseGame.java
 *
 * Created on Nov 21, 2011, 3:34:10 PM
 */
package org.mhag.swing;

import javax.swing.WindowConstants;

/**
 *
 * @author lvmy
 */
public class ChooseGame extends javax.swing.JDialog {

	/** Creates new form ChooseGame */
	public ChooseGame(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelLogo = new javax.swing.JLabel();
        gamePanel = new javax.swing.JPanel();
        buttonTri = new javax.swing.JButton();
        buttonP3rd = new javax.swing.JButton();
        buttonMHFU = new javax.swing.JButton();
        button3G = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome to MHAG");
        setResizable(false);

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mhag/model/pic/logo.png"))); // NOI18N

        gamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Choose a Game")));

        buttonTri.setBackground(new java.awt.Color(123, 181, 210));
        buttonTri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mhag/model/pic/mhtri.png"))); // NOI18N
        buttonTri.setToolTipText("Monster Hunter Tri (Wii)");
        buttonTri.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttonTri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTriActionPerformed(evt);
            }
        });

        buttonP3rd.setBackground(new java.awt.Color(251, 219, 148));
        buttonP3rd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mhag/model/pic/mhp3rd.png"))); // NOI18N
        buttonP3rd.setToolTipText("Monster Hunter Portable 3rd (PSP/PS3)");
        buttonP3rd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttonP3rd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonP3rdActionPerformed(evt);
            }
        });

        buttonMHFU.setBackground(new java.awt.Color(182, 236, 182));
        buttonMHFU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mhag/model/pic/mhfu.png"))); // NOI18N
        buttonMHFU.setToolTipText("Monster Hunter Freedom Unite (PSP)");
        buttonMHFU.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buttonMHFU.setEnabled(false);
        buttonMHFU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMHFUActionPerformed(evt);
            }
        });

        button3G.setBackground(new java.awt.Color(204, 204, 255));
        button3G.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/mhag/model/pic/mh3g.png"))); // NOI18N
        button3G.setToolTipText("Monster Hunter Tri G (3DS)");
        button3G.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button3G.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3GActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addComponent(buttonTri, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonP3rd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addComponent(buttonMHFU, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button3G, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonTri, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonP3rd, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonMHFU, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button3G, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(labelLogo))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void buttonTriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTriActionPerformed
		gameOpt = 0;
		this.setVisible(false);
	}//GEN-LAST:event_buttonTriActionPerformed

	private void buttonP3rdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonP3rdActionPerformed
		gameOpt = 1;
		this.setVisible(false);
	}//GEN-LAST:event_buttonP3rdActionPerformed

	private void buttonMHFUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMHFUActionPerformed
		gameOpt = 2;
		this.setVisible(false);
	}//GEN-LAST:event_buttonMHFUActionPerformed

	private void button3GActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3GActionPerformed
		gameOpt = 3;
		this.setVisible(false);
	}//GEN-LAST:event_button3GActionPerformed

	public int getGameOpt() {return gameOpt;}

	/**
	 * @param args the command line arguments
	 */
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
			java.util.logging.Logger.getLogger(ChooseGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ChooseGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ChooseGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ChooseGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				int i = 0;
				ChooseGame dialog = new ChooseGame(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {

					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button3G;
    private javax.swing.JButton buttonMHFU;
    private javax.swing.JButton buttonP3rd;
    private javax.swing.JButton buttonTri;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLabel labelLogo;
    // End of variables declaration//GEN-END:variables

	private int gameOpt = 0; //  default : mh3
}
