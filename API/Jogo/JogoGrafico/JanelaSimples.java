package API.Jogo.JogoGrafico;

import javax.swing.*;

public class JanelaSimples {
    public JanelaSimples(String titulo, String conteudo) {
        JFrame frame = new JFrame(titulo);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(400, 300);

        frame.add(new JLabel(conteudo, SwingConstants.CENTER));

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new JanelaSimples(null, null);
    }
}