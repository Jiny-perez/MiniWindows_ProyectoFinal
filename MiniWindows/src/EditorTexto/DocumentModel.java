package EditorTexto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author marye
 */
public class DocumentModel {

    private final List<FormatoTexto> formato = new ArrayList<>();

    private String texto;
    private String defaultFont = "SansSerif";
    private int defaultSize = 14;
    private int defaultStyle = 0;
    private String defaultColor = "#000000";

    public DocumentModel(String texto) {
        this.texto = texto == null ? "" : texto;
    }

    public List<FormatoTexto> getFormato() {
        return formato;
    }

    public String getTexto() {
        return texto;
    }

    public void setText(String texto) {
        this.texto = texto == null ? "" : texto;
    }

    public String getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(String defaultFont) {
        if (defaultFont != null && !defaultFont.isEmpty()) {
            this.defaultFont = defaultFont;
        }
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        if (defaultSize > 0) {
            this.defaultSize = defaultSize;
        }
    }

    public int getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(int defaultStyle) {
        if (defaultStyle >= 0) {
            this.defaultStyle = defaultStyle;
        }
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(String defaultColor) {
        if (defaultColor != null && !defaultColor.isEmpty()) {
            this.defaultColor = defaultColor;
        }
    }

    public void AgregarFormato(FormatoTexto ft) {
        if (ft == null) {
            return;
        }
        ft.normalize();
        if (ft.inicio < 0) {
            ft.inicio = 0;
        }
        if (ft.fin > texto.length()) {
            ft.fin = texto.length();
        }
        if (ft.inicio >= ft.fin) {
            return;
        }
        formato.add(ft);
    }

    public void limpiarFormato() {
        formato.clear();
    }

    public void aplicarFormato(int inicio, int fin, String newFontFamily, Integer newFontSize, Integer newFontStyle, String newColor) {
        if (texto == null) {
            texto = "";
        }
        if (inicio < 0) {
            inicio = 0;
        }
        if (fin > texto.length()) {
            fin = texto.length();
        }
        if (inicio >= fin) {
            return;
        }

        Collections.sort(formato, Comparator.comparingInt(a -> a.inicio));
        List<FormatoTexto> nuevos = new ArrayList<>();

        for (FormatoTexto f : formato) {
            if (f.fin <= inicio || f.inicio >= fin) {
                nuevos.add(new FormatoTexto(f.inicio, f.fin, f.fontFamily, f.fontSize, f.fontStyle, f.color));
            } else {
                if (f.inicio < inicio) {
                    nuevos.add(new FormatoTexto(f.inicio, inicio, f.fontFamily, f.fontSize, f.fontStyle, f.color));
                }
                if (f.fin > fin) {
                    nuevos.add(new FormatoTexto(fin, f.fin, f.fontFamily, f.fontSize, f.fontStyle, f.color));
                }
            }
        }

        int cursor = inicio;
        int indice = 0;
        while (indice < formato.size() && formato.get(indice).fin <= inicio) {
            indice++;
        }

        while (cursor < fin) {
            FormatoTexto f = (indice < formato.size()) ? formato.get(indice) : null;

            if (f == null || f.inicio >= fin) {
                String font;
                if (newFontFamily != null && !newFontFamily.isEmpty()) {
                    font = newFontFamily;
                } else {
                    font = this.defaultFont;
                }

                int fsize;
                if (newFontSize != null && newFontSize > 0) {
                    fsize = newFontSize;
                } else {
                    fsize = this.defaultSize;
                }

                int fstyle;
                if (newFontStyle != null && newFontStyle >= 0) {
                    fstyle = newFontStyle;
                } else {
                    fstyle = this.defaultStyle;
                }

                String fcolor;
                if (newColor != null && !newColor.isEmpty()) {
                    fcolor = newColor;
                } else {
                    fcolor = this.defaultColor;
                }

                nuevos.add(new FormatoTexto(cursor, fin, font, fsize, fstyle, fcolor));
                cursor = fin;
                continue;
            }


            if (f.fin <= cursor) {
                indice++;
                continue;
            }


            if (f.inicio > cursor) {
                int gapEnd = Math.min(f.inicio, fin);

                String font;
                if (newFontFamily != null && !newFontFamily.isEmpty()) {
                    font = newFontFamily;
                } else {
                    font = this.defaultFont;
                }

                int fsize;
                if (newFontSize != null && newFontSize > 0) {
                    fsize = newFontSize;
                } else {
                    fsize = this.defaultSize;
                }

                int fstyle;
                if (newFontStyle != null && newFontStyle >= 0) {
                    fstyle = newFontStyle;
                } else {
                    fstyle = this.defaultStyle;
                }

                String fcolor;
                if (newColor != null && !newColor.isEmpty()) {
                    fcolor = newColor;
                } else {
                    fcolor = this.defaultColor;
                }

                nuevos.add(new FormatoTexto(cursor, gapEnd, font, fsize, fstyle, fcolor));
                cursor = gapEnd;
                continue;
            }


            int inicioSegmento = cursor;
            int finSegmento = Math.min(f.fin, fin);

            String font;
            if (newFontFamily != null && !newFontFamily.isEmpty()) {
                font = newFontFamily;
            } else if (f.fontFamily != null && !f.fontFamily.isEmpty()) {
                font = f.fontFamily;
            } else {
                font = this.defaultFont;
            }

            int fsize;
            if (newFontSize != null && newFontSize > 0) {
                fsize = newFontSize;
            } else if (f.fontSize > 0) {
                fsize = f.fontSize;
            } else {
                fsize = this.defaultSize;
            }

            int fstyle;
            if (newFontStyle != null && newFontStyle >= 0) {
                fstyle = newFontStyle;
            } else if (f.fontStyle >= 0) {
                fstyle = f.fontStyle;
            } else {
                fstyle = this.defaultStyle;
            }

            String fcolor;
            if (newColor != null && !newColor.isEmpty()) {
                fcolor = newColor;
            } else if (f.color != null && !f.color.isEmpty()) {
                fcolor = f.color;
            } else {
                fcolor = this.defaultColor;
            }

            nuevos.add(new FormatoTexto(inicioSegmento, finSegmento, font, fsize, fstyle, fcolor));
            cursor = finSegmento;
            if (f.fin <= cursor) {
                indice++;
            }
        }

        formato.clear();
        formato.addAll(nuevos);
        normalizeFormato();
    }

    public void normalizeFormato() {
        Collections.sort(formato, Comparator.comparingInt(a -> a.inicio));

        List<FormatoTexto> salida = new ArrayList<>();
        for (FormatoTexto f : formato) {
            if (salida.isEmpty()) {
                salida.add(new FormatoTexto(f.inicio, f.fin, f.fontFamily, f.fontSize, f.fontStyle, f.color));
            } else {
                FormatoTexto last = salida.get(salida.size() - 1);
                if (last.fin >= f.inicio && last.mismoFormato(f)) {
                    last.fin = Math.max(last.fin, f.fin);
                } else {
                    salida.add(new FormatoTexto(f.inicio, f.fin, f.fontFamily, f.fontSize, f.fontStyle, f.color));
                }
            }
        }
        formato.clear();
        formato.addAll(salida);
    }
}
