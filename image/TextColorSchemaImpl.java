package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    static final String density = "#$@%*+-'";

    @Override
    public char convert(int color) {
        int value = (int) Math.round(density.length() / 255.0 * color);
        value = Math.max(value, 0);
        value = Math.min(value, density.length() - 1);
        return density.charAt(value);
    }
}
