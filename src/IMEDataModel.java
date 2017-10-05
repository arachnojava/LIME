import java.io.File;
import mhframework.MHActor;
import mhframework.MHDataModel;
import mhframework.tilemap.MHDiamondMap;
import mhframework.tilemap.MHIsometricMap;
import mhframework.tilemap.MHMap;
import mhframework.tilemap.MHMapCellAddress;
import mhframework.tilemap.MHMapFileInfo;
import mhframework.tilemap.MHObjectFactory;
import mhframework.tilemap.MHStaggeredMap;


public class IMEDataModel extends MHDataModel
{

    private static IMEDataModel INSTANCE;

    public static final String APP_TITLE   = "LIME: Layered Isometric Map Editor";
    public static final String APP_VERSION = "3.6 R03/2012";
    public static final String APP_AUTHOR  = "Michael Henson";

    public MHStaggeredMap staggeredMap;
    public MHDiamondMap diamondMap;
    public MHIsometricMap map;

    public static String mapFile = "M9999999" + MHMapFileInfo.MAP_FILE_EXTENSION;

    public IMEFileData fileData;

    public boolean mapChanged = false;

    private IMEDataModel()
    {
        super();

		final File testFile = new File(mapFile);
		if (!testFile.exists())
		{
		    newMap(20, 20, 9999999, 0);
		}

        loadMapFile(mapFile);
    }


    public static IMEDataModel getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new IMEDataModel();

        return INSTANCE;
    }


    public void loadMapFile(final String filename)
    {
        mapFile = filename;

        diamondMap = new MHDiamondMap(mapFile, new IMEVendor());

        map = diamondMap;

        fileData = new IMEFileData();
    }


    public void newMap(final int width, final int height, final int mapNumber, final int tileSetID)
    {
        newMap(width, height, mapNumber, tileSetID, MHMap.getMapFileInfo());
    }


    public void newMap(final int width, final int height, final int mapNumber, final int tileSetID, final MHMapFileInfo fileInfo)
    {
        fileData = new IMEFileData(width, height, mapNumber, tileSetID);
        loadMapFile(MHMap.getMapFileInfo().fileName);
    }


    public void saveMapData()
    {
        fileData.saveMapFile();
    }


    public void insertTileID(final int tileNum, final int layer, final int row, final int col)
    {
        fileData.fileData[layer][row][col] = tileNum;
    }
}


class IMEVendor implements MHObjectFactory
{
    public MHActor getObject(final int layer, final int tileID, MHMapCellAddress location)
    {
        return null;
    }
}
