/*
 * Copyright 2021 Root101 (jhernandezb96@gmail.com, +53-5-426-8660).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Or read it directly from LICENCE.txt file at the root of this project.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.root101.swing.derivable_icons;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Icon extracted from a true type font (ttf)
 *
 * @author Root101 (jhernandezb96@gmail.com, +53-5-426-8660)
 * @author JesusHdezWaterloo@Github
 */
public class IconTTF extends DerivableIcon {

    private char ch;

    private Color color = Color.BLACK;

    private float size = 24f;

    private Font font;

    public IconTTF(ImageIcon icon) {
        super(icon.getImage());
    }

    public IconTTF(Image icon) {
        super(icon);
    }

    public IconTTF(BufferedImage icon) {
        super(icon);
    }

    private IconTTF(ImageIcon icon, Font font, char c, Color color, float size) {
        super(icon.getImage());
        this.ch = c;
        this.color = color;
        this.size = size;
        this.font = font;
    }

    public IconTTF(Font font, char c) {
        this.ch = c;
        this.font = font;

        loadInitialImage();
    }

    public IconTTF(Font font, char c, Color color, float size) {
        this.ch = c;
        this.color = color;
        this.size = size;
        this.font = font;

        loadInitialImage();
    }

    public Font getFont() {
        return font;
    }

    public char getCh() {
        return ch;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getSize() {
        return size;
    }

    private void loadInitialImage() {
        ImageIcon extractedIcon = extractIcon(font, String.valueOf(ch), color, size);
        super.setImage(extractedIcon.getImage());
    }

    @Override
    public IconTTF deriveIcon(Color color) {
        return buildIcon(ch, color, size);
    }

    @Override
    public IconTTF deriveIcon(float size) {
        return buildIcon(ch, color, size);
    }

    private IconTTF buildIcon(char ch, Color color, float size) {
        ImageIcon extractedIcon = extractIcon(font, String.valueOf(ch), color, size);
        return new IconTTF(extractedIcon, font, ch, color, size);
    }

    /**
     * Agregados los hints de Graphics a mano xq se llaman antes que se llame la
     * personalizacion.
     *
     * @param font TTF de donde se van a sacar los iconos.
     * @param str
     * @param color color del que se va a mostrar el icono.
     * @param size tamanno del icono.
     * @return ImageIcon extraido con las propiedades especificas.
     */
    public static ImageIcon extractIcon(Font font, String str, Color color, float size) {
        //First, we have to calculate the string's width and height
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        //Set the font to be used when drawing the string
        //set up the size and force to use the plain style
        font = font.deriveFont(size);
        g2.setFont(font);

        //Get the string visual bounds
        FontRenderContext frc = g2.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = font.getStringBounds(str, frc);
        //Release resources
        g2.dispose();

        //Then, we have to draw the string on the final image
        //Create a new image where to print the character
        img = new BufferedImage((int) Math.ceil(rect.getWidth()), (int) Math.ceil(rect.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);

        g2 = (Graphics2D) img.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2.setColor(color); //Otherwise the text would be white
        g2.setFont(font);

        //Calculate x and y for that string
        FontMetrics fm = g2.getFontMetrics();
        int x = 0;
        int y = fm.getAscent(); //getAscent() = baseline
        g2.drawString(str, x, y);

        //Release resources
        g2.dispose();

        //Return the image
        return new ImageIcon(img);
    }
}
