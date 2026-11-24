import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;
import java.util.*;
import java.util.List;
import java.util.function.*;


// THEME MANAGER - global singleton; once set, applies to all windows

class ThemeManager {
    private static boolean darkMode = true;
    private static final List<Runnable> listeners = new ArrayList<>();

    public static boolean isDark() { return darkMode; }

    public static void toggle() {
        darkMode = !darkMode;
        for (Runnable r : new ArrayList<>(listeners)) r.run();
    }

    public static void addListener(Runnable r) { listeners.add(r); }
    public static void removeListener(Runnable r) { listeners.remove(r); }

    // -- Colours
    // Palette shared with the landing page
    public static Color bg()        { return darkMode ? Color.web("#0B1220") : Color.web("#F3F5F9"); }
    public static Color panel()     { return darkMode ? Color.web("#131E33") : Color.web("#FFFFFF"); }
    public static Color card()      { return darkMode ? Color.web("#172640") : Color.web("#F7F9FC"); }
    public static Color field()     { return darkMode ? Color.web("#0E1729") : Color.web("#FFFFFF"); }
    public static Color textPrim()  { return darkMode ? Color.web("#EAF0FA") : Color.web("#0B2545"); }
    public static Color textSec()   { return darkMode ? Color.web("#9FB0C8") : Color.web("#475A75"); }
    public static Color textMuted() { return darkMode ? Color.web("#6B7C96") : Color.web("#7A8AA3"); }
    public static Color border()    { return darkMode ? Color.web("#26354F") : Color.web("#D6DCE7"); }
    public static Color accentRed()    { return Color.web("#dc2626"); }
    public static Color accentBlue()   { return Color.web("#2563eb"); }
    public static Color accentTeal()   { return Color.web("#14b8a6"); }
    public static Color accentPurple() { return Color.web("#7c3aed"); }
    public static Color accentOrange() { return Color.web("#ea7c14"); }
    public static Color softOrange() { return Color.web("#ea7c14"); }
    public static Color success()      { return Color.web("#22c55e"); }

    public static String bgCss()    { return toHex(bg()); }
    public static String toHex(Color c) {
        return String.format("#%02x%02x%02x",
            (int)(c.getRed()*255),(int)(c.getGreen()*255),(int)(c.getBlue()*255));
    }

    public static String rootStyle() {
        return "-fx-background-color:" + bgCss() + ";";
    }
    public static String panelStyle() {
        return "-fx-background-color:" + toHex(panel()) + ";";
    }
    public static String cardStyle() {
        return "-fx-background-color:" + toHex(card()) + ";" +
               "-fx-border-color:" + toHex(border()) + ";" +
               "-fx-border-radius:8;" + "-fx-background-radius:8;";
    }
    public static String fieldStyle() {
        return "-fx-background-color:" + toHex(field()) + ";" +
               "-fx-text-fill:" + toHex(textPrim()) + ";" +
               "-fx-border-color:" + toHex(border()) + ";" +
               "-fx-border-radius:6;" + "-fx-background-radius:6;" +
               "-fx-padding:8 12;";
    }
    public static String labelStyle(Color c) { return "-fx-text-fill:" + toHex(c) + ";"; }
    public static String btnStyle(Color bg) {
        return "-fx-background-color:" + toHex(bg) + ";" +
               "-fx-text-fill:#f0f5ff;" +
               "-fx-background-radius:8;" +
               "-fx-font-weight:bold;" +
               "-fx-padding:8 18;" +
               "-fx-cursor:hand;";
    }
    public static String sideBtn() {
        return "-fx-background-color:" + toHex(card()) + ";" +
               "-fx-text-fill:" + toHex(textPrim()) + ";" +
               "-fx-border-color:" + toHex(border()) + ";" +
               "-fx-border-radius:6;" + "-fx-background-radius:6;" +
               "-fx-padding:8 10;" + "-fx-cursor:hand;" +
               "-fx-font-size:12;" + "-fx-alignment:CENTER-LEFT;";
    }
    public static String sideBtnHover() {
        return "-fx-background-color:" + toHex(bg()) + ";" +
               "-fx-text-fill:" + toHex(textPrim()) + ";" +
               "-fx-border-color:" + toHex(softOrange()) + ";" +
               "-fx-border-radius:6;" + "-fx-background-radius:6;" +
               "-fx-padding:8 10;" + "-fx-cursor:hand;" +
               "-fx-font-size:12;" + "-fx-alignment:CENTER-LEFT;";
    }

    public static String sideBtnHoverAdmin() {
        return "-fx-background-color:" + toHex(bg()) + ";" +
               "-fx-text-fill:" + toHex(softOrange()) + ";" +
               "-fx-border-color:" + toHex(softOrange()) + ";" +
               "-fx-border-radius:6;" + "-fx-background-radius:6;" +
               "-fx-padding:8 10;" + "-fx-cursor:hand;" +
               "-fx-font-size:12;" + "-fx-alignment:CENTER-LEFT;" +
               "-fx-effect:dropshadow(gaussian,#ea7c1460,8,0.2,0,2);";
    }

    public static String sideBtnHoverTeam() {
        return "-fx-background-color:" + toHex(bg()) + ";" +
               "-fx-text-fill:#93c5fd;" +
               "-fx-border-color:#3b82f6;" +
               "-fx-border-radius:6;" + "-fx-background-radius:6;" +
               "-fx-padding:8 10;" + "-fx-cursor:hand;" +
               "-fx-font-size:12;" + "-fx-alignment:CENTER-LEFT;" +
               "-fx-effect:dropshadow(gaussian,#2563eb60,8,0.2,0,2);";
    }
    public static String tableStyle() {
        return "-fx-base:" + toHex(panel()) + ";" +
               "-fx-background-color:" + toHex(panel()) + ";" +
               "-fx-control-inner-background:" + toHex(panel()) + ";" +
               "-fx-control-inner-background-alt:" + toHex(card()) + ";" +
               "-fx-table-cell-border-color:" + toHex(border()) + ";" +
               "-fx-border-color:" + toHex(border()) + ";" +
               "-fx-border-radius:6;-fx-background-radius:6;";
    }
}


// UI HELPER FACTORY

class FX {
    static Button btn(String text, Color bg) {
        Button b = new Button(text);
        b.setStyle(ThemeManager.btnStyle(bg));
        b.setMinWidth(140);
        b.setOnMouseEntered(e -> b.setStyle(ThemeManager.btnStyle(bg.brighter())));
        b.setOnMouseExited(e  -> b.setStyle(ThemeManager.btnStyle(bg)));
        addClickAnim(b);
        ThemeManager.addListener(() -> b.setStyle(ThemeManager.btnStyle(bg)));
        return b;
    }

    static void addClickAnim(Button b) {
        ScaleTransition st = new ScaleTransition(Duration.millis(80), b);
        b.setOnMousePressed(e  -> { st.setToX(0.95); st.setToY(0.95); st.playFromStart(); });
        b.setOnMouseReleased(e -> { st.setToX(1.00); st.setToY(1.00); st.playFromStart(); });
    }

    static Button sideBtnAdmin(String text) {
        Button b = new Button(text);
        b.setStyle(ThemeManager.sideBtn());
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnMouseEntered(e -> b.setStyle(ThemeManager.sideBtnHoverAdmin()));
        b.setOnMouseExited(e  -> b.setStyle(ThemeManager.sideBtn()));
        ThemeManager.addListener(() -> b.setStyle(ThemeManager.sideBtn()));
        return b;
    }

    static Button sideBtnTeam(String text) {
        Button b = new Button(text);
        b.setStyle(ThemeManager.sideBtn());
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnMouseEntered(e -> b.setStyle(ThemeManager.sideBtnHoverTeam()));
        b.setOnMouseExited(e  -> b.setStyle(ThemeManager.sideBtn()));
        ThemeManager.addListener(() -> b.setStyle(ThemeManager.sideBtn()));
        return b;
    }

    static Button sideBtn(String text) {
        Button b = new Button(text);
        b.setStyle(ThemeManager.sideBtn());
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnMouseEntered(e -> b.setStyle(ThemeManager.sideBtnHover()));
        b.setOnMouseExited(e  -> b.setStyle(ThemeManager.sideBtn()));
        ThemeManager.addListener(() -> b.setStyle(ThemeManager.sideBtn()));
        return b;
    }

    static Label label(String text, double size, boolean bold, Color c) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setTextFill(c);
        ThemeManager.addListener(() -> l.setTextFill(c));
        return l;
    }

    static Label dynLabel(String text, double size, boolean bold, Supplier<Color> colorFn) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setTextFill(colorFn.get());
        ThemeManager.addListener(() -> l.setTextFill(colorFn.get()));
        return l;
    }

    static TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(ThemeManager.fieldStyle());
        f.setMaxWidth(400);
        ThemeManager.addListener(() -> f.setStyle(ThemeManager.fieldStyle()));
        return f;
    }

    static PasswordField passField() {
        PasswordField f = new PasswordField();
        f.setStyle(ThemeManager.fieldStyle());
        f.setMaxWidth(400);
        ThemeManager.addListener(() -> f.setStyle(ThemeManager.fieldStyle()));
        return f;
    }

    static TextArea textArea(String prompt, boolean editable) {
        TextArea a = new TextArea();
        a.setPromptText(prompt);
        a.setEditable(editable);
        a.setWrapText(true);
        a.setStyle(ThemeManager.fieldStyle());
        ThemeManager.addListener(() -> a.setStyle(ThemeManager.fieldStyle()));
        return a;
    }

    static <T> ComboBox<T> combo() {
        ComboBox<T> c = new ComboBox<>();
        c.setStyle(ThemeManager.fieldStyle());
        c.setMaxWidth(400);
        ThemeManager.addListener(() -> c.setStyle(ThemeManager.fieldStyle()));
        return c;
    }

    static VBox card(Node... children) {
        VBox v = new VBox(10);
        v.setStyle(ThemeManager.cardStyle());
        v.setPadding(new Insets(14, 18, 14, 18));
        v.getChildren().addAll(children);
        ThemeManager.addListener(() -> v.setStyle(ThemeManager.cardStyle()));
        return v;
    }

    static VBox formRow(String labelText, Node input) {
        Label l = label(labelText, 12, false, ThemeManager.textSec());
        ThemeManager.addListener(() -> l.setTextFill(ThemeManager.textSec()));
        VBox v = new VBox(4, l, input);
        v.setFillWidth(true);
        return v;
    }

    static Region hSpacer() { Region r = new Region(); HBox.setHgrow(r, Priority.ALWAYS); return r; }
    static Region vSpacer() { Region r = new Region(); VBox.setVgrow(r, Priority.ALWAYS); return r; }

    static Separator sep() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + ThemeManager.toHex(ThemeManager.border()) + ";");
        return s;
    }

    static void fadeIn(Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), n);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    static void applyRootStyle(Region r) {
        r.setStyle(ThemeManager.rootStyle());
        ThemeManager.addListener(() -> r.setStyle(ThemeManager.rootStyle()));
    }

    static void applyPanelStyle(Region r) {
        r.setStyle(ThemeManager.panelStyle());
        ThemeManager.addListener(() -> r.setStyle(ThemeManager.panelStyle()));
    }

    static void applyCardStyle(Region r) {
        r.setStyle(ThemeManager.cardStyle());
        ThemeManager.addListener(() -> r.setStyle(ThemeManager.cardStyle()));
    }

    static void info(Stage owner, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.setTitle("Information");
        styleAlertImproved(a);
        if (owner != null) a.initOwner(owner);
        boolean wasMax = owner != null && owner.isMaximized();
        a.showAndWait();
        WindowKit.restore(owner, wasMax);
    }

    static void error(Stage owner, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.setTitle("Error");
        styleAlertImproved(a);
        if (owner != null) a.initOwner(owner);
        boolean wasMax = owner != null && owner.isMaximized();
        a.showAndWait();
        WindowKit.restore(owner, wasMax);
    }

    static boolean confirm(Stage owner, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.setTitle("Confirm");
        styleAlertImproved(a);
        if (owner != null) a.initOwner(owner);
        boolean wasMax = owner != null && owner.isMaximized();
        Optional<ButtonType> r = a.showAndWait();
        WindowKit.restore(owner, wasMax);
        return r.isPresent() && r.get() == ButtonType.YES;
    }

    private static void styleAlertImproved(Alert a) {
        DialogPane dp = a.getDialogPane();

        Color bgColor = ThemeManager.isDark() ? Color.web("#131E33") : Color.web("#FFFFFF");
        Color textColor = ThemeManager.isDark() ? Color.web("#f0f5ff") : Color.web("#0d111e");
        Color buttonBg = Color.web("#2563eb");

        dp.setStyle(
            "-fx-background-color:" + ThemeManager.toHex(bgColor) + ";" +
            "-fx-padding: 20px;" +
            "-fx-font-family: 'Segoe UI';"
        );

        dp.setContentText(a.getContentText());

        dp.lookupAll(".label").forEach(node -> {
            if (node instanceof Label) {
                Label lbl = (Label) node;
                lbl.setTextFill(textColor);
                lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
                lbl.setWrapText(true);
                lbl.setStyle(
                    "-fx-text-fill:" + ThemeManager.toHex(textColor) + ";" +
                    "-fx-font-family: 'Segoe UI';" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 8px;"
                );
            }
        });

        dp.lookupAll(".header-panel").forEach(node -> {
            node.setStyle("-fx-background-color: transparent;");
        });

        dp.lookupAll(".button-bar").forEach(node -> {
            node.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-padding: 16px 0 0 0;"
            );
        });

        dp.lookupAll(".button").forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                String btnText = btn.getText();

                Color btnColor = buttonBg;
                if (btnText != null && (btnText.contains("No") || btnText.contains("Cancel"))) {
                    btnColor = Color.web("#6b7280");
                } else if (btnText != null && (btnText.contains("OK") || btnText.contains("Yes"))) {
                    btnColor = Color.web("#16a34a");
                }

                applyButtonStyle(btn, btnColor, textColor);
            }
        });

        // Theme change listener
        Runnable updateAlert = () -> {
            Color newBg = ThemeManager.isDark() ? Color.web("#131E33") : Color.web("#FFFFFF");
            Color newText = ThemeManager.isDark() ? Color.web("#f0f5ff") : Color.web("#0d111e");

            dp.setStyle(
                "-fx-background-color:" + ThemeManager.toHex(newBg) + ";" +
                "-fx-padding: 20px;" +
                "-fx-font-family: 'Segoe UI';"
            );

            dp.lookupAll(".label").forEach(node -> {
                if (node instanceof Label) {
                    Label lbl = (Label) node;
                    lbl.setTextFill(newText);
                    lbl.setStyle(
                        "-fx-text-fill:" + ThemeManager.toHex(newText) + ";" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;"
                    );
                }
            });

            dp.lookupAll(".button").forEach(node -> {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    applyButtonStyle(btn, buttonBg, newText);
                }
            });
        };

        ThemeManager.addListener(updateAlert);
        a.setOnHidden(ev -> ThemeManager.removeListener(updateAlert));
    }

    private static void applyButtonStyle(Button btn, Color bgColor, Color textColor) {
        btn.setStyle(
            "-fx-background-color:" + ThemeManager.toHex(bgColor) + ";" +
            "-fx-text-fill:" + ThemeManager.toHex(Color.web("#ffffff")) + ";" +
            "-fx-padding: 10px 24px;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI';" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-border-radius: 6;"
        );

        btn.setOnMouseEntered(e ->
            btn.setStyle(
                "-fx-background-color:" + ThemeManager.toHex(bgColor.brighter()) + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 10px 24px;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0.3, 0, 2);"
            )
        );

        btn.setOnMouseExited(e ->
            btn.setStyle(
                "-fx-background-color:" + ThemeManager.toHex(bgColor) + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 10px 24px;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
            )
        );
    }

    // INCERIA LOGO

    static Node logoWidget(double height) {
        javafx.scene.image.Image img = null;
        String[] paths = {
            "file:sda.png",
            "file:src/sda.png",
            "file:resources/sda.png",
            "file:assets/sda.png"
        };
        for (String p : paths) {
            try {
                javafx.scene.image.Image candidate = new javafx.scene.image.Image(p, false);
                if (!candidate.isError()) { img = candidate; break; }
            } catch (Exception ignored) {}
        }

        if (img != null && !img.isError()) {
            javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
            iv.setFitHeight(height);
            iv.setFitWidth(height);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            Circle clip = new Circle(height / 2, height / 2, height / 2);
            iv.setClip(clip);

            Circle glow = new Circle(height / 2);
            glow.setFill(Color.web("#0e2240"));
            glow.setStroke(Color.web("#14b8a6"));
            glow.setStrokeWidth(1.5);
            glow.setEffect(new javafx.scene.effect.DropShadow(height * 0.6, Color.web("#14b8a688")));

            StackPane imgStack = new StackPane(glow, iv);
            imgStack.setPrefSize(height, height);
            imgStack.setMaxSize(height, height);

            Label brand = new Label("INCERIA");
            brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, Math.max(9, height * 0.32)));
            brand.setTextFill(Color.web("#14b8a6"));
            brand.setStyle("-fx-letter-spacing:3;");

            VBox box = new VBox(2, imgStack, brand);
            box.setAlignment(Pos.CENTER);
            return box;
        } else {
            // Fallback: drawn canvas logo
            double sz = height;
            Canvas c = new Canvas(sz, sz + height * 0.38);
            GraphicsContext g = c.getGraphicsContext2D();

            RadialGradient radGlow = new RadialGradient(0, 0, sz/2, sz/2, sz/2,
                false, CycleMethod.NO_CYCLE,
                new Stop(0.5, Color.web("#14b8a622")),
                new Stop(1.0, Color.TRANSPARENT));
            g.setFill(radGlow);
            g.fillOval(0, 0, sz, sz);

            g.setFill(Color.web("#0e2240"));
            g.fillOval(sz*0.05, sz*0.05, sz*0.90, sz*0.90);

            g.setStroke(Color.web("#14b8a6"));
            g.setLineWidth(sz * 0.04);
            g.strokeOval(sz*0.08, sz*0.08, sz*0.84, sz*0.84);

            double cx = sz/2, cy = sz/2 - sz*0.04;
            double pinH = sz * 0.38, pinW = sz * 0.22;
            g.setFill(Color.web("#14b8a6"));
            g.fillOval(cx - pinW/2, cy - pinH*0.45, pinW, pinH*0.6);
            g.beginPath();
            g.moveTo(cx - pinW/2, cy - pinH*0.15);
            g.lineTo(cx, cy + pinH*0.42);
            g.lineTo(cx + pinW/2, cy - pinH*0.15);
            g.setFill(Color.web("#14b8a6"));
            g.fill();
            g.setFill(Color.web("#0e2240"));
            g.fillOval(cx - pinW*0.22, cy - pinH*0.22, pinW*0.44, pinW*0.44);

            g.setFill(Color.web("#14b8a6"));
            g.setFont(Font.font("Segoe UI", FontWeight.BOLD, Math.max(8, sz * 0.22)));
            double tw = "INCERIA".length() * sz * 0.13;
            g.fillText("INCERIA", cx - tw / 2, sz + sz * 0.3);

            return c;
        }
    }

    static Color statusColor(IncidentStatus s) {
        if (s == null) return ThemeManager.textMuted();
        switch (s) {
            case REPORTED:      return Color.web("#dc2626");
            case PENDING:       return Color.web("#787878");
            case TEAM_ASSIGNED: return Color.web("#dcc814");
            case IN_PROGRESS:   return Color.web("#ea7c14");
            case UNDER_REVIEW:  return Color.web("#8c1edc");
            case RESOLVED:      return Color.web("#22c55e");
            default:            return ThemeManager.textMuted();
        }
    }
}


// DISPATCH BACKDROP - dark city-map background (grid, streets, parks, glowing
// alert pins) placed behind every pre-login / info page so the whole app
// matches the "Public Safety Dispatch" look.

class DispatchBackdrop extends Canvas {
    private static final String TRANSPARENT = "-fx-background-color:transparent;";
    private final Runnable redraw = this::draw;

    // street polylines in relative coordinates (x1,y1,x2,y2,...)
    private static final double[][] ROADS = {
        {0.00, 0.22, 0.18, 0.26, 0.38, 0.20, 0.62, 0.27, 0.82, 0.22, 1.00, 0.26},
        {0.00, 0.62, 0.22, 0.58, 0.45, 0.66, 0.70, 0.60, 1.00, 0.68},
        {0.12, 0.00, 0.16, 0.30, 0.10, 0.62, 0.14, 1.00},
        {0.34, 0.00, 0.38, 0.20, 0.45, 0.66, 0.42, 1.00},
        {0.66, 0.00, 0.62, 0.27, 0.70, 0.60, 0.66, 1.00},
        {0.90, 0.00, 0.82, 0.22, 0.88, 0.64, 0.92, 1.00},
        {0.00, 0.88, 0.30, 0.84, 0.60, 0.90, 1.00, 0.86},
        {0.18, 0.26, 0.45, 0.45, 0.70, 0.60},
        {0.38, 0.20, 0.55, 0.42, 0.82, 0.50}
    };
    // parks / green areas {cx, cy, rx, ry}
    private static final double[][] PARKS = {
        {0.05, 0.45, 0.07, 0.10},
        {0.55, 0.78, 0.09, 0.07},
        {0.93, 0.40, 0.06, 0.09},
        {0.28, 0.08, 0.06, 0.05}
    };
    // alert pins {x, y, colorIndex}
    private static final double[][] PINS = {
        {0.22, 0.30, 0}, {0.78, 0.27, 1}, {0.50, 0.58, 2},
        {0.84, 0.76, 0}, {0.14, 0.80, 1}, {0.60, 0.14, 2}
    };

    DispatchBackdrop() {
        setManaged(false);
        setMouseTransparent(true);
        widthProperty().addListener((o, a, b) -> draw());
        heightProperty().addListener((o, a, b) -> draw());
        sceneProperty().addListener((o, a, s) -> {
            if (s != null) { ThemeManager.addListener(redraw); draw(); }
            else ThemeManager.removeListener(redraw);
        });
    }

    /** Puts the backdrop behind the content and returns the new root. */
    static StackPane wrap(Region content) {
        DispatchBackdrop bd = new DispatchBackdrop();
        StackPane st = new StackPane(bd, content);
        bd.widthProperty().bind(st.widthProperty());
        bd.heightProperty().bind(st.heightProperty());
        return st;
    }

    /** Makes a region see-through so the backdrop shows (and stays so after theme toggles). */
    static void makeTransparent(Region r) {
        r.setStyle(TRANSPARENT);
        ThemeManager.addListener(() -> r.setStyle(TRANSPARENT));
    }

    private static Color a(Color c, double alpha) {
        return Color.color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    private void draw() {
        double w = getWidth(), h = getHeight();
        if (w < 2 || h < 2) return;
        GraphicsContext g = getGraphicsContext2D();
        boolean dk = ThemeManager.isDark();
        g.clearRect(0, 0, w, h);

        // base
        g.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, dk ? Color.web("#08111F") : Color.web("#E9EEF8")),
            new Stop(1, dk ? Color.web("#0C1A2E") : Color.web("#D8E2F1"))));
        g.fillRect(0, 0, w, h);

        // parks
        g.setFill(dk ? Color.rgb(30, 90, 60, 0.28) : Color.rgb(90, 170, 120, 0.25));
        for (double[] p : PARKS)
            g.fillOval((p[0] - p[2]) * w, (p[1] - p[3]) * h, p[2] * 2 * w, p[3] * 2 * h);

        // city blocks grid
        g.setStroke(dk ? Color.rgb(255, 255, 255, 0.035) : Color.rgb(20, 40, 80, 0.06));
        g.setLineWidth(1);
        for (double x = 0; x < w; x += 46) g.strokeLine(x + 0.5, 0, x + 0.5, h);
        for (double y = 0; y < h; y += 46) g.strokeLine(0, y + 0.5, w, y + 0.5);

        // streets
        g.setLineCap(StrokeLineCap.ROUND);
        g.setLineJoin(StrokeLineJoin.ROUND);
        for (double[] r : ROADS) {
            int n = r.length / 2;
            double[] xs = new double[n], ys = new double[n];
            for (int i = 0; i < n; i++) { xs[i] = r[2 * i] * w; ys[i] = r[2 * i + 1] * h; }
            g.setStroke(dk ? Color.rgb(120, 150, 200, 0.10) : Color.rgb(60, 90, 140, 0.13));
            g.setLineWidth(9);
            g.strokePolyline(xs, ys, n);
            g.setStroke(dk ? Color.rgb(160, 190, 235, 0.16) : Color.rgb(60, 90, 140, 0.22));
            g.setLineWidth(1);
            g.strokePolyline(xs, ys, n);
        }

        // alert pins
        Color[] cols = {Color.web("#E5484D"), Color.web("#F2A33A"), Color.web("#38A8F5")};
        double s = Math.max(9, Math.min(w, h) * 0.022);
        for (double[] p : PINS) pin(g, p[0] * w, p[1] * h, cols[(int) p[2]], s, dk);

        // vignette so foreground content stays readable
        g.setFill(new RadialGradient(0, 0, w / 2, h / 2, Math.max(w, h) * 0.75, false, CycleMethod.NO_CYCLE,
            new Stop(0.35, Color.TRANSPARENT),
            new Stop(1.0, dk ? Color.rgb(3, 8, 16, 0.65) : Color.rgb(200, 212, 235, 0.55))));
        g.fillRect(0, 0, w, h);
    }

    private void pin(GraphicsContext g, double x, double y, Color c, double s, boolean dk) {
        double k = dk ? 1.0 : 0.7;
        g.setFill(new RadialGradient(0, 0, x, y, s * 3.2, false, CycleMethod.NO_CYCLE,
            new Stop(0, a(c, 0.38 * k)), new Stop(1, a(c, 0))));
        g.fillOval(x - s * 3.2, y - s * 3.2, s * 6.4, s * 6.4);

        g.setLineWidth(1.4);
        g.setStroke(a(c, 0.50 * k));
        g.strokeOval(x - s * 1.7, y - s * 1.7, s * 3.4, s * 3.4);
        g.setStroke(a(c, 0.25 * k));
        g.strokeOval(x - s * 2.5, y - s * 2.5, s * 5.0, s * 5.0);

        double cy = y - s * 0.9;
        g.setFill(a(c, 0.95));
        g.fillOval(x - s, cy - s, s * 2, s * 2);
        g.fillPolygon(new double[]{x - s * 0.88, x + s * 0.88, x},
                      new double[]{cy + s * 0.45, cy + s * 0.45, y + s * 0.95}, 3);
        g.setFill(Color.web("#0B1220"));
        g.fillOval(x - s * 0.46, cy - s * 0.46, s * 0.92, s * 0.92);
        g.setFill(Color.WHITE);
        g.fillOval(x - s * 0.2, cy - s * 0.2, s * 0.4, s * 0.4);
    }
}


// WINDOW KIT - keeps a maximized owner maximized when its child window closes

class WindowKit {
    /** Remembers the owner's state when the child shows and restores it when the child closes. */
    static void keepOwnerState(Stage child) {
        Window o = child.getOwner();
        if (!(o instanceof Stage)) return;
        Stage owner = (Stage) o;
        boolean[] wasMax = {false};
        child.addEventHandler(WindowEvent.WINDOW_SHOWING, ev -> wasMax[0] = owner.isMaximized());
        child.addEventHandler(WindowEvent.WINDOW_HIDDEN, ev -> restore(owner, wasMax[0]));
    }

    static void restore(Window w, boolean wasMax) {
        if (!(w instanceof Stage)) return;
        Stage owner = (Stage) w;
        Platform.runLater(() -> {
            if (!owner.isShowing()) return;
            owner.setIconified(false);
            if (wasMax && !owner.isMaximized()) owner.setMaximized(true);
            owner.toFront();
        });
    }
}


// DESIGN KIT - landing-page style building blocks shared by dashboards and auth screens

class DashKit {
    static final Color[] STAT_COLORS = {
        Color.web("#5B8DEF"), Color.web("#F2A33A"), Color.web("#3DD68C"), Color.web("#E5484D")
    };

    static boolean dk() { return ThemeManager.isDark(); }
    static String hex(Color c) { return ThemeManager.toHex(c); }
    static String rgba(Color c, double a) {
        return String.format(Locale.US, "rgba(%d,%d,%d,%.3f)",
            (int) Math.round(c.getRed() * 255), (int) Math.round(c.getGreen() * 255),
            (int) Math.round(c.getBlue() * 255), a);
    }
    static Color surface()  { return ThemeManager.panel(); }
    static Color line()     { return ThemeManager.border(); }
    static Color ink()      { return ThemeManager.textPrim(); }
    static Color inkSoft()  { return ThemeManager.textSec(); }
    static Color inkMuted() { return ThemeManager.textMuted(); }
    static Color teal()     { return dk() ? Color.web("#2DB5A9") : Color.web("#0E8F86"); }
    static Color orange()   { return dk() ? Color.web("#F59232") : Color.web("#EA7C14"); }
    static Color blue()     { return dk() ? Color.web("#5B8DEF") : Color.web("#2563EB"); }
    static Color onAccent() { return dk() ? Color.web("#0B1220") : Color.WHITE; }

    /** Runs the styler now and again on every theme toggle. */
    static void bind(Runnable r) { r.run(); ThemeManager.addListener(r); }

    static Label lbl(String text, double size, FontWeight w, Supplier<Color> c) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", w, size));
        bind(() -> l.setTextFill(c.get()));
        return l;
    }

    static Label sectionLabel(String text) {
        Label l = lbl(text, 10, FontWeight.BOLD, DashKit::inkMuted);
        VBox.setMargin(l, new Insets(0, 0, 6, 4));
        return l;
    }

    // ---- buttons -------------------------------------------------------

    static Button themeButton() {
        Button b = new Button();
        b.setFocusTraversable(false);
        boolean[] hover = {false};
        Runnable r = () -> {
            b.setText(dk() ? "Light Mode" : "Dark Mode");
            b.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + hex(ink()) + ";" +
                "-fx-border-color:" + hex(hover[0] ? teal() : line()) + ";" +
                "-fx-border-width:1;-fx-border-radius:4;-fx-background-radius:4;" +
                "-fx-padding:7 16;-fx-font-family:'Segoe UI';-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-cursor:hand;");
        };
        bind(r);
        b.setOnMouseEntered(e -> { hover[0] = true;  r.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; r.run(); });
        b.setOnAction(e -> ThemeManager.toggle());
        return b;
    }

    static Button logoutButton(Runnable action) {
        Button b = new Button("Logout");
        b.setFocusTraversable(false);
        boolean[] hover = {false};
        Runnable r = () -> {
            String red = "#E5484D";
            b.setStyle(
                "-fx-background-color:" + (hover[0] ? red : "transparent") + ";" +
                "-fx-text-fill:" + (hover[0] ? "white" : red) + ";" +
                "-fx-border-color:" + red + ";" +
                "-fx-border-width:1;-fx-border-radius:4;-fx-background-radius:4;" +
                "-fx-padding:7 18;-fx-font-family:'Segoe UI';-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-cursor:hand;");
        };
        bind(r);
        b.setOnMouseEntered(e -> { hover[0] = true;  r.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; r.run(); });
        b.setOnAction(e -> action.run());
        return b;
    }

    static Button backButton(Supplier<Color> accent, Runnable action) {
        Button b = new Button("←  Back");
        b.setFocusTraversable(false);
        boolean[] hover = {false};
        Runnable r = () -> {
            Color a = accent.get();
            b.setStyle(
                "-fx-background-color:" + (hover[0] ? hex(a) : rgba(surface(), 0.85)) + ";" +
                "-fx-text-fill:" + (hover[0] ? hex(onAccent()) : hex(a)) + ";" +
                "-fx-border-color:" + hex(a) + ";" +
                "-fx-border-width:1.5;-fx-border-radius:20;-fx-background-radius:20;" +
                "-fx-padding:8 22;-fx-font-family:'Segoe UI';-fx-font-size:13px;-fx-font-weight:bold;" +
                "-fx-cursor:hand;");
        };
        bind(r);
        b.setOnMouseEntered(e -> { hover[0] = true;  r.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; r.run(); });
        b.setOnAction(e -> action.run());
        return b;
    }

    static Button navBtn(String label, Supplier<Color> accent, Runnable action) {
        Button b = new Button(label);
        b.setFocusTraversable(false);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        boolean[] hover = {false};
        Runnable r = () -> {
            Color a = accent.get();
            boolean h = hover[0];
            b.setStyle(
                "-fx-background-color:" + (h ? rgba(a, 0.14) : "transparent") + ";" +
                "-fx-text-fill:" + hex(h ? ink() : inkSoft()) + ";" +
                "-fx-font-family:'Segoe UI';-fx-font-size:13px;" +
                "-fx-font-weight:" + (h ? "bold" : "normal") + ";" +
                "-fx-padding:11 14;-fx-cursor:hand;" +
                "-fx-background-radius:6;-fx-border-radius:6;" +
                "-fx-border-color:transparent transparent transparent " + (h ? hex(a) : "transparent") + ";" +
                "-fx-border-width:0 0 0 3;-fx-alignment:CENTER-LEFT;");
        };
        bind(r);
        b.setOnMouseEntered(e -> { hover[0] = true;  r.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; r.run(); });
        b.setOnAction(e -> action.run());
        return b;
    }

    // ---- header / sidebar / status bar ---------------------------------

    static Node header(String roleTag, String title, String userInfo, Supplier<Color> accent, Runnable onLogout) {
        Node logo = FX.logoWidget(32);
        Region divider = new Region();
        divider.setMinSize(1, 30);
        divider.setPrefSize(1, 30);
        divider.setMaxSize(1, 30);
        bind(() -> divider.setStyle("-fx-background-color:" + hex(line()) + ";"));

        Label tag = lbl(roleTag, 10.5, FontWeight.BOLD, accent);
        Label t = lbl(title, 17, FontWeight.BOLD, DashKit::ink);
        HBox brand = new HBox(14, logo, divider, new VBox(1, tag, t));
        brand.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(4, Color.web("#3DD68C"));
        Label user = lbl(userInfo, 12, FontWeight.NORMAL, DashKit::inkSoft);
        HBox chip = new HBox(8, dot, user);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setPadding(new Insets(7, 14, 7, 14));
        bind(() -> chip.setStyle(
            "-fx-background-color:" + rgba(accent.get(), 0.10) + ";" +
            "-fx-background-radius:20;-fx-border-radius:20;" +
            "-fx-border-color:" + rgba(accent.get(), 0.40) + ";-fx-border-width:1;"));

        HBox header = new HBox(14, brand, FX.hSpacer(), chip, themeButton(), logoutButton(onLogout));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8, 24, 8, 22));
        bind(() -> header.setStyle(
            "-fx-background-color:" + rgba(surface(), 0.95) + ";" +
            "-fx-border-color:" + hex(accent.get()) + " transparent " + hex(line()) + " transparent;" +
            "-fx-border-width:3 0 1 0;"));
        return header;
    }

    static VBox sidebarShell(double width) {
        VBox side = new VBox(6);
        side.setPadding(new Insets(22, 14, 18, 14));
        side.setPrefWidth(width);
        side.setMinWidth(width);
        bind(() -> side.setStyle(
            "-fx-background-color:" + rgba(surface(), 0.95) + ";" +
            "-fx-border-color:transparent " + hex(line()) + " transparent transparent;" +
            "-fx-border-width:0 1 0 0;"));
        return side;
    }

    static VBox infoCard(String tag, Supplier<Color> accent, String... lines) {
        VBox card = new VBox(3);
        card.setPadding(new Insets(12, 14, 12, 14));
        card.getChildren().add(lbl(tag, 10, FontWeight.BOLD, accent));
        for (int i = 0; i < lines.length; i++) {
            Label l;
            if (i == 0) l = lbl(lines[i], 13, FontWeight.BOLD, DashKit::ink);
            else        l = lbl(lines[i], 11.5, FontWeight.NORMAL, DashKit::inkSoft);
            l.setWrapText(true);
            card.getChildren().add(l);
        }
        bind(() -> card.setStyle(
            "-fx-background-color:" + rgba(accent.get(), 0.10) + ";" +
            "-fx-background-radius:8;-fx-border-radius:8;" +
            "-fx-border-color:" + rgba(accent.get(), 0.40) + ";-fx-border-width:1;"));
        return card;
    }

    static HBox statusBar(Label msg) {
        Circle dot = new Circle(4, Color.web("#3DD68C"));
        FadeTransition ft = new FadeTransition(Duration.millis(900), dot);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setAutoReverse(true);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.play();
        Label right = lbl("INCERIA  |  LIVE", 10, FontWeight.BOLD, DashKit::inkMuted);
        HBox sb = new HBox(8, dot, msg, FX.hSpacer(), right);
        sb.setAlignment(Pos.CENTER_LEFT);
        sb.setPadding(new Insets(7, 18, 7, 18));
        bind(() -> sb.setStyle(
            "-fx-background-color:" + rgba(surface(), 0.95) + ";" +
            "-fx-border-color:" + hex(line()) + " transparent transparent transparent;" +
            "-fx-border-width:1 0 0 0;"));
        return sb;
    }

    // ---- content blocks ------------------------------------------------

    static Node banner(String eyebrow, String title, String sub, Supplier<Color> accent) {
        Label e = lbl(eyebrow, 11, FontWeight.BOLD, () -> Color.web("#5CD6CB"));
        Label t = lbl(title, 26, FontWeight.BOLD, () -> Color.WHITE);
        t.setWrapText(true);
        Label s = lbl(sub, 13.5, FontWeight.NORMAL, () -> Color.web("#C5D3E8"));
        s.setWrapText(true);
        VBox text = new VBox(6, e, t, s);
        text.setPadding(new Insets(22, 30, 22, 30));
        text.setMaxWidth(760);

        StackPane st = new StackPane(text);
        st.setAlignment(Pos.CENTER_LEFT);

        Group rings = new Group();
        for (double r : new double[]{50, 90, 130, 170}) {
            Circle c = new Circle(r);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.rgb(255, 255, 255, 0.08));
            c.setStrokeWidth(1.2);
            rings.getChildren().add(c);
        }
        rings.setManaged(false);
        rings.setMouseTransparent(true);
        rings.layoutXProperty().bind(st.widthProperty().subtract(40));
        rings.layoutYProperty().bind(st.heightProperty().divide(2));
        st.getChildren().add(0, rings);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        clip.widthProperty().bind(st.widthProperty());
        clip.heightProperty().bind(st.heightProperty());
        st.setClip(clip);

        bind(() -> st.setStyle(
            "-fx-background-color:" + (dk()
                ? "linear-gradient(to bottom right, rgba(8,18,31,0.90), rgba(13,35,64,0.86));"
                : "linear-gradient(to bottom right, rgba(11,37,69,0.95), rgba(19,64,116,0.90));") +
            "-fx-background-radius:8;" +
            "-fx-border-color:transparent transparent " + hex(accent.get()) + " transparent;" +
            "-fx-border-width:0 0 3 0;"));
        return st;
    }

    static HBox statRow(Label[] out, String[] names, Color[] cols) {
        HBox row = new HBox(14);
        for (int i = 0; i < names.length; i++) {
            Label n = lbl(names[i].toUpperCase(), 10.5, FontWeight.BOLD, DashKit::inkMuted);
            Label v = lbl("0", 28, FontWeight.BOLD, DashKit::ink);
            out[i] = v;
            VBox tile = new VBox(2, n, v);
            tile.setPadding(new Insets(14, 18, 14, 18));
            tile.setPrefWidth(100);
            tile.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(tile, Priority.ALWAYS);
            Color c = cols[i];
            bind(() -> tile.setStyle(
                "-fx-background-color:" + rgba(surface(), 0.96) + ";" +
                "-fx-background-radius:6;-fx-border-radius:6;" +
                "-fx-border-color:" + hex(line()) + " " + hex(line()) + " " + hex(line()) + " " + hex(c) + ";" +
                "-fx-border-width:1 1 1 4;" +
                "-fx-effect:dropshadow(gaussian," + (dk() ? "rgba(0,0,0,0.35)" : "rgba(11,37,69,0.10)") + ",10,0.1,0,2);"));
            row.getChildren().add(tile);
        }
        return row;
    }

    static void updateStats(Label[] v, List<Incident> list) {
        int total = list.size(), resolved = 0, disputes = 0;
        for (Incident i : list) {
            if (i.getStatus() == IncidentStatus.RESOLVED) resolved++;
            if (i.isDisputeRaised() && !i.isDisputeResolved()) disputes++;
        }
        v[0].setText(String.valueOf(total));
        v[1].setText(String.valueOf(total - resolved));
        v[2].setText(String.valueOf(resolved));
        v[3].setText(String.valueOf(disputes));
    }

    static VBox panelCard(String title, Supplier<Color> accent, Node content) {
        Rectangle bar = new Rectangle(4, 16);
        bar.setArcWidth(2);
        bar.setArcHeight(2);
        bind(() -> bar.setFill(accent.get()));
        Label t = lbl(title, 13, FontWeight.BOLD, DashKit::ink);
        HBox head = new HBox(10, bar, t);
        head.setAlignment(Pos.CENTER_LEFT);
        VBox card = new VBox(12, head, content);
        card.setPadding(new Insets(16, 18, 16, 18));
        VBox.setVgrow(content, Priority.ALWAYS);
        bind(() -> card.setStyle(
            "-fx-background-color:" + rgba(surface(), 0.96) + ";" +
            "-fx-background-radius:8;-fx-border-radius:8;" +
            "-fx-border-color:" + hex(line()) + ";-fx-border-width:1;" +
            "-fx-effect:dropshadow(gaussian," + (dk() ? "rgba(0,0,0,0.40)" : "rgba(11,37,69,0.12)") + ",14,0.1,0,3);"));
        return card;
    }

    static VBox mapCard(SystemFacade facade, Supplier<Color> accent) {
        MapCanvas map = new MapCanvas(facade, 900, 300);
        Pane mp = new Pane(map);
        mp.setMinSize(0, 0);
        mp.setPrefHeight(300);
        map.widthProperty().bind(mp.widthProperty());
        map.heightProperty().bind(mp.heightProperty());
        map.widthProperty().addListener((o, a, b) -> map.draw());
        map.heightProperty().addListener((o, a, b) -> map.draw());
        Rectangle clip = new Rectangle();
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        clip.widthProperty().bind(mp.widthProperty());
        clip.heightProperty().bind(mp.heightProperty());
        mp.setClip(clip);
        VBox card = panelCard("Live Incident Map", accent, mp);
        card.setMinHeight(280);
        card.setPrefHeight(360);
        return card;
    }

    static ScrollPane centerColumn(Node... items) {
        VBox v = new VBox(16, items);
        v.setPadding(new Insets(20, 24, 20, 24));
        v.setStyle("-fx-background-color:transparent;");
        ScrollPane sp = new ScrollPane(v);
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-border-color:transparent;");
        return sp;
    }
}


// AUTH KIT - full-screen, landing-style login / registration pages

class AuthKit {
    static void bigField(TextInputControl f) {
        f.setMaxWidth(Double.MAX_VALUE);
        DashKit.bind(() -> f.setStyle(ThemeManager.fieldStyle() +
            "-fx-font-family:'Segoe UI';-fx-font-size:15px;-fx-padding:12 14;" +
            "-fx-prompt-text-fill:" + DashKit.hex(DashKit.inkMuted()) + ";"));
    }

    static VBox row(String label, Node field) {
        Label l = DashKit.lbl(label, 13, FontWeight.SEMI_BOLD, DashKit::inkSoft);
        VBox v = new VBox(7, l, field);
        v.setFillWidth(true);
        return v;
    }

    static Button primaryButton(String text, Supplier<Color> accent, Runnable action) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        boolean[] hover = {false};
        Runnable r = () -> {
            Color a = accent.get();
            Color bg = hover[0] ? a.interpolate(Color.BLACK, 0.15) : a;
            b.setStyle(
                "-fx-background-color:" + DashKit.hex(bg) + ";" +
                "-fx-text-fill:" + DashKit.hex(DashKit.onAccent()) + ";" +
                "-fx-font-family:'Segoe UI';-fx-font-size:15px;-fx-font-weight:bold;" +
                "-fx-background-radius:6;-fx-padding:14 0;-fx-cursor:hand;" +
                "-fx-effect:dropshadow(gaussian," + DashKit.rgba(a, hover[0] ? 0.55 : 0.35) + ",14,0.2,0,4);");
        };
        DashKit.bind(r);
        b.setOnMouseEntered(e -> { hover[0] = true;  r.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; r.run(); });
        FX.addClickAnim(b);
        b.setOnAction(e -> action.run());
        return b;
    }

    static VBox card(Supplier<Color> accent, String tag, String title, String sub, Node... body) {
        Node logo = FX.logoWidget(48);
        HBox logoRow = new HBox(logo);
        logoRow.setAlignment(Pos.CENTER_LEFT);
        Label tagL = DashKit.lbl(tag, 12, FontWeight.BOLD, accent);
        Label t = DashKit.lbl(title, 30, FontWeight.BOLD, DashKit::ink);
        t.setWrapText(true);
        Rectangle bar = new Rectangle(48, 3);
        DashKit.bind(() -> bar.setFill(accent.get()));
        Label s = DashKit.lbl(sub, 14, FontWeight.NORMAL, DashKit::inkSoft);
        s.setWrapText(true);
        VBox head = new VBox(8, logoRow, tagL, t, bar, s);

        VBox card = new VBox(20, head);
        card.getChildren().addAll(body);
        card.setPadding(new Insets(34, 44, 38, 44));
        card.setMaxHeight(Region.USE_PREF_SIZE);
        DashKit.bind(() -> {
            Color a = accent.get();
            String ln = DashKit.hex(DashKit.line());
            card.setStyle(
                "-fx-background-color:" + DashKit.rgba(DashKit.surface(), 0.97) + ";" +
                "-fx-background-radius:8;-fx-border-radius:8;" +
                "-fx-border-color:" + DashKit.hex(a) + " " + ln + " " + ln + " " + ln + ";" +
                "-fx-border-width:4 1 1 1;" +
                "-fx-effect:dropshadow(gaussian," + (DashKit.dk() ? "rgba(0,0,0,0.55)" : "rgba(11,37,69,0.18)") + ",28,0.1,0,8);");
        });
        return card;
    }

    /** Full-screen page: back button top-left, theme toggle top-right, card centred and sized to the window. */
    static Parent page(Supplier<Color> accent, Region card, double maxCardWidth, Runnable onBack) {
        Button back = DashKit.backButton(accent, onBack);
        HBox top = new HBox(12, back, FX.hSpacer(), DashKit.themeButton());
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(18, 28, 6, 28));

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(14, 28, 36, 28));
        ScrollPane sp = new ScrollPane(center);
        sp.setFitToWidth(true);
        sp.setFitToHeight(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-border-color:transparent;");

        BorderPane bp = new BorderPane();
        bp.setTop(top);
        bp.setCenter(sp);
        DispatchBackdrop.makeTransparent(bp);

        card.maxWidthProperty().bind(javafx.beans.binding.Bindings.createDoubleBinding(
            () -> Math.max(460.0, Math.min(maxCardWidth, bp.getWidth() * 0.40)), bp.widthProperty()));

        FX.fadeIn(bp);
        return DispatchBackdrop.wrap(bp);
    }
}


// LIVE MAP CANVAS

class MapCanvas extends Canvas implements SystemObserver {
    private static final double NODE_R = 22;
    private static final String[] NAMES = {"City Center","Green Valley","Western Bridge","Seon's Alley"};
    private final SystemFacade facade;

    public MapCanvas(SystemFacade facade, double w, double h) {
        super(w, h);
        this.facade = facade;
        facade.addObserver(this);
        ThemeManager.addListener(this::draw);
        draw();
    }

    public void draw() {
        GraphicsContext g = getGraphicsContext2D();
        double w = getWidth(), h = getHeight();
        g.clearRect(0, 0, w, h);

        g.setFill(ThemeManager.isDark() ? Color.web("#0a0e1a") : Color.web("#e8edf8"));
        g.fillRect(0, 0, w, h);

        g.setStroke(ThemeManager.isDark() ? Color.web("#16203480") : Color.web("#c0c8e080"));
        g.setLineWidth(1);
        for (double x = 0; x < w; x += 38) { g.beginPath(); g.moveTo(x,0); g.lineTo(x,h); g.stroke(); }
        for (double y = 0; y < h; y += 38) { g.beginPath(); g.moveTo(0,y); g.lineTo(w,y); g.stroke(); }

        g.setFill(ThemeManager.accentTeal());
        g.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        g.fillText("LIVE INCIDENT MAP", 14, 20);

        List<MapPoint> pts = new ArrayList<>(facade.getAllMapPoints());
        double[] px = new double[pts.size()], py = new double[pts.size()];
        for (int i = 0; i < pts.size(); i++) {
            px[i] = clamp(pts.get(i).getRX() * w, NODE_R + 5, w - NODE_R - 5);
            py[i] = clamp(pts.get(i).getRY() * h, NODE_R + 5, h - NODE_R - 5);
        }

        drawRoads(g, pts, px, py);

        for (int i = 0; i < pts.size(); i++) {
            MapPoint pt = pts.get(i);
            Color nc = awtToFx(pt.getColor());
            RadialGradient glow = new RadialGradient(0, 0, px[i], py[i], NODE_R + 12,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(nc.getRed(), nc.getGreen(), nc.getBlue(), 0.25)),
                new Stop(1, Color.TRANSPARENT));
            g.setFill(glow);
            g.fillOval(px[i]-NODE_R-12, py[i]-NODE_R-12, (NODE_R+12)*2, (NODE_R+12)*2);

            g.setFill(Color.color(nc.getRed()*0.6, nc.getGreen()*0.6, nc.getBlue()*0.6, 1.0));
            g.fillOval(px[i]-NODE_R, py[i]-NODE_R, NODE_R*2, NODE_R*2);
            g.setStroke(nc); g.setLineWidth(2.5);
            g.strokeOval(px[i]-NODE_R, py[i]-NODE_R, NODE_R*2, NODE_R*2);

            g.setFill(Color.WHITE); g.fillOval(px[i]-4, py[i]-4, 9, 9);

            String name = i < NAMES.length ? NAMES[i] : pt.getLocation().name();
            g.setFill(ThemeManager.textPrim());
            g.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            double tw = computeTextWidth(name, 11);
            g.fillText(name, px[i]-tw/2, py[i]+NODE_R+15);

            String badge = badge(pt.getCurrentStatus());
            g.setFill(nc);
            g.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
            double bw = computeTextWidth(badge, 10);
            g.fillText(badge, px[i]-bw/2, py[i]+NODE_R+28);
        }
    }

    private void drawRoads(GraphicsContext g, List<MapPoint> pts, double[] px, double[] py) {
        Color defaultRoad = ThemeManager.isDark() ? Color.web("#28416e80") : Color.web("#8090b080");
        g.setLineDashes(6, 5);
        g.setLineWidth(2);
        g.setStroke(defaultRoad);

        for (int i = 0; i < pts.size(); i++)
            for (int j = i + 1; j < pts.size(); j++) {
                g.beginPath(); g.moveTo(px[i],py[i]); g.lineTo(px[j],py[j]); g.stroke();
            }
        g.setLineDashes(null);

        boolean colored = false;
        for (int i = 0; i < pts.size() && !colored; i++) {
            MapPoint pt = pts.get(i);
            if (pt.getCurrentStatus() != IncidentStatus.RESOLVED && i + 1 < pts.size()) {
                Color nc = awtToFx(pt.getColor());
                g.setLineWidth(4);
                g.setStroke(nc);
                g.setLineDashes(null);
                g.beginPath(); g.moveTo(px[i], py[i]); g.lineTo(px[i+1], py[i+1]); g.stroke();
                colored = true;
            }
        }
        g.setLineDashes(null);
        g.setLineWidth(1);
    }

    private Color awtToFx(java.awt.Color c) {
        return Color.rgb(c.getRed(), c.getGreen(), c.getBlue());
    }
    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
    private double computeTextWidth(String s, double size) { return s.length() * size * 0.58; }
    private String badge(IncidentStatus s) {
        if (s == null || s == IncidentStatus.RESOLVED) return "SAFE";
        switch (s) {
            case REPORTED:      return "DANGER";
            case PENDING:       return "PENDING";
            case TEAM_ASSIGNED: return "ASSIGNED";
            case IN_PROGRESS:   return "ACTIVE";
            case UNDER_REVIEW:  return "REVIEW";
            default:            return "SAFE";
        }
    }

    @Override public void onIncidentUpdated(Incident i)    { Platform.runLater(this::draw); }
    @Override public void onMapUpdated(MapLocation l, IncidentStatus s) { Platform.runLater(this::draw); }
    @Override public void onDisputeRaised(Incident i)      { Platform.runLater(this::draw); }
}

// Shared legend panel builder - used in all sidebars

class LegendPanel {
    static VBox build() {
        VBox leg = new VBox(6);
        leg.setStyle(ThemeManager.cardStyle());
        leg.setPadding(new Insets(10, 12, 10, 12));
        ThemeManager.addListener(() -> leg.setStyle(ThemeManager.cardStyle()));

        Label title = new Label("MAP STATUS");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        title.setTextFill(ThemeManager.textMuted());
        ThemeManager.addListener(() -> title.setTextFill(ThemeManager.textMuted()));
        leg.getChildren().add(title);

        String[][] legend = {
            {"#dc1e1e","Reported"},
            {"#787878","Pending"},
            {"#dcc814","Assigned"},
            {"#e68c14","In Progress"},
            {"#8c1edc","Under Review"},
            {"#22c55e","Resolved"}
        };
        for (String[] l : legend) {
            HBox row = new HBox(6);
            row.setAlignment(Pos.CENTER_LEFT);
            Circle dot = new Circle(5, Color.web(l[0]));
            Label lbl = new Label(l[1]);
            lbl.setFont(Font.font("Segoe UI", 11));
            lbl.setTextFill(Color.web(l[0]));
            row.getChildren().addAll(dot, lbl);
            leg.getChildren().add(row);
        }
        return leg;
    }
}


// TOP BAR (reusable) - with theme toggle

class TopBar extends HBox {
    private final Button themeToggle;

    public TopBar(String title, String info, Runnable onLogout) {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 20, 10, 20));
        setSpacing(16);
        FX.applyPanelStyle(this);
        setStyle(getStyle() + "-fx-border-color:" + ThemeManager.toHex(ThemeManager.border()) +
                 ";-fx-border-width:0 0 1 0;");
        ThemeManager.addListener(() -> setStyle(ThemeManager.panelStyle() +
            "-fx-border-color:" + ThemeManager.toHex(ThemeManager.border()) + ";-fx-border-width:0 0 1 0;"));

        Label titleLbl = FX.dynLabel(title, 15, true, ThemeManager::textPrim);
        Label infoLbl  = FX.dynLabel(info,  12, false, ThemeManager::textPrim);

        themeToggle = FX.btn(
            ThemeManager.isDark() ? "\u2600 Light" : "\u263E Dark",
            ThemeManager.isDark()
                ? Color.web("#4a148c")
                : Color.web("#ffffff")
        );
        themeToggle.setMinWidth(90);
        themeToggle.setOnAction(e -> {
            ThemeManager.toggle();
            themeToggle.setText(ThemeManager.isDark() ? "\u2600 Light" : "\u263E Dark");
        });

        Button logout = FX.btn("Logout", Color.web("#d32f2f"));

        Runnable updateLogout = () -> {
            logout.setStyle(
                "-fx-background-color: " + (ThemeManager.isDark() ? "#ffffff" : "#d32f2f") + ";" +
                "-fx-text-fill: " + (ThemeManager.isDark() ? "#000000" : "#ffffff") + ";" +
                "-fx-background-radius: 6;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
            );
        };

        updateLogout.run();
        ThemeManager.addListener(updateLogout);

        logout.setMinWidth(80);
        logout.setOnAction(e -> onLogout.run());

        Node logo = FX.logoWidget(36);
        getChildren().addAll(logo, titleLbl, FX.hSpacer(), infoLbl, FX.hSpacer(), themeToggle, logout);
    }
}


// ROLE SELECTION SCREEN (landing page)

class RoleSelectionScreen extends Stage {
    private final SystemFacade facade = SystemFacade.getInstance();

    // Local re-style hooks: every themed node registers a Runnable here and
    // all of them are re-run whenever ThemeManager.toggle() fires.
    private final List<Runnable> themed = new ArrayList<>();
    private final Runnable themeListener = this::applyTheme;

    private BorderPane shell;
    private ScrollPane scroll;
    private Node rolesSection;
    private Canvas mapCanvas;
    private Circle liveDot;
    private Timeline pulseTimeline;
    private double pulsePhase = 0;
    private int activeNav = 0;
    private boolean themeBusy = false;

    public RoleSelectionScreen() {
        setTitle("INCERIA - Emergency Response & Dispatch System");
        setMinWidth(860);
        setMinHeight(600);
        setWidth(1180);
        setHeight(800);
        setMaximized(true);
        initStyle(StageStyle.DECORATED);
        buildUI();
    }

    // ------------------------------------------------------------------
    // PALETTE (landing page only - ThemeManager itself is untouched)
    // ------------------------------------------------------------------
    private static boolean dk()      { return ThemeManager.isDark(); }
    private static Color pageBg()    { return dk() ? Color.web("#0B1220") : Color.web("#F3F5F9"); }
    private static Color surface()   { return dk() ? Color.web("#131E33") : Color.WHITE; }
    private static Color line()      { return dk() ? Color.web("#26354F") : Color.web("#D6DCE7"); }
    private static Color ink()       { return dk() ? Color.web("#EAF0FA") : Color.web("#0B2545"); }
    private static Color inkSoft()   { return dk() ? Color.web("#9FB0C8") : Color.web("#475A75"); }
    private static Color inkMuted()  { return dk() ? Color.web("#6B7C96") : Color.web("#7A8AA3"); }
    private static Color teal()      { return dk() ? Color.web("#2DB5A9") : Color.web("#0E8F86"); }
    private static Color navyAccent(){ return dk() ? Color.web("#5B8DEF") : Color.web("#1E4FA3"); }
    private static Color alertAccent(){ return dk() ? Color.web("#F08A5D") : Color.web("#D9531E"); }
    private static Color orangeAccent(){ return dk() ? Color.web("#F59232") : Color.web("#EA7C14"); }
    private static Color blueAccent()  { return dk() ? Color.web("#5B8DEF") : Color.web("#2563EB"); }
    private static Color onAccent()  { return dk() ? Color.web("#0B1220") : Color.WHITE; }

    private static String hex(Color c) { return ThemeManager.toHex(c); }
    private static String rgba(Color c) {
        return String.format(Locale.US, "rgba(%d,%d,%d,%.3f)",
            (int) Math.round(c.getRed() * 255), (int) Math.round(c.getGreen() * 255),
            (int) Math.round(c.getBlue() * 255), c.getOpacity());
    }
    private static Color withAlpha(Color c, double a) {
        return Color.color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    private void applyTheme() {
        for (Runnable r : themed) r.run();
    }

    // ------------------------------------------------------------------
    // SMALL FACTORIES
    // ------------------------------------------------------------------
    private Label text(String s, double size, FontWeight w, Supplier<Color> c) {
        Label l = new Label(s);
        l.setFont(Font.font("Segoe UI", w, size));
        Runnable r = () -> l.setTextFill(c.get());
        r.run();
        themed.add(r);
        return l;
    }

    private static String btnCss(Color bg, Color fg, Color border) {
        return "-fx-background-color:" + hex(bg) + ";" +
               "-fx-text-fill:" + hex(fg) + ";" +
               "-fx-border-color:" + hex(border) + ";" +
               "-fx-border-width:1;" +
               "-fx-background-radius:4;-fx-border-radius:4;" +
               "-fx-padding:10 20;" +
               "-fx-font-family:'Segoe UI';-fx-font-size:13px;-fx-font-weight:bold;" +
               "-fx-cursor:hand;" +
               "-fx-focus-color:transparent;-fx-faint-focus-color:transparent;";
    }

    /** Button whose background colour animates smoothly between base and hover. */
    private Button actionButton(String label, Supplier<Color> base, Supplier<Color> hover,
                                Supplier<Color> fg, Supplier<Color> border) {
        Button b = new Button(label);
        DoubleProperty mix = new SimpleDoubleProperty(0);
        Runnable restyle = () ->
            b.setStyle(btnCss(base.get().interpolate(hover.get(), mix.get()), fg.get(), border.get()));
        mix.addListener((o, a, n) -> restyle.run());
        restyle.run();
        themed.add(restyle);

        Timeline tl = new Timeline();
        b.setOnMouseEntered(e -> {
            tl.stop();
            tl.getKeyFrames().setAll(new KeyFrame(Duration.millis(150), new KeyValue(mix, 1.0)));
            tl.play();
        });
        b.setOnMouseExited(e -> {
            tl.stop();
            tl.getKeyFrames().setAll(new KeyFrame(Duration.millis(150), new KeyValue(mix, 0.0)));
            tl.play();
        });
        FX.addClickAnim(b);
        return b;
    }

    /** Subtle elevation (shadow + 4px lift) on hover. */
    private void addHoverLift(Region node) {
        javafx.scene.effect.DropShadow ds = new javafx.scene.effect.DropShadow();
        ds.setRadius(10);
        ds.setOffsetY(2);
        themed.add(() -> ds.setColor(dk() ? Color.rgb(0, 0, 0, 0.45) : Color.rgb(11, 37, 69, 0.14)));
        node.setEffect(ds);

        Timeline tl = new Timeline();
        node.setOnMouseEntered(e -> lift(tl, node, ds, true));
        node.setOnMouseExited(e -> lift(tl, node, ds, false));
    }

    private void lift(Timeline tl, Region node, javafx.scene.effect.DropShadow ds, boolean up) {
        tl.stop();
        tl.getKeyFrames().setAll(new KeyFrame(Duration.millis(170),
            new KeyValue(node.translateYProperty(), up ? -4.0 : 0.0, Interpolator.EASE_OUT),
            new KeyValue(ds.radiusProperty(),       up ? 26.0 : 10.0, Interpolator.EASE_OUT),
            new KeyValue(ds.offsetYProperty(),      up ? 8.0 : 2.0,  Interpolator.EASE_OUT)));
        tl.play();
    }

    private VBox featureList(String[] items, Supplier<Color> textColor, Supplier<Color> markColor,
                             double size, double gap) {
        VBox box = new VBox(gap);
        for (String it : items) {
            Rectangle mark = new Rectangle(5, 5);
            mark.setRotate(45);
            Runnable r = () -> mark.setFill(markColor.get());
            r.run();
            themed.add(r);
            Label l = text(it, size, FontWeight.NORMAL, textColor);
            HBox row = new HBox(11, mark, l);
            row.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().add(row);
        }
        return box;
    }

    /** Small drawn role glyph (no emoji): 0 = location pin, 1 = bars, 2 = shield. */
    private Node iconTile(int kind, Supplier<Color> fg, Supplier<Color> tileBg) {
        Pane glyph = new Pane();
        glyph.setPrefSize(36, 36);
        glyph.setMinSize(36, 36);
        glyph.setMaxSize(36, 36);

        if (kind == 0) {
            Circle ring = new Circle(18, 14, 7.5);
            ring.setFill(Color.TRANSPARENT);
            ring.setStrokeWidth(3);
            Polygon tail = new Polygon(11.5, 18.5, 24.5, 18.5, 18.0, 30.0);
            Circle dot = new Circle(18, 14, 2.2);
            themed.add(() -> { ring.setStroke(fg.get()); tail.setFill(fg.get()); dot.setFill(fg.get()); });
            glyph.getChildren().addAll(tail, ring, dot);
        } else if (kind == 1) {
            Rectangle b1 = new Rectangle(8, 19, 6, 10);
            Rectangle b2 = new Rectangle(15, 12, 6, 17);
            Rectangle b3 = new Rectangle(22, 6, 6, 23);
            for (Rectangle r : new Rectangle[]{b1, b2, b3}) { r.setArcWidth(2); r.setArcHeight(2); }
            themed.add(() -> { Color c = fg.get(); b1.setFill(c); b2.setFill(c); b3.setFill(c); });
            glyph.getChildren().addAll(b1, b2, b3);
        } else {
            Polygon shield = new Polygon(18, 5, 28, 9, 28, 18, 18, 30, 8, 18, 8, 9);
            Rectangle v = new Rectangle(16.5, 11, 3, 11);
            Rectangle h = new Rectangle(12.5, 15, 11, 3);
            v.setFill(Color.WHITE);
            h.setFill(Color.WHITE);
            themed.add(() -> shield.setFill(fg.get()));
            glyph.getChildren().addAll(shield, v, h);
        }

        StackPane tile = new StackPane(glyph);
        tile.setPrefSize(46, 46);
        tile.setMinSize(46, 46);
        tile.setMaxSize(46, 46);
        themed.add(() -> tile.setStyle("-fx-background-color:" + rgba(tileBg.get()) + ";-fx-background-radius:6;"));
        return tile;
    }

    // ------------------------------------------------------------------
    // PAGE ASSEMBLY
    // ------------------------------------------------------------------
    private void buildUI() {
        shell = new BorderPane();
        shell.setTop(buildHeader());
        scroll = buildBody();
        shell.setCenter(scroll);
        shell.setBottom(buildFooter());
        // transparent shell so the dispatch-map backdrop shows through
        themed.add(() -> shell.setStyle("-fx-background-color:transparent;"));

        StackPane stack = DispatchBackdrop.wrap(shell);
        Scene scene = new Scene(stack);
        themed.add(() -> scene.setFill(pageBg()));

        applyTheme();
        ThemeManager.addListener(themeListener);

        shell.setOpacity(0);
        setOnShown(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(450), shell);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            pulseTimeline.play();
        });
        setOnHidden(e -> {
            pulseTimeline.stop();
            ThemeManager.removeListener(themeListener);
        });
        setScene(scene);
    }

    // ------------------------------------------------------------------
    // HEADER
    // ------------------------------------------------------------------
    private Node buildHeader() {
        Node logo = FX.logoWidget(32);

        Region divider = new Region();
        divider.setMinSize(1, 26);
        divider.setPrefSize(1, 26);
        divider.setMaxSize(1, 26);
        themed.add(() -> divider.setStyle("-fx-background-color:" + hex(line()) + ";"));

        Label tag = text("Emergency Management Portal", 12, FontWeight.NORMAL, RoleSelectionScreen::inkSoft);
        HBox brand = new HBox(14, logo, divider, tag);
        brand.setAlignment(Pos.CENTER_LEFT);

        // About / Emergency Services / Contact open full pages (not dialog boxes)
        HBox nav = new HBox(2,
            navButton("Home", 0, () -> { activeNav = 0; applyTheme(); scrollTo(null); }),
            navButton("About", 1, () -> new InfoPageScreen(this, InfoPageScreen.ABOUT).show()),
            navButton("Emergency Services", 2, () -> new InfoPageScreen(this, InfoPageScreen.SERVICES).show()),
            navButton("Contact", 3, () -> new InfoPageScreen(this, InfoPageScreen.CONTACT).show()));
        nav.setAlignment(Pos.CENTER);

        Button themeBtn = new Button();
        themeBtn.setFocusTraversable(false);
        boolean[] hover = {false};
        Runnable restyle = () -> {
            themeBtn.setText(ThemeManager.isDark() ? "Light Mode" : "Dark Mode");
            themeBtn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + hex(ink()) + ";" +
                "-fx-border-color:" + hex(hover[0] ? teal() : line()) + ";" +
                "-fx-border-width:1;-fx-border-radius:4;-fx-background-radius:4;" +
                "-fx-padding:7 16;-fx-font-family:'Segoe UI';-fx-font-size:12px;-fx-font-weight:bold;" +
                "-fx-cursor:hand;");
        };
        themed.add(restyle);
        themeBtn.setOnMouseEntered(e -> { hover[0] = true;  restyle.run(); });
        themeBtn.setOnMouseExited(e  -> { hover[0] = false; restyle.run(); });
        themeBtn.setOnAction(e -> toggleThemeSmoothly());

        HBox header = new HBox(20, brand, FX.hSpacer(), nav, FX.hSpacer(), themeBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8, 28, 8, 24));
        themed.add(() -> header.setStyle(
            "-fx-background-color:" + rgba(withAlpha(surface(), 0.92)) + ";" +
            "-fx-border-color:transparent transparent " + hex(line()) + " transparent;" +
            "-fx-border-width:0 0 1 0;"));
        return header;
    }

    private Button navButton(String label, int idx, Runnable action) {
        Button b = new Button(label);
        b.setFocusTraversable(false);
        boolean[] hover = {false};
        Runnable restyle = () -> {
            boolean active = activeNav == idx;
            Color fg = (active || hover[0]) ? ink() : inkSoft();
            String underline = active ? hex(teal()) : (hover[0] ? hex(line()) : "transparent");
            b.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:" + hex(fg) + ";" +
                "-fx-font-family:'Segoe UI';-fx-font-size:13px;" +
                "-fx-font-weight:" + (active ? "bold" : "normal") + ";" +
                "-fx-padding:10 14;-fx-cursor:hand;" +
                "-fx-background-radius:0;-fx-border-radius:0;" +
                "-fx-border-color:transparent transparent " + underline + " transparent;" +
                "-fx-border-width:0 0 2 0;");
        };
        themed.add(restyle);
        b.setOnMouseEntered(e -> { hover[0] = true;  restyle.run(); });
        b.setOnMouseExited(e  -> { hover[0] = false; restyle.run(); });
        b.setOnAction(e -> action.run());
        return b;
    }

    private void toggleThemeSmoothly() {
        if (themeBusy) return;
        themeBusy = true;
        FadeTransition out = new FadeTransition(Duration.millis(110), shell);
        out.setToValue(0.25);
        out.setOnFinished(e -> {
            ThemeManager.toggle();           // fires every listener, including applyTheme()
            FadeTransition in = new FadeTransition(Duration.millis(260), shell);
            in.setToValue(1.0);
            in.setOnFinished(ev -> themeBusy = false);
            in.play();
        });
        out.play();
    }

    private void scrollTo(Node target) {
        Node content = scroll.getContent();
        double max = content.getBoundsInLocal().getHeight() - scroll.getViewportBounds().getHeight();
        double y = (target == null) ? 0 : Math.max(0, target.getBoundsInParent().getMinY() - 16);
        double v = (max <= 0) ? 0 : Math.min(1.0, y / max);
        new Timeline(new KeyFrame(Duration.millis(380),
            new KeyValue(scroll.vvalueProperty(), v, Interpolator.EASE_BOTH))).play();
    }

    // ------------------------------------------------------------------
    // BODY = HERO + ROLE SELECTION
    // ------------------------------------------------------------------
    private ScrollPane buildBody() {
        VBox content = new VBox(0);
        content.getChildren().addAll(buildHero(), buildRolesSection());
        themed.add(() -> content.setStyle("-fx-background-color:transparent;"));

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        themed.add(() -> sp.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:transparent;" +
            "-fx-border-color:transparent;"));
        return sp;
    }

    // ------------------------------------------------------------------
    // HERO
    // ------------------------------------------------------------------
    private Node buildHero() {
        Color tealLight = Color.web("#5CD6CB");

        Label eyebrow = text("INCIDENT MANAGEMENT PLATFORM", 11, FontWeight.BOLD, () -> tealLight);
        Label h1 = text("Emergency Response & Dispatch System", 32, FontWeight.BOLD, () -> Color.WHITE);
        h1.setWrapText(true);
        h1.setMaxWidth(540);
        Label h2 = text("One Platform. Faster Response. Safer Communities.", 16, FontWeight.SEMI_BOLD, () -> tealLight);
        h2.setWrapText(true);
        Label desc = text("Connecting citizens, administrators and emergency response teams " +
                          "through a unified incident management platform.",
                          14, FontWeight.NORMAL, () -> Color.web("#C5D3E8"));
        desc.setWrapText(true);
        desc.setMaxWidth(500);

        Button start = actionButton("Get Started",
            () -> Color.web("#14B8A6"), () -> Color.web("#2DD4BF"),
            () -> Color.web("#04222A"), () -> Color.web("#14B8A6"));
        start.setOnAction(e -> { activeNav = 2; applyTheme(); scrollTo(rolesSection); });

        liveDot = new Circle(4, Color.web("#3DD68C"));
        Label avail = text("System available  |  24/7 response", 12, FontWeight.NORMAL, () -> Color.web("#D7E3F4"));
        HBox pill = new HBox(8, liveDot, avail);
        pill.setAlignment(Pos.CENTER_LEFT);
        pill.setPadding(new Insets(9, 13, 9, 13));
        pill.setStyle("-fx-background-color:rgba(255,255,255,0.07);" +
                      "-fx-border-color:rgba(255,255,255,0.16);" +
                      "-fx-border-radius:4;-fx-background-radius:4;");

        HBox actions = new HBox(18, start, pill);
        actions.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(actions, new Insets(12, 0, 0, 0));

        VBox left = new VBox(10, eyebrow, h1, h2, desc, actions);
        left.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(left, Priority.ALWAYS);

        mapCanvas = new Canvas(360, 230);
        drawMapCard();

        HBox row = new HBox(40, left, mapCanvas);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(38, 48, 38, 48));

        Canvas bg = new Canvas();
        bg.setManaged(false);
        bg.setMouseTransparent(true);
        StackPane hero = new StackPane(bg, row);
        bg.widthProperty().bind(hero.widthProperty());
        bg.heightProperty().bind(hero.heightProperty());
        bg.widthProperty().addListener((o, a, b) -> drawHeroGrid(bg));
        bg.heightProperty().addListener((o, a, b) -> drawHeroGrid(bg));

        // semi-transparent gradient so the dispatch map shows faintly through the hero
        themed.add(() -> hero.setStyle(
            "-fx-background-color:" + (dk()
                ? "linear-gradient(to bottom right, rgba(8,18,31,0.86), rgba(13,35,64,0.82));"
                : "linear-gradient(to bottom right, rgba(11,37,69,0.92), rgba(19,64,116,0.88));") +
            "-fx-border-color:transparent transparent " + hex(teal()) + " transparent;" +
            "-fx-border-width:0 0 3 0;"));

        pulseTimeline = new Timeline(new KeyFrame(Duration.millis(40), e -> {
            pulsePhase += 0.022;
            drawMapCard();
            liveDot.setOpacity(0.65 + 0.35 * Math.sin(pulsePhase * 2 * Math.PI * 0.6));
        }));
        pulseTimeline.setCycleCount(Animation.INDEFINITE);
        return hero;
    }

    private void drawHeroGrid(Canvas c) {
        GraphicsContext g = c.getGraphicsContext2D();
        double w = c.getWidth(), h = c.getHeight();
        g.clearRect(0, 0, w, h);
        g.setStroke(Color.rgb(255, 255, 255, 0.035));
        g.setLineWidth(1);
        for (double x = 0; x < w; x += 44) g.strokeLine(x + 0.5, 0, x + 0.5, h);
        for (double y = 0; y < h; y += 44) g.strokeLine(0, y + 0.5, w, y + 0.5);
        RadialGradient glow = new RadialGradient(0, 0, w * 0.78, h * 0.2, Math.max(w, h) * 0.45,
            false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(20, 184, 166, 0.16)),
            new Stop(1, Color.rgb(20, 184, 166, 0.0)));
        g.setFill(glow);
        g.fillRect(0, 0, w, h);
    }

    /** Decorative, map-inspired illustration (not live data). */
    private void drawMapCard() {
        GraphicsContext g = mapCanvas.getGraphicsContext2D();
        double w = mapCanvas.getWidth(), h = mapCanvas.getHeight();
        g.clearRect(0, 0, w, h);

        g.setFill(Color.rgb(5, 14, 28, 0.62));
        g.fillRoundRect(0, 0, w, h, 10, 10);

        g.setStroke(Color.rgb(255, 255, 255, 0.05));
        g.setLineWidth(1);
        for (double x = 30; x < w; x += 30) g.strokeLine(x + 0.5, 0, x + 0.5, h);
        for (double y = 30; y < h; y += 30) g.strokeLine(0, y + 0.5, w, y + 0.5);

        double[][] roads = {
            {0, 165, 85, 150, 170, 104, 262, 98, w, 60},
            {64, h, 92, 150, 150, 92, 168, 0},
            {196, h, 232, 168, 262, 98},
            {0, 64, 80, 76, 150, 92},
            {262, 98, 300, 150, w, 176}
        };
        g.setLineCap(StrokeLineCap.ROUND);
        g.setLineJoin(StrokeLineJoin.ROUND);
        for (double[] r : roads) {
            int n = r.length / 2;
            double[] xs = new double[n], ys = new double[n];
            for (int i = 0; i < n; i++) { xs[i] = r[2 * i]; ys[i] = r[2 * i + 1]; }
            g.setStroke(Color.rgb(255, 255, 255, 0.10));
            g.setLineWidth(7);
            g.strokePolyline(xs, ys, n);
            g.setStroke(Color.rgb(255, 255, 255, 0.14));
            g.setLineWidth(1);
            g.strokePolyline(xs, ys, n);
        }

        // animated dispatch route: unit -> incident
        g.setStroke(Color.web("#2DD4BF"));
        g.setLineWidth(3);
        g.setLineDashes(8, 6);
        g.setLineDashOffset(-pulsePhase * 70);
        g.strokePolyline(new double[]{150, 170, 262}, new double[]{92, 104, 98}, 3);
        g.setLineDashes((double[]) null);
        g.setLineDashOffset(0);

        // pulse ring on the reported incident
        double p = pulsePhase % 1.0;
        g.setStroke(Color.color(0.90, 0.28, 0.30, 0.55 * (1 - p)));
        g.setLineWidth(2);
        double pr = 8 + 22 * p;
        g.strokeOval(262 - pr, 98 - pr, pr * 2, pr * 2);

        mapNode(g, 262, 98, Color.web("#E5484D"));
        mapNode(g, 92, 150, Color.web("#F2A33A"));
        mapNode(g, 232, 168, Color.web("#3DD68C"));
        mapNode(g, 150, 92, Color.web("#2DD4BF"));

        g.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        g.setFill(Color.rgb(255, 255, 255, 0.85));
        g.fillText("Response unit", 112, 76);

        g.setFill(Color.rgb(255, 255, 255, 0.70));
        g.fillText("COVERAGE OVERVIEW", 14, 22);

        String[] names = {"Reported", "Responding", "Resolved"};
        Color[] cols = {Color.web("#E5484D"), Color.web("#F2A33A"), Color.web("#3DD68C")};
        double lx = 14;
        for (int i = 0; i < 3; i++) {
            g.setFill(cols[i]);
            g.fillOval(lx, h - 22, 8, 8);
            g.setFill(Color.rgb(255, 255, 255, 0.75));
            g.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
            g.fillText(names[i], lx + 13, h - 14);
            lx += 13 + names[i].length() * 6 + 16;
        }

        g.setStroke(Color.rgb(255, 255, 255, 0.14));
        g.setLineWidth(1);
        g.strokeRoundRect(0.5, 0.5, w - 1, h - 1, 10, 10);
    }

    private void mapNode(GraphicsContext g, double x, double y, Color c) {
        g.setFill(withAlpha(c, 0.22));
        g.fillOval(x - 13, y - 13, 26, 26);
        g.setFill(c);
        g.fillOval(x - 6, y - 6, 12, 12);
        g.setStroke(Color.rgb(255, 255, 255, 0.9));
        g.setLineWidth(1.5);
        g.strokeOval(x - 6, y - 6, 12, 12);
    }

    // ------------------------------------------------------------------
    // ROLE SELECTION (featured Citizen panel + two stacked panels)
    // ------------------------------------------------------------------
    private Node buildRolesSection() {
        Label title = text("Select your portal", 24, FontWeight.BOLD, RoleSelectionScreen::ink);
        Rectangle bar = new Rectangle(44, 3);
        themed.add(() -> bar.setFill(teal()));
        Label sub = text("Choose the role that matches your responsibilities to continue.",
                         13, FontWeight.NORMAL, RoleSelectionScreen::inkSoft);
        VBox head = new VBox(8, title, bar, sub);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(55);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(45);
        grid.getColumnConstraints().addAll(c0, c1);
        RowConstraints r0 = new RowConstraints();
        r0.setVgrow(Priority.ALWAYS);
        RowConstraints r1 = new RowConstraints();
        r1.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().addAll(r0, r1);

        Node citizen = buildCitizenPanel();
        Node admin = sidePanel(1, "ADMINISTRATOR", "Administration Portal",
            "Manage emergency operations, monitor incidents and coordinate response teams.",
            new String[]{"Monitor All Incidents", "Manage Emergency Teams", "Review and Resolve Disputes"},
            "Access Admin Portal", () -> openAdminLogin(), RoleSelectionScreen::orangeAccent);
        Node team = sidePanel(2, "RESPONSE TEAM", "Emergency Response Team",
            "Receive assignments, manage emergency responses and update incident progress.",
            new String[]{"View Assigned Incidents", "Update Response Status", "Mark Incidents as Resolved"},
            "Access Team Portal", () -> openTeamLogin(), RoleSelectionScreen::blueAccent);

        grid.add(citizen, 0, 0, 1, 2);
        grid.add(admin, 1, 0);
        grid.add(team, 1, 1);

        // staggered fade-in once the window is on screen
        Node[] panels = {citizen, admin, team};
        for (int i = 0; i < panels.length; i++) {
            panels[i].setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(420), panels[i]);
            ft.setDelay(Duration.millis(200 + i * 120));
            ft.setToValue(1);
            Node pn = panels[i];
            pn.sceneProperty().addListener((o, a, s) -> { if (s != null) ft.play(); });
        }

        VBox section = new VBox(24, head, grid);
        section.setPadding(new Insets(38, 48, 46, 48));
        rolesSection = section;
        return section;
    }

    private Node buildCitizenPanel() {
        Node icon = iconTile(0, () -> Color.WHITE, () -> Color.rgb(255, 255, 255, 0.18));
        Label tag = text("CITIZEN", 11, FontWeight.BOLD, () -> Color.rgb(255, 255, 255, 0.80));
        Label title = text("Citizen Portal", 28, FontWeight.BOLD, () -> Color.WHITE);
        HBox head = new HBox(14, icon, new VBox(2, tag, title));
        head.setAlignment(Pos.CENTER_LEFT);

        Label desc = text("Report emergencies, monitor incidents and stay informed.",
                          15, FontWeight.NORMAL, () -> Color.rgb(255, 255, 255, 0.90));
        desc.setWrapText(true);

        VBox feats = featureList(
            new String[]{"Report an Emergency", "Track Incident Status", "Raise a Dispute"},
            () -> Color.WHITE, () -> Color.rgb(255, 255, 255, 0.70), 14.5, 12);

        Button btn = actionButton("Access Citizen Portal",
            () -> Color.WHITE, () -> Color.web("#DDF1EF"),
            () -> Color.web("#0B4F57"), () -> Color.WHITE);
        btn.setMinWidth(Region.USE_PREF_SIZE);
        btn.setPrefWidth(250);
        btn.setOnAction(e -> openCitizenReg());

        Label caption = text("Quick registration with your name and email.", 11.5,
                             FontWeight.NORMAL, () -> Color.rgb(255, 255, 255, 0.72));

        VBox body = new VBox(16, head, desc, feats, FX.vSpacer(), btn, caption);
        body.setPadding(new Insets(30));

        StackPane panel = new StackPane(body);
        panel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // decorative concentric rings anchored to the bottom-right corner
        Group rings = new Group();
        for (double r : new double[]{70, 120, 170, 220}) {
            Circle c = new Circle(r);
            c.setFill(Color.TRANSPARENT);
            c.setStroke(Color.rgb(255, 255, 255, 0.09));
            c.setStrokeWidth(1.2);
            rings.getChildren().add(c);
        }
        rings.setManaged(false);
        rings.setMouseTransparent(true);
        rings.layoutXProperty().bind(panel.widthProperty().subtract(24));
        rings.layoutYProperty().bind(panel.heightProperty().subtract(24));
        panel.getChildren().add(0, rings);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        clip.widthProperty().bind(panel.widthProperty());
        clip.heightProperty().bind(panel.heightProperty());
        panel.setClip(clip);

        themed.add(() -> panel.setStyle(
            "-fx-background-radius:6;-fx-background-color:" + (dk()
                ? "linear-gradient(to bottom right, #0F6E69, #123A5E);"
                : "linear-gradient(to bottom right, #0E8F86, #17508A);")));
        addHoverLift(panel);
        return panel;
    }

    /** kind 1 = Admin (navy, top accent bar), kind 2 = Team (orange, left accent bar + tint). */
    private Node sidePanel(int kind, String tagTxt, String titleTxt, String descTxt, String[] feats,
                           String btnTxt, Runnable action, Supplier<Color> accent) {
        Node icon = iconTile(kind, accent, () -> withAlpha(accent.get(), 0.13));
        Label tag = text(tagTxt, 11, FontWeight.BOLD, accent);
        Label title = text(titleTxt, 19, FontWeight.BOLD, RoleSelectionScreen::ink);
        HBox head = new HBox(13, icon, new VBox(1, tag, title));
        head.setAlignment(Pos.CENTER_LEFT);

        Label desc = text(descTxt, 12.5, FontWeight.NORMAL, RoleSelectionScreen::inkSoft);
        desc.setWrapText(true);

        VBox featBox = featureList(feats, RoleSelectionScreen::ink, accent, 12.5, 5);

        Button btn = actionButton(btnTxt, accent,
            () -> accent.get().interpolate(Color.BLACK, 0.18),
            RoleSelectionScreen::onAccent, accent);
        btn.setMinWidth(Region.USE_PREF_SIZE);
        btn.setOnAction(e -> action.run());

        HBox lower = new HBox(12, featBox, FX.hSpacer(), btn);
        lower.setAlignment(Pos.BOTTOM_LEFT);

        VBox body = new VBox(10, head, desc, FX.vSpacer(), lower);
        body.setPadding(new Insets(20, 22, 20, 22));
        body.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        themed.add(() -> {
            Color a = accent.get();
            Color bg = (kind == 2) ? surface().interpolate(a, dk() ? 0.08 : 0.05) : surface();
            String border = (kind == 1)
                ? "-fx-border-color:" + hex(a) + " " + hex(line()) + " " + hex(line()) + " " + hex(line()) + ";" +
                  "-fx-border-width:3 1 1 1;"
                : "-fx-border-color:" + hex(line()) + " " + hex(line()) + " " + hex(line()) + " " + hex(a) + ";" +
                  "-fx-border-width:1 1 1 4;";
            body.setStyle("-fx-background-color:" + rgba(withAlpha(bg, 0.94)) + ";-fx-background-radius:4;-fx-border-radius:4;" + border);
        });
        addHoverLift(body);
        return body;
    }

    // ------------------------------------------------------------------
    // FOOTER
    // ------------------------------------------------------------------
    private Node buildFooter() {
        Label brand = text("INCERIA  |  Emergency Services 24/7", 12, FontWeight.BOLD, RoleSelectionScreen::ink);

        Circle sysDot = new Circle(4, Color.web("#22C55E"));
        Label sys = text("All systems operational", 11.5, FontWeight.NORMAL, RoleSelectionScreen::inkSoft);

        boolean db = facade.isDbConnected();
        Circle dbDot = new Circle(4, db ? Color.web("#22C55E") : Color.web("#EA7C14"));
        Label dbLbl = text(db ? "Database connected" : "Database offline (in-memory mode)",
                           11.5, FontWeight.NORMAL, RoleSelectionScreen::inkSoft);
        HBox.setMargin(dbDot, new Insets(0, 0, 0, 14));

        HBox status = new HBox(6, sysDot, sys, dbDot, dbLbl);
        status.setAlignment(Pos.CENTER_RIGHT);

        HBox row1 = new HBox(brand, FX.hSpacer(), status);
        row1.setAlignment(Pos.CENTER_LEFT);

        Label info = text("Emergency response and dispatch platform connecting citizens, administrators and response teams.",
                          11, FontWeight.NORMAL, RoleSelectionScreen::inkMuted);
        Label copy = text("\u00A9 " + java.time.Year.now().getValue() + " INCERIA. All rights reserved.",
                          11, FontWeight.NORMAL, RoleSelectionScreen::inkMuted);
        HBox row2 = new HBox(info, FX.hSpacer(), copy);
        row2.setAlignment(Pos.CENTER_LEFT);

        VBox footer = new VBox(6, row1, row2);
        footer.setPadding(new Insets(12, 32, 12, 32));
        themed.add(() -> footer.setStyle(
            "-fx-background-color:" + rgba(withAlpha(surface(), 0.92)) + ";" +
            "-fx-border-color:" + hex(line()) + " transparent transparent transparent;" +
            "-fx-border-width:1 0 0 0;"));
        return footer;
    }

    // ------------------------------------------------------------------
    // NAVIGATION
    // ------------------------------------------------------------------
    private void openCitizenReg()  { new CitizenNameEmailScreen(this).show(); }
    private void openAdminLogin()  { new AdminLoginScreen(this).show(); }
    private void openTeamLogin()   { new TeamLoginScreen(this).show(); }
}


// CITIZEN REGISTRATION

class CitizenNameEmailScreen extends Stage {
    private final Stage parent;
    private TextField nameF, emailF;

    public CitizenNameEmailScreen(Stage parent) {
        this.parent = parent;
        setTitle("Citizen Registration");
        setWidth(560);
        setHeight(560);
        setResizable(true);
        initStyle(StageStyle.DECORATED);
        initOwner(parent);
        setMaximized(true);
        WindowKit.keepOwnerState(this);
        buildUI();
    }
    public Stage getParentStage() {
        return parent;
    }

    private void buildUI() {
        Supplier<Color> acc = DashKit::teal;
        nameF = FX.field("e.g. Ali Hassan");
        emailF = FX.field("e.g. ali@email.com");
        AuthKit.bigField(nameF);
        AuthKit.bigField(emailF);
        nameF.setOnAction(e -> emailF.requestFocus());
        emailF.setOnAction(e -> proceed());

        Button cont = AuthKit.primaryButton("Continue  →", acc, this::proceed);

        VBox card = AuthKit.card(acc, "CITIZEN", "Citizen Registration",
            "Enter your name and email to report and track emergencies.",
            AuthKit.row("Full Name", nameF),
            AuthKit.row("Email Address", emailF),
            cont);
        setScene(new Scene(AuthKit.page(acc, card, 640, this::close)));
    }

    private void proceed() {
        ValidationResult nr = SystemFacade.getInstance().validateName(nameF.getText());
        if (!nr.isValid()) { FX.error(this, nr.getMessage()); return; }
        ValidationResult er = SystemFacade.getInstance().validateEmail(emailF.getText());
        if (!er.isValid()) { FX.error(this, er.getMessage()); return; }

        String email = emailF.getText().trim().toLowerCase();
        if (SystemFacade.getInstance().hasDeclarationAccepted(email)) {
            try {
                Citizen citizen = SystemFacade.getInstance().registerCitizen(nameF.getText(), email, true);
                if (SystemFacade.getInstance().isCitizenBlocked(citizen.getEmail())) {
                    FX.error(this, "\u26A0 Your account has been temporarily blocked.\nPlease contact emergency services.");
                    return;
                }
                new CitizenDashboard(citizen).show();
                parent.close();
                close();
            } catch (Exception ex) { FX.error(this, ex.getMessage()); }
        } else {
            new LegalDeclarationScreen(this, nameF.getText().trim(), email).show();
        }
    }
}


// LEGAL DECLARATION

class LegalDeclarationScreen extends Stage {
    private final Stage parent;
    private final String name, email;

    public LegalDeclarationScreen(Stage parent, String name, String email) {
        this.parent = parent;
        this.name = name;
        this.email = email;

        setTitle("Legal Declaration");
        setWidth(600);
        setHeight(580);
        initStyle(StageStyle.DECORATED);
        initOwner(parent);
        setMaximized(true);
        WindowKit.keepOwnerState(this);

        buildUI();
    }

    private void buildUI() {
        Supplier<Color> acc = DashKit::teal;

        TextArea text = FX.textArea("", false);
        text.setText(
            "TERMS OF USE — EMERGENCY REPORTING SYSTEM\n\n" +
            "By accessing this system, you agree:\n\n" +
            "1. All incident reports must be truthful and accurate.\n" +
            "   Filing false reports is a criminal offense.\n\n" +
            "2. This platform is for genuine emergencies only.\n\n" +
            "3. Your personal data is stored securely for incident\n" +
            "   management purposes only.\n\n" +
            "4. Emergency response times may vary based on resources.\n\n" +
            "5. Disputes must be filed in good faith.\n\n" +
            "Thank you for helping keep our community safe."
        );
        text.setPrefRowCount(13);
        DashKit.bind(() -> text.setStyle(ThemeManager.fieldStyle() +
            "-fx-font-family:'Segoe UI';-fx-font-size:14px;-fx-padding:10 12;"));

        CheckBox accept = new CheckBox("I have read and accept the Legal Declaration");
        DashKit.bind(() -> accept.setStyle(
            "-fx-text-fill:" + DashKit.hex(DashKit.ink()) + ";" +
            "-fx-font-family:'Segoe UI';-fx-font-size:14px;"));

        Button proceed = AuthKit.primaryButton("Proceed to Dashboard", acc, () -> {
            if (!accept.isSelected()) {
                FX.error(this, "You must accept the Legal Declaration.");
                return;
            }
            try {
                Citizen citizen = SystemFacade.getInstance()
                        .registerCitizen(name, email, true);

                SystemFacade.getInstance()
                        .persistDeclarationAcceptance(email);

                if (SystemFacade.getInstance().isCitizenBlocked(citizen.getEmail())) {
                    FX.error(this, "⚠ Your account has been temporarily blocked.");
                    return;
                }
                new CitizenDashboard(citizen).show();
                if (parent instanceof CitizenNameEmailScreen) {
                    Stage grandParent = ((CitizenNameEmailScreen) parent).getParentStage();
                    grandParent.close();
                }
                parent.close();
                close();

            } catch (Exception ex) {
                FX.error(this, ex.getMessage());
            }
        });

        VBox card = AuthKit.card(acc, "CITIZEN", "Legal Declaration",
            "Please read the terms carefully before continuing.",
            text, accept, proceed);
        setScene(new Scene(AuthKit.page(acc, card, 820, this::close)));
    }
}

class CitizenDashboard extends Stage implements SystemObserver {
    private final Citizen citizen;
    private final SystemFacade facade = SystemFacade.getInstance();
    private TableView<Incident> table;
    private Label statusBar;
    private final Label[] statVals = new Label[4];

    public CitizenDashboard(Citizen citizen) {
        this.citizen = citizen;
        facade.addObserver(this);
        setTitle("Citizen Dashboard \u2014 " + citizen.getName());
        setWidth(1150); setHeight(800);
        setMaximized(true);
        initStyle(StageStyle.DECORATED);
        setOnHidden(e -> facade.removeObserver(this));
        buildUI();
        refresh();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        DispatchBackdrop.makeTransparent(root);

        root.setTop(DashKit.header("CITIZEN PORTAL", "Emergency Dispatch",
            citizen.getName() + "  |  " + citizen.getEmail(), DashKit::teal,
            () -> { facade.removeObserver(this); new RoleSelectionScreen().show(); close(); }));

        root.setLeft(buildSidebar());
        root.setCenter(buildCenter());

        statusBar = DashKit.lbl(" Ready", 11.5, FontWeight.NORMAL, DashKit::inkSoft);
        root.setBottom(DashKit.statusBar(statusBar));
        FX.fadeIn(root);
        setScene(new Scene(DispatchBackdrop.wrap(root)));
    }

    private VBox buildSidebar() {
        VBox side = DashKit.sidebarShell(230);
        side.getChildren().add(DashKit.sectionLabel("ACTIONS"));

        String[][] actions = {
            {"Report Incident","report"},
            {"View My Incidents","view"},
            {"Track Progress","track"},
            {"Confirm Arrival","confirmArrival"},
            {"Confirm Resolution","confirmResolution"}
        };
        for (String[] a : actions) {
            String cmd = a[1];
            side.getChildren().add(DashKit.navBtn(a[0], DashKit::teal, () -> handle(cmd)));
        }

        side.getChildren().add(FX.vSpacer());
        side.getChildren().add(LegendPanel.build());
        side.getChildren().add(DashKit.infoCard("SIGNED IN", DashKit::teal, citizen.getName(), citizen.getEmail()));
        return side;
    }

    private Node buildCenter() {
        table = buildTable();
        VBox tableCard = DashKit.panelCard("My Incidents", DashKit::teal, table);
        tableCard.setMinHeight(260);
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox mapCard = DashKit.mapCard(facade, DashKit::teal);
        HBox stats = DashKit.statRow(statVals,
            new String[]{"My Reports", "Active", "Resolved", "Open Disputes"}, DashKit.STAT_COLORS);
        Node banner = DashKit.banner("CITIZEN PORTAL", "Welcome back, " + citizen.getName(),
            "Report emergencies, follow their progress and confirm arrival and resolution in real time.",
            DashKit::teal);
        return DashKit.centerColumn(banner, stats, tableCard, mapCard);
    }

    @SuppressWarnings("unchecked")
    private TableView<Incident> buildTable() {
        TableView<Incident> tv = new TableView<>();
        tv.setStyle(ThemeManager.tableStyle()); tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        ThemeManager.addListener(() -> tv.setStyle(ThemeManager.tableStyle()));

        TableColumn<Incident,String> c1 = new TableColumn<>("Incident ID");   c1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIncidentId()));
        TableColumn<Incident,String> c2 = new TableColumn<>("Type");          c2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType().name()));
        TableColumn<Incident,String> c3 = new TableColumn<>("Location");      c3.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLocation().name()));
        TableColumn<Incident,String> c4 = new TableColumn<>("Status");        c4.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().name()));
        TableColumn<Incident,String> c5 = new TableColumn<>("Reported At");   c5.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedTimestamp()));
        TableColumn<Incident,String> c6 = new TableColumn<>("Team");
        c6.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getAssignedTeam() != null ? d.getValue().getAssignedTeam().getTeamName() : "Pending..."));
        TableColumn<Incident,String> c7 = new TableColumn<>("Reporter #");
        c7.setCellValueFactory(d -> {
            int n = d.getValue().getReporterNumber(citizen.getEmail());
            return new SimpleStringProperty(n > 0 ? "Reporter " + n : "\u2014");
        });

        c4.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                try {
                    Color c = FX.statusColor(IncidentStatus.valueOf(item));
                    setStyle("-fx-text-fill:" + ThemeManager.toHex(c) + ";-fx-font-weight:bold;");
                } catch (Exception e) { setStyle(""); }
            }
        });

        tv.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7);
        return tv;
    }

    private void handle(String cmd) {
        switch (cmd) {
            case "report":           showReportDialog();           break;
            case "view":             refresh();                    break;
            case "track":            showTrackDialog();            break;
            case "confirmArrival":   showConfirmArrivalDialog();   break;
            case "confirmResolution":showConfirmResolutionDialog();break;
        }
    }

    private void showReportDialog() {
        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Report Incident"); dlg.setWidth(500); dlg.setHeight(460);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> typeBox = FX.combo();
        typeBox.getItems().addAll("FIRE","THEFT","ACCIDENT"); typeBox.getSelectionModel().selectFirst();

        ComboBox<String> locBox = FX.combo();
        locBox.getItems().addAll("CITY_CENTER","GREEN_VALLEY","WESTERN_BRIDGE","SEONS_ALLEY");
        locBox.getSelectionModel().selectFirst();

        TextArea desc = FX.textArea("Describe the incident (min 10 chars)", true);
        desc.setPrefRowCount(4);

        Button submit = FX.btn("Submit Incident Report", ThemeManager.accentRed());
        submit.setMaxWidth(Double.MAX_VALUE);
        submit.setOnAction(e -> {
            try {
                IncidentType type = IncidentType.valueOf(typeBox.getValue());
                MapLocation  loc  = MapLocation.valueOf(locBox.getValue());
                Incident inc = facade.reportIncident(citizen, type, loc, desc.getText());
                FX.info(dlg, "Incident reported!\nID: " + inc.getIncidentId() +
                    "\nStatus: " + inc.getStatus() + "\nAn admin will assign a team.");
                status("Reported: " + inc.getIncidentId()); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        root.getChildren().addAll(
            FX.dynLabel("Report New Incident", 16, true, ThemeManager::textPrim),
            FX.dynLabel("Admin will review and assign a team.", 12, false, ThemeManager::textPrim),
            FX.formRow("Incident Type:", typeBox),
            FX.formRow("Location:", locBox),
            FX.formRow("Description:", desc),
            submit);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showTrackDialog() {
        List<Incident> list = facade.getCitizenIncidents(citizen.getEmail());
        if (list.isEmpty()) { FX.info(this, "No incidents found."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Track Incident"); dlg.setWidth(540); dlg.setHeight(460);

        VBox root = new VBox(12); FX.applyRootStyle(root); root.setPadding(new Insets(18));

        ComboBox<String> box = FX.combo();
        list.forEach(i -> box.getItems().add(i.getIncidentId())); box.getSelectionModel().selectFirst();

        TextArea detail = FX.textArea("", false); detail.setPrefRowCount(14);
        VBox.setVgrow(detail, Priority.ALWAYS);

        Runnable update = () -> {
            String sel = box.getValue();
            if (sel != null) { Incident i = facade.getIncidentById(sel); if (i!=null) detail.setText(buildDetail(i)); }
        };
        box.setOnAction(e -> update.run()); update.run();

        root.getChildren().addAll(FX.dynLabel("Track Incident",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box), detail);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showConfirmArrivalDialog() {
        List<Incident> eligible = new ArrayList<>();
        for (Incident i : facade.getCitizenIncidents(citizen.getEmail()))
            if (i.isFirstReporter(citizen.getEmail()) &&
                i.isArrivedNotified() && i.getStatus() == IncidentStatus.TEAM_ASSIGNED)
                eligible.add(i);

        if (eligible.isEmpty()) {
            FX.info(this, "No incidents awaiting arrival confirmation.\n" +
                "(You must be the first reporter AND the team must have marked arrival.)");
            return;
        }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Confirm Team Arrival"); dlg.setWidth(520); dlg.setHeight(460);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        eligible.forEach(i -> box.getItems().add(i.getIncidentId() + " | " + i.getType() +
            " | Team: " + (i.getAssignedTeam()!=null?i.getAssignedTeam().getTeamName():"N/A")));
        box.getSelectionModel().selectFirst();

        TextArea info = FX.textArea("", false); info.setPrefRowCount(3);
        Runnable updateInfo = () -> {
            int idx = box.getSelectionModel().getSelectedIndex();
            if (idx>=0 && eligible.get(idx).getAssignedTeam()!=null) {
                ResponseTeam t = eligible.get(idx).getAssignedTeam();
                info.setText("  Team    : "+t.getTeamName()+"\n  Captain : "+t.getTeamLeader()+"\n  Vehicle : "+t.getVehicleNumber());
            }
        };
        box.setOnAction(e -> updateInfo.run()); updateInfo.run();

        ComboBox<CitizenDisputeType> dispBox = FX.combo();
        dispBox.getItems().addAll(CitizenDisputeType.values());
        dispBox.setDisable(true);

        CheckBox raiseDisputeCheck = new CheckBox("Raise a dispute instead");
        raiseDisputeCheck.setStyle("-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";");
        ThemeManager.addListener(() -> raiseDisputeCheck.setStyle("-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";"));
        raiseDisputeCheck.setOnAction(e -> dispBox.setDisable(!raiseDisputeCheck.isSelected()));

        Button confirmBtn = FX.btn("\u2714 Confirm Arrival", ThemeManager.success());
        Button submitBtn  = FX.btn("\u2717 Submit Dispute", ThemeManager.accentRed());
        confirmBtn.setMaxWidth(Double.MAX_VALUE); submitBtn.setMaxWidth(Double.MAX_VALUE);

        confirmBtn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.citizenConfirmArrival(incId, citizen);
                FX.info(dlg, "Arrival confirmed! Incident is now IN PROGRESS.");
                status("Arrival confirmed: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        submitBtn.setOnAction(e -> {
            if (!raiseDisputeCheck.isSelected()) { FX.error(dlg, "Check 'Raise a dispute' and select a reason."); return; }
            CitizenDisputeType dtype = dispBox.getValue();
            if (dtype==null) { FX.error(dlg, "Select a dispute reason."); return; }
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.raiseCitizenDispute(incId, citizen, dtype, "CONFIRM_ARRIVAL");
                FX.info(dlg, "Dispute raised: "+dtype.getLabel()+"\nAdmin has been notified.");
                status("Dispute raised: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        HBox btns = new HBox(10, confirmBtn, submitBtn); btns.setFillHeight(true);
        HBox.setHgrow(confirmBtn, Priority.ALWAYS); HBox.setHgrow(submitBtn, Priority.ALWAYS);

        root.getChildren().addAll(
            FX.dynLabel("Confirm Team Arrival", 16, true, ThemeManager::textPrim),
            FX.formRow("Select Incident:", box),
            FX.formRow("Team Details:", info),
            raiseDisputeCheck,
            FX.formRow("Dispute Reason:", dispBox),
            btns);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showConfirmResolutionDialog() {
        List<Incident> eligible = new ArrayList<>();
        for (Incident i : facade.getCitizenIncidents(citizen.getEmail()))
            if (i.isFirstReporter(citizen.getEmail()) &&
                i.isTeamMarkedResolved() && i.getStatus() == IncidentStatus.IN_PROGRESS)
                eligible.add(i);

        if (eligible.isEmpty()) {
            FX.info(this, "No incidents awaiting resolution confirmation.\n" +
                "(You must be the first reporter AND the team must have marked resolved.)");
            return;
        }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Confirm Incident Resolution"); dlg.setWidth(520); dlg.setHeight(420);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        eligible.forEach(i -> box.getItems().add(i.getIncidentId()+" | "+i.getType()));
        box.getSelectionModel().selectFirst();

        ComboBox<CitizenDisputeType> dispBox = FX.combo();
        dispBox.getItems().addAll(CitizenDisputeType.values());
        dispBox.setDisable(true);

        CheckBox raiseDisputeCheck = new CheckBox("Not satisfied \u2014 raise dispute");
        raiseDisputeCheck.setStyle("-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";");
        ThemeManager.addListener(() -> raiseDisputeCheck.setStyle("-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";"));
        raiseDisputeCheck.setOnAction(e -> dispBox.setDisable(!raiseDisputeCheck.isSelected()));

        Button resolveBtn = FX.btn("\u2714 Confirm Resolved", ThemeManager.success());
        Button disputeBtn = FX.btn("\u2717 Submit Dispute", ThemeManager.accentRed());
        resolveBtn.setMaxWidth(Double.MAX_VALUE); disputeBtn.setMaxWidth(Double.MAX_VALUE);

        resolveBtn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.citizenConfirmResolution(incId, citizen);
                int pts = facade.getRewardPoints(citizen.getEmail());
                FX.info(dlg, "Incident resolved! +10 reward points.\nYour total: "+pts+" pts");
                status("Resolved: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        disputeBtn.setOnAction(e -> {
            if (!raiseDisputeCheck.isSelected()) { FX.error(dlg, "Check 'raise dispute' and select a reason."); return; }
            CitizenDisputeType dtype = dispBox.getValue();
            if (dtype==null) { FX.error(dlg, "Select a dispute reason."); return; }
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.raiseCitizenDispute(incId, citizen, dtype, "CONFIRM_RESOLUTION");
                FX.info(dlg, "Dispute raised: "+dtype.getLabel());
                status("Dispute raised: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        HBox btns = new HBox(10, resolveBtn, disputeBtn);
        HBox.setHgrow(resolveBtn, Priority.ALWAYS); HBox.setHgrow(disputeBtn, Priority.ALWAYS);

        root.getChildren().addAll(
            FX.dynLabel("Confirm Incident Resolution", 16, true, ThemeManager::textPrim),
            FX.dynLabel("Has the issue been fully resolved?", 12, false, ThemeManager::textPrim),
            FX.formRow("Select Incident:", box),
            raiseDisputeCheck,
            FX.formRow("Dispute Reason:", dispBox),
            btns);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private String buildDetail(Incident inc) {
        StringBuilder sb = new StringBuilder();
        sb.append("===================================\n  INCIDENT DETAILS\n===================================\n");
        sb.append("  ID          : ").append(inc.getIncidentId()).append("\n");
        sb.append("  Type        : ").append(inc.getType()).append("\n");
        sb.append("  Location    : ").append(inc.getLocation()).append("\n");
        sb.append("  Status      : ").append(inc.getStatus()).append("\n");
        sb.append("  Reported At : ").append(inc.getFormattedTimestamp()).append("\n");
        sb.append("  Description : ").append(inc.getDescription()).append("\n");
        int rn = inc.getReporterNumber(citizen.getEmail());
        sb.append("  Your Role   : ").append(rn > 0 ? "Reporter " + rn + (rn==1?" (First Reporter)":"") : "\u2014").append("\n");
        if (inc.getAssignedTeam() != null) {
            sb.append("-----------------------------------\n  ASSIGNED TEAM\n");
            sb.append("  Name    : ").append(inc.getAssignedTeam().getTeamName()).append("\n");
            sb.append("  Leader  : ").append(inc.getAssignedTeam().getTeamLeader()).append("\n");
            sb.append("  Vehicle : ").append(inc.getAssignedTeam().getVehicleNumber()).append("\n");
        } else sb.append("-----------------------------------\n  No team assigned yet.\n");
        if (inc.isDisputeRaised())
            sb.append("-----------------------------------\n  DISPUTE: ").append(inc.getDisputeNote())
              .append("\n  Resolved: ").append(inc.isDisputeResolved()).append("\n");
        sb.append("===================================\n");
        return sb.toString();
    }

    private void refresh() {
        List<Incident> list = facade.getCitizenIncidents(citizen.getEmail());
        table.getItems().setAll(list);
        DashKit.updateStats(statVals, list);
    }
    private void status(String m) { statusBar.setText(" " + m); }

    @Override public void onIncidentUpdated(Incident i)               { Platform.runLater(this::refresh); }
    @Override public void onMapUpdated(MapLocation l, IncidentStatus s){ }
    @Override public void onDisputeRaised(Incident i)                 { Platform.runLater(this::refresh); }
}


// ADMIN LOGIN
class AdminLoginScreen extends Stage {
    private final Stage parent;
    private TextField nameF, workIdF;
    private PasswordField passF;

    public AdminLoginScreen(Stage parent) {
        this.parent = parent;
        setTitle("Admin Login");
        setWidth(560);
        setHeight(640);
        initStyle(StageStyle.DECORATED);
        initOwner(parent);
        setMaximized(true);
        WindowKit.keepOwnerState(this);
        buildUI();
    }

    private void buildUI() {
        Supplier<Color> acc = DashKit::orange;
        nameF   = FX.field("Full name");
        passF   = FX.passField();
        workIdF = FX.field("e.g. ADM001");
        AuthKit.bigField(nameF);
        AuthKit.bigField(passF);
        AuthKit.bigField(workIdF);
        nameF.setOnAction(e -> passF.requestFocus());
        passF.setOnAction(e -> workIdF.requestFocus());
        workIdF.setOnAction(e -> doLogin());

        Button loginBtn = AuthKit.primaryButton("Login", acc, this::doLogin);

        VBox card = AuthKit.card(acc, "ADMINISTRATOR", "Administration Login",
            "Authorized personnel only.",
            AuthKit.row("Full Name", nameF),
            AuthKit.row("Password", passF),
            AuthKit.row("Work ID", workIdF),
            loginBtn);
        setScene(new Scene(AuthKit.page(acc, card, 640, this::close)));
    }

    private void doLogin() {
        try {
            Admin admin = SystemFacade.getInstance().loginAdmin(
                nameF.getText(), passF.getText(), workIdF.getText());
            new AdminDashboard(admin).show(); parent.close(); close();
        } catch (Exception ex) { FX.error(this, ex.getMessage()); }
    }
}

// ADMIN DASHBOARD

class AdminDashboard extends Stage implements SystemObserver {
    private final Admin admin;
    private final SystemFacade facade = SystemFacade.getInstance();
    private TableView<Incident> table;
    private Label statusBar;
    private final Label[] statVals = new Label[4];

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        facade.addObserver(this);
        setTitle("Admin Dashboard \u2014 " + admin.getName());
        setWidth(1280); setHeight(820);
        setMaximized(true);
        initStyle(StageStyle.DECORATED);
        setOnHidden(e -> facade.removeObserver(this));
        buildUI(); refresh();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        DispatchBackdrop.makeTransparent(root);

        root.setTop(DashKit.header("ADMINISTRATOR", "Admin Control Center",
            admin.getName() + "  |  ID: " + admin.getWorkId(), DashKit::orange,
            () -> { facade.removeObserver(this); new RoleSelectionScreen().show(); close(); }));

        root.setLeft(buildSidebar());
        root.setCenter(buildCenter());

        statusBar = DashKit.lbl(" Ready", 11.5, FontWeight.NORMAL, DashKit::inkSoft);
        root.setBottom(DashKit.statusBar(statusBar));
        FX.fadeIn(root);
        setScene(new Scene(DispatchBackdrop.wrap(root)));
    }

    private VBox buildSidebar() {
        VBox side = DashKit.sidebarShell(240);
        side.getChildren().add(DashKit.sectionLabel("ADMIN ACTIONS"));

        String[][] actions = {
            {"All Incidents","all"},{"Re-Assign Team","assign"},{"Auto Dispatch","auto"},
            {"Resolve Incident","resolve"},{"Refresh Live Data","monitor"},
            {"Handle Disputes","disputes"},{"View All Teams","teams"}
        };
        for (String[] a : actions) {
            String cmd = a[1];
            side.getChildren().add(DashKit.navBtn(a[0], DashKit::orange, () -> handle(cmd)));
        }

        side.getChildren().add(FX.vSpacer());
        side.getChildren().add(LegendPanel.build());
        side.getChildren().add(DashKit.infoCard("LOGGED IN", DashKit::orange, admin.getName(), "ID: " + admin.getWorkId()));
        return side;
    }

    @SuppressWarnings("unchecked")
    private Node buildCenter() {
        table = new TableView<>();
        table.setStyle(ThemeManager.tableStyle()); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        ThemeManager.addListener(() -> table.setStyle(ThemeManager.tableStyle()));

        TableColumn<Incident,String> c1 = new TableColumn<>("Incident ID");  c1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIncidentId()));
        TableColumn<Incident,String> c2 = new TableColumn<>("Type");         c2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType().name()));
        TableColumn<Incident,String> c3 = new TableColumn<>("Location");     c3.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLocation().name()));
        TableColumn<Incident,String> c4 = new TableColumn<>("Status");       c4.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().name()));
        TableColumn<Incident,String> c5 = new TableColumn<>("Reported At");  c5.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFormattedTimestamp()));
        TableColumn<Incident,String> c6 = new TableColumn<>("Team");
        c6.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getAssignedTeam()!=null?d.getValue().getAssignedTeam().getTeamName():"Unassigned"));
        TableColumn<Incident,String> c7 = new TableColumn<>("Citizens");     c7.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getLinkedCitizens().size())));
        TableColumn<Incident,String> c8 = new TableColumn<>("Dispute");
        c8.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isDisputeRaised()?(d.getValue().isDisputeResolved()?"Resolved":"OPEN"):"None"));

        c4.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty||item==null) { setText(null); setStyle(""); return; }
                setText(item);
                try { Color c = FX.statusColor(IncidentStatus.valueOf(item));
                      setStyle("-fx-text-fill:"+ThemeManager.toHex(c)+";-fx-font-weight:bold;"); }
                catch (Exception e) { setStyle(""); }
            }
        });

        table.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7,c8);

        VBox tableCard = DashKit.panelCard("All Incidents", DashKit::orange, table);
        tableCard.setMinHeight(260);
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox mapCard = DashKit.mapCard(facade, DashKit::orange);
        HBox stats = DashKit.statRow(statVals,
            new String[]{"Total Incidents", "Open", "Resolved", "Open Disputes"}, DashKit.STAT_COLORS);
        Node banner = DashKit.banner("ADMINISTRATOR CONSOLE", "Welcome, " + admin.getName(),
            "Monitor every incident, dispatch response teams and resolve disputes from one control centre.",
            DashKit::orange);
        return DashKit.centerColumn(banner, stats, tableCard, mapCard);
    }

    private void handle(String cmd) {
        switch (cmd) {
            case "all":      refresh();             break;
            case "assign":   showAssignDialog();    break;
            case "auto":     showAutoDispatch();    break;
            case "resolve":  showResolveDialog();   break;
            case "monitor":  doRefresh();           break;
            case "disputes": showDisputesDialog();  break;
            case "teams":    showTeamsDialog();     break;
        }
    }

    private void doRefresh() {
        refresh();
        facade.syncMapOnStartup();
        status("Live data refreshed \u2014 " + new java.util.Date());
    }

    private void showAssignDialog() {
        List<Incident> open = new ArrayList<>();
        for (Incident i : facade.getAllIncidents()) if (i.getStatus()!=IncidentStatus.RESOLVED) open.add(i);
        if (open.isEmpty()) { FX.info(this, "No open incidents."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Re-Assign Team"); dlg.setWidth(540); dlg.setHeight(360);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> iBox = FX.combo();
        open.forEach(i -> iBox.getItems().add(i.getIncidentId()+" | "+i.getType()+" @ "+i.getLocation()));
        iBox.getSelectionModel().selectFirst();

        ComboBox<String> tBox = FX.combo();
        Runnable upd = () -> {
            int si = iBox.getSelectionModel().getSelectedIndex(); if (si<0) return;
            tBox.getItems().clear();
            for (ResponseTeam t : facade.getAvailableTeams(open.get(si).getType()))
                tBox.getItems().add(t.getTeamName()+" | "+t.getTeamLeader());
            if (tBox.getItems().isEmpty()) tBox.getItems().add("No available teams");
            tBox.getSelectionModel().selectFirst();
        };
        iBox.setOnAction(e -> upd.run()); upd.run();

        Button assignBtn = FX.btn("Assign Team", ThemeManager.accentBlue());
        assignBtn.setMaxWidth(Double.MAX_VALUE);
        assignBtn.setOnAction(e -> {
            int si = iBox.getSelectionModel().getSelectedIndex(); if (si<0) return;
            String te = tBox.getValue();
            if (te==null||te.startsWith("No")) { FX.error(dlg,"No available team."); return; }
            String incId = open.get(si).getIncidentId();
            try {
                facade.assignTeam(incId, te.split(" \\| ")[0]);
                FX.info(dlg, "Team assigned to "+incId); status("Assigned -> "+incId);
                refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        root.getChildren().addAll(
            FX.dynLabel("Re-Assign / Override Team",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", iBox),
            FX.formRow("Available Teams:", tBox),
            assignBtn);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showAutoDispatch() {
        List<Incident> open = new ArrayList<>();
        for (Incident i : facade.getAllIncidents()) if (i.getStatus()==IncidentStatus.REPORTED) open.add(i);
        if (open.isEmpty()) { FX.info(this,"No unassigned incidents."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Auto Dispatch"); dlg.setWidth(440); dlg.setHeight(260);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        open.forEach(i -> box.getItems().add(i.getIncidentId()+" | "+i.getType()));
        box.getSelectionModel().selectFirst();

        Button btn = FX.btn("Auto Dispatch", ThemeManager.accentOrange());
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = open.get(idx).getIncidentId();
            try {
                ResponseTeam t = facade.autoDispatch(incId);
                FX.info(dlg,"Dispatched: "+t.getTeamName()+"\nLeader: "+t.getTeamLeader());
                status("Auto-dispatched "+t.getTeamName()+" -> "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        root.getChildren().addAll(FX.dynLabel("Auto Dispatch",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box), btn);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showResolveDialog() {
        List<Incident> active = new ArrayList<>();
        for (Incident i : facade.getAllIncidents()) if (i.getStatus()!=IncidentStatus.RESOLVED) active.add(i);
        if (active.isEmpty()) { FX.info(this,"No active incidents."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Resolve Incident"); dlg.setWidth(440); dlg.setHeight(260);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        active.forEach(i -> box.getItems().add(i.getIncidentId()+" | "+i.getType()+" | "+i.getStatus()));
        box.getSelectionModel().selectFirst();

        Button btn = FX.btn("Force Resolve", ThemeManager.success());
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = active.get(idx).getIncidentId();
            if (!FX.confirm(dlg,"Mark "+incId+" as RESOLVED?")) return;
            try {
                facade.resolveIncident(incId);
                FX.info(dlg, incId+" resolved."); status("Resolved: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        root.getChildren().addAll(FX.dynLabel("Force Resolve Incident",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box), btn);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void showDisputesDialog() {
        List<Incident> disp = new ArrayList<>(facade.getOpenDisputes());

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Disputes & Investigations"); dlg.setWidth(920); dlg.setHeight(580);

        TableView<Incident> dt = new TableView<>();
        dt.setStyle(ThemeManager.tableStyle()); dt.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        ThemeManager.addListener(() -> dt.setStyle(ThemeManager.tableStyle()));

        TableColumn<Incident,String> d1 = new TableColumn<>("Incident ID"); d1.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIncidentId()));
        TableColumn<Incident,String> d2 = new TableColumn<>("Type");        d2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getType().name()));
        TableColumn<Incident,String> d3 = new TableColumn<>("Status");      d3.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getStatus().name()));
        TableColumn<Incident,String> d4 = new TableColumn<>("Dispute Note");d4.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getDisputeNote()!=null?d.getValue().getDisputeNote():""));
        TableColumn<Incident,String> d5 = new TableColumn<>("Reporter");
        d5.setCellValueFactory(d -> {
            Incident i = d.getValue();
            String r = i.getLinkedCitizens().isEmpty()?"N/A":i.getLinkedCitizens().get(0).getName()+" <"+i.getLinkedCitizens().get(0).getEmail()+">";
            return new SimpleStringProperty(r);
        });
        dt.getColumns().addAll(Arrays.asList(d1, d2, d3, d4,d5));
        dt.getItems().setAll(disp);

        TextArea detail = FX.textArea("",false); detail.setPrefRowCount(5);
        dt.getSelectionModel().selectedItemProperty().addListener((o,a,b) -> {
            if (b!=null) detail.setText("  ID     : "+b.getIncidentId()+"\n  Note   : "+b.getDisputeNote()+"\n  Status : "+b.getStatus()+"\n  Citizens: "+b.getLinkedCitizens().size());
        });

        Button validBtn   = FX.btn("\u2714 Mark Valid", ThemeManager.success());
        Button invalidBtn = FX.btn("\u2718 Invalid \u2192 Penalize", ThemeManager.accentRed());
        Button warnBtn    = FX.btn("\u26A0 Issue Warning", ThemeManager.accentOrange());

        validBtn.setOnAction(e -> {
            Incident sel = dt.getSelectionModel().getSelectedItem();
            if (sel==null) { FX.error(dlg,"Select a dispute first."); return; }
            if (!FX.confirm(dlg,"Mark dispute VALID for "+sel.getIncidentId()+"?")) return;
            try { facade.resolveDispute(sel.getIncidentId()); disp.remove(sel); dt.getItems().setAll(disp); status("Resolved: "+sel.getIncidentId()); }
            catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });
        invalidBtn.setOnAction(e -> {
            Incident sel = dt.getSelectionModel().getSelectedItem();
            if (sel==null) { FX.error(dlg,"Select a dispute first."); return; }
            String who = sel.getLinkedCitizens().isEmpty()?"citizen":sel.getLinkedCitizens().get(0).getName();
            if (!FX.confirm(dlg,"Mark INVALID? "+who+" will be blocked.")) return;
            try { facade.markDisputeInvalid(sel.getIncidentId()); disp.remove(sel); dt.getItems().setAll(disp); status("Invalid: "+sel.getIncidentId()); }
            catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });
        warnBtn.setOnAction(e -> {
            Incident sel = dt.getSelectionModel().getSelectedItem();
            if (sel==null) { FX.error(dlg,"Select a dispute first."); return; }
            FX.info(dlg,"Warning issued for "+sel.getIncidentId()+". No penalty applied.");
        });

        HBox btns = new HBox(10, validBtn, warnBtn, invalidBtn); btns.setPadding(new Insets(8,0,0,0));

        VBox root = new VBox(12);
        FX.applyRootStyle(root); root.setPadding(new Insets(18));
        root.getChildren().addAll(
            FX.dynLabel("Disputes & Investigations ("+disp.size()+" open)",16,true,ThemeManager::textPrim),
            dt, detail, btns);
        VBox.setVgrow(dt, Priority.ALWAYS);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void showTeamsDialog() {
        List<ResponseTeam> teams = facade.getAllTeams();

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("All Teams"); dlg.setWidth(800); dlg.setHeight(440);

        TableView<ResponseTeam> tt = new TableView<>();
        tt.setStyle(ThemeManager.tableStyle()); tt.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        ThemeManager.addListener(() -> tt.setStyle(ThemeManager.tableStyle()));

        TableColumn<ResponseTeam,String> t1 = new TableColumn<>("Team Name");  t1.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getTeamName()));
        TableColumn<ResponseTeam,String> t2 = new TableColumn<>("Type");       t2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getTeamType()));
        TableColumn<ResponseTeam,String> t3 = new TableColumn<>("Category");   t3.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getCategory().name()));
        TableColumn<ResponseTeam,String> t4 = new TableColumn<>("Leader");     t4.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getTeamLeader()));
        TableColumn<ResponseTeam,String> t5 = new TableColumn<>("Vehicle");    t5.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getVehicleNumber()));
        TableColumn<ResponseTeam,String> t6 = new TableColumn<>("Available");  t6.setCellValueFactory(d->new SimpleStringProperty(d.getValue().isAvailable()?"Yes":"Busy"));
        tt.getColumns().addAll(t1,t2,t3,t4,t5,t6); tt.getItems().setAll(teams);

        VBox root = new VBox(12); FX.applyRootStyle(root); root.setPadding(new Insets(18));
        root.getChildren().addAll(FX.dynLabel("All Teams",16,true,ThemeManager::textPrim), tt);
        VBox.setVgrow(tt, Priority.ALWAYS);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void refresh() {
        List<Incident> list = new ArrayList<>(facade.getAllIncidents());
        table.getItems().setAll(list);
        DashKit.updateStats(statVals, list);
    }
    private void status(String m) { statusBar.setText(" "+m); }

    @Override public void onIncidentUpdated(Incident i)                { Platform.runLater(this::refresh); }
    @Override public void onMapUpdated(MapLocation l, IncidentStatus s){ }
    @Override public void onDisputeRaised(Incident i)                  { Platform.runLater(this::refresh); }
}


// TEAM LOGIN

class TeamLoginScreen extends Stage {
    private final Stage parent;

    public TeamLoginScreen(Stage parent) {
        this.parent = parent;
        setTitle("Team Selection");
        setWidth(580);
        setHeight(520);
        initStyle(StageStyle.DECORATED);
        initOwner(parent);
        setMaximized(true);
        WindowKit.keepOwnerState(this);
        buildUI();
    }

    private void buildUI() {
        Supplier<Color> acc = DashKit::blue;

        List<ResponseTeam> teams = SystemFacade.getInstance().getAllTeams();
        ComboBox<String> teamBox = new ComboBox<>();
        teamBox.setMaxWidth(Double.MAX_VALUE);
        teams.forEach(t -> teamBox.getItems().add(t.getTeamName()));
        teamBox.getSelectionModel().selectFirst();

        DashKit.bind(() -> teamBox.setStyle(
            "-fx-background-color:" + ThemeManager.toHex(ThemeManager.field()) + ";" +
            "-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";" +
            "-fx-border-color:" + DashKit.hex(DashKit.blue()) + ";" +
            "-fx-border-width:1.5;" +
            "-fx-border-radius:6;" +
            "-fx-background-radius:6;" +
            "-fx-padding:8 10;" +
            "-fx-font-size:15px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Segoe UI';"));

        teamBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
                    setStyle(
                        "-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";" +
                        "-fx-background-color:" + ThemeManager.toHex(ThemeManager.field()) + ";" +
                        "-fx-padding: 9 14;"
                    );
                    setOnMouseEntered(e -> setStyle(
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-color: #2563eb;" +
                        "-fx-padding: 9 14;"
                    ));
                    setOnMouseExited(e -> setStyle(
                        "-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";" +
                        "-fx-background-color:" + ThemeManager.toHex(ThemeManager.field()) + ";" +
                        "-fx-padding: 9 14;"
                    ));
                }
            }
        });

        teamBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
                    setStyle("-fx-text-fill:" + ThemeManager.toHex(ThemeManager.textPrim()) + ";" +
                             "-fx-background-color: transparent;");
                }
            }
        });

        Button enter = AuthKit.primaryButton("Enter Dashboard  →", acc, () -> {
            String sel = teamBox.getValue();
            if (sel == null || sel.isEmpty()) { FX.error(this, "Please select a team."); return; }
            ResponseTeam chosen = null;
            for (ResponseTeam t : teams) if (t.getTeamName().equals(sel)) { chosen = t; break; }
            if (chosen == null) { FX.error(this, "Team not found."); return; }
            new TeamDashboard(chosen).show(); parent.close(); close();
        });

        VBox card = AuthKit.card(acc, "RESPONSE TEAM", "Team Login",
            "Select your assigned team to continue.",
            AuthKit.row("Select Your Team", teamBox),
            enter);
        setScene(new Scene(AuthKit.page(acc, card, 640, this::close)));
    }
}

class TeamDashboard extends Stage implements SystemObserver {
    private final ResponseTeam team;
    private final SystemFacade facade = SystemFacade.getInstance();
    private TableView<Incident> table;
    private Label statusBar;
    private final Label[] statVals = new Label[4];

    public TeamDashboard(ResponseTeam team) {
        this.team = team;
        facade.addObserver(this);
        setTitle("Team Dashboard \u2014 " + team.getTeamName());
        setWidth(1100); setHeight(760);
        setMaximized(true);
        initStyle(StageStyle.DECORATED);
        setOnHidden(e -> facade.removeObserver(this));
        buildUI(); refresh();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();
        DispatchBackdrop.makeTransparent(root);

        root.setTop(DashKit.header("RESPONSE TEAM", "Team Portal",
            team.getTeamName() + "  |  Leader: " + team.getTeamLeader() + "  |  " + team.getTeamType(), DashKit::blue,
            () -> { facade.removeObserver(this); new RoleSelectionScreen().show(); close(); }));

        root.setLeft(buildSidebar());
        root.setCenter(buildCenter());

        statusBar = DashKit.lbl(" Ready", 11.5, FontWeight.NORMAL, DashKit::inkSoft);
        root.setBottom(DashKit.statusBar(statusBar));
        FX.fadeIn(root);
        setScene(new Scene(DispatchBackdrop.wrap(root)));
    }

    private VBox buildSidebar() {
        VBox side = DashKit.sidebarShell(230);
        side.getChildren().add(DashKit.sectionLabel("TEAM ACTIONS"));

        String[][] actions = {
            {"View Assigned Jobs","view"},
            {"Mark Arrival","arrival"},
            {"Incident Resolved","resolve"},
            {"View Detail","detail"}
        };
        for (String[] action : actions) {
            String cmd = action[1];
            side.getChildren().add(DashKit.navBtn(action[0], DashKit::blue, () -> handle(cmd)));
        }

        side.getChildren().add(FX.vSpacer());
        side.getChildren().add(LegendPanel.build());
        side.getChildren().add(DashKit.infoCard("TEAM INFO", DashKit::blue,
            team.getTeamName(), team.getTeamType(), "Vehicle: " + team.getVehicleNumber()));
        return side;
    }

    @SuppressWarnings("unchecked")
    private Node buildCenter() {
        table = new TableView<>();
        table.setStyle(ThemeManager.tableStyle()); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        ThemeManager.addListener(()->table.setStyle(ThemeManager.tableStyle()));

        TableColumn<Incident,String> c1 = new TableColumn<>("Incident ID"); c1.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getIncidentId()));
        TableColumn<Incident,String> c2 = new TableColumn<>("Type");        c2.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getType().name()));
        TableColumn<Incident,String> c3 = new TableColumn<>("Location");    c3.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getLocation().name()));
        TableColumn<Incident,String> c4 = new TableColumn<>("Status");      c4.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getStatus().name()));
        TableColumn<Incident,String> c5 = new TableColumn<>("Reported At"); c5.setCellValueFactory(d->new SimpleStringProperty(d.getValue().getFormattedTimestamp()));
        TableColumn<Incident,String> c6 = new TableColumn<>("Description");
        c6.setCellValueFactory(d -> { String desc = d.getValue().getDescription(); return new SimpleStringProperty(desc.length()>40?desc.substring(0,40)+"...":desc); });

        c4.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item,empty);
                if (empty||item==null){setText(null);setStyle("");return;}
                setText(item);
                try { Color c=FX.statusColor(IncidentStatus.valueOf(item)); setStyle("-fx-text-fill:"+ThemeManager.toHex(c)+";-fx-font-weight:bold;"); }
                catch(Exception e){setStyle("");}
            }
        });

        table.getColumns().addAll(c1,c2,c3,c4,c5,c6);

        VBox tableCard = DashKit.panelCard("Assigned Incidents", DashKit::blue, table);
        tableCard.setMinHeight(260);
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox mapCard = DashKit.mapCard(facade, DashKit::blue);
        HBox stats = DashKit.statRow(statVals,
            new String[]{"Assigned Jobs", "Active", "Resolved", "Open Disputes"}, DashKit.STAT_COLORS);
        Node banner = DashKit.banner("RESPONSE TEAM CONSOLE", team.getTeamName(),
            "Review assigned incidents, mark arrival and update progress until each one is resolved.",
            DashKit::blue);
        return DashKit.centerColumn(banner, stats, tableCard, mapCard);
    }

    private void handle(String cmd) {
        switch (cmd) {
            case "view":    refresh();            break;
            case "arrival": markArrival();        break;
            case "resolve": requestResolution();  break;
            case "detail":  showDetail();         break;
        }
    }

    private void markArrival() {
        List<Incident> eligible = new ArrayList<>();
        for (Incident i : facade.getTeamIncidents(team.getTeamName()))
            if (i.getStatus()==IncidentStatus.TEAM_ASSIGNED && !i.isArrivedNotified() && i.isAssignedTeam(team.getTeamName()))
                eligible.add(i);

        if (eligible.isEmpty()) { FX.info(this,"No incidents awaiting arrival marking."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Mark Arrival"); dlg.setWidth(500); dlg.setHeight(400);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        eligible.forEach(i -> box.getItems().add(i.getIncidentId()+" | "+i.getLocation()));
        box.getSelectionModel().selectFirst();

        ComboBox<TeamDisputeType> dispBox = FX.combo();
        dispBox.getItems().addAll(TeamDisputeType.values());
        dispBox.setDisable(true);

        CheckBox raiseDisputeCheck = new CheckBox("Raise a dispute instead");
        raiseDisputeCheck.setStyle("-fx-text-fill:"+ThemeManager.toHex(ThemeManager.textPrim())+";");
        ThemeManager.addListener(()->raiseDisputeCheck.setStyle("-fx-text-fill:"+ThemeManager.toHex(ThemeManager.textPrim())+";"));
        raiseDisputeCheck.setOnAction(e -> dispBox.setDisable(!raiseDisputeCheck.isSelected()));

        Button markBtn    = FX.btn("\u2714 Mark Arrival", ThemeManager.success());
        Button disputeBtn = FX.btn("\u2717 Submit Dispute", ThemeManager.accentRed());
        markBtn.setMaxWidth(Double.MAX_VALUE); disputeBtn.setMaxWidth(Double.MAX_VALUE);

        markBtn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            if (!FX.confirm(dlg,"Mark arrival for "+incId+"? Citizen will be notified.")) return;
            try {
                facade.markArrival(incId, team.getTeamName());
                FX.info(dlg,"Arrival marked for: "+incId+"\nCitizen notified.");
                status("Arrival marked: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });
        disputeBtn.setOnAction(e -> {
            if (!raiseDisputeCheck.isSelected()) { FX.error(dlg,"Check 'Raise a dispute' and select reason."); return; }
            TeamDisputeType dtype = dispBox.getValue();
            if (dtype==null) { FX.error(dlg,"Select dispute reason."); return; }
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.raiseTeamDispute(incId, team.getTeamName(), dtype, "MARK_ARRIVAL");
                FX.info(dlg,"Dispute raised: "+dtype.getLabel());
                status("Dispute raised: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        HBox btns = new HBox(10,markBtn,disputeBtn);
        HBox.setHgrow(markBtn,Priority.ALWAYS); HBox.setHgrow(disputeBtn,Priority.ALWAYS);

        root.getChildren().addAll(
            FX.dynLabel("Mark Arrival",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box),
            raiseDisputeCheck,
            FX.formRow("Dispute Reason:", dispBox),
            btns);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void requestResolution() {
        List<Incident> eligible = new ArrayList<>();
        for (Incident i : facade.getTeamIncidents(team.getTeamName()))
            if (i.getStatus()==IncidentStatus.IN_PROGRESS && !i.isTeamMarkedResolved() && i.isAssignedTeam(team.getTeamName()))
                eligible.add(i);

        if (eligible.isEmpty()) { FX.info(this,"No incidents in progress to mark resolved."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Mark Resolved"); dlg.setWidth(500); dlg.setHeight(400);

        VBox root = new VBox(14); FX.applyRootStyle(root); root.setPadding(new Insets(22,26,22,26));

        ComboBox<String> box = FX.combo();
        eligible.forEach(i -> box.getItems().add(i.getIncidentId()+" | "+i.getType()));
        box.getSelectionModel().selectFirst();

        ComboBox<TeamDisputeType> dispBox = FX.combo();
        dispBox.getItems().addAll(TeamDisputeType.values());
        dispBox.setDisable(true);

        CheckBox raiseDisputeCheck = new CheckBox("Raise a dispute instead");
        raiseDisputeCheck.setStyle("-fx-text-fill:"+ThemeManager.toHex(ThemeManager.textPrim())+";");
        ThemeManager.addListener(()->raiseDisputeCheck.setStyle("-fx-text-fill:"+ThemeManager.toHex(ThemeManager.textPrim())+";"));
        raiseDisputeCheck.setOnAction(e -> dispBox.setDisable(!raiseDisputeCheck.isSelected()));

        Button resolveBtn = FX.btn("\u2714 Mark Resolved", ThemeManager.success());
        Button disputeBtn = FX.btn("\u2717 Submit Dispute", ThemeManager.accentRed());
        resolveBtn.setMaxWidth(Double.MAX_VALUE); disputeBtn.setMaxWidth(Double.MAX_VALUE);

        resolveBtn.setOnAction(e -> {
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            if (!FX.confirm(dlg,"Mark "+incId+" as resolved? Citizen will confirm.")) return;
            try {
                facade.teamMarkResolved(incId, team.getTeamName());
                FX.info(dlg,"Resolution requested. Waiting for citizen confirmation.");
                status("Resolution requested: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });
        disputeBtn.setOnAction(e -> {
            if (!raiseDisputeCheck.isSelected()) { FX.error(dlg,"Check 'Raise a dispute' and select reason."); return; }
            TeamDisputeType dtype = dispBox.getValue();
            if (dtype==null) { FX.error(dlg,"Select dispute reason."); return; }
            int idx = box.getSelectionModel().getSelectedIndex(); if (idx<0) return;
            String incId = eligible.get(idx).getIncidentId();
            try {
                facade.raiseTeamDispute(incId, team.getTeamName(), dtype, "MARK_RESOLUTION");
                FX.info(dlg,"Dispute raised: "+dtype.getLabel());
                status("Dispute raised: "+incId); refresh(); dlg.close();
            } catch (Exception ex) { FX.error(dlg, ex.getMessage()); }
        });

        HBox btns = new HBox(10,resolveBtn,disputeBtn);
        HBox.setHgrow(resolveBtn,Priority.ALWAYS); HBox.setHgrow(disputeBtn,Priority.ALWAYS);

        root.getChildren().addAll(
            FX.dynLabel("Mark Incident Resolved",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box),
            raiseDisputeCheck,
            FX.formRow("Dispute Reason:", dispBox),
            btns);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private void showDetail() {
        List<Incident> list = facade.getTeamIncidents(team.getTeamName());
        if (list.isEmpty()) { FX.info(this,"No assigned incidents."); return; }

        Stage dlg = new Stage(); dlg.initOwner(this); WindowKit.keepOwnerState(dlg); dlg.initModality(Modality.WINDOW_MODAL);
        dlg.setTitle("Incident Detail"); dlg.setWidth(520); dlg.setHeight(440);

        VBox root = new VBox(12); FX.applyRootStyle(root); root.setPadding(new Insets(18));

        ComboBox<String> box = FX.combo();
        list.forEach(i -> box.getItems().add(i.getIncidentId())); box.getSelectionModel().selectFirst();

        TextArea detail = FX.textArea("",false); detail.setPrefRowCount(14);
        VBox.setVgrow(detail, Priority.ALWAYS);

        Runnable upd = () -> { String s = box.getValue(); if (s!=null){ Incident i=facade.getIncidentById(s); if(i!=null) detail.setText(buildDetail(i)); } };
        box.setOnAction(e->upd.run()); upd.run();

        root.getChildren().addAll(FX.dynLabel("Incident Detail",16,true,ThemeManager::textPrim),
            FX.formRow("Select Incident:", box), detail);
        FX.fadeIn(root); dlg.setScene(new Scene(root)); dlg.showAndWait();
    }

    private String buildDetail(Incident inc) {
        StringBuilder sb = new StringBuilder();
        sb.append("===================================\n  INCIDENT DETAILS\n===================================\n");
        sb.append("  ID          : ").append(inc.getIncidentId()).append("\n");
        sb.append("  Type        : ").append(inc.getType()).append("\n");
        sb.append("  Location    : ").append(inc.getLocation()).append("\n");
        sb.append("  Status      : ").append(inc.getStatus()).append("\n");
        sb.append("  Reported At : ").append(inc.getFormattedTimestamp()).append("\n");
        sb.append("  Description : ").append(inc.getDescription()).append("\n");
        sb.append("===================================\n  Citizens Linked: ").append(inc.getLinkedCitizens().size()).append("\n");
        if (!inc.getLinkedCitizens().isEmpty())
            sb.append("  First Reporter: ").append(inc.getLinkedCitizens().get(0).getName()).append("\n");
        return sb.toString();
    }

    private void refresh() {
        List<Incident> list = facade.getTeamIncidents(team.getTeamName());
        table.getItems().setAll(list);
        DashKit.updateStats(statVals, list);
    }
    private void status(String m) { statusBar.setText(" "+m); }

    @Override public void onIncidentUpdated(Incident i)               { Platform.runLater(this::refresh); }
    @Override public void onMapUpdated(MapLocation l, IncidentStatus s){ }
    @Override public void onDisputeRaised(Incident i)                 { Platform.runLater(this::refresh); }
}


// INFO PAGES (About / Emergency Services / Contact)

class InfoPageScreen extends Stage {
    static final int ABOUT = 0, SERVICES = 1, CONTACT = 2;

    private final Stage owner;
    private final int kind;
    private final Runnable themeListener = this::render;

    public InfoPageScreen(Stage owner, int kind) {
        this.owner = owner;
        this.kind = kind;
        setTitle("INCERIA - " + (kind == ABOUT ? "About" : kind == SERVICES ? "Emergency Services" : "Contact"));
        setWidth(960);
        setHeight(700);
        setMinWidth(680);
        setMinHeight(500);
        initStyle(StageStyle.DECORATED);
        initOwner(owner);
        setMaximized(true);   // open full screen
        WindowKit.keepOwnerState(this);
        setScene(new Scene(build()));
        ThemeManager.addListener(themeListener);
        setOnHidden(e -> ThemeManager.removeListener(themeListener));
    }

    private void render() { getScene().setRoot((Parent) build()); }

    // palette
    private static boolean dk()     { return ThemeManager.isDark(); }
    private static Color surface()  { return dk() ? Color.web("#131E33") : Color.WHITE; }
    private static Color line()     { return dk() ? Color.web("#26354F") : Color.web("#D6DCE7"); }
    private static Color ink()      { return dk() ? Color.web("#EAF0FA") : Color.web("#0B2545"); }
    private static Color inkSoft()  { return dk() ? Color.web("#9FB0C8") : Color.web("#475A75"); }
    private static Color teal()     { return dk() ? Color.web("#2DB5A9") : Color.web("#0E8F86"); }
    private static String hex(Color c) { return ThemeManager.toHex(c); }
    private static String rgba(Color c, double a) {
        return String.format(Locale.US, "rgba(%d,%d,%d,%.3f)",
            (int) Math.round(c.getRed() * 255), (int) Math.round(c.getGreen() * 255),
            (int) Math.round(c.getBlue() * 255), a);
    }

    private Label lbl(String s, double size, FontWeight w, Color c, boolean wrap) {
        Label l = new Label(s);
        l.setFont(Font.font("Segoe UI", w, size));
        l.setTextFill(c);
        l.setWrapText(wrap);
        return l;
    }

    private VBox card(String title, String body, Color accent) {
        Rectangle bar = new Rectangle(34, 3);
        bar.setFill(accent);
        Label t = lbl(title, 16, FontWeight.BOLD, ink(), true);
        Label b = lbl(body, 12.5, FontWeight.NORMAL, inkSoft(), true);
        VBox c = new VBox(10, bar, t, b);
        c.setPadding(new Insets(20));
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle("-fx-background-color:" + rgba(surface(), 0.94) + ";-fx-background-radius:4;-fx-border-radius:4;" +
                   "-fx-border-color:" + hex(line()) + ";-fx-border-width:1;");
        HBox.setHgrow(c, Priority.ALWAYS);
        return c;
    }

    private HBox cardRow(VBox... cards) {
        HBox row = new HBox(18, cards);
        row.setFillHeight(true);
        return row;
    }

    private VBox steps(String heading, String[] items) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(18, 22, 18, 22));
        box.setStyle("-fx-background-color:" + rgba(surface(), 0.88) + ";-fx-background-radius:4;" +
                     "-fx-border-radius:4;-fx-border-color:" + hex(line()) + ";-fx-border-width:1;");
        box.getChildren().add(lbl(heading, 20, FontWeight.BOLD, ink(), false));
        for (int i = 0; i < items.length; i++) {
            Label n = lbl(String.valueOf(i + 1), 13, FontWeight.BOLD, Color.WHITE, false);
            StackPane badge = new StackPane(n);
            badge.setMinSize(28, 28);
            badge.setMaxSize(28, 28);
            badge.setStyle("-fx-background-color:" + hex(teal()) + ";-fx-background-radius:14;");
            Label t = lbl(items[i], 13.5, FontWeight.NORMAL, ink(), true);
            HBox row = new HBox(14, badge, t);
            row.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().add(row);
        }
        return box;
    }

    private Parent build() {
        BorderPane shell = new BorderPane();
        shell.setStyle("-fx-background-color:transparent;");

        // top bar
        Button back = new Button("\u2190 Back to Home");
        back.setFocusTraversable(false);
        back.setStyle("-fx-background-color:transparent;-fx-text-fill:" + hex(ink()) + ";" +
            "-fx-border-color:" + hex(line()) + ";-fx-border-width:1;-fx-border-radius:4;-fx-background-radius:4;" +
            "-fx-padding:7 16;-fx-font-family:'Segoe UI';-fx-font-size:12px;-fx-font-weight:bold;-fx-cursor:hand;");
        back.setOnAction(e -> close());

        Button themeBtn = new Button(dk() ? "Light Mode" : "Dark Mode");
        themeBtn.setFocusTraversable(false);
        themeBtn.setStyle(back.getStyle());
        themeBtn.setOnAction(e -> ThemeManager.toggle());

        HBox top = new HBox(16, FX.logoWidget(30), back, FX.hSpacer(), themeBtn);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(8, 28, 8, 24));
        top.setStyle("-fx-background-color:" + rgba(surface(), 0.92) + ";-fx-border-color:transparent transparent " +
                     hex(line()) + " transparent;-fx-border-width:0 0 1 0;");
        shell.setTop(top);

        // hero
        String eyebrow, title, sub;
        if (kind == ABOUT) {
            eyebrow = "ABOUT INCERIA"; title = "One platform for faster emergency response";
            sub = "INCERIA connects citizens, administrators and response teams in a single incident management system.";
        } else if (kind == SERVICES) {
            eyebrow = "EMERGENCY SERVICES"; title = "Help when it matters, around the clock";
            sub = "Report an incident and track it from the first report to confirmed resolution.";
        } else {
            eyebrow = "CONTACT"; title = "Get in touch with INCERIA";
            sub = "Emergency Services are available 24/7. Choose the right channel for what you need.";
        }
        Label e1 = lbl(eyebrow, 11, FontWeight.BOLD, Color.web("#5CD6CB"), false);
        Label t1 = lbl(title, 30, FontWeight.BOLD, Color.WHITE, true);
        Label s1 = lbl(sub, 14, FontWeight.NORMAL, Color.web("#C5D3E8"), true);
        s1.setMaxWidth(620);
        VBox hero = new VBox(8, e1, t1, s1);
        hero.setPadding(new Insets(34, 48, 34, 48));
        hero.setStyle("-fx-background-color:" + (dk()
            ? "linear-gradient(to bottom right, rgba(8,18,31,0.86), rgba(13,35,64,0.82));"
            : "linear-gradient(to bottom right, rgba(11,37,69,0.92), rgba(19,64,116,0.88));") +
            "-fx-border-color:transparent transparent " + hex(teal()) + " transparent;-fx-border-width:0 0 3 0;");

        // body
        VBox body = new VBox(30);
        body.setPadding(new Insets(34, 48, 40, 48));
        body.setStyle("-fx-background-color:transparent;");

        Color navy = dk() ? Color.web("#5B8DEF") : Color.web("#1E4FA3");
        Color alert = dk() ? Color.web("#F08A5D") : Color.web("#D9531E");
        Color orange = dk() ? Color.web("#F59232") : Color.web("#EA7C14");
        Color blue = dk() ? Color.web("#5B8DEF") : Color.web("#2563EB");

        if (kind == ABOUT) {
            body.getChildren().addAll(
                cardRow(
                    card("Citizens", "Report emergencies, follow the progress of your incidents and confirm arrival and resolution.", teal()),
                    card("Administrators", "Monitor every incident, dispatch or re-assign teams and review disputes.", orange),
                    card("Response Teams", "Receive assignments, mark arrival and update incident progress until resolved.", blue)),
                steps("How it works", new String[]{
                    "A citizen reports an incident with its type, location and description.",
                    "An administrator reviews it and assigns the right response team.",
                    "The team marks arrival and the citizen confirms it.",
                    "The team marks the incident resolved and the citizen confirms the resolution."}));
        } else if (kind == SERVICES) {
            Button report = new Button("Report an Emergency");
            report.setStyle("-fx-background-color:" + hex(teal()) + ";-fx-text-fill:white;-fx-background-radius:4;" +
                "-fx-padding:11 24;-fx-font-family:'Segoe UI';-fx-font-size:13px;-fx-font-weight:bold;-fx-cursor:hand;");
            report.setOnAction(e -> { new CitizenNameEmailScreen(owner).show(); close(); });
            body.getChildren().addAll(
                cardRow(
                    card("Fire", "Fire and rescue teams for building fires, smoke and related hazards.", alert),
                    card("Theft", "Police response for theft, break-ins and other security incidents.", navy),
                    card("Accident", "Medical and traffic response teams for accidents and injuries.", teal())),
                steps("How to report", new String[]{
                    "Open the Citizen Portal and register with your name and email.",
                    "Choose the incident type and location, then describe what happened.",
                    "Track the assigned team and status live on the map."}),
                report);
        } else {
            body.getChildren().addAll(
                cardRow(
                    card("Availability", "Emergency Services run 24/7, every day of the year.", teal()),
                    card("Report an emergency", "Use the Citizen Portal to file a report with your name and email.", alert)),
                cardRow(
                    card("Administrators and teams", "Sign in from the Administration Portal or the Response Team portal on the home page.", orange),
                    card("General enquiries", "Email: support@inceria.example\nPhone: +00 000 000 0000", teal())));
        }

        ScrollPane sp = new ScrollPane(new VBox(hero, body));
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-border-color:transparent;");
        shell.setCenter(sp);
        return DispatchBackdrop.wrap(shell);
    }
}


// MAIN - JavaFX Application Entry Point

public class UILayer extends Application {
    @Override
    public void start(Stage primaryStage) {
        RoleSelectionScreen screen = new RoleSelectionScreen();
        screen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
