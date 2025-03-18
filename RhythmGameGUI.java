import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Abstract parent class: Background
abstract class RhythmGameLayer extends JPanel {
    private BufferedImage backgroundImage;

    public RhythmGameLayer() {
        loadImage();
    }

    protected abstract void loadImage();

    protected void setBackgroundImage(String filePath) {
        try {
            backgroundImage = ImageIO.read(new File(filePath));
            System.out.println("Background image loaded successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to load background image: " + filePath);
            e.printStackTrace();
        }
    }

    protected BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// Child class: Adds Judgement Line
class JudgementLineLayer extends RhythmGameLayer {
    private BufferedImage judgementLineImage;

    public JudgementLineLayer() {
        super();
    }

    @Override
    protected void loadImage() {
        setBackgroundImage("Background.png"); // Load background
        try {
            judgementLineImage = ImageIO.read(new File("Judgement Line.png"));
            System.out.println("Judgement line image loaded successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load judgement line image.");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (judgementLineImage != null) {
            // Scale judgement line to match background size
            g.drawImage(judgementLineImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// Grandchild class: Adds UI
class UILayer extends JudgementLineLayer {
    private BufferedImage uiImage;

    public UILayer() {
        super();
    }

    @Override
    protected void loadImage() {
        super.loadImage();
        try {
            uiImage = ImageIO.read(new File("UI.png"));
            System.out.println("UI image loaded successfully.");
        } catch (IOException e) {
            System.err.println("Failed to load UI image.");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (uiImage != null) {
            // Scale UI to match background size
            g.drawImage(uiImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// Great-grandchild class: Adds Notes
class NotesLayer extends UILayer {
    private BufferedImage notesImage;
    private String noteType;

    public NotesLayer() {
        super();
    }

    @Override
    protected void loadImage() {
        super.loadImage();
        // Randomly choose between two note images
        if (Math.random() < 0.5) {
            noteType = "easy";
            try {
                notesImage = ImageIO.read(new File("Notes1.png"));
                System.out.println("Notes1 image loaded successfully.");
            } catch (IOException e) {
                System.err.println("Failed to load Notes1 image.");
                e.printStackTrace();
            }
        } else {
            noteType = "hard";
            try {
                notesImage = ImageIO.read(new File("Notes2.png"));
                System.out.println("Notes2 image loaded successfully.");
            } catch (IOException e) {
                System.err.println("Failed to load Notes2 image.");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Draw background, judgement line, and UI first
        if (notesImage != null) {
            // Scale notes to match background size
            g.drawImage(notesImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Add description based on the note type
        String description = "The song 'Inverted World' has a " + getNoteType() + " score.";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(description, 20, getHeight() - 30);
    }

    public String getNoteType() {
        return noteType;
    }
}

// Main class to display GUI
public class RhythmGameGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rhythm Game GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the layered panel
            RhythmGameLayer rhythmGame = new NotesLayer();

            // Set the frame size to match the background image size
            BufferedImage backgroundImage = rhythmGame.getBackgroundImage();
            if (backgroundImage != null) {
                int width = backgroundImage.getWidth();
                int height = backgroundImage.getHeight();
                frame.setSize(width, height); // Set window size to match the background image
            } else {
                frame.setSize(800, 600); // Default size if background image is not loaded
            }

            frame.add(rhythmGame);
            frame.setVisible(true);
        });
    }
}