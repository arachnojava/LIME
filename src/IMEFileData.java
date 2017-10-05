import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import mhframework.tilemap.MHMap;
import mhframework.tilemap.MHMapCell;
import mhframework.tilemap.MHMapFileInfo;
import mhframework.tilemap.MHTileSetManager;


public class IMEFileData
{
    public int[][][] fileData;


    /** Creates an IMEFileData object from existing map files. */
    public IMEFileData()
    {
        final MHMapFileInfo info = MHMap.getMapFileInfo();

        fileData = new int[MHMapCell.NUM_LAYERS][][];

        for (int layer = 0;
             layer < MHMapCell.NUM_LAYERS;
             layer++)
        {
            fileData[layer] = new int[info.height][info.width];
        }

        this.loadMapFile(info.fileName);
    }


    /**
     * Creates a new IMEFileData object and the associated files
     * with the given dimensions on each layer.
     */
    public IMEFileData(final int width, final int height, final int mapNumber, final int tileSetID)
    {
        final DecimalFormat formatter = new DecimalFormat("0000000");
        final String mapNum = formatter.format(mapNumber);

        final MHMapFileInfo info = MHMap.getMapFileInfo();

        info.width = width;
        info.height = height;
        info.tileSetId = tileSetID;
        info.fileName        = "M" + mapNum + IMEScreenBase.MAP_FILE_EXTENSION;
        info.floorFile       = "F" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.floorDetailFile = "L" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.itemFile        = "I" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.obstacleFile    = "O" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.wallFile        = "W" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.detailFile      = "D" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;
        info.ceilingFile     = "C" + mapNum + IMEScreenBase.LAYER_FILE_EXTENSION;

        fileData = new int[MHMapCell.NUM_LAYERS][][];

        for (int layer = 0;
             layer < MHMapCell.NUM_LAYERS;
             layer++)
        {
            fileData[layer] = new int[height][width];
        }

        initialize();

        saveMapFile();
    }


    /** Saves to files according to specification in MHMapFileInfo object. */
    public void saveMapFile()
    {
        final MHMapFileInfo info = MHMap.getMapFileInfo();

        try
        {
            System.out.println("IMEFileData.saveMapFile():  Saving " + info.fileName);
            final RandomAccessFile file = new RandomAccessFile(info.fileName, "rw");
            file.setLength(0L);
            file.writeBytes(info.tileSetId       + "\n");
            file.writeBytes(info.floorFile       + "\n");
            file.writeBytes(info.floorDetailFile + "\n");
            file.writeBytes(info.itemFile        + "\n");
            file.writeBytes(info.obstacleFile    + "\n");
            file.writeBytes(info.wallFile        + "\n");
            file.writeBytes(info.detailFile      + "\n");
            file.writeBytes(info.ceilingFile     + "\n");
            file.close();

            writeToFile(MHMapCell.FLOOR_LAYER,        info.floorFile);
            writeToFile(MHMapCell.FLOOR_DETAIL_LAYER, info.floorDetailFile);
            writeToFile(MHMapCell.ITEM_LAYER,         info.itemFile);
            writeToFile(MHMapCell.OBSTACLE_LAYER,     info.obstacleFile);
            writeToFile(MHMapCell.WALL_LAYER,         info.wallFile);
            writeToFile(MHMapCell.WALL_DETAIL_LAYER,  info.detailFile);
            writeToFile(MHMapCell.CEILING_LAYER,      info.ceilingFile);
        }
        catch(final Exception e)
        {
            System.err.println("ERROR:  There was a problem saving the data files.");
        }
    }


    private void writeToFile(final int layer, final String fileName)
    {
        final MHMapFileInfo info = MHMap.getMapFileInfo();

        try
        {
            System.out.println("\tIMEFileData.writeToFile():  Saving " + fileName);

            final RandomAccessFile file = new RandomAccessFile(fileName, "rw");

            file.setLength(0L);

            for (int row = 0; row < info.height; row++)
            {
                for (int col = 0; col < info.width; col++)
                {
                    file.writeBytes(new String(fileData[layer][row][col] + "\t"));

                    // DEBUG:
                    //System.out.print(fileData[layer][row][col] + " ");
                }
                file.writeBytes("\n");
                //System.out.println();
            }
            file.close();
        }
        catch(final Exception e)
        {
            System.err.println("ERROR:  Problem saving file " + fileName);
        }
    }


    private void initialize()
    {
        final MHMapFileInfo info = MHMap.getMapFileInfo();

        for (int layer = 0; layer < MHMapCell.NUM_LAYERS; layer++)
        {
            fileData[layer] = new int[info.height][info.width];

            for (int row = 0; row < fileData[layer].length; row++)
            {
                for (int col = 0; col < fileData[layer][row].length; col++)
                {
                    if (layer == MHMapCell.FLOOR_LAYER)
                        fileData[layer][row][col] = 0;
                    else
                        fileData[layer][row][col] = MHTileSetManager.NULL_TILE_ID;
                }
            }
        }
    }


    public void loadMapFile(final String filename)
    {
        final MHMapFileInfo info = MHMap.getMapFileInfo();
        fileData = new int[MHMapCell.NUM_LAYERS][info.height][info.width];
        initialize();

        for (int layer = 0;
             layer < MHMapCell.NUM_LAYERS;
             layer++)
        {
            int row = 0, col = 0;

            final String layerFile = chooseLayerFile(layer);

                try
                {
                    final RandomAccessFile file =
                                    new RandomAccessFile(layerFile, "r");

                    for (row = 0; row < info.height; row++)
                    {
                        final String line = file.readLine().trim();
                        final String[] dataRow = line.split("\t");
                        for (col = 0; col < dataRow.length; col++)
                        {
                            int tileID = MHTileSetManager.NULL_TILE_ID;

                            try
                            {
                                tileID = Integer.parseInt(dataRow[col]);
                            }
                            catch (final NumberFormatException nfe)
                            {
                                tileID = MHTileSetManager.NULL_TILE_ID;
                            }

                            fileData[layer][row][col] = tileID;
                        } // for (col...
                    } // for (row...
                } // try
                catch (final Exception e)
                {
                    try
                    {
                        fileData[layer][row][col] = MHTileSetManager.NULL_TILE_ID;
                    }
                    catch (final Exception ex) { }
                }
        } // for (layer...)
    } // loadMapFile


        /****************************************************************
         * Choose the correct data file for the layer we're building.
         */
        private String chooseLayerFile(final int layer)
        {
                String layerFile = null;
                final MHMapFileInfo info = MHMap.getMapFileInfo();

                switch (layer)
                {
                        case MHMapCell.FLOOR_LAYER:
                                layerFile = info.floorFile;
                                break;
                        case MHMapCell.FLOOR_DETAIL_LAYER:
                                layerFile = info.floorDetailFile;
                                break;
                        case MHMapCell.WALL_LAYER:
                                layerFile = info.wallFile;
                                break;
                        case MHMapCell.ITEM_LAYER:
                                layerFile = info.itemFile;
                                break;
                        case MHMapCell.WALL_DETAIL_LAYER:
                                layerFile = info.detailFile;
                                break;
                        case MHMapCell.CEILING_LAYER:
                                layerFile = info.ceilingFile;
                                break;
                }

                return layerFile;
        }
}

