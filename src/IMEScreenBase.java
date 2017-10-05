import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHScreen;
import mhframework.gui.MHGUIButton;
import mhframework.gui.MHGUIButtonMenu;
import mhframework.gui.MHGUIDialogScreen;
import mhframework.gui.MHGUIFileDialogScreen;
import mhframework.tilemap.MHMap;
import mhframework.tilemap.MHMapFileInfo;
import mhframework.tilemap.MHTileMap;


public class IMEScreenBase extends MHScreen
{

    public static final String MAP_FILE_EXTENSION = MHMapFileInfo.MAP_FILE_EXTENSION;
    public static final String LAYER_FILE_EXTENSION = MHMapFileInfo.LAYER_FILE_EXTENSION;

    private boolean openDialog = false;
    private boolean quitting = false;
    private boolean newMap = false;

    private MHGUIDialogScreen dlgSave;
    private MHGUIFileDialogScreen dlgOpen;
    private IMENewMapScreen dlgNew;

    protected static MHGUIButtonMenu viewMenu;
    protected static MHGUIButton mapViewButton;
    protected static MHGUIButton mapEditButton;

    protected static MHGUIButtonMenu fileMenu;
    protected static MHGUIButton fileNewButton;
    protected static MHGUIButton fileSaveButton;
    protected static MHGUIButton fileOpenButton;
    protected static MHGUIButton fileExitButton;

    public IMEScreenBase()
    {
        super();
        
        viewMenu = new MHGUIButtonMenu();
        viewMenu.setText("View");
        viewMenu.setBackgroundColor(Color.LIGHT_GRAY);
        viewMenu.setTextColor(Color.BLACK);
        viewMenu.setButtonSize(60, 20);
        viewMenu.setBorderSpacing(2);

        mapViewButton = new MHGUIButton();
        mapViewButton.setText("View");
        mapViewButton.addActionListener(this);

        mapEditButton = new MHGUIButton();
        mapEditButton.setText("Edit");
        mapEditButton.addActionListener(this);

        //tilesetViewButton  = new MHGUIButton();
        //tilesetViewButton.addActionListener(this);
        //tilesetViewButton.setText("Tile Set");

        viewMenu.add(mapViewButton);
        viewMenu.add(mapEditButton);
        //viewMenu.add(tilesetViewButton);

        fileMenu = new MHGUIButtonMenu();
        fileMenu.setText("File");
        fileMenu.setBackgroundColor(Color.LIGHT_GRAY);
        fileMenu.setTextColor(Color.BLACK);
        fileMenu.setButtonSize(60, 20);
        fileMenu.setBorderSpacing(2);

        fileNewButton = new MHGUIButton();
        fileNewButton.setText("New");
        fileNewButton.addActionListener(this);

        fileOpenButton = new MHGUIButton();
        fileOpenButton.setText("Open");
        fileOpenButton.addActionListener(this);

        fileSaveButton = new MHGUIButton();
        fileSaveButton.setText("Save");
        fileSaveButton.addActionListener(this);

        fileExitButton = new MHGUIButton();
        fileExitButton.setText("Exit");
        fileExitButton.addActionListener(this);

        fileMenu.add(fileNewButton);
        fileMenu.add(fileOpenButton);
        fileMenu.add(fileSaveButton);
        fileMenu.add(fileExitButton);

        add(viewMenu);
        add(fileMenu);
    }


    @Override
    public void load()
    {
        int rc; // return code from dialogs

        if (dlgSave != null) // if save dialog has been created, that means we're returning from it
        {
            rc = dlgSave.getReturnCode();

            if (rc == MHGUIDialogScreen.OK_OPTION)
            {
                IMEDataModel.getInstance().fileData.saveMapFile();
                IMEDataModel.getInstance().mapChanged = false;
            }

            dlgSave = null; // Destroy the save dialog.  We're done with it for now.

            if (quitting)
            {
                MHGame.setProgramOver(true);
                return;
            }
            if (openDialog)
            {
                showOpenDialog();
                openDialog = false;
                return;
            }
            if (newMap)
            {
                setNextScreen(new IMENewMapScreen());
                setFinished(true);
                newMap = false;
                return;
            }
        }
        if (dlgOpen != null)
        {
            rc = dlgOpen.getReturnCode();

            if (rc == MHGUIDialogScreen.OK_OPTION)
            {
                final File file = dlgOpen.getSelectedFile();
                final MHTileMap map = IMEDataModel.getInstance().map;
                map.getMapData().loadMapFile(file.getName());
            }
            dlgOpen = null;
        }
        if (dlgNew != null)
        {
            rc = dlgNew.getReturnCode();

            if (rc == MHGUIDialogScreen.OK_OPTION)
            {
                IMEDataModel.getInstance().newMap(dlgNew.getMapWidth(),
                                                  dlgNew.getMapHeight(),
                                                  dlgNew.getMapNumber(),
                                                  dlgNew.getTileSetID(),
                                                  MHMap.getMapFileInfo());
            }
            dlgNew = null;
        }

        final MHTileMap map = IMEDataModel.getInstance().map;

        final int mapViewWidth = (int)(MHDisplayModeChooser.getScreenSize().width - fileMenu.getBounds().getWidth());
        final int mapViewHeight = MHDisplayModeChooser.getScreenSize().height - 146;

        map.setScreenSpace(0, 0, mapViewWidth, mapViewHeight);

        final Point origin = map.plotTile(0, 0);

        origin.x -= map.getScreenSpace().getWidth()/2 -
                    map.getTileWidth() / 2;

        map.setScreenAnchor(origin);

        map.setCursorPoint(origin);
        
        map.setMouseScroll(true);

        setFinished(false);

        viewMenu.setPosition(MHDisplayModeChooser.getScreenSize().width - (int)viewMenu.getBounds().getWidth(), 0);
        fileMenu.setPosition(viewMenu.getX(), (int)viewMenu.getBounds().getHeight()+4);
    }


    @Override
    public void unload()
    {
    }


    private void showSaveDialog()
    {
        dlgSave = new MHGUIDialogScreen();
        dlgSave.setTitle("Save Changes?");
        dlgSave.setMessage("The current map has changed.  Would you like to save changes?");
        dlgSave.setButtonCaptions("Yes", "No");
        setNextScreen(dlgSave);
        setFinished(true);
    }


    private void showOpenDialog()
    {
        dlgOpen = new MHGUIFileDialogScreen(".", IMEScreenBase.MAP_FILE_EXTENSION);
        dlgOpen.setTitle("Open Map File");
        setNextScreen(dlgOpen);
        setFinished(true);
    }


    @Override
    public void render(final Graphics2D g)
    {
            //g.setColor(Color.LIGHT_GRAY);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, MHDisplayModeChooser.getScreenSize().width, MHDisplayModeChooser.getScreenSize().height);

            // Draw map here
            IMEDataModel.getInstance().map.render(g);

            // Draw map interface stuff here
            g.setColor(Color.LIGHT_GRAY);
            // Bottom (tile set) panel
            g.fillRect(0, MHDisplayModeChooser.getScreenSize().height - 146, MHDisplayModeChooser.getScreenSize().width - (int)fileMenu.getBounds().getWidth(), MHDisplayModeChooser.getScreenSize().height);
            // Right (menu) panel
            g.fillRect(MHDisplayModeChooser.getScreenSize().width - (int)viewMenu.getBounds().getWidth(), 0,
                       (int)viewMenu.getBounds().getWidth(), MHDisplayModeChooser.getScreenSize().height);

            // Credits
            g.setColor(Color.BLACK);
            g.drawString(IMEDataModel.APP_TITLE, 10, MHDisplayModeChooser.getScreenSize().height - (480-442));
            g.drawString("Version " + IMEDataModel.APP_VERSION, 10, MHDisplayModeChooser.getScreenSize().height - (480-456));
            g.drawString("by " + IMEDataModel.APP_AUTHOR, 10, MHDisplayModeChooser.getScreenSize().height - 10);

            // Map file info
            final MHMap map = IMEDataModel.getInstance().map.getMapData();
            final int x = MHDisplayModeChooser.getScreenSize().width - 280,
                y =  MHDisplayModeChooser.getScreenSize().height - 126,
                dx = 150,
                dy = 16;
            g.setColor(Color.GRAY);
            g.drawLine(x-10, y+3, x + 260, y+3);
            g.setColor(Color.BLACK);
            g.drawString("Map Information File:", x+10, y);
            g.drawString("Tile Set ID:", x+10, y + (dy));
            g.drawString("Map Dimensions:", x+10, y + (dy*2));
            g.drawString("Floor Data File:", x+10, y + (dy*3));
            g.drawString("Item Data File:", x+10, y + (dy*4));
            g.drawString("Wall Data File:", x+10, y + (dy*5));
            g.drawString("Detail Data File:", x+10, y + (dy*6));
            g.drawString("Ceiling Data File:", x+10, y + (dy*7));

            g.setColor(Color.BLUE);
            g.drawString(map.getMapFileName(), x + dx, y);
            g.drawString(map.getTileSetId()+"", x+dx, y + (dy));
            g.drawString(map.getWidth() + " x " + map.getHeight(), x+dx, y + (dy*2));
            g.drawString(map.getFloorFile(), x+dx, y + (dy*3));
            g.drawString(map.getItemFile(), x+dx, y + (dy*4));
            g.drawString(map.getWallFile(), x+dx, y + (dy*5));
            g.drawString(map.getWallDetailFile(), x+dx, y + (dy*6));
            g.drawString(map.getCeilingFile(), x+dx, y + (dy*7));

            g.setColor(Color.LIGHT_GRAY);
            g.draw3DRect(x-10, y-12, 270, dy*8, false);

            // Bounding rectangle around the map area
            final Rectangle2D bounds = IMEDataModel.getInstance().map.getScreenSpace();
            g.setColor(Color.BLUE);
            g.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());

        super.render(g);
    }


    @Override
    public void advance()
    {
    	(IMEDataModel.getInstance()).map.advance();

    }


    public void actionPerformed(final ActionEvent e)
    {
        final Object button = e.getSource();

        if (button == mapViewButton)
        {
            setNextScreen(new IMEViewScreen());
            setFinished(true);
        }
        else if (button == mapEditButton)
        {
            setNextScreen(new IMEEditScreen());
            setFinished(true);
        }
        else if (button == fileNewButton)
        {
            if (IMEDataModel.getInstance().mapChanged)
            {
                newMap = true;
                showSaveDialog();
            }
            else
            {
                dlgNew = new IMENewMapScreen();
                setNextScreen(dlgNew);
                setFinished(true);
            }
        }
        else if (button == fileOpenButton)
        {
            if (IMEDataModel.getInstance().mapChanged)
            {
                openDialog = true;
                showSaveDialog();
            }
            else
            {
                showOpenDialog();
            }
        }
        else if (button == fileSaveButton)
        {
            IMEDataModel.getInstance().fileData.saveMapFile();
            IMEDataModel.getInstance().mapChanged = false;
        }
        else if (button == fileExitButton)
        {
            quitting = true;
            if (IMEDataModel.getInstance().mapChanged)
                showSaveDialog();
            else
                MHGame.setProgramOver(true);
        }
    }


    @Override
    public void keyPressed(final KeyEvent e)
    {
    	final MHTileMap map = (IMEDataModel.getInstance()).map;

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
        }
    }


    @Override
    public void mouseMoved(final MouseEvent e)
    {
        super.mouseMoved(e);

        IMEDataModel.getInstance().map.mouseMoved(e);
    }
}


class IMEFileFilter extends FileFilter
{
	@Override
    public String getDescription()
	{
		return "LIME Map Files";
	}

	@Override
    public boolean accept(final File f)
	{
		return f.getName().endsWith(IMEScreenBase.MAP_FILE_EXTENSION) || f.isDirectory();
	}
}
