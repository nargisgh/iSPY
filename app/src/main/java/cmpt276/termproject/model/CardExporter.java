package cmpt276.termproject.model;
import android.graphics.Canvas;

public class CardExporter
{
    private static CardExporter instance;
    private static Canvas canvas;
    private static int imgTally;

    private CardExporter()
    {
    }

    public static CardExporter getInstance()
    {
        if (instance == null)
        {
            instance = new CardExporter();
            canvas = new Canvas();
            imgTally = 0;
        }
        return instance;
    }

    public Canvas getCanvas(int numImg)
    {
        if (imgTally == numImg)
        {
            canvas = new Canvas();
            imgTally = 0;
        }
        imgTally++;
        return canvas;
    }

}
