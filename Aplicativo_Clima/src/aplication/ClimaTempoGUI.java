package aplication;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ClimaTempoGUI extends JFrame {

	private JSONObject climaData;

	public ClimaTempoGUI() {
		super("Clima Tempo ");

		// Configurações da janela principal

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(450, 630);

		setLocationRelativeTo(null);

		setLayout(null);

		setResizable(false);

		addGuiComponentes();
	}

	// Adiciona os componentes da interface gráfica
	private void addGuiComponentes() {
		// Caixa de pesquisa
		JTextField caixaDePesquisa = new JTextField();
		caixaDePesquisa.setBounds(15, 15, 351, 45);
		caixaDePesquisa.setFont(new Font("Arial", Font.PLAIN, 14));
		add(caixaDePesquisa);		

		// Ícone de condição climática
		JLabel condClimIcon = new JLabel(LoadImageSol());
		condClimIcon.setBounds(0, 125, 450, 217);
		add(condClimIcon);

		// Temperatura
		JLabel temperaturaText = new JLabel("10° C");
		temperaturaText.setBounds(0, 350, 450, 54);
		temperaturaText.setFont(new Font("Arial", Font.BOLD, 50));

		temperaturaText.setHorizontalAlignment(SwingConstants.CENTER);
		add(temperaturaText);

		// Descrição do Clima
		JLabel climaText = new JLabel("Limpo");
		climaText.setBounds(0, 400, 450, 36);
		climaText.setFont(new Font("Arial", Font.BOLD, 40));
		climaText.setHorizontalAlignment(SwingConstants.CENTER);
		add(climaText);

		// Ícone de umidade
		JLabel umidadeicon = new JLabel(LoadImageHumidade());
		umidadeicon.setBounds(15, 500, 74, 66);
		add(umidadeicon);

		// Descrição da umidade
		JLabel umidadetext = new JLabel("<html><b>Umidade</b><br> 100%</html>");
		umidadetext.setBounds(90, 500, 85, 55);
		umidadetext.setFont(new Font("Arial", Font.PLAIN, 15));
		add(umidadetext);

		// Ícone de vento
		JLabel ventoIcon = new JLabel(LoadImageVento());
		ventoIcon.setBounds(220, 500, 74, 66);
		add(ventoIcon);

		// Velocidade do Vento
		JLabel ventoText = new JLabel("<html><b>Velocidade:</b><br>15 Km/h</html>");
		ventoText.setBounds(310, 500, 85, 55);
		ventoText.setFont(new Font("Arial", Font.PLAIN, 16));
		add(ventoText);

		// Botão de busca com ícone de lupa
		JButton lupaPesquisa = new JButton(LoadImageProcura());
		lupaPesquisa.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lupaPesquisa.setBounds(375, 13, 47, 45);
		
		// Ação ao clicar no botão de busca
		lupaPesquisa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userInput = caixaDePesquisa.getText();
				
				if(userInput.replaceAll("\\s", "").length() <=0) {
					return;
				}
				climaData = ClimaApp.getClimaData(userInput);
				// Atualiza a descrição do clima e o ícone correspondente
				
				String condiClima = (String) climaData.get("weather_condition");
				climaText.setText(condiClima); 

				
				switch(condiClima) {
				case "Limpo":
					condClimIcon.setIcon(LoadImageSol());
					break;
				case "Nublado":
					condClimIcon.setIcon(LoadImageClima());
					break;
				case "Chovendo":
					condClimIcon.setIcon(LoadImageChuva());
					break;
				case "Nevando":
					condClimIcon.setIcon(LoadImageNeve());
					break;
				}
				
				//condClimIcon.setText(condiClima);
				
				// Atualiza temperatura
				double temperatura = (double) climaData.get("temperature");
				temperaturaText.setText(temperatura + " °C");
				
				
				// Atualiza umidade
				long umidade = (long) climaData.get("humidity");
				umidadetext.setText("<html><b>Umidade</b><br>"+ umidade +"%</html>");
				
				
				// Atualiza velocidade do vento
				double ventos = (double) climaData.get("windspeed");
				ventoText.setText("<html><b>Ventos:</b><br>"+ ventos +" Km/h</html>");
			}
		});
		add(lupaPesquisa);
	}
	// Métodos para carregar os ícones
	
	public ImageIcon LoadImageProcura() {
		return new ImageIcon("assets/lupa.png");
	}

	public ImageIcon LoadImageClima() {
		return new ImageIcon("assets/cloudy1.png");
	}

	public ImageIcon LoadImageHumidade() {
		return new ImageIcon("assets/umidade.png");
	}

	public ImageIcon LoadImageVento() {
		return new ImageIcon("assets/vento.png");
	}
	
	public ImageIcon LoadImageSol() {
		return new ImageIcon("assets/sun1.png");
	}
	
	public ImageIcon LoadImageChuva() {
		return new ImageIcon("assets/rain1.png");
	}
	
	public ImageIcon LoadImageNeve() {
		return new ImageIcon("assets/snow1.png");
	}

}
