package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    TextColorSchema schema;
    double maxRatio;
    int maxWidth;
    int maxHeight;
    int newWidth;
    int newHeight;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        if (maxRatio != 0) {
            double currentRatio = (double) img.getWidth() / img.getHeight();
            double currentRatio1 = (double) img.getHeight() / img.getWidth();
            if (maxRatio < currentRatio || maxRatio < currentRatio1) {
                throw new BadImageSizeException(currentRatio, maxRatio);
            }
        }

        if (maxWidth != 0 || maxWidth < img.getWidth()) {
            newWidth = maxWidth;
            newHeight = img.getHeight() / (img.getWidth() / maxWidth);
        }
        if (maxHeight != 0 || maxHeight < img.getHeight()) {
            newHeight = maxHeight;
            newWidth = img.getWidth() / (img.getHeight() / maxHeight);
        } else {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        }


        if (schema == null) {
            setTextColorSchema(new TextColorSchemaImpl());
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        char[][] charResult = new char[bwRaster.getHeight()][bwRaster.getWidth()];
        for (int i = 0; i < bwRaster.getHeight(); i++) {
            for (int j = 0; j < bwRaster.getWidth(); j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                charResult[i][j] = c;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char[] row : charResult) {
            for (char cell : row) {
                sb.append(cell);
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}


