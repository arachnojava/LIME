import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import mhframework.MHDisplayModeChooser;
import mhframework.MHScreen;
import mhframework.gui.MHGUIButton;
import mhframework.tilemap.MHTileMap;


/**
 *
 */
public class IMEViewScreen extends MHScreen
{
	MHGUIButton exitButton;

    public IMEViewScreen()
    {
        super();

        exitButton = new MHGUIButton();
        exitButton.setText("Done");
        exitButton.setSize(48, 24);
        exitButton.addActionListener(this);
        final int px = MHDisplayModeChooser.getScreenSize().width - (int)exitButton.getBounds().getWidth() - 10,
            py = MHDisplayModeChooser.getScreenSize().height - (int)exitButton.getBounds().getHeight() - 10;
        exitButton.setPosition(px, py);

        add(exitButton);
    }


    @Override
    public void load()
    {
    	IMEDataModel.getInstance().map.setScreenSpace(0, 0, MHDisplayModeChooser.getScreenSize().width, MHDisplayModeChooser.getScreenSize().height);
        setFinished(false);
    }


    @Override
    public void unload()
    {
    }


    @Override
    public void render(final Graphics2D g)
    {
        fill(g, Color.BLACK);

        IMEDataModel.getInstance().map.render(g);

        super.render(g);
    }


    @Override
    public void advance()
    {
    	IMEDataModel.getInstance().map.advance();
    }


    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == exitButton)
        {
			setNextScreen(null);
        	setFinished(true);
        }
    }


    @Override
    public void keyPressed(final KeyEvent e)
    {
        final MHTileMap map = IMEDataModel.getInstance().map;

    	switch (e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
            	map.scrollMap(16, 0);
                break;
            case KeyEvent.VK_LEFT:
                map.scrollMap(-16, 0);
                break;
            case KeyEvent.VK_UP:
                map.scrollMap(0, -16);
                break;
            case KeyEvent.VK_DOWN:
                map.scrollMap(0, 16);
                break;
            case KeyEvent.VK_ESCAPE:
    			setNextScreen(null);
            	setFinished(true);
            	break;

        }
    }


    @Override
    public void mouseMoved(final MouseEvent e)
    {
        super.mouseMoved(e);

        IMEDataModel.getInstance().map.mouseMoved(e);
    }
}
