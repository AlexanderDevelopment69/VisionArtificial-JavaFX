package com.app.appvisionartificial.Componets;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ResizeHelper {
    public static double xOffset = 0;
    public static double yOffset = 0;

    private static double minWidth = 950;
    private static double minHeight = 620;
    private static boolean isResizing = false;

    public static void setResizeHandlers(Stage stage, AnchorPane root) {
        // Evento de movimiento del ratón para cambiar el cursor cuando está sobre una esquina
        root.setOnMouseMoved(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = root.getWidth();
            double height = root.getHeight();

            if (x <= 10 && y <= 10) {
                isResizing = true;
                root.setCursor(Cursor.NW_RESIZE);
            } else if (x >= width - 10 && y <= 10) {
                isResizing = true;
                root.setCursor(Cursor.NE_RESIZE);
            } else if (x <= 10 && y >= height - 10) {
                isResizing = true;
                root.setCursor(Cursor.SW_RESIZE);
            } else if (x >= width - 10 && y >= height - 10) {
                isResizing = true;
                root.setCursor(Cursor.SE_RESIZE);
            } else {
                isResizing = false;
                root.setCursor(Cursor.DEFAULT);
            }
        });

        // Evento de clic del ratón para determinar si se está redimensionando
        root.setOnMouseDragged(event -> {
            if (isResizing) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                double width = stage.getWidth();
                double height = stage.getHeight();

                if (root.getCursor() == Cursor.NW_RESIZE) {
                    double newWidth = width + (xOffset - x);
                    double newHeight = height + (yOffset - y);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setX(x);
                        stage.setY(y);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (root.getCursor() == Cursor.NE_RESIZE) {
                    double newWidth = width + (x - width);
                    double newHeight = height + (yOffset - y);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setY(y);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (root.getCursor() == Cursor.SW_RESIZE) {
                    double newWidth = width + (xOffset - x);
                    double newHeight = height + (y - height);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setX(x);
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                } else if (root.getCursor() == Cursor.SE_RESIZE) {
                    double newWidth = width + (x - width);
                    double newHeight = height + (y - height);
                    if (newWidth >= minWidth && newHeight >= minHeight) {
                        stage.setWidth(newWidth);
                        stage.setHeight(newHeight);
                    }
                }
            }
        });

        // Evento de liberación del ratón para finalizar el redimensionamiento
        root.setOnMouseReleased(event -> {
            isResizing = false;
            root.setCursor(Cursor.DEFAULT);
        });
    }




    public static void setMoveHandlers(Stage stage) {
        // Agregar un ChangeListener para escuchar cuando se establezca la escena
        stage.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Scene scene = stage.getScene();
                if (scene == null) {
                    throw new IllegalStateException("Scene must be set on the Stage before setting move handlers.");
                }

                scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                });

                scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
                    if (!isResizing) {
                        stage.setX(event.getScreenX() - xOffset);
                        stage.setY(event.getScreenY() - yOffset);
                    }
                });

            }
        });

    }
}

