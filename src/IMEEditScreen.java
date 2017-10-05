import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import mhframework.MHActor;
import mhframework.MHDisplayModeChooser;
import mhframework.MHScreen;
import mhframework.gui.MHGUIButton;
import mhframework.gui.MHGUIButtonMenu;
import mhframework.gui.MHGUILabel;
import mhframework.tilemap.MHMapCell;
import mhframework.tilemap.MHMapCellAddress;
import mhframework.tilemap.MHTileSetManager;

/**
 *
 */
public class IMEEditScreen extends MHScreen
{
    private final IMETileSetNavigator navigator = new IMETileSetNavigator();

    protected static MHGUIButtonMenu layerViewMenu;
    protected static MHGUIButton viewFloorDetailButton;
    protected static MHGUIButton viewItemsButton;
    protected static MHGUIButton viewObstaclesButton;
    protected static MHGUIButton viewWallsButton;
    protected static MHGUIButton viewDetailsButton;
    protected static MHGUIButton viewCeilingsButton;

    protected static MHGUIButtonMenu layerEditMenu;
    protected static MHGUIButton editFloorButton;
    protected static MHGUIButton editFloorDetailButton;
    protected static MHGUIButton editObstaclesButton;
    protected static MHGUIButton editItemsButton;
    protected static MHGUIButton editWallsButton;
    protected static MHGUIButton editDetailsButton;
    protected static MHGUIButton editCeilingsButton;

    protected static MHGUIButtonMenu toolsMenu;
    protected static MHGUIButton toolsEraseButton;
    protected static MHGUIButton toolsPickButton;


    protected static MHGUIButton exitButton;

    protected static MHGUIButton tsBackButton;
    protected static MHGUIButton tsForwardButton;


    protected static MHGUILabel tileSetLabel;
    protected static MHGUILabel selectedLabel;

    private boolean painting = false;
    private boolean fastScrolling = false;
    private boolean erasing = false;
    private boolean pickTool = false;

    private final boolean[] layerFlags = new boolean[MHMapCell.NUM_LAYERS];

    private int selectedLayer = MHMapCell.FLOOR_LAYER;

    private MHActor selectedTile = null;

    /****************************************************************
     */
    public IMEEditScreen()
    {
        super();

        navigator.setPosition(25, MHDisplayModeChooser.getScreenSize().height-170);
        navigator.setLayer(MHMapCell.FLOOR_LAYER);
        navigator.addActionListener(this);
        this.add(navigator);

        selectedTile = navigator.getSelectedTile();

        layerViewMenu = new MHGUIButtonMenu();
        layerViewMenu.setText("View");
        layerViewMenu.setBackgroundColor(Color.LIGHT_GRAY);
        layerViewMenu.setTextColor(Color.BLACK);
        layerViewMenu.setButtonSize(60, 20);
        layerViewMenu.setBorderSpacing(2);

        viewFloorDetailButton = new MHGUIButton();
        viewFloorDetailButton.setText("F.Details");
        viewFloorDetailButton.addActionListener(this);

        viewItemsButton = new MHGUIButton();
        viewItemsButton.setText("Items");
        viewItemsButton.addActionListener(this);

        viewObstaclesButton = new MHGUIButton();
        viewObstaclesButton.setText("Obstacles");
        viewObstaclesButton.addActionListener(this);

        viewWallsButton  = new MHGUIButton();
        viewWallsButton.addActionListener(this);
        viewWallsButton.setText("Walls");

        viewDetailsButton  = new MHGUIButton();
        viewDetailsButton.addActionListener(this);
        viewDetailsButton.setText("W.Details");

        viewCeilingsButton  = new MHGUIButton();
        viewCeilingsButton.addActionListener(this);
        viewCeilingsButton.setText("Ceilings");

        layerViewMenu.add(viewCeilingsButton);
        layerViewMenu.add(viewDetailsButton);
        layerViewMenu.add(viewWallsButton);
        layerViewMenu.add(viewObstaclesButton);
        layerViewMenu.add(viewItemsButton);
        layerViewMenu.add(viewFloorDetailButton);

        layerViewMenu.setPosition(MHDisplayModeChooser.getScreenSize().width - (int)layerViewMenu.getBounds().getWidth(), 0);


        layerEditMenu = new MHGUIButtonMenu();
        layerEditMenu.setText("Edit");
        layerEditMenu.setBackgroundColor(Color.LIGHT_GRAY);
        layerEditMenu.setTextColor(Color.BLACK);
        layerEditMenu.setButtonSize(60, 20);
        layerEditMenu.setBorderSpacing(2);

        editFloorButton = new MHGUIButton();
        editFloorButton.setText("Floor");
        editFloorButton.addActionListener(this);

        editFloorDetailButton = new MHGUIButton();
        editFloorDetailButton.setText("F.Details");
        editFloorDetailButton.addActionListener(this);

        editItemsButton = new MHGUIButton();
        editItemsButton.setText("Items");
        editItemsButton.addActionListener(this);

        editObstaclesButton = new MHGUIButton();
        editObstaclesButton.setText("Obstacles");
        editObstaclesButton.addActionListener(this);

        editWallsButton  = new MHGUIButton();
        editWallsButton.addActionListener(this);
        editWallsButton.setText("Walls");

        editDetailsButton  = new MHGUIButton();
        editDetailsButton.addActionListener(this);
        editDetailsButton.setText("W.Details");

        editCeilingsButton  = new MHGUIButton();
        editCeilingsButton.addActionListener(this);
        editCeilingsButton.setText("Ceilings");

        layerEditMenu.add(editCeilingsButton);
        layerEditMenu.add(editDetailsButton);
        layerEditMenu.add(editWallsButton);
        layerEditMenu.add(editObstaclesButton);
        layerEditMenu.add(editItemsButton);
        layerEditMenu.add(editFloorDetailButton);
        layerEditMenu.add(editFloorButton);

        layerEditMenu.setPosition(layerViewMenu.getX(),
                                  layerViewMenu.getY()+(int)layerViewMenu.getBounds().getHeight()+2);

        toolsMenu = new MHGUIButtonMenu();
        toolsMenu.setText("Tools");
        toolsMenu.setBackgroundColor(Color.LIGHT_GRAY);
        toolsMenu.setTextColor(Color.BLACK);
        toolsMenu.setButtonSize(60, 20);
        toolsMenu.setBorderSpacing(2);

        toolsEraseButton = new MHGUIButton();
        toolsEraseButton.setText("Erase");
        toolsEraseButton.addActionListener(this);

        toolsPickButton = new MHGUIButton();
        toolsPickButton.setText("Pick");
        toolsPickButton.addActionListener(this);

        toolsMenu.add(toolsEraseButton);
        toolsMenu.add(toolsPickButton);

        toolsMenu.setPosition(layerEditMenu.getX(),
                                  layerEditMenu.getY()+(int)layerEditMenu.getBounds().getHeight()+2);

        exitButton  = new MHGUIButton();
        exitButton.addActionListener(this);
        exitButton.setText("Done");
        exitButton.setSize(60, 60);
        exitButton.setPosition(toolsMenu.getX() + 2,
                        (int)(MHDisplayModeChooser.getScreenSize().height - exitButton.getBounds().getHeight() - 2));

        tsBackButton = new MHGUIButton();
        tsBackButton.addActionListener(this);
        tsBackButton.setText("<");
        tsBackButton.setSize(10, navigator.getHeight());
        tsBackButton.setPosition(navigator.getX()-10, navigator.getY());

        tsForwardButton = new MHGUIButton();
        tsForwardButton.addActionListener(this);
        tsForwardButton.setText(">");
        tsForwardButton.setSize(10, navigator.getHeight());
        tsForwardButton.setPosition(navigator.getX()+navigator.getWidth(), navigator.getY());

        tileSetLabel = new MHGUILabel("Tile Set " + IMEDataModel.getInstance().map.getMapData().getTileSetId()+":");

        selectedLabel = new MHGUILabel("Selected Tile:");


        add(layerViewMenu);
        add(layerEditMenu);
        add(toolsMenu);
        add(exitButton);
        add(tileSetLabel);
        add(tsBackButton);
        add(tsForwardButton);
        add(selectedLabel);

        for (int i = 0; i < layerFlags.length; i++)
            layerFlags[i] = true;

        load();
    }


    @Override
    public void load()
    {
        IMEDataModel.getInstance().map.setScreenSpace(0, 0, MHDisplayModeChooser.getScreenSize().width -
                                        (int)layerViewMenu.getBounds().getWidth(), MHDisplayModeChooser.getScreenSize().height - 200);

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

            // Draw map here
            (IMEDataModel.getInstance()).map.render(g, layerFlags);

			if (erasing || pickTool)
			{
                final StringBuffer sb = new StringBuffer();
                final Point p = IMEDataModel.getInstance().map.getCursorPoint();

				if (erasing) sb.append("ERASE ");
				else sb.append("PICK ");

                if (this.selectedLayer == MHMapCell.FLOOR_LAYER)
                	sb.append("FLOOR TILE");
                if (this.selectedLayer == MHMapCell.FLOOR_DETAIL_LAYER)
                    sb.append("FLOOR DETAIL");
                if (this.selectedLayer == MHMapCell.ITEM_LAYER)
                	sb.append("ITEM");
                if (this.selectedLayer == MHMapCell.OBSTACLE_LAYER)
                    sb.append("OBSTACLE");
                if (this.selectedLayer == MHMapCell.WALL_LAYER)
                	sb.append("WALL TILE");
                if (this.selectedLayer == MHMapCell.WALL_DETAIL_LAYER)
                    sb.append("WALL DETAIL");
                if (this.selectedLayer == MHMapCell.CEILING_LAYER)
                	sb.append("CEILING TILE");

                g.drawString(sb.toString(), p.x, p.y + 20);

			}

            // Draw map interface stuff here
            g.setColor(Color.LIGHT_GRAY);
            // Bottom (tile set) panel
            g.fillRect(0, (int)IMEDataModel.getInstance().map.getScreenSpace().getHeight(), MHDisplayModeChooser.getScreenSize().width - (int)layerViewMenu.getBounds().getWidth(), MHDisplayModeChooser.getScreenSize().height);
            // Right (menu) panel
            g.fillRect(MHDisplayModeChooser.getScreenSize().width - (int)layerViewMenu.getBounds().getWidth(), 0,
                            (int)layerViewMenu.getBounds().getWidth(), MHDisplayModeChooser.getScreenSize().height);

            final Rectangle2D bounds = IMEDataModel.getInstance().map.getScreenSpace();
            g.setColor(Color.BLUE);
            g.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());

            // Tile set navigator
            tileSetLabel.setPosition(10, (int)bounds.getHeight() + 20);
            navigator.render(g);

            // Selected Tile:
            final int stx = IMEEditScreen.tsForwardButton.getX() + tsForwardButton.getWidth() + 10;  // selected tile x
            final int sty = navigator.getY();
            final int stw = navigator.getWidth()/IMETileSetNavigator.NUM_TILES_SHOWN;
            final int sth = navigator.getHeight();
            g.setColor(Color.LIGHT_GRAY);
            g.fill3DRect(stx, sty, stw, sth, false);

            try
            {
                final java.awt.Image image = selectedTile.getImage();
                if (image != null)
                {
                    selectedTile.setLocation((stx+stw/2) - image.getWidth(null)/2,
                                             (sty+sth/2) - image.getHeight(null)/2);
                    selectedTile.render(g);
                }
            } catch (final Exception e) {}

            selectedLabel.setPosition(stx, (int)bounds.getHeight() + 20);

            // Map cursor
            final MHMapCellAddress cursor = IMEDataModel.getInstance().map.getCursorAddress();
            g.setColor(Color.WHITE);
            g.drawString("Map Cell:  " + cursor.row + ", " + cursor.column, 6, 12);
        super.render(g);
    }


    @Override
    public void advance()
    {
    	IMEDataModel.getInstance().map.advance();

        navigator.setLayer(selectedLayer);
        navigator.advance();

        if (painting)
        {
            final MHMapCellAddress cursor = (IMEDataModel.getInstance()).map.getCursorAddress();
            final MHMapCell currentCell = (IMEDataModel.getInstance()).map.getMapData().getMapCell(cursor.row, cursor.column);
            if (erasing)
            {
                currentCell.setLayer(selectedLayer, null);
                (IMEDataModel.getInstance()).fileData.fileData[selectedLayer][cursor.row][cursor.column] = MHTileSetManager.NULL_TILE_ID;
            }
            else
            {
                currentCell.setLayer(selectedLayer, selectedTile);
                (IMEDataModel.getInstance()).fileData.fileData[selectedLayer][cursor.row][cursor.column] = selectedTile.getAnimationSequenceNumber();
            }
            (IMEDataModel.getInstance()).mapChanged = true;
        }

        if (layerFlags[MHMapCell.FLOOR_DETAIL_LAYER])
            viewFloorDetailButton.setForeColor(Color.GREEN);
        else
            viewFloorDetailButton.setForeColor(Color.RED);

        if (layerFlags[MHMapCell.ITEM_LAYER])
            viewItemsButton.setForeColor(Color.GREEN);
        else
            viewItemsButton.setForeColor(Color.RED);

        if (layerFlags[MHMapCell.WALL_LAYER])
            viewWallsButton.setForeColor(Color.GREEN);
        else
            viewWallsButton.setForeColor(Color.RED);

        if (layerFlags[MHMapCell.OBSTACLE_LAYER])
            viewObstaclesButton.setForeColor(Color.GREEN);
        else
            viewObstaclesButton.setForeColor(Color.RED);

        if (layerFlags[MHMapCell.CEILING_LAYER])
            viewCeilingsButton.setForeColor(Color.GREEN);
        else
            viewCeilingsButton.setForeColor(Color.RED);

        if (layerFlags[MHMapCell.WALL_DETAIL_LAYER])
            viewDetailsButton.setForeColor(Color.GREEN);
        else
            viewDetailsButton.setForeColor(Color.RED);

        if (selectedLayer == MHMapCell.FLOOR_LAYER)
        {
            editFloorButton.setNormalColor(Color.BLUE);
            editFloorButton.setOverColor(Color.BLUE);
            editFloorButton.setForeColor(Color.WHITE);
        }
        else
        {
            editFloorButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editFloorButton.setOverColor(MHGUIButton.OVER_COLOR);
            editFloorButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.FLOOR_DETAIL_LAYER)
        {
            editFloorDetailButton.setNormalColor(Color.BLUE);
            editFloorDetailButton.setOverColor(Color.BLUE);
            editFloorDetailButton.setForeColor(Color.WHITE);
        }
        else
        {
            editFloorDetailButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editFloorDetailButton.setOverColor(MHGUIButton.OVER_COLOR);
            editFloorDetailButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.WALL_LAYER)
        {
            editWallsButton.setNormalColor(Color.BLUE);
            editWallsButton.setOverColor(Color.BLUE);
            editWallsButton.setForeColor(Color.WHITE);
        }
        else
        {
            editWallsButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editWallsButton.setOverColor(MHGUIButton.OVER_COLOR);
            editWallsButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.ITEM_LAYER)
        {
            editItemsButton.setNormalColor(Color.BLUE);
            editItemsButton.setOverColor(Color.BLUE);
            editItemsButton.setForeColor(Color.WHITE);
        }
        else
        {
            editItemsButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editItemsButton.setOverColor(MHGUIButton.OVER_COLOR);
            editItemsButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.OBSTACLE_LAYER)
        {
            editObstaclesButton.setNormalColor(Color.BLUE);
            editObstaclesButton.setOverColor(Color.BLUE);
            editObstaclesButton.setForeColor(Color.WHITE);
        }
        else
        {
            editObstaclesButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editObstaclesButton.setOverColor(MHGUIButton.OVER_COLOR);
            editObstaclesButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.WALL_DETAIL_LAYER)
        {
            editDetailsButton.setNormalColor(Color.BLUE);
            editDetailsButton.setOverColor(Color.BLUE);
            editDetailsButton.setForeColor(Color.WHITE);
        }
        else
        {
            editDetailsButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editDetailsButton.setOverColor(MHGUIButton.OVER_COLOR);
            editDetailsButton.setForeColor(Color.BLACK);
        }

        if (selectedLayer == MHMapCell.CEILING_LAYER)
        {
            editCeilingsButton.setNormalColor(Color.BLUE);
            editCeilingsButton.setOverColor(Color.BLUE);
            editCeilingsButton.setForeColor(Color.WHITE);
        }
        else
        {
            editCeilingsButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            editCeilingsButton.setOverColor(MHGUIButton.OVER_COLOR);
            editCeilingsButton.setForeColor(Color.BLACK);
        }

        if (erasing)
        {
            toolsEraseButton.setNormalColor(Color.GREEN);
            toolsEraseButton.setOverColor(Color.GREEN);
            toolsEraseButton.setForeColor(Color.WHITE);
        }
        else
        {
            toolsEraseButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            toolsEraseButton.setOverColor(MHGUIButton.OVER_COLOR);
            toolsEraseButton.setForeColor(Color.BLACK);
        }

        if (pickTool)
        {
            toolsPickButton.setNormalColor(Color.GREEN);
            toolsPickButton.setOverColor(Color.GREEN);
            toolsPickButton.setForeColor(Color.WHITE);
        }
        else
        {
            toolsPickButton.setNormalColor(MHGUIButton.NORMAL_COLOR);
            toolsPickButton.setOverColor(MHGUIButton.OVER_COLOR);
            toolsPickButton.setForeColor(Color.BLACK);
        }
    }


    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == viewItemsButton)
        {
            if (selectedLayer != MHMapCell.ITEM_LAYER)
                layerFlags[MHMapCell.ITEM_LAYER] = !layerFlags[MHMapCell.ITEM_LAYER];
        }
        else if (e.getSource() == viewFloorDetailButton)
        {
            if (selectedLayer != MHMapCell.FLOOR_DETAIL_LAYER)
                layerFlags[MHMapCell.FLOOR_DETAIL_LAYER] = !layerFlags[MHMapCell.FLOOR_DETAIL_LAYER];
        }
        else if (e.getSource() == viewObstaclesButton)
        {
            if (selectedLayer != MHMapCell.OBSTACLE_LAYER)
                layerFlags[MHMapCell.OBSTACLE_LAYER] = !layerFlags[MHMapCell.OBSTACLE_LAYER];
        }
        else if (e.getSource() == viewWallsButton)
        {
            if (selectedLayer != MHMapCell.WALL_LAYER)
                layerFlags[MHMapCell.WALL_LAYER] = !layerFlags[MHMapCell.WALL_LAYER];
        }
        else if (e.getSource() == viewDetailsButton)
        {
            if (selectedLayer != MHMapCell.WALL_DETAIL_LAYER)
                layerFlags[MHMapCell.WALL_DETAIL_LAYER] = !layerFlags[MHMapCell.WALL_DETAIL_LAYER];
        }
        else if (e.getSource() == viewCeilingsButton)
        {
            if (selectedLayer != MHMapCell.CEILING_LAYER)
                layerFlags[MHMapCell.CEILING_LAYER] = !layerFlags[MHMapCell.CEILING_LAYER];
        }
        else if (e.getSource() == editFloorButton)
        {
            selectedLayer = MHMapCell.FLOOR_LAYER;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editFloorDetailButton)
        {
            selectedLayer = MHMapCell.FLOOR_DETAIL_LAYER;
            layerFlags[MHMapCell.FLOOR_DETAIL_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editItemsButton)
        {
            selectedLayer = MHMapCell.ITEM_LAYER;
            layerFlags[MHMapCell.ITEM_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editObstaclesButton)
        {
            selectedLayer = MHMapCell.OBSTACLE_LAYER;
            layerFlags[MHMapCell.OBSTACLE_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editWallsButton)
        {
            selectedLayer = MHMapCell.WALL_LAYER;
            layerFlags[MHMapCell.WALL_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editDetailsButton)
        {
            selectedLayer = MHMapCell.WALL_DETAIL_LAYER;
            layerFlags[MHMapCell.WALL_DETAIL_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == editCeilingsButton)
        {
            selectedLayer = MHMapCell.CEILING_LAYER;
            layerFlags[MHMapCell.CEILING_LAYER] = true;
            navigator.setLayer(selectedLayer);
            navigator.setSelectedTile(0);
            selectedTile = navigator.getSelectedTile();
        }
        else if (e.getSource() == toolsEraseButton)
        {
			if (erasing)
			    erasing = false;
			else
			{
        	    erasing = true;
                pickTool = false;
			}
        }
        else if (e.getSource() == toolsPickButton)
        {
			if (pickTool)
			    pickTool = false;
			else
			{
                pickTool = true;
        	    erasing = false;
			}
        }
        else if (e.getSource() == tsBackButton)
        {
            if (fastScrolling)
                navigator.scrollLeft(4);
            else
                navigator.scrollLeft();
        }
        else if (e.getSource() == tsForwardButton)
        {
            if (fastScrolling)
                navigator.scrollRight(4);
            else
                navigator.scrollRight();
        }
        else if (e.getSource() == navigator)
        {
            selectedTile = navigator.getSelectedTile();
            pickTool = false;
            erasing = false;
        }
        else if (e.getSource() == exitButton)
        {
            setNextScreen(null);
            setFinished(true);
        }


    }


    @Override
    public void keyPressed(final KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
            	(IMEDataModel.getInstance()).map.scrollMap(16, 0);
                break;
            case KeyEvent.VK_LEFT:
            	(IMEDataModel.getInstance()).map.scrollMap(-16, 0);
                break;
            case KeyEvent.VK_UP:
            	(IMEDataModel.getInstance()).map.scrollMap(0, -16);
                break;
            case KeyEvent.VK_DOWN:
            	(IMEDataModel.getInstance()).map.scrollMap(0, 16);
                break;
            case KeyEvent.VK_SHIFT:
                fastScrolling = true;
                break;
        }
    }


    @Override
    public void keyReleased(final KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_SHIFT:
                fastScrolling = false;
                break;
        }
    }


    @Override
    public void mousePressed(final MouseEvent e)
    {
        super.mousePressed(e);

		if ((IMEDataModel.getInstance()).map.isScreenCoordinate(e.getPoint()))
		{
    		if (pickTool)
    		{
                final MHMapCellAddress cursor = (IMEDataModel.getInstance()).map.getCursorAddress();
                final MHMapCell currentCell = (IMEDataModel.getInstance()).map.getMapData().getMapCell(cursor.row, cursor.column);
		    	selectedTile = currentCell.getLayer(selectedLayer);
		    	pickTool = false;
    		}
    		else
    		{
                painting = true;
    		}
		}
    }


    @Override
    public void mouseReleased(final MouseEvent e)
    {
        super.mouseReleased(e);

        painting = false;
    }


//    public void mouseDragged(MouseEvent e)
//    {
//        super.mouseDragged(e);
//
//        ((IMEDataModel) IMEDataModel.getInstance()).map.mouseMoved(e);
//    }


    @Override
    public void mouseMoved(final MouseEvent e)
    {
        super.mouseMoved(e);

        IMEDataModel.getInstance().map.mouseMoved(e);
    }
}
