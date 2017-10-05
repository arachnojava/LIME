import mhframework.MHAppLauncher;
import mhframework.MHGameApplication;
import mhframework.MHVideoSettings;

public class IsoMapEditor
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(final String args[])
    {
        final MHVideoSettings settings = new MHVideoSettings();
        settings.bitDepth = 32;
        settings.fullScreen = MHAppLauncher.showDialog(null, true);
        settings.displayWidth = MHAppLauncher.getResolution().width;
        settings.displayHeight = MHAppLauncher.getResolution().height;
        settings.windowCaption = IMEDataModel.APP_TITLE + " " + IMEDataModel.APP_VERSION;

        if (args.length > 0)
        {
            IMEDataModel.mapFile = args[0];
        }
        
        new MHGameApplication(new IMEScreenBase(), settings);

        System.exit(0);
     }
}
