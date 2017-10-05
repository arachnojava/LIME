import java.awt.Color;
import java.awt.Font;
import mhframework.MHGameApplication;
import mhframework.MHVideoSettings;
import mhframework.gui.MHGUIDialogScreen;
import mhframework.gui.MHGUILabel;
import mhframework.gui.MHGUINumericCycleControl;
import mhframework.media.MHFont;


public class IMENewMapScreen extends MHGUIDialogScreen
{
    private static final int MIN_SIZE = 20;
    private static final int MAX_SIZE = 250;
    private static final int SIZE_INCREMENT = 10;
    private static final int MAX_TILE_SET = 99;
    private static final int MAX_MAP_NUMBER = 999;

    private final MHGUINumericCycleControl cycWidth, cycHeight, cycTileSet, cycMapNumber;
    private final MHGUILabel lblWidth, lblHeight, lblTileSet, lblMapNumber;

    public IMENewMapScreen()
    {
        // Values for sizing and positioning.
        final int labelWidth = 100, labelHeight = 40;
        final int spacing = 50, top = 200, col1 = 160, col2 = 350;

        setTitle("Create New Map");
        setMessage("Set the parameters for the new map.");


        final MHFont labelFont = new MHFont("SansSerif", Font.BOLD, 24);
        final MHFont cycleFont = new MHFont("Monospaced", Font.BOLD, 24);


        lblMapNumber = new MHGUILabel();
        lblMapNumber.setText("Map Number:");
        lblMapNumber.setSize(labelWidth, labelHeight);
        lblMapNumber.setPosition(col1, top);
        lblMapNumber.setFont(labelFont);
        lblMapNumber.setPaint(Color.LIGHT_GRAY);

        lblTileSet = new MHGUILabel();
        lblTileSet.setText("Tile Set:");
        lblTileSet.setSize(labelWidth, labelHeight);
        lblTileSet.setPosition(col1, top+spacing);
        lblTileSet.setFont(labelFont);
        lblTileSet.setPaint(Color.LIGHT_GRAY);

        lblWidth = new MHGUILabel();
        lblWidth.setText("Map Width:");
        lblWidth.setSize(labelWidth, labelHeight);
        lblWidth.setPosition(col1, top+spacing*2);
        lblWidth.setFont(labelFont);
        lblWidth.setPaint(Color.LIGHT_GRAY);

        lblHeight = new MHGUILabel();
        lblHeight.setText("Map Height:");
        lblHeight.setSize(labelWidth, labelHeight);
        lblHeight.setPosition(col1, top+spacing*3);
        lblHeight.setFont(labelFont);
        lblHeight.setPaint(Color.LIGHT_GRAY);

        cycMapNumber = new MHGUINumericCycleControl();
        cycMapNumber.setMinValue(0);
        cycMapNumber.setMaxValue(MAX_MAP_NUMBER);
        cycMapNumber.setSelectedIndex(0);
        cycMapNumber.setSize(labelWidth, labelHeight);
        cycMapNumber.setPosition(col2, lblMapNumber.getY()-20);
        cycMapNumber.setFont(cycleFont);
        cycMapNumber.setLabelColor(Color.WHITE);

        cycTileSet = new MHGUINumericCycleControl();
        cycTileSet.setMinValue(0);
        cycTileSet.setMaxValue(MAX_TILE_SET);
        cycTileSet.setSelectedIndex(0);
        cycTileSet.setSize(labelWidth, labelHeight);
        cycTileSet.setPosition(col2, lblTileSet.getY()-20);
        cycTileSet.setFont(cycleFont);
        cycTileSet.setLabelColor(Color.WHITE);

        cycWidth = new MHGUINumericCycleControl();
        cycWidth.setMinValue(MIN_SIZE);
        cycWidth.setMaxValue(MAX_SIZE);
        cycWidth.setSelectedIndex(MIN_SIZE);
        cycWidth.setSize(labelWidth, labelHeight);
        cycWidth.setPosition(col2, lblWidth.getY()-20);
        cycWidth.setFont(cycleFont);
        cycWidth.setLabelColor(Color.WHITE);
        cycWidth.setIncrement(SIZE_INCREMENT);

        cycHeight = new MHGUINumericCycleControl();
        cycHeight.setMinValue(MIN_SIZE);
        cycHeight.setMaxValue(MAX_SIZE);
        cycHeight.setSelectedIndex(MIN_SIZE);
        cycHeight.setSize(labelWidth, labelHeight);
        cycHeight.setPosition(col2, lblHeight.getY()-20);
        cycHeight.setFont(cycleFont);
        cycHeight.setLabelColor(Color.WHITE);
        cycHeight.setIncrement(SIZE_INCREMENT);

        add(lblMapNumber);
        add(lblTileSet);
        add(lblWidth);
        add(lblHeight);
        add(cycMapNumber);
        add(cycTileSet);
        add(cycWidth);
        add(cycHeight);
    }


    public int getMapWidth()
    {
        return ((Integer)cycWidth.getSelectedValue()).intValue();
    }

    public int getMapHeight()
    {
        return ((Integer)cycHeight.getSelectedValue()).intValue();
    }

    public int getTileSetID()
    {
        return ((Integer)cycTileSet.getSelectedValue()).intValue();
    }

    public int getMapNumber()
    {
        return ((Integer)cycMapNumber.getSelectedValue()).intValue();
    }

    public static void main(final String args[])
    {
        final MHVideoSettings settings = new MHVideoSettings();
        settings.displayWidth = 800;
        settings.displayHeight = 600;
        settings.bitDepth = 32;
        settings.fullScreen = false;
        settings.windowCaption = "New Map Screen Test";

        new MHGameApplication(new IMENewMapScreen(), settings);

        System.exit(0);
     }
}
