import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import mhframework.MHActor;
import mhframework.MHDataModel;
import mhframework.gui.MHGUIButton;
import mhframework.gui.MHGUIComponent;
import mhframework.gui.MHGUIComponentList;
import mhframework.tilemap.MHIsoMouseMap;
import mhframework.tilemap.MHMapCell;
import mhframework.tilemap.MHTileSetManager;

/**
 *
 */
public class IMETileSetNavigator extends MHGUIComponent //MHGUIButtonMenu
{
    public static final int NUM_TILES_SHOWN = 5;
    private static final int BUTTON_SIZE = MHIsoMouseMap.WIDTH+10;
    private static final int BUTTON_HEIGHT = (int)(MHIsoMouseMap.HEIGHT*2.5);


    private int firstTile = 0;

    private final MHGUIComponentList buttons = new MHGUIComponentList();

    private MHActor selectedTile = null;

    private int layer;

    private ActionListener actionListener;

    /**
     * Constructor for IMETileSetNavigator.
     */
    public IMETileSetNavigator()
    {
        super();

        this.setSize(BUTTON_SIZE*NUM_TILES_SHOWN, BUTTON_HEIGHT);

        MHGUIButton button;
        for (int i = 0; i < NUM_TILES_SHOWN; i++)
        {
            button = new MHGUIButton();
            button.setType(MHGUIButton.TYPE_IMAGE_BUTTON);
            button.setText(" ");
            button.setPosition(BUTTON_SIZE*i, 0);
            button.setSize(BUTTON_SIZE, BUTTON_HEIGHT);

            buttons.add(button);
        }

        setLayer(MHMapCell.FLOOR_LAYER);
        firstTile = 0;
        setSelectedTile(firstTile);
    }


    public void advance()
    {
        IMEDataModel.getInstance();
        final MHTileSetManager tsm = MHDataModel.getTileSetManager();
        Image image;
        int buttonIndex = 0;

        try {
            IMEDataModel.getInstance();
            if (firstTile + NUM_TILES_SHOWN > MHDataModel.getTileSetManager().getTileImageGroup(layer).getNumSequences())
                firstTile = 0;
        } catch (final Exception e) {}

        for (int i = firstTile; i < firstTile + NUM_TILES_SHOWN; i++)
        {
            final MHGUIButton button = (MHGUIButton) buttons.get(buttonIndex);
            image = button.getIcon();

            if (tsm != null)
            {
                image = tsm.getTileImage(layer, i, 0);
                button.setIcon(image);
            }

            buttonIndex++;
        }

    }


    public void render(final Graphics2D g)
    {
        final BufferedImage image = new BufferedImage((int)getBounds().getWidth(),
                                                (int)getBounds().getHeight(),
                                                BufferedImage.TYPE_INT_RGB);

        final Graphics2D bg = (Graphics2D) image.getGraphics();

        bg.setColor(Color.LIGHT_GRAY);
        bg.fill3DRect(0, 0, BUTTON_SIZE*NUM_TILES_SHOWN, BUTTON_HEIGHT, false);

        bg.setFont(new Font("Monospaced", Font.PLAIN, 10));

        for (int i = 0; i < buttons.getSize(); i++)
        {
            final MHGUIButton button = (MHGUIButton) buttons.get(i);
            final Image buttonIcon = button.getIcon();

            if (buttonIcon != null)
            {
                button.setPosition(i*BUTTON_SIZE, BUTTON_HEIGHT - buttonIcon.getHeight(null));
                button.render(bg);
                final int tileNumber = firstTile + i;

                bg.setColor(Color.WHITE);
                bg.drawString(tileNumber+"", i*BUTTON_SIZE+8, BUTTON_HEIGHT-4);
            }
        }

        g.drawImage(image, getX(), getY(), null);
    }


    public void scrollRight()
    {
        IMEDataModel.getInstance();
        if (firstTile + NUM_TILES_SHOWN < MHDataModel.getTileSetManager().getTileImageGroup(layer).getNumSequences())
            firstTile++;
    }


    public void scrollLeft()
    {
        if (firstTile > 0)
            firstTile--;
    }


    public void scrollRight(final int scrollAmount)
    {
        IMEDataModel.getInstance();
        if (firstTile + NUM_TILES_SHOWN + scrollAmount <
            MHDataModel.getTileSetManager().getTileImageGroup(layer).getNumSequences())
            firstTile += scrollAmount;
        else
        {
            IMEDataModel.getInstance();
            firstTile = MHDataModel.getTileSetManager().getTileImageGroup(layer).getNumSequences() - NUM_TILES_SHOWN;
        }
    }


    public void scrollLeft(final int scrollAmount)
    {
        if (firstTile - scrollAmount >= 0)
            firstTile -= scrollAmount;
        else
            firstTile = 0;
    }


    /**
     * Sets the layer.
     * @param layer The layer to set
     */
    public void setLayer(final int layer)
    {
        this.layer = layer;
    }


    public MHActor getSelectedTile()
    {
        return selectedTile;
    }


    @Override
    public void mousePressed(final MouseEvent e)
    {
        final Point p = e.getPoint();

        if (!getBounds().contains(p))
            return;

        p.x -= getX();
        p.y -= getY();

        for (int i = 0; i < buttons.getSize(); i++)
        {
            if (buttons.get(i).getBounds().contains(p))
            {
                setSelectedTile(i);

                final ActionEvent thisEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");

                if (this.actionListener != null)
                    actionListener.actionPerformed(thisEvent);
            }
        }
    }

    public void setSelectedTile(final int buttonIndex)
    {
        selectedTile = new MHActor();
        IMEDataModel.getInstance();
        selectedTile.setImageGroup(IMEDataModel.getTileSetManager().getTileImageGroup(layer));
		final int tileNum = firstTile + buttonIndex;
        selectedTile.setAnimationSequence(tileNum);
    }

    @Override
    public void mouseReleased(final MouseEvent e)
    {
    }

    @Override
    public void mouseClicked(final MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(final MouseEvent e)
    {
    }


    @Override
    public void keyPressed(final KeyEvent e)
    {
    }

    @Override
    public void keyReleased(final KeyEvent e)
    {
    }

    @Override
    public void keyTyped(final KeyEvent e)
    {
    }

    public void addActionListener(final java.awt.event.ActionListener listener)
    {
        this.actionListener = listener;
    }


    /**
     * Returns the buttons.
     * @return MHGUIComponentList
     */
    public MHGUIComponentList getButtons()
    {
        return buttons;
    }
}
