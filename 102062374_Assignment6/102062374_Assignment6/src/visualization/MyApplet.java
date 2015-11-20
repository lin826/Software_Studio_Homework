package visualization;

import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

import processing.core.*;

public class MyApplet extends PApplet {

	private static final int DIV = 1000;
	private static final int initialTime = 0;
	/**
	 * Declare your variables here so that all the method can access.
	 */
	private PImage worldMapImage;
	private MercatorMap mercatorMap;

	boolean mouseEntered, mousePressed;
	private HashMap<String, PVector> place = new HashMap<String, PVector>();
	private HashMap<String, Country> countryList = new HashMap<String, Country>();

	Reader reader;
	// The X-coordinate and Y-coordinate of the last click.
	int xpos;
	int ypos;
	private float time =initialTime;
	private String selectedCountry;
	private float partx;
	private float party;
	private int lineWidth;

	/**
	 * Initiate variables once in setup().
	 */
	public void setup() {

		smooth();

		// A safer way to read the target image, by using getPath().
		// Note that the path is different from "../res/WorldMap.jpeg".
		// worldMapImage =
		// loadImage(this.getClass().getResource("res/WorldMap.jpeg").getPath());
		worldMapImage = loadImage(this.getClass()
				.getResource("/res/WorldMap.jpeg").getPath());
		size(worldMapImage.width, worldMapImage.height);

		// Be careful of the parameters for MercatorMap constructor.
		// Need to be exactly the same size of the Map image.
		mercatorMap = new MercatorMap(worldMapImage.width, worldMapImage.height);

		addMouseListener(this);

		try {
			FileReader reader = new FileReader(this.getClass()
					.getResource("/res/MigrationData.csv").getPath());

			CSVReader<String[]> csvPersonReader = CSVReaderBuilder
					.newDefaultReader(reader);
			List<String[]> country = csvPersonReader.readAll();
			String[] strings = country.get(0);
			this.countryList.put(strings[0], new Country(strings[0]));
			PVector p = mercatorMap.getScreenLocation(new PVector(Float
					.parseFloat(strings[1]), Float.parseFloat(strings[2])));
			this.place.put(strings[0], p);

			for (int i = 1; i < country.size(); i++) { // put immigrate
				if (!countryList.containsKey(country.get(i)[0])) {
					strings = country.get(i);
					this.countryList.put(strings[0], new Country(strings[0]));
					p = mercatorMap.getScreenLocation(new PVector(Float
							.parseFloat(strings[1]), Float
							.parseFloat(strings[2])));
					this.place.put(strings[0], p);
					// System.out.println("i = "+i+"  new country = "+strings[0]);

					this.countryList.put(country.get(i)[0], new Country(
							(country.get(i))[0]));
				}
				countryList.get(country.get(i)[0]).putEmigration(
						(country.get(i))[3], (country.get(i))[6]);
			}
			for (int i = 1; i < country.size(); i++) { // put emigration

				countryList.get(country.get(i)[3]).putImmigrate(
						(country.get(i))[0], (country.get(i))[6]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * image(worldMapImage, 0, 0, width, height);
		 * 
		 * Set<String> s = this.place.keySet(); for (String name : s) { //
		 * System.out.println("name = "+name); drawCountry(name,
		 * place.get(name)); name = s.iterator().next(); }
		 */
	}

	/**
	 * Draw anything you want here. You can also create many methods for drawing
	 * different objects. In that way, it is more convenient to manage your
	 * program.
	 */
	public void draw() {

		image(worldMapImage, 0, 0, width, height);

		Set<String> s = this.place.keySet();
		for (String name : s) {
			// System.out.println("name = "+name);
			drawCountry(name, place.get(name));
			name = s.iterator().next();
			try {
				fill(0, 97, 48, 200);
				stroke(0,0);
				textAlign(CENTER);
				text(selectedCountry, xpos, ypos-10);
				if (!mousePressed) {
					drawEmigration(selectedCountry);
				} else {
					drawImmigrate(selectedCountry);
				}
				if (time <= DIV)
					time++;
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * A method in charge of drawing countries
	 */
	public void drawCountry(String name, PVector point) {

		// Choose the color you want to use before you draw anything, just like
		// painting in real life
		Country thisCountry = countryList.get(name);
		int size = thisCountry.getPeople();
		if (size < 0)
			size *= -1;
		if(name.equals(selectedCountry))
		{
			fill(0, 97, 48, 200);
			stroke(0, 36, 18, 50);
		}
		else
		{
		fill(133, 194, 255, 200);
		stroke(23, 114, 232, 50);
		}
		ellipse(point.x, point.y, 3 * (int) Math.pow(size, (1 / 9.0)),
				3 * (int) Math.pow(size, (1 / 9.0)));
		
		

	}

	/**
	 * A method in charge of drawing connections
	 */
	public void drawConnection(PVector source, PVector target) {
		
		// Choose the color you want to use before you draw anything, just like
		// painting in real life
		if (mousePressed) {
			PVector temp = source;
			source = target;
			target = temp;
			stroke(255, 0, 0, lineWidth+10);
		}
		else
			stroke(168, 255, 168, lineWidth+10);
		partx = (target.x - source.x) / DIV;
		party = (target.y - source.y) / DIV;
		line(source.x, source.y, source.x + partx * time, source.y + party
				* time);

	}

	/*
	 * These methods always have to present when you implement MouseListener
	 * 
	 * public void mouseClicked (MouseEvent me) {} public void mouseEntered
	 * (MouseEvent me) {} public void mousePressed (MouseEvent me) {} public
	 * void mouseReleased (MouseEvent me) {} public void mouseExited (MouseEvent
	 * me) {}
	 */

	// This is called when the mouse has been pressed
	public void mousePressed(MouseEvent me) {
		// Save the coordinates of the click like this.
		xpos = me.getX();
		ypos = me.getY();
		mousePressed = true;
		checkSelectedCountry(me);

		System.out.print("Pressed: " + xpos + "  " + ypos + "\n");

		repaint();
	}

	

	// When it has been released
	// not that a click also calls these Mouse-Pressed and Released.
	// since they are empty nothing happens here.
	public void mouseReleased(MouseEvent me) {
		xpos = me.getX();
		ypos = me.getY();

		mousePressed = false;
		checkSelectedCountry(me);

		System.out.print("Released: " + xpos + "  " + ypos + "\n");

		repaint();

	}

	@Override
	public void mouseMoved(MouseEvent me) {
		// Save the coordinates of the click like this.
		xpos = me.getX();
		ypos = me.getY();
		checkSelectedCountry(me);
		System.out.print("Hovered: " + xpos + "  " + ypos + "\n");

		repaint();
	}
	
	private void checkSelectedCountry(MouseEvent me) {
		
		time = initialTime;
		selectedCountry = null;

		Set<String> s = this.place.keySet();
		for (String name : s) {
			PVector p = place.get(name);
			if (xpos - p.x < 3 && p.x - xpos < 3 && ypos - p.y < 3
					&& p.y - ypos < 3) {
				selectedCountry = name;
			}
		}
		
	}

	private void drawEmigration(String name) {
		Country c = countryList.get(name);
		for (String out : c.getEmigrationName()) {
			int amount = c.getEmigration(out);
			if (amount != 0) {
				drawConnection(place.get(name), place.get(out));
				lineWidth = c.getEmigration(out)/50;
			}
		}

	}

	private void drawImmigrate(String name) {
		Country c = countryList.get(name);
		for (String out : c.getImmigrateName()) {
			int amount = c.getImmigrate(out);
			if (amount != 0) {
				drawConnection(place.get(name), place.get(out));
				lineWidth = c.getImmigrate(out)/50;
			}
		}

	}

}
