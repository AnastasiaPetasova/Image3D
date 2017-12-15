package com.anastasia.app.image3d;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Polygon;
import com.anastasia.app.image3d.algo.transform.*;
import com.anastasia.app.image3d.algo.triangulation.Figure3D;
import com.anastasia.app.image3d.algo.triangulation.Figures;
import com.anastasia.app.image3d.algo.triangulation.Polygonization;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Image3dController implements Initializable {
    @FXML
    Canvas imageCanvas;

    @FXML
    Button startRotateClockwiseButton;

    @FXML
    Button startRotateAntiClockwiseButton;

    @FXML
    Button stopTurnButton;

    @FXML
    Button zoomInButton;

    @FXML
    Button zoomOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCanvas();
        initTextField();
        initButtons();

        initThreads();

        drawImage();
    }

    private void clearCanvas() {
        GraphicsContext graphics = imageCanvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, imageCanvas.getWidth(), imageCanvas.getHeight());
    }

    private void initCanvas() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int sizeCoeff = Image3dApplication.SIZE_COEFF;

        imageCanvas.setWidth(screenSize.width * sizeCoeff / (sizeCoeff + 1) - 100);
        imageCanvas.setHeight(screenSize.height * sizeCoeff / (sizeCoeff + 1) - 100);

        clearCanvas();
    }

    private void initTextField() {

    }

    private double extractRadius() {
        return 1000;
    }

    private PolygonPointsTransform with(PointTransform pointTransform) {
        return PolygonPointsTransform.with(pointTransform);
    }

    private Polygon[] prepareToDraw(Polygon[] polygons) {

        AffineTransform rotateXZ = AffineTransforms
                .rotate(rotateAngle, AffineTransforms.X_AXIS + AffineTransforms.Z_AXIS);

        PointTransform userViewTransform = userViewTransform();

        AffineTransform centeredZoom = centeredZoomTranform();

        PolygonsTransform[] transforms = {
                with(rotateXZ),
                Transforms.ACSONOMETRIC_TRANSFORM,
                with(rotateXZ),

                with(userViewTransform),
                Transforms.SORT_BY_DEPTH,
                screenTransform(),

                with(centeredZoom)
        };

        for (PolygonsTransform transform : transforms) {
            polygons = transform.transform(polygons);
        }

        return polygons;
    }

    /**
     * MAX_H = (-R .. R + R) = 3R
     * Экран -> x = [-1.5R, 1.5R], y = [0..MAX_H], z = 1.5R
     * Пользователь -> (0, MAX_H + DELTA_H, 6R] -> tg rotate -> DELTA_H / Y ( OR Y / DELTA_H)
     *
     * double x = xPoints[j] * (zUser - zScreen) / (zUser - zPoints[j]);
     * double y = yPoints[j] * (zUser - zScreen) / (zUser - zPoints[j]);
     * double z = zPoints[j] - zScreen;
     */
    private PointTransform userViewTransform() {
        double radius = extractRadius();

        double zUser = 6 * radius;
        double zScreen = 1.5 * radius;

        return value -> {
            double coeff = (zUser - zScreen) / (zUser - value.z);

            double x = value.x * coeff;
            double y = value.y * coeff;
            double z = value.z - zScreen;

            return new Point3D(x, y, z);
        };
    }

    private void initButtons() {
        initDrawImageButton();

        initRotateButtons();
        initZoomButtons();
    }

    private PolygonPointsTransform screenTransform() {

        double radius = extractRadius();

        return new PolygonPointsTransform() {
            @Override
            protected PointTransform collectTransformInfo(Polygon[] polygons) {
                double xMinScreen = Integer.MAX_VALUE;
                double xMaxScreen = Integer.MIN_VALUE;

                double yMinScreen = Integer.MAX_VALUE;
                double yMaxScreen = Integer.MIN_VALUE;

                for (Polygon polygon : polygons) {
                    for (double x : polygon.xPoints()) {
                        xMinScreen = Math.min(xMinScreen, x);
                        xMaxScreen = Math.max(xMaxScreen, x);
                    }

                    for (double y : polygon.yPoints()) {
                        yMinScreen = Math.min(yMinScreen, y);
                        yMaxScreen = Math.max(yMaxScreen, y);
                    }
                }

                xMinScreen -= 0.5 * radius;
                xMaxScreen += 0.5 * radius;

                yMinScreen -= 0.1 * radius;
                yMaxScreen += 0.1 * radius;

                double coeffX = imageCanvas.getWidth() / (xMaxScreen - xMinScreen);
                double coeffY = imageCanvas.getHeight() / (yMaxScreen - yMinScreen);

                final double finalXMin = xMinScreen;
                final double finalYMax = yMaxScreen;

                return value -> {
                    int x = (int)((value.x - finalXMin) * coeffX);
                    int y = (int)((finalYMax - value.y) * coeffY);

                    int z = 0;

                    return new Point3D(x, y, z);
                };
            }
        };


    }

    private AffineTransform centeredZoomTranform() {
        double centerX = imageCanvas.getWidth() / 2;
        double centerY = imageCanvas.getHeight() / 2;

        return AffineTransforms.chain(
                AffineTransforms.shift(-centerX, -centerY, 0),
                AffineTransforms.zoom(zoomCoeff, zoomCoeff, 0),
                AffineTransforms.shift(centerX, centerY, 0)
        );
    }

    private void drawImage(Polygon[] polygons) {
        GraphicsContext graphicsContext = imageCanvas.getGraphicsContext2D();

        clearCanvas();

        graphicsContext.setStroke(Color.BLACK);

        final double lineWidth = 5;
        graphicsContext.setLineWidth(lineWidth);

        graphicsContext.setFill(Color.WHITE);

        for (Polygon polygon : polygons) {
            double[] xPoints = polygon.xPoints();
            double[] yPoints = polygon.yPoints();
            int n = polygon.size();

            graphicsContext.strokePolygon(
                    xPoints, yPoints, n
            );

            graphicsContext.fillPolygon(
                    xPoints, yPoints, n
            );
        }
    }

    private void drawImage() {
        double radius = extractRadius();
        Figure3D figure = Figures.KINDER_SURPRISE;

        int nAlpha = 40, nBeta = 20;

        Polygon[] polygons = Polygonization.generate(radius, nAlpha, nBeta, figure);
        polygons = prepareToDraw(polygons);
        drawImage(polygons);
    }

    private void initDrawImageButton() {

    }

    private static final double DELTA_ROTATE_ANGLE = Math.PI / 180;
    private volatile double rotateAngle = 0, deltaRotateAngle = 0;
    private volatile boolean rotating = false;

    private void rotateImage(){
        if (rotating) {
            drawImage();

            rotateAngle += deltaRotateAngle;
            if (rotateAngle > 2 * Math.PI) rotateAngle -= 2 * Math.PI;
            if (rotateAngle < 0) rotateAngle += 2 * Math.PI;
        }
    }

    private void initRotateButtons() {
        startRotateClockwiseButton.setOnAction(event -> {
            rotating = true;
            deltaRotateAngle = DELTA_ROTATE_ANGLE;
        });

        startRotateAntiClockwiseButton.setOnAction(event -> {
            rotating = true;
            deltaRotateAngle = -DELTA_ROTATE_ANGLE;
        });

        stopTurnButton.setOnAction(event -> {
            rotating = false;

            drawImage();
        });
    }

    private static final double DELTA_ZOOM_COEFF = 0.1;
    private double zoomCoeff = 1;

    private void initZoomButtons() {
        zoomInButton.setOnAction(event -> {
            zoomCoeff *= (1 + DELTA_ZOOM_COEFF);
            drawImage();
        });

        zoomOutButton.setOnAction(event -> {
            zoomCoeff /= (1 + DELTA_ZOOM_COEFF);
            drawImage();
        });
    }

    private void initThreads() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                rotateImage();
            }
        };

        timer.start();
    }
}
