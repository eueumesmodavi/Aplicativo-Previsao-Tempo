package aplication;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ClimaTempoGUI extends JFrame{
	public ClimaTempoGUI() {
		super("Clima | Tempo ");
		
		//Comandos Menu | Dimensões
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(450, 630);
		
		setLocationRelativeTo(null);
		
		setLayout(null);
		
		setResizable(false);
		
		addGuiComponentes();
	}
	
	//Class para retornar os icons
	
	//Lupa
	public ImageIcon LoadImageProcura() {
	    return new ImageIcon("assets/iconeprocurar.png");
	}
	
	
	//Clima Nublado
	public ImageIcon LoadImageClima() {
	    return new ImageIcon("assets/nublado.png");
	}
	
	//Umidade
	public ImageIcon LoadImageHumidade() {
	    return new ImageIcon("assets/gota.png");
	}
	
	//Vento
	public ImageIcon LoadImageVento() {
	    return new ImageIcon("assets/vento.png");
	}
	

	
	//Configurações da caixa de pesquisa + escolha da fonte e tamanho
	private void addGuiComponentes() {
		JTextField caixaDePesquisa = new JTextField();
		
		caixaDePesquisa.setBounds(15, 15, 351, 45);
		
		caixaDePesquisa.setFont(new Font("Arial", Font.PLAIN,14));
		
		add(caixaDePesquisa);
	

	
	//Adicionando butão de procura junto com o icone do mesmo	
		JButton lupaPesquisa = new JButton(LoadImageProcura());
		
		//Configurando o cursor
		lupaPesquisa.setCursor(new Cursor(Cursor.HAND_CURSOR));

		lupaPesquisa.setBounds(370, 15, 45, 45);
		add(lupaPesquisa);

		//Condições Climaticas Icone
		JLabel condClimIcon = new JLabel(LoadImageClima());
		condClimIcon.setBounds(0,125, 450, 217);
		add(condClimIcon);
		
		//Temperatura
		JLabel temperaturaText = new JLabel("10° C");
		temperaturaText.setBounds(0, 350, 450, 54);
		temperaturaText.setFont(new Font("Arial", Font.BOLD, 50));
		
		temperaturaText.setHorizontalAlignment(SwingConstants.CENTER);
		add(temperaturaText);
		
		//Descrição do Clima
		JLabel climaText = new JLabel("Nublado");
		climaText.setBounds(0, 400, 450, 36);
		climaText.setFont(new Font("Arial", Font.BOLD, 40));
		climaText.setHorizontalAlignment(SwingConstants.CENTER);
		add(climaText);
	
		//Umidade Icone
		JLabel umidadeicon = new JLabel(LoadImageHumidade());
		umidadeicon.setBounds(15, 500, 74, 66);
		add(umidadeicon);
	
		//Descrição da Umidade
		JLabel umidadetext = new JLabel("<html><b>Umidade</b><br>100%</html>");
		umidadetext.setBounds(90, 500, 85, 55);
		umidadetext.setFont(new Font("Arial", Font.PLAIN, 15));
		add(umidadetext);
		
		//Vento Icone
		JLabel ventoIcon = new JLabel(LoadImageVento());
		ventoIcon.setBounds(220, 500, 74, 66);
		add(ventoIcon);
		
		//Velocidade dos Ventos
		JLabel ventoText = new JLabel("<html><b>Velocidade:</b><br>15Km/h</html>");
		ventoText.setBounds(310, 500, 85, 55);
		ventoText.setFont(new Font("Arial", Font.PLAIN, 15));
		add(ventoText);		
	}
}
