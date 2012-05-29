/**
 * ---------------------------------------------------------------------------
 * File name: LaserTutor.java<br/>
 * Project name: TypingTutor<br/>
 * ---------------------------------------------------------------------------
 * Creator's name and email: Matthew Paul, paulmr@goldmail.etsu.edu<br/>
 * Course:  CSCI 1260-088<br/>
 * Creation Date: Apr 19, 2012<br/>
 * Date of Last Modification: Apr 19, 2012
 * ---------------------------------------------------------------------------
 */

package laserDefense;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import core.FileOperator;
import core.State;
import core.Tutor;
import core.Word;
import core.WordPool;
import front.Gui;
import front.ImageHotSpot;


/**
 * A specialized game class that does a missile command like typing game.<br>
 *
 * <hr>
 * Date created: Apr 19, 2012<br>
 * Date last modified: Apr 19, 2012<br>
 * <hr>
 * @author Matthew Paul
 */
public class LaserTutor extends Tutor
{
	
//////////////////
//
//FIELDS
//
//////////////////
	
	
//	buffered images for the different images used in the game
	BufferedImage backgroundImage,
				  crosshairImage,
				  gameOverImage,
				  winImage,
				  titleImage,
				  lives3Image,
				  lives2Image,
				  lives1Image,
				  threeImage,
				  twoImage,
				  oneImage,
				  goImage;
	
//	files used for the images
	File backgroundFile = new File("resources/laserBackground.gif");
	File crosshairFile = new File("resources/laserCrossHairs.gif");
	File gameOverFile = new File("resources/gameOverScreen.gif");
	File winFile = new File("resources/winScreen.gif");
	File titleFile = new File("resources/titleScreen.gif");
	
	File lives3File = new File("resources/shield3Lives.gif");
	File lives2File = new File("resources/shield2Lives.gif");
	File lives1File = new File("resources/shield1Life.gif");
	
	File threeFile = new File ("resources/3.gif");
	File twoFile = new File ("resources/2.gif");
	File oneFile = new File ("resources/1.gif");
	File goFile = new File ("resources/go.gif");
	
//	formatter used for the score
	DecimalFormat f = new DecimalFormat("#,##0");
	
	ImageHotSpot playButton;
	File playButtonFile = new File("resources/playButton.gif");
	

//////////////////
//
//CONSTRUCTORS
//
//////////////////
	
	
	/**
	 * Constructor <br>        
	 *
	 * <hr>
	 * Date created: Apr 19, 2012 <br>
	 * Date last modified: Apr 19, 2012 <br>
	 *
	 * <hr>
	 * @param wordsOnScreen
	 * @param acceptableWords
	 * @param clearedWords
	 * @param livesLeft
	 * @param errors
	 * @param buffer
	 */
	public LaserTutor(ArrayList <LaserWord> wordsOnScreen,
						ArrayList <LaserWord> acceptableWords,
						ArrayList <LaserWord> clearedWords, int livesLeft,
						int errors, StringBuffer buffer)
	{
//		create the array lists used
		this.wordsOnScreen = new ArrayList<Word>(wordsOnScreen);
		this.acceptableWords = new ArrayList<Word>(acceptableWords);
		this.clearedWords = new ArrayList<Word>(clearedWords);
		
//		set the livesLeft and error
		this.livesLeft = livesLeft;
		this.errors = errors;
		
//		set the buffer
		this.buffer = new StringBuffer(buffer);
		
//		create a file operator to read in the text file
		FileOperator file = new FileOperator( );
		
		String [ ] lines = null;
		
		try
		{
			lines = file.read(FileOperator.WORD_FILE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
//		create the pool from the words
		this.pool = createWordPool(lines);
		
//		set the file operator to be cleaned up
		file = null;
		
//		load the images needed
		try
		{
			loadImages( );
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage( ));
		}
		
		playButton = new ImageHotSpot(138, 156, playButtonFile);
	}
	
	public LaserTutor(File wordFile)
	{
//		create the array lists used
		this.wordsOnScreen = new ArrayList<Word>();
		this.acceptableWords = new ArrayList<Word>();
		this.clearedWords = new ArrayList<Word>();
		
//		set the livesLeft and error
		this.livesLeft = DEFAULT_LIVES_LEFT;
		this.errors = DEFAULT_ERRORS;
		
//		set the buffer
		this.buffer = new StringBuffer("");
		
//		create a file operator to read in the text file
		FileOperator file = new FileOperator( );
		
		
		String [ ] lines = null;
		
		try
		{
			file.setWordFile(wordFile);
			lines = file.read(FileOperator.WORD_FILE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
//		create the pool from the words
		this.pool = createWordPool(lines);
		
//		set the file operator to be cleaned up
		file = null;
		
//		load the images needed
		try
		{
			loadImages( );
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage( ));
		}
		
		playButton = new ImageHotSpot(138, 156, playButtonFile);
	}

	/**
	 * Constructor <br>        
	 *
	 * <hr>
	 * Date created: Apr 19, 2012 <br>
	 * Date last modified: Apr 19, 2012 <br>
	 *
	 * <hr>
	 * @param array
	 */
	public LaserTutor(String [ ] array)
	{
		super(array);
		playButton = new ImageHotSpot(138, 156, playButtonFile);
	}

	/**
	 * Constructor <br>        
	 *
	 * <hr>
	 * Date created: Apr 19, 2012 <br>
	 * Date last modified: Apr 19, 2012 <br>
	 *
	 * <hr>
	 */
	public LaserTutor()
	{
		try
		{
			loadImages( );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		playButton = new ImageHotSpot(138, 156, playButtonFile);
	}
	
	
//////////////////
//
//METHODS
//
//////////////////
	
	
	/**
	 * Creates a word pool from an array of strings. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param lines String array of words
	 * @return WordPool containing the words
	 * @see core.Tutor#createWordPool(java.lang.String[])
	 */
	@Override
	protected WordPool createWordPool(String [ ] lines)
	{
//		create array of laser words
		LaserWord[] words = new LaserWord[lines.length];
		
//		initialize random variable 
		Random r = new Random();
		
//		create a new laserWord with a random x-location
		for (int c = 0; c < words.length; c++)
		{
			words[c] = new LaserWord(lines[c], r.nextInt(530) + 35, 0);
		}
		
//		return the wordpool
		return new WordPool(words);
	}
	
	/**
	 * Draws the normal frames of the game. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 * @see core.Tutor#drawGame(java.awt.Graphics)
	 */
	@Override
	public void drawGame(Graphics g)
	{
//		draw the background
		g.drawImage(backgroundImage, 0, 0, Gui.WIDTH, Gui.HEIGHT, null);
		
//		draw the HUD
		drawHUD(g);
		
//		draw the lives
		drawLives(g);
		
//		draw the words
		for (int c = 0; c < wordsOnScreen.size( ); c++)
		{
			wordsOnScreen.get(c).drawWord(g);
		}
		
		
//		draw the crosshairs
		drawCrosshairs(g);
		
//		if a word needs to be shot, shoot it
		if (killedWord != null)
			drawKillShot(g);
	}

	/**
	 * Draw the shield that protects the city <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 */
	private void drawLives(Graphics g)
	{
		switch(livesLeft)
		{
			case 3:
				g.drawImage(lives3Image, 0, 295, 600, 200, null);
				break;
			case 2:
				g.drawImage(lives2Image, 0, 295, 600, 200, null);
				break;
			case 1:
				g.drawImage(lives1Image, 0, 295, 600, 200, null);
				break;
//			it should never reach the default case
			default:
				System.out.println("Error! Default case reached in drawLives()");
		}
	}

	/**
	 * Draw the HUD for the game <br>        
	 *
	 * <hr>
	 * Date created: Apr 21, 2012 <br>
	 * Date last modified: Apr 21, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 */
	protected void drawHUD(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.drawString("Score: " + f.format(score), 10, Gui.HEIGHT / 30);
		g.drawString("Errors: " + errors, 10, Gui.HEIGHT / 30 + 13);
		g.drawString("Aliens Defeated: " + clearedWords.size( ), 10, Gui.HEIGHT / 30 + 26);
		g.drawString("WPM: " + wpm, 10, Gui.HEIGHT / 30 + 39);
	}
	
	/**
	 * Draws a line from the base to the word to be killed. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 */
	private void drawKillShot(Graphics g)
	{
		g.setColor(Color.ORANGE);
		
		g.drawLine(288, 442, killedWord.getLocX( ), killedWord.getLocY( ));
		
//		remove the killedWord to stop drawing the line to it
		killedWord = null;
	}

	/**
	 * Draw the crosshairs around the acceptable words. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 */
	private void drawCrosshairs(Graphics g)
	{
		Word word;
		
		for (int c = 0; c < acceptableWords.size( ); c++)
		{
			word = acceptableWords.get(c);
						
			g.drawImage(crosshairImage, word.getLocX( ) - crosshairImage.getWidth( ) / 2, word.getLocY( ) - crosshairImage.getHeight( ) / 2, null);
		}
	}
	
	/**
	 * Displays the game over screen. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 * @see core.Tutor#drawGameOver(java.awt.Graphics)
	 */
	@Override
	public void drawGameOver(Graphics g)
	{
		
		g.drawImage(gameOverImage, 0, 0, null);
		drawHUD(g);
	}
	
	/**
	 * Displays the win screen. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @param g the Graphics object to be drawn to.
	 * @see core.Tutor#drawWin(java.awt.Graphics)
	 */
	@Override
	public void drawWin(Graphics g)
	{
		g.drawImage(winImage, 0, 0, null);
		drawHUD(g);
	}
	
	/**
	 * Draws the countdown timer <br>        
	 *
	 * <hr>
	 * Date created: May 9, 2012 <br>
	 * Date last modified: May 9, 2012 <br>
	 *
	 * <hr>
	 * @param g
	 * @see core.Tutor#drawCountDown(java.awt.Graphics)
	 */
	@Override
	public void drawCountDown(Graphics g)
	{
		g.drawImage(backgroundImage, 0, 0, Gui.WIDTH, Gui.HEIGHT, null);
		frameCount++;

		if (frameCount < 60)
			g.drawImage(threeImage, 250, 375, null);
		else if (frameCount < 120)
			g.drawImage(twoImage, 250, 375, null);
		else if (frameCount < 180)
			g.drawImage(oneImage, 250, 375, null);
		else if (frameCount < 240)
			g.drawImage(goImage, 250, 375, null);
		else
		{
			countdown = true;
			frameCount = 0;
			gameState = State.LOOP;
		}
	}
	
	/**
	 * Draws the title screen. <br>        
	 *
	 * <hr>
	 * Date created: May 9, 2012 <br>
	 * Date last modified: May 9, 2012 <br>
	 *
	 * <hr>
	 * @param g
	 * @see core.Tutor#drawTitleScreen(java.awt.Graphics)
	 */
	@Override
	public void drawTitleScreen(Graphics g)
	{
		g.drawImage(titleImage, 0, 0, null);
		playButton.drawHotSpot(g);
	}
	
	/**
	 * Processes the mouse event. <br>        
	 *
	 * <hr>
	 * Date created: May 9, 2012 <br>
	 * Date last modified: May 9, 2012 <br>
	 *
	 * <hr>
	 * @param e MouseEvent to be checked
	 * @see core.Tutor#processMouse(java.awt.event.MouseEvent)
	 */
	@Override
	public void processMouse(MouseEvent e)
	{
		if (gameState == State.TITLE)
		{
			if (playButton.isClicked(e))
				gameState = State.COUNTDOWN;
		}
	}
	
	/**
	 * Loads all the necessary images. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadImages() throws IOException
	{
			loadBackgroundImage();
			loadCrosshairImage();
			loadGameOverImage();
			loadWinImage();
			loadTitleImage();
			loadCountdownImages();
			loadShieldImages();
	}
	
	private void loadCountdownImages() throws IOException
	{
		threeImage = ImageIO.read(threeFile);
		twoImage = ImageIO.read(twoFile);
		oneImage = ImageIO.read(oneFile);
		goImage = ImageIO.read(goFile);
	}

	/**
	 * Loads the title image into memory. <br>        
	 *
	 * <hr>
	 * Date created: May 9, 2012 <br>
	 * Date last modified: May 9, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadTitleImage() throws IOException
	{
		titleImage = ImageIO.read(titleFile);
	}

	/**
	 * Loads the shield images <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadShieldImages() throws IOException 
	{
		lives3Image = ImageIO.read(lives3File);
		lives2Image = ImageIO.read(lives2File);
		lives1Image = ImageIO.read(lives1File);
	}

	/**
	 * Loads the win image <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadWinImage() throws IOException
	{
		winImage = ImageIO.read(winFile);
	}

	/**
	 * Loads the crosshair image <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadCrosshairImage() throws IOException
	{
		crosshairImage = ImageIO.read(crosshairFile);
	}
	
	/**
	 * Loads the background image. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadBackgroundImage() throws IOException
	{
		backgroundImage = ImageIO.read(backgroundFile);
	}
	
	/**
	 * Loads the game over image. <br>        
	 *
	 * <hr>
	 * Date created: Apr 24, 2012 <br>
	 * Date last modified: Apr 24, 2012 <br>
	 *
	 * <hr>
	 * @throws IOException
	 */
	private void loadGameOverImage() throws IOException
	{
		gameOverImage = ImageIO.read(gameOverFile);
	}
	


}
