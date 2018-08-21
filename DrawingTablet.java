package cs2410.assn6;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static java.lang.Math.abs;

/**
 * @author Ryan
 * @version 1.0
 */
public class DrawingTablet extends Application
{
    private Pane drawingPane;
    private ToolPane toolPane = new ToolPane();

    private Scene drawTabScene;
    private BorderPane tabletPane = new BorderPane();
    private Rectangle drawClip;

    private EventHandler<MouseEvent> drawingHandler;
    private EventHandler<MouseEvent> shapeStartHandler;
    private EventHandler<MouseEvent> shapeFinishHandler;
    private EventHandler<MouseEvent> startLocationHandler;

    private double startX;
    private double startY;

    private double shapeStartX;
    private double shapeStartY;

    private Ellipse currentEllipse;
    private Rectangle currentRectangle;
    private Path currentPath;
    private Shape currentShape;

    private boolean drawStarted;

    /**
     * This is the main function for the project, basic setup is done here
     *
     * @param primaryStage The main stage for the program
     */
    public void start(Stage primaryStage)
    {
        initDrawingHandler();
        initShapeStartHandler();
        initShapeFinishHandler();
        initStartLocationHandler();

        tabletPane.setTop(toolPane);

        // drawing pane
        drawingPane = new Pane();
        drawingPane.setPrefSize(500, 550);
        drawingPane.scaleXProperty().bind(tabletPane.scaleXProperty());
        drawingPane.scaleYProperty().bind(tabletPane.scaleYProperty());
        drawingPane.setOnDragDetected(shapeStartHandler);
        drawingPane.setOnMouseDragged(drawingHandler);
        drawingPane.setOnMouseReleased(shapeFinishHandler);
        drawingPane.setOnMousePressed(startLocationHandler);
        drawClip = new Rectangle();
        drawClip.widthProperty().bind(drawingPane.widthProperty());
        drawClip.heightProperty().bind(drawingPane.heightProperty());
        drawingPane.setClip(drawClip);

        // Overall Pane
        tabletPane.setCenter(drawingPane);

        drawTabScene = new Scene(tabletPane, 550, 500);

        primaryStage.setMinWidth(550);
        primaryStage.setMinHeight(100);
        primaryStage.setTitle("Drawing Tablet");
        primaryStage.setScene(drawTabScene);
        primaryStage.show();

        /**
         * closes the program upon click of the 'X' at the top corner
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                System.out.println("Program Closed Properly");
                System.exit(0);
            }
        });
    }

    /**
     * The event handler for dragging the mouse to resize the shape
     */
    private void initDrawingHandler()
    {
        drawingHandler = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (drawStarted)
                {
                    if (toolPane.ellBtnSelected())
                    {
                        currentEllipse.setCenterX(((event.getX() - startX) / 2) + startX);
                        currentEllipse.setCenterY(((event.getY() - startY) / 2) + startY);
                        currentEllipse.setRadiusX(abs(event.getX() - startX) / 2);
                        currentEllipse.setRadiusY(abs(event.getY() - startY) / 2);
                    }
                    else if (toolPane.rectBtnSelected())
                    {
                        if (event.getX() > startX)
                        {
                            currentRectangle.setWidth(event.getX() - startX);
                        }
                        else
                        {
                            currentRectangle.setLayoutX(event.getX());
                            currentRectangle.setWidth(startX - event.getX());
                        }
                        if (event.getY() > startY)
                        {
                            currentRectangle.setHeight(abs(startY - event.getY()));
                        }
                        else
                        {
                            currentRectangle.setLayoutY(event.getY());
                            currentRectangle.setHeight(abs(startY - event.getY()));
                        }
                    }
                    else if (toolPane.freeBtnSelected())
                    {
                        currentPath.getElements().add(new LineTo(event.getX(), event.getY()));
                    }
                }

            }
        };
    }

    /**
     * the event handler for setting when to create a shape
     */
    private void initShapeStartHandler()
    {
        shapeStartHandler = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (toolPane.ellBtnSelected())
                {
                    currentEllipse = new Ellipse();
                    currentEllipse.setFill(toolPane.getFillPickerValue());
                    currentEllipse.setStroke(toolPane.getStrokePickerValue());
                    currentEllipse.setStrokeWidth(toolPane.getStrokeSizeValue());
                    currentEllipse.setCenterX(event.getX());
                    currentEllipse.setCenterY(event.getY());
                    currentEllipse.setRadiusX(abs(event.getX() - startX));
                    currentEllipse.setRadiusY(abs(event.getY() - startY));
                    drawingPane.getChildren().add(currentEllipse);
                    drawStarted = true;
                    initShapeEdit(currentEllipse);
                }
                else if (toolPane.rectBtnSelected())
                {
                    currentRectangle = new Rectangle();
                    currentRectangle.setFill(toolPane.getFillPickerValue());
                    currentRectangle.setStroke(toolPane.getStrokePickerValue());
                    currentRectangle.setStrokeWidth(toolPane.getStrokeSizeValue());
                    currentRectangle.setLayoutX(event.getX());
                    currentRectangle.setLayoutY(event.getY());
                    currentRectangle.setWidth(event.getX() - startX);
                    currentRectangle.setHeight(event.getX() - startY);
                    drawingPane.getChildren().add(currentRectangle);
                    drawStarted = true;
                    initShapeEdit(currentRectangle);
                }
            }
        };
    }

    /**
     * the event handler for ending the drawing of a shape
     */
    private void initShapeFinishHandler()
    {
        shapeFinishHandler = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (drawStarted)
                {
                    drawStarted = false;
                }
            }
        };
    }

    /**
     * this event handler detects where the mouse was originally pressed and sets initial location for object creation,
     * also sets where a free drawn path should start
     */
    private void initStartLocationHandler()
    {
        startLocationHandler = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                startX = event.getX();
                startY = event.getY();

                if (toolPane.freeBtnSelected())
                {
                    currentPath = new Path();
                    drawingPane.getChildren().add(currentPath);
                    currentPath.setStrokeWidth(toolPane.getStrokeSizeValue());
                    currentPath.setStroke(toolPane.getStrokePickerValue());
                    currentPath.getElements().add(new MoveTo(event.getX(), event.getY()));
                    drawStarted = true;
                    initShapeEdit(currentPath);
                }
            }
        };
    }

    /**
     * this event handler is created for each ellipse upon initial creation
     * it is responsible for detecting when an ellipse is clicked on to delete, edit, or move
     *
     * @param shape The ellipse passed in upon creation
     */
    private void initShapeEdit(Shape shape)
    {
        /**
         * event handler for deleting shapes, or if in edit mode, changing fill and stroke to current shape's values
         */
        shape.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                currentShape = (Shape) event.getSource();

                if (toolPane.eraseBtnSelected())
                {
                    drawingPane.getChildren().remove(shape);
                }
                else if (toolPane.editBtnSelected())
                {
                    toolPane.setFillPickerValue((Color) shape.getFill());
                    toolPane.setStrokePickerValue((Color) shape.getStroke());
                    toolPane.setStrokeSizeValue((int) shape.getStrokeWidth());
                }
            }
        });
        /**
          * this is the event handler to identify where the begining of a shape occurs
          */
        shape.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                shapeStartX = event.getX();
                shapeStartY = event.getY();
            }
        });
        /**
         * event handler for dragging shapes
         */
        shape.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (toolPane.editBtnSelected())
                {
                    shape.setTranslateX(shape.getTranslateX() + event.getX() - shapeStartX);
                    shape.setTranslateY(shape.getTranslateY() + event.getY() - shapeStartY);
                }
            }
        });

        /**
         * event handler for changing fill in edit mode
         */
        toolPane.setFillPickerAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (currentShape != null)
                {
                    currentShape.setFill(toolPane.getFillPickerValue());
                }
            }
        });

        /**
         * event handler for changing stroke color in edit mode
         */
        toolPane.setStrokePickerAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (currentShape != null)
                {
                    currentShape.setStroke(toolPane.getStrokePickerValue());
                }
            }
        });

        /**
         * event handler for changing stroke size in edit mode
         */
        toolPane.setStrokeSizeAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (currentShape != null)
                {
                    currentShape.setStrokeWidth(toolPane.getStrokeSizeValue());
                }
            }
        });
    }
}


