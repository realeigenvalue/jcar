package com.realeigenvalue.jcar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import com.realeigenvalue.jcar.helper.Location;
import com.realeigenvalue.jcar.helper.Direction;
import com.realeigenvalue.jcar.helper.Tile;

public class World extends JFrame {
	private Dimension mapDimension;
	private int[][] map;
	private Dimension tileDimension;
	private ArrayList<Tile> tiles;
	private JPanel mainPanel;
	private JPanel displayPanel;
	private JPanel optionsPanel;
	private final static int OFFSETX = 0;
	private final static int OFFSETY = 37;
	private Timer timer;
	private JRadioButton rBlack, rRed, rBlue;
	private JButton startButton, stopButton, resetButton;
	private JProgressBar speedBar;
	private Car car;
	private Location carArrayLocation;
	private int startRow = 0;
	private int startColumn = 0;
	private int carStartDx;
	private int carStartDy;
	private int carStartx;
	private int carStarty;
	private final static int baseDelay = 100;
	private final static int minDelay = 50;
	private final static int maxDelay = 150;
	private final static int incrementDelay = 10;
	public World() {
		URL mapURL = this.getClass().getResource("/com/realeigenvalue/jcar/data/map.txt");
		loadMap(mapURL.getPath()); 
		carStartx = startColumn*tileDimension.width+World.OFFSETX;
		carStarty = startRow*tileDimension.height+World.OFFSETY;
		car = new Car(new Location(carStartx, carStarty), 
				      new Dimension(tileDimension.width , tileDimension.height), 
				      Color.BLACK);
		carArrayLocation = new Location(startRow, startColumn);
		car.setDx(carStartDx);
		car.setDy(carStartDy);
		createGUI();
		timer = new Timer(World.baseDelay, modelListener);
	}
	public void model() {
		ArrayList<Location> checkLocation = new ArrayList<Location>();
		ArrayList<Location> validLocation = new ArrayList<Location>();
		Direction carDirection = null;
		if(car.getdX() == 0 && car.getdY() == 1) {
			carDirection = Direction.UP;
		} else if(car.getdX() == 0 && car.getdY() == -1) {
			carDirection = Direction.DOWN;
		} else if(car.getdX() == -1 && car.getdY() == 0) {
			carDirection = Direction.LEFT;
		} else if(car.getdX() == 1 && car.getdY() == 0) {
			carDirection = Direction.RIGHT;
		}
		Location UP = new Location(carArrayLocation.x - 1, carArrayLocation.y);
		Location DOWN = new Location(carArrayLocation.x + 1, carArrayLocation.y);
		Location LEFT = new Location(carArrayLocation.x, carArrayLocation.y - 1);
		Location RIGHT = new Location(carArrayLocation.x, carArrayLocation.y + 1);
		if(carDirection == Direction.UP) {
			checkLocation.add(LEFT); // left
			checkLocation.add(UP); // up
			checkLocation.add(RIGHT); // right
		} else if(carDirection == Direction.DOWN) {
			checkLocation.add(LEFT); // left
			checkLocation.add(DOWN); // down
			checkLocation.add(RIGHT); // right
		} else if(carDirection == Direction.LEFT) {
			checkLocation.add(UP); // up
			checkLocation.add(LEFT); // left
			checkLocation.add(DOWN); // down
		} else if(carDirection == Direction.RIGHT) {
			checkLocation.add(UP); // up
			checkLocation.add(RIGHT); // right
			checkLocation.add(DOWN); // down
		}
		for(Location l : checkLocation) {
			if(map[l.x][l.y] == 2) {
				validLocation.add(l);
			}
		}
		if(validLocation.size() > 0) {
			Location choosenLocation = validLocation.get((int)(Math.random() * validLocation.size()));
			if(choosenLocation.equals(UP)) {
				car.setDx(0);
				car.setDy(1);
			} else if(choosenLocation.equals(DOWN)) {
				car.setDx(0);
				car.setDy(-1);
			} else if(choosenLocation.equals(LEFT)) {
				car.setDx(-1);
				car.setDy(0);
			} else if(choosenLocation.equals(RIGHT)) {
				car.setDx(1);
				car.setDy(0);
			}
			carArrayLocation.setLocation(choosenLocation);
			car.getLocation().x = carArrayLocation.y * tileDimension.width + World.OFFSETX;
			car.getLocation().y = carArrayLocation.x * tileDimension.height + World.OFFSETY;
			repaint();
		} else {
			carArrayLocation.x = startRow;
			carArrayLocation.y = startColumn;
			car.getLocation().x = carStartx;
			car.getLocation().y = carStarty;
			car.setDx(carStartDx);
			car.setDy(carStartDy);
		}
	}
	public void createGUI() {
		tiles = new ArrayList<Tile>();
		URL spriteURL = this.getClass().getResource("/com/realeigenvalue/jcar/sprites/");
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				Tile t = new Tile(spriteURL.getPath() + map[i][j] + ".JPG"); 
				t.setPreferredSize(tileDimension);
				tiles.add(t);
			}
		}
		mainPanel = new JPanel(new BorderLayout());
		displayPanel = new JPanel(new GridLayout(mapDimension.width, mapDimension.height));
		for(Tile t: tiles) {
			displayPanel.add(t);
		}
		optionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		rBlack = new JRadioButton("Black");
		rRed = new JRadioButton("Red");
		rBlue = new JRadioButton("Blue");
		ButtonGroup colorButtons = new ButtonGroup();
		colorButtons.add(rBlack);
		colorButtons.add(rRed);
		colorButtons.add(rBlue);
		rBlack.setSelected(true);
		rBlack.addItemListener(radioButtonListener);
		rRed.addItemListener(radioButtonListener);
		rBlue.addItemListener(radioButtonListener);
		optionsPanel.add(rBlack);
		optionsPanel.add(rRed);
		optionsPanel.add(rBlue);
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		resetButton = new JButton("Reset");
		startButton.addActionListener(buttonListener);
		stopButton.addActionListener(buttonListener);
		resetButton.addActionListener(buttonListener);
		optionsPanel.add(startButton);
		optionsPanel.add(stopButton);
		optionsPanel.add(resetButton);
		JLabel speedBarLabel = new JLabel("Animation Speed");
		speedBar = new JProgressBar(World.minDelay, World.maxDelay);
		speedBar.setStringPainted(true);
		speedBar.setValue(World.baseDelay);
		optionsPanel.add(speedBarLabel);
		optionsPanel.add(speedBar);
		mainPanel.setFocusable(true);
		mainPanel.addKeyListener(keyboardListener);
		mainPanel.add(optionsPanel, BorderLayout.LINE_END);
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		this.add(mainPanel);
		this.pack();
		this.setTitle("jcar");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public void paint(Graphics g) {
		super.paint(g);
		car.draw(g);
	}
	public void loadMap(String textFile) {
		Scanner input = null;
		try {
			input = new Scanner(new File(textFile));
			mapDimension = new Dimension(input.nextInt(), input.nextInt());
			startRow = input.nextInt();
			startColumn = input.nextInt();
			carStartDx = input.nextInt();
			carStartDy = input.nextInt();
			tileDimension = new Dimension(input.nextInt(), input.nextInt());
			map = new int[mapDimension.width][mapDimension.height];
			for(int i = 0; i < map.length; i++) {
				for(int j = 0; j < map[i].length; j++) {
					map[i][j] = input.nextInt();
				}
			}
		} catch (IOException e) {
			System.out.println("Can't load map!");
		} finally {
			input.close();
		}
	}
	// Listeners
	// Keyboard
	KeyAdapter keyboardListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			int delay = timer.getDelay();
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(timer.getDelay() > World.minDelay) 
					timer.setDelay(delay - World.incrementDelay);
					speedBar.setValue(speedBar.getValue() + World.incrementDelay);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(timer.getDelay() < World.maxDelay)
					timer.setDelay(delay + World.incrementDelay);
					speedBar.setValue(speedBar.getValue() - World.incrementDelay);
			}
		}
	};
	// Buttons
	ActionListener buttonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == startButton) {
				timer.start();
			} else if(e.getSource() == stopButton) {
				timer.stop();
			} else if(e.getSource() == resetButton) {
				timer.stop();
				carArrayLocation.x = startRow;
				carArrayLocation.y = startColumn;
				car.getLocation().x = carStartx;
				car.getLocation().y = carStarty;
				car.setDx(carStartDx);
				car.setDy(carStartDy);
				repaint();
			}
			mainPanel.requestFocusInWindow();
		}
		
	};
	// Radio Buttons
	ItemListener radioButtonListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() == rBlack) {
				car.setColor(Color.BLACK);
			} else if(e.getSource() == rRed) {
				car.setColor(Color.RED);
			} else if(e.getSource() == rBlue) {
				car.setColor(Color.BLUE);
			}
			repaint();
			mainPanel.requestFocusInWindow();
		}
	};
	// Timer
	ActionListener modelListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			model();
		}	
	};
}