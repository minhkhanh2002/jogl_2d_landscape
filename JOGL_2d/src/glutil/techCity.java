package glutil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class techCity extends GLJPanel implements GLEventListener {
	 	private static final int PANEL_WIDTH = 720;  
	    private static final int PANEL_HEIGHT = 1080; 
	    private static final int FPS = 60;           // khung hình 
	    private GLU glu;
	    private GLUT glut = new GLUT();
	    private Animator animator;
	    private GLJPanel canvas;
	    private boolean isNightMode = false; // Ban đầu là ban ngày

	    private float p = -10;
	    private int i = -700;
	    private float j = -250;
	    private float k = -100;
	    
    // Main entry point
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                techCity techCityInstance = new techCity(); // Tạo instance của lớp
	                techCityInstance.canvas = techCityInstance; // Gán canvas cho instance

	                techCityInstance.canvas.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
	                FPSAnimator animator = new FPSAnimator(techCityInstance.canvas, FPS, true);

	                JFrame frame = new JFrame("JOGL Scene");
	                frame.getContentPane().add(techCityInstance.canvas);
	                frame.pack();

	                // set kích thước tùy theo kích cỡ màn hình
	                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	                int width = Math.min(screenSize.width, 800);
	                int height = Math.min(screenSize.height, 1000);
	                frame.setSize(width, height);

	                frame.setLocationRelativeTo(null);
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.setVisible(true);
	                animator.start();
	            }
	        });
	    }
    private void glutBitmapCharacter(GL2 gl, char character) {
        // Phương pháp này sử dụng GLUT để hiển thị các ký tự
        com.jogamp.opengl.util.gl2.GLUT glut = new com.jogamp.opengl.util.gl2.GLUT();
        glut.glutBitmapCharacter(GLUT.BITMAP_9_BY_15, character);
    }

//		drawCircle(gl, 100.0f, 50.0f, 350.0f, 400.0f);
	
		
	@Override
	public void dispose(GLAutoDrawable drawable) {
        if (animator != null && animator.isAnimating()) {
            animator.stop();
        }
        }
	private void drawCircle(GL2 gl, float rx, float ry, float cx, float cy) {

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex2f(cx, cy); // Tâm của hình tròn
        for (int angle = 0; angle <= 360; angle++) {
            double radian = Math.toRadians(angle);
            float x = (float) (rx * Math.cos(radian));
            float y = (float) (ry * Math.sin(radian));
            gl.glVertex2f(x + cx, y + cy);
        }
        gl.glEnd();
    }
	
	@Override
	public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background là màu đen
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, PANEL_WIDTH, 0.0, PANEL_HEIGHT, -10.0, 10.0); // Thiết lập hệ tọa độ
        
        //Gọi hàm tự động chuyển đổi giữa ngày và đêm
        setupAutoToggle();
    }
	private void setupAutoToggle() {
	    Timer timer = new Timer(10000, new ActionListener() { // Cứ mỗi 5 giây sẽ chuyển đổi
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            toggleNightMode(); // Đảo trạng thái ngày/đêm
	            canvas.display();  // Vẽ lại khung hình
	        }
	    });
	    timer.start(); // Khởi động bộ đếm thời gian
	}
	public void toggleNightMode() {
	    isNightMode = !isNightMode; // Đảo ngược trạng thái
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    final GL2 gl = drawable.getGL().getGL2();

	    if (height == 0) height = 1;
	    float aspect = (float) width / height;

	    gl.glViewport(0, 0, width, height); // Viewport theo chiều mới

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();

	    if (width > height) {
	        gl.glOrtho(0.0, PANEL_WIDTH, 0.0, PANEL_HEIGHT / aspect, -10.0, 10.0);
	    } else {
	        gl.glOrtho(0.0, PANEL_WIDTH * aspect, 0.0, PANEL_HEIGHT, -10.0, 10.0);
	    }

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

    // Constructor
    public techCity() {
        this.addGLEventListener(this);
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

	@Override
	public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        // xóa màn hình
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // vẽ
        gl.glPushMatrix();
        draw(gl);
     
    }

	private void draw(GL2 gl) {
		
		// 1. Tô màu nền với gradient vàng -> xanh lam
		gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3ub((byte) 255, (byte) 255, (byte) 147); // Vàng nhạt
	    gl.glVertex2f(0, 100);
	    gl.glVertex2f(800, 100);
	    gl.glColor3ub((byte) 102, (byte) 204, (byte) 255); // Xanh dương nhạt
	    gl.glVertex2f(800, 800);
	    gl.glVertex2f(0, 800);
	    gl.glEnd();

	    // 2. Mặt đất phía trước
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3ub((byte) 90, (byte) 147, (byte) 48); // Màu xanh lá
	    gl.glVertex2f(0, 100);
	    gl.glVertex2f(800, 100);
	    gl.glVertex2f(800, 119.5f);
	    gl.glVertex2f(0, 119.5f);
	    gl.glEnd();

	    // 3. Đường chính (màu xám đen)
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3f(0.2f, 0.2f, 0.2f); // Màu xám đậm
	    gl.glVertex2f(0, 30);
	    gl.glVertex2f(800, 30);
	    gl.glVertex2f(800, 80);
	    gl.glVertex2f(0, 80);
	    gl.glEnd();

	    // 4. Các vạch kẻ trên đường
	    // Đường trên
	    gl.glBegin(GL2.GL_LINES);
	    gl.glLineWidth(2.0f);
	    gl.glColor3f(1.0f, 1.0f, 1.0f); // Màu trắng
	    gl.glVertex2i(0, 81);
	    gl.glVertex2i(800, 81);
	    gl.glEnd();

	    // Đường giữa
	    gl.glBegin(GL2.GL_LINES);
	    gl.glLineWidth(2.0f);
	    gl.glColor3f(1.0f, 1.0f, 1.0f); // Màu trắng
	    gl.glVertex2i(0, 62);
	    gl.glVertex2i(800, 62);
	    gl.glEnd();

	    // Đường dưới
	    gl.glBegin(GL2.GL_LINES);
	    gl.glLineWidth(2.0f);
	    gl.glColor3f(1.0f, 1.0f, 1.0f); // Màu trắng
	    gl.glVertex2i(0, 39);
	    gl.glVertex2i(800, 39);
	    gl.glEnd();

	    // 5. Vỉa hè phía trên
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3ub((byte) 90, (byte) 147, (byte) 48); // Màu xanh lá
	    gl.glVertex2f(0, 82);
	    gl.glVertex2f(800, 82);
	    gl.glVertex2f(800, 90);
	    gl.glVertex2f(0, 90);
	    gl.glEnd();

	    // 6. Vỉa hè phía dưới
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3ub((byte) 90, (byte) 147, (byte) 48); // Màu xanh lá
	    gl.glVertex2f(0, 38);
	    gl.glVertex2f(800, 38);
	    gl.glVertex2f(800, 0);
	    gl.glVertex2f(0, 0);
	    gl.glEnd();
	    
	 // 1. Bầu trời
	    gl.glBegin(GL2.GL_QUADS);
	    if (isNightMode) {
	        gl.glColor3ub((byte) 25, (byte) 25, (byte) 112); // Màu xanh đậm (night sky)
	    } else {
	        gl.glColor3ub((byte) 255, (byte) 255, (byte) 147); // Vàng nhạt (day sky)
	    }
	    gl.glVertex2f(0, 100);
	    gl.glVertex2f(800, 100);

	    if (isNightMode) {
	        gl.glColor3ub((byte) 0, (byte) 0, (byte) 51); // Xanh đen
	    } else {
	        gl.glColor3ub((byte) 102, (byte) 204, (byte) 255); // Xanh dương nhạt
	    }
	    gl.glVertex2f(800, 800);
	    gl.glVertex2f(0, 800);
	    gl.glEnd();

	    // 2. Mặt đất
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glColor3ub((byte) 90, (byte) 147, (byte) 48); // Giữ nguyên màu đất
	    gl.glVertex2f(0, 100);
	    gl.glVertex2f(800, 100);
	    gl.glVertex2f(800, 119.5f);
	    gl.glVertex2f(0, 119.5f);
	    gl.glEnd();

	    // 3. Mặt trời hoặc mặt trăng
	    if (isNightMode) {
	        // Vẽ mặt trăng
	        gl.glColor3ub((byte) 230, (byte) 230, (byte) 250); // Màu trắng nhạt
	        drawCircle(gl, 18, 36, 400, 705);
	    } else {
	        // Vẽ mặt trời
	        gl.glColor3ub((byte) 253, (byte) 183, (byte) 77);
	        drawCircle(gl, 18, 36, 400, 705);
	    }
//	    // Vẽ cột đèn đường tại các vị trí khác nhau
//	    drawStreetLamp(gl, 100.0f, 100.0f);  // Vị trí đầu tiên
//	    drawStreetLamp(gl, 200.0f, 100.0f);  // Vị trí thứ hai
//	    drawStreetLamp(gl, 300.0f, 100.0f);  // Vị trí thứ ba
	    
	 // Cây 1: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(680, 0, 0);
	    gl.glVertex3f(685, 0, 0);
	    gl.glVertex3f(685, 20, 0);
	    gl.glVertex3f(680, 20, 0);
	    gl.glEnd();

	    // Cây 1: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(675, 10, 0);
	    gl.glVertex3f(690, 10, 0);
	    gl.glVertex3f(682.5f, 40, 0);
	    gl.glVertex3f(682.5f, 40, 0);
	    gl.glEnd();

	    // Cây 1: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(676, 15, 0);
	    gl.glVertex3f(689, 15, 0);
	    gl.glVertex3f(682.5f, 45, 0);
	    gl.glVertex3f(682.5f, 45, 0);
	    gl.glEnd();

	    // Cây 2: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(580, 0, 0);
	    gl.glVertex3f(585, 0, 0);
	    gl.glVertex3f(585, 20, 0);
	    gl.glVertex3f(580, 20, 0);
	    gl.glEnd();

	    // Cây 2: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(575, 10, 0);
	    gl.glVertex3f(590, 10, 0);
	    gl.glVertex3f(582.5f, 40, 0);
	    gl.glVertex3f(582.5f, 40, 0);
	    gl.glEnd();

	    // Cây 2: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(576, 15, 0);
	    gl.glVertex3f(589, 15, 0);
	    gl.glVertex3f(582.5f, 45, 0);
	    gl.glVertex3f(582.5f, 45, 0);
	    gl.glEnd();

	    // Cây 3: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(480, 0, 0);
	    gl.glVertex3f(485, 0, 0);
	    gl.glVertex3f(485, 20, 0);
	    gl.glVertex3f(480, 20, 0);
	    gl.glEnd();

	    // Cây 3: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(475, 10, 0);
	    gl.glVertex3f(490, 10, 0);
	    gl.glVertex3f(482.5f, 40, 0);
	    gl.glVertex3f(482.5f, 40, 0);
	    gl.glEnd();

	    // Cây 3: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(476, 15, 0);
	    gl.glVertex3f(489, 15, 0);
	    gl.glVertex3f(482.5f, 45, 0);
	    gl.glVertex3f(482.5f, 45, 0);
	    gl.glEnd();
	    
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(380, 0, 0);
	    gl.glVertex3f(385, 0, 0);
	    gl.glVertex3f(385, 20, 0);
	    gl.glVertex3f(380, 20, 0);
	    gl.glEnd();

	    // Cây 4: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(375, 10, 0);
	    gl.glVertex3f(390, 10, 0);
	    gl.glVertex3f(382.5f, 40, 0);
	    gl.glVertex3f(382.5f, 40, 0);
	    gl.glEnd();

	    // Cây 4: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(376, 15, 0);
	    gl.glVertex3f(389, 15, 0);
	    gl.glVertex3f(382.5f, 45, 0);
	    gl.glVertex3f(382.5f, 45, 0);
	    gl.glEnd();

	    // Cây 5: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(280, 0, 0);
	    gl.glVertex3f(285, 0, 0);
	    gl.glVertex3f(285, 20, 0);
	    gl.glVertex3f(280, 20, 0);
	    gl.glEnd();

	    // Cây 5: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(275, 10, 0);
	    gl.glVertex3f(290, 10, 0);
	    gl.glVertex3f(282.5f, 40, 0);
	    gl.glVertex3f(282.5f, 40, 0);
	    gl.glEnd();

	    // Cây 5: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(276, 15, 0);
	    gl.glVertex3f(289, 15, 0);
	    gl.glVertex3f(282.5f, 45, 0);
	    gl.glVertex3f(282.5f, 45, 0);
	    gl.glEnd();

	    // Cây 6: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(180, 0, 0);
	    gl.glVertex3f(185, 0, 0);
	    gl.glVertex3f(185, 20, 0);
	    gl.glVertex3f(180, 20, 0);
	    gl.glEnd();

	    // Cây 6: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(175, 10, 0);
	    gl.glVertex3f(190, 10, 0);
	    gl.glVertex3f(182.5f, 40, 0);
	    gl.glVertex3f(182.5f, 40, 0);
	    gl.glEnd();

	    // Cây 6: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(176, 15, 0);
	    gl.glVertex3f(189, 15, 0);
	    gl.glVertex3f(182.5f, 45, 0);
	    gl.glVertex3f(182.5f, 45, 0);
	    gl.glEnd();

	    // Cây 7: Thân cây
	    gl.glBegin(GL2.GL_TRIANGLE_FAN); 
	    gl.glColor3ub((byte) 75, (byte) 35, (byte) 5); // Màu nâu
	    gl.glVertex3f(80, 0, 0);
	    gl.glVertex3f(85, 0, 0);
	    gl.glVertex3f(85, 20, 0);
	    gl.glVertex3f(80, 20, 0);
	    gl.glEnd();

	    // Cây 7: Tán cây (lớp dưới cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(75, 10, 0);
	    gl.glVertex3f(90, 10, 0);
	    gl.glVertex3f(82.5f, 40, 0);
	    gl.glVertex3f(82.5f, 40, 0);
	    gl.glEnd();

	    // Cây 7: Tán cây (lớp trên cùng)
	    gl.glBegin(GL2.GL_TRIANGLE_FAN);
	    gl.glColor3ub((byte) 0, (byte) 102, (byte) 0); // Màu xanh lá
	    gl.glVertex3f(76, 15, 0);
	    gl.glVertex3f(89, 15, 0);
	    gl.glVertex3f(82.5f, 45, 0);
	    gl.glVertex3f(82.5f, 45, 0);
	    gl.glEnd();
	    

		// Tầng 1 tòa nhà p1
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(50, 90);
		gl.glVertex2f(110, 90);
		gl.glVertex2f(110, 475);
		gl.glVertex2f(50, 475);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(52.5f, 90); // Tầng 1 tòa nhà p2
		gl.glVertex2f(106, 90);
		gl.glVertex2f(106, 460);
		gl.glVertex2f(52.5f, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(110, 90); 
		gl.glVertex2f(140, 90);
		gl.glVertex2f(140, 420);
		gl.glVertex2f(110, 420);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(110, 90); 
		gl.glVertex2f(137.5f, 90);
		gl.glVertex2f(137.5f, 410);
		gl.glVertex2f(110, 410);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(113, 370); 
		gl.glVertex2f(137.5f, 370);
		gl.glVertex2f(137.5f, 400);
		gl.glVertex2f(113, 400);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71);
		gl.glVertex2f(113, 365); 
		gl.glVertex2f(123, 365);
		gl.glVertex2f(123, 400);
		gl.glVertex2f(113, 400);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(118, 365); 
		gl.glVertex2f(119, 365);
		gl.glVertex2f(119, 392);
		gl.glVertex2f(118, 392);
		gl.glEnd();

		// Vẽ các tầng của 1st Building
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		//
		gl.glVertex2f(90, 90);
		gl.glVertex2f(197, 90);
		gl.glVertex2f(197, 145);
		gl.glVertex2f(90, 145);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 2nd floor1
		gl.glVertex2f(90, 145);
		gl.glVertex2f(197, 145);
		gl.glVertex2f(197, 200);
		gl.glVertex2f(90, 200);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 3rd floor1
		gl.glVertex2f(90, 200);
		gl.glVertex2f(197, 200);
		gl.glVertex2f(197, 255);
		gl.glVertex2f(90, 255);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 4th floor1
		gl.glVertex2f(90, 255);
		gl.glVertex2f(197, 255);
		gl.glVertex2f(197, 310);
		gl.glVertex2f(90, 310);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 5th floor1
		gl.glVertex2f(90, 310);
		gl.glVertex2f(197, 310);
		gl.glVertex2f(197, 365);
		gl.glVertex2f(90, 365);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 6th floor1
		gl.glVertex2f(90, 365);
		gl.glVertex2f(197, 365);
		gl.glVertex2f(197, 420);
		gl.glVertex2f(90, 420);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// 1st Building 7th floor1
		gl.glVertex2f(90, 420);
		gl.glVertex2f(197, 420);
		gl.glVertex2f(197, 475);
		gl.glVertex2f(90, 475);
		gl.glEnd();


		// Vẽ các phần của SIRI GHORE
		gl.glBegin(GL2.GL_QUADS);
		// Hình chữ nhật chính (màu xám nhạt)
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(90, 475);
		gl.glVertex2f(120, 475);
		gl.glVertex2f(120, 515);
		gl.glVertex2f(90, 515);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// Hình chữ nhật bên trong (màu trắng)
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(92, 475);
		gl.glVertex2f(118, 475);
		gl.glVertex2f(118, 510);
		gl.glVertex2f(92, 510);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// Hình chữ nhật giữa (màu nâu)
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71);
		gl.glVertex2f(100, 475);
		gl.glVertex2f(110, 475);
		gl.glVertex2f(110, 505);
		gl.glVertex2f(100, 505);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// Cửa sổ nhỏ (màu trắng)
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(105, 483);
		gl.glVertex2f(106, 483);
		gl.glVertex2f(106, 492);
		gl.glVertex2f(105, 492);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		// Đường thẳng đứng (màu xám đậm)
		gl.glColor3ub((byte) 140, (byte) 140, (byte) 140);
		gl.glVertex2f(65, 475);
		gl.glVertex2f(66, 475);
		gl.glVertex2f(66, 600);
		gl.glVertex2f(65, 600);
		gl.glEnd();

		// Vẽ các vòng tròn
//		drawCircle(gl, 3, 6, 64, 580, (byte) 102, (byte) 102, (byte) 102); // Vòng tròn lớn (màu xám đậm)
//		drawCircle(gl, 2.5f, 5, 64, 580, (byte) 217, (byte) 217, (byte) 217); // Vòng tròn nhỏ hơn (màu xám nhạt)
//		drawCircle(gl, 2, 4, 67, 565, (byte) 102, (byte) 102, (byte) 102); // Vòng tròn khác (màu xám đậm)
//		drawCircle(gl, 1.5f, 3, 67, 565, (byte) 217, (byte) 217, (byte) 217); // Vòng tròn nhỏ nhất (màu xám nhạt)

		// Vẽ vòng tròn lớn (màu xám đậm)
		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102); // Thiết lập màu xám đậm
		drawCircle(gl, 3, 6, 64, 580);

		// Vẽ vòng tròn nhỏ hơn (màu xám nhạt)
		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217); // Thiết lập màu xám nhạt
		drawCircle(gl, 2.5f, 5, 64, 580);

		// Vẽ vòng tròn khác (màu xám đậm)
		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102); // Thiết lập màu xám đậm
		drawCircle(gl, 2, 4, 67, 565);

		// Vẽ vòng tròn nhỏ nhất (màu xám nhạt)
		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217); // Thiết lập màu xám nhạt
		drawCircle(gl, 1.5f, 3, 67, 565);

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(110, 420); // Phần 2, Tầng 7, 1st Building (màu trắng)
		gl.glVertex2f(194, 420);
		gl.glVertex2f(194, 460);
		gl.glVertex2f(110, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 365); // Phần 2, Tầng 6, 1st Building
		gl.glVertex2f(194, 365);
		gl.glVertex2f(194, 405);
		gl.glVertex2f(140, 405);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 310); // Phần 2, Tầng 5, 1st Building
		gl.glVertex2f(194, 310);
		gl.glVertex2f(194, 350);
		gl.glVertex2f(140, 350);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 255); // Phần 2, Tầng 4, 1st Building
		gl.glVertex2f(194, 255);
		gl.glVertex2f(194, 295);
		gl.glVertex2f(140, 295);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 200); // Phần 2, Tầng 3, 1st Building
		gl.glVertex2f(194, 200);
		gl.glVertex2f(194, 240);
		gl.glVertex2f(140, 240);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 145); // Phần 2, Tầng 2, 1st Building
		gl.glVertex2f(194, 145);
		gl.glVertex2f(194, 185);
		gl.glVertex2f(140, 185);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(140, 90); // Phần 2, Tầng 1, 1st Building
		gl.glVertex2f(194, 90);
		gl.glVertex2f(194, 130);
		gl.glVertex2f(140, 130);
		gl.glEnd();

		/// design of main building....red and glass..............
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 420); // Phần 3, Tầng 7, 1st Building
		gl.glVertex2f(67f, 420);
		gl.glVertex2f(67f, 460);
		gl.glVertex2f(52.5f, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 365); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(67f, 365);
		gl.glVertex2f(67f, 405);
		gl.glVertex2f(52.5f, 405);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 310); // Phần 3, Tầng 5, 1st Building
		gl.glVertex2f(67f, 310);
		gl.glVertex2f(67f, 350);
		gl.glVertex2f(52.5f, 350);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 255); // Phần 3, Tầng 4, 1st Building
		gl.glVertex2f(67f, 255);
		gl.glVertex2f(67f, 295);
		gl.glVertex2f(52.5f, 295);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 200); // Phần 3, Tầng 3, 1st Building
		gl.glVertex2f(67f, 200);
		gl.glVertex2f(67f, 240);
		gl.glVertex2f(52.5f, 240);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 145); // Phần 3, Tầng 2, 1st Building
		gl.glVertex2f(67f, 145);
		gl.glVertex2f(67f, 185);
		gl.glVertex2f(52.5f, 185);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(52.5f, 90); // Phần 3, Tầng 1, 1st Building
		gl.glVertex2f(67f, 90);
		gl.glVertex2f(67f, 130);
		gl.glVertex2f(52.5f, 130);
		gl.glEnd();

		///////// .....................
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 420); // Phần 3, Tầng 7, 1st Building
		gl.glVertex2f(106f, 420);
		gl.glVertex2f(106f, 460);
		gl.glVertex2f(95f, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 365); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(106f, 365);
		gl.glVertex2f(106f, 405);
		gl.glVertex2f(95f, 405);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 310); // Phần 3, Tầng 5, 1st Building
		gl.glVertex2f(106f, 310);
		gl.glVertex2f(106f, 350);
		gl.glVertex2f(95f, 350);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 255); // Phần 3, Tầng 4, 1st Building
		gl.glVertex2f(106f, 255);
		gl.glVertex2f(106f, 295);
		gl.glVertex2f(95f, 295);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 200); // Phần 3, Tầng 3, 1st Building
		gl.glVertex2f(106f, 200);
		gl.glVertex2f(106f, 240);
		gl.glVertex2f(95f, 240);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(95f, 145); // Phần 3, Tầng 2, 1st Building
		gl.glVertex2f(106f, 145);
		gl.glVertex2f(106f, 185);
		gl.glVertex2f(95f, 185);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(85f, 90); // Cửa chính
		gl.glVertex2f(106f, 90);
		gl.glVertex2f(106f, 130);
		gl.glVertex2f(85f, 130);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(95f, 90); // Cửa chính 1
		gl.glVertex2f(96f, 90);
		gl.glVertex2f(96f, 130);
		gl.glVertex2f(95f, 130);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(96f, 100); // Cửa chính 2
		gl.glVertex2f(97f, 100);
		gl.glVertex2f(97f, 120);
		gl.glVertex2f(96f, 120);
		gl.glEnd();

		////////// ............................
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 420f); // Phần 3, Tầng 7, 1st Building
		gl.glVertex2f(110f, 420f);
		gl.glVertex2f(110f, 421f);
		gl.glVertex2f(50f, 421f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 365f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(137.5f, 365f);
		gl.glVertex2f(137.5f, 366f);
		gl.glVertex2f(50f, 366f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 310f); // Phần 3, Tầng 5, 1st Building
		gl.glVertex2f(110f, 310f);
		gl.glVertex2f(110f, 311f);
		gl.glVertex2f(50f, 311f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 255f); // Phần 3, Tầng 4, 1st Building
		gl.glVertex2f(110f, 255f);
		gl.glVertex2f(110f, 256f);
		gl.glVertex2f(50f, 256f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 199f); // Phần 3, Tầng 3, 1st Building
		gl.glVertex2f(110f, 199f);
		gl.glVertex2f(110f, 200f);
		gl.glVertex2f(50f, 200f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 145f); // Phần 3, Tầng 2, 1st Building
		gl.glVertex2f(110f, 145f);
		gl.glVertex2f(110f, 146f);
		gl.glVertex2f(50f, 146f);
		gl.glEnd();

		/////// ..............................

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 405f); // Phần 3, Tầng 7, 1st Building
		gl.glVertex2f(110f, 405f);
		gl.glVertex2f(110f, 406f);
		gl.glVertex2f(50f, 406f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 350f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(110f, 350f);
		gl.glVertex2f(110f, 351f);
		gl.glVertex2f(50f, 351f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 295f); // Phần 3, Tầng 5, 1st Building
		gl.glVertex2f(110f, 295f);
		gl.glVertex2f(110f, 296f);
		gl.glVertex2f(50f, 296f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 240f); // Phần 3, Tầng 4, 1st Building
		gl.glVertex2f(110f, 240f);
		gl.glVertex2f(110f, 241f);
		gl.glVertex2f(50f, 241f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 185f); // Phần 3, Tầng 3, 1st Building
		gl.glVertex2f(110f, 185f);
		gl.glVertex2f(110f, 186f);
		gl.glVertex2f(50f, 186f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(50f, 130f); // Phần 3, Tầng 2, 1st Building
		gl.glVertex2f(110f, 130f);
		gl.glVertex2f(110f, 131f);
		gl.glVertex2f(50f, 131f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 89, (byte) 89, (byte) 89);
		gl.glVertex2f(140f, 90f); // Phần 2
		gl.glVertex2f(141f, 90f);
		gl.glVertex2f(141f, 420f);
		gl.glVertex2f(140f, 420f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(110f, 365f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(120f, 365f);
		gl.glVertex2f(120f, 385f);
		gl.glVertex2f(110f, 385f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(128f, 365f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(137.5f, 365f);
		gl.glVertex2f(137.5f, 385f);
		gl.glVertex2f(128f, 385f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 57, (byte) 80, (byte) 80);
		gl.glVertex2f(120f, 370f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(128f, 370f);
		gl.glVertex2f(128.5f, 372f);
		gl.glVertex2f(120f, 372f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 57, (byte) 80, (byte) 80);
		gl.glVertex2f(120f, 379f); // Phần 3, Tầng 6, 1st Building
		gl.glVertex2f(128f, 379f);
		gl.glVertex2f(128.5f, 381f);
		gl.glVertex2f(120f, 381f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(110f, 310f); // Phần 2, 2nd part2
		gl.glVertex2f(137.5f, 310f);
		gl.glVertex2f(137.5f, 350f);
		gl.glVertex2f(110f, 350f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(110f, 255f); // Phần 2, 2nd part3
		gl.glVertex2f(137.5f, 255f);
		gl.glVertex2f(137.5f, 295f);
		gl.glVertex2f(110f, 295f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(110f, 200f); // Phần 2, 2nd part3
		gl.glVertex2f(137.5f, 200f);
		gl.glVertex2f(137.5f, 240f);
		gl.glVertex2f(110f, 240f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(110f, 145f); // Phần 2, 2nd part3
		gl.glVertex2f(137.5f, 145f);
		gl.glVertex2f(137.5f, 185f);
		gl.glVertex2f(110f, 185f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(110f, 90f); // Phần 1, 1st Building main part3
		gl.glVertex2f(137.5f, 90f);
		gl.glVertex2f(137.5f, 130f);
		gl.glVertex2f(110f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(117f, 90f); // Phần 1, 1st Building main part3
		gl.glVertex2f(118f, 90f);
		gl.glVertex2f(118f, 130f);
		gl.glVertex2f(117f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(127f, 90f); // Phần 1, 1st Building main part3
		gl.glVertex2f(128f, 90f);
		gl.glVertex2f(128f, 130f);
		gl.glVertex2f(127f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(118f, 100f); // Phần 1, 1st Building main part3
		gl.glVertex2f(119f, 100f);
		gl.glVertex2f(119f, 120f);
		gl.glVertex2f(118f, 120f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(128f, 100f); // Phần 1, 1st Building main part3
		gl.glVertex2f(129f, 100f);
		gl.glVertex2f(129f, 120f);
		gl.glVertex2f(128f, 120f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51);
		gl.glVertex2f(110f, 90f); // Partition
		gl.glVertex2f(111f, 90f);
		gl.glVertex2f(111f, 475f);
		gl.glVertex2f(110f, 475f);
		gl.glEnd();

		// 7th Floor Design

		// Part 2, 1st Building, 7th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(113f, 425f);
		gl.glVertex2f(191f, 425f);
		gl.glVertex2f(191f, 455f);
		gl.glVertex2f(113f, 455f);
		gl.glEnd();

		// Door (d1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71);
		gl.glVertex2f(113f, 420f);
		gl.glVertex2f(123f, 420f);
		gl.glVertex2f(123f, 455f);
		gl.glVertex2f(113f, 455f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(118f, 430f);
		gl.glVertex2f(119f, 430f);
		gl.glVertex2f(119f, 445f);
		gl.glVertex2f(118f, 445f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(133.5f, 433f);
		gl.glVertex2f(152.5f, 433f);
		gl.glVertex2f(152.5f, 447f);
		gl.glVertex2f(133.5f, 447f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 432f);
		gl.glVertex2f(144f, 432f);
		gl.glVertex2f(144f, 448f);
		gl.glVertex2f(143f, 448f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 432f);
		gl.glVertex2f(183f, 432f);
		gl.glVertex2f(183f, 448f);
		gl.glVertex2f(163f, 448f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 433f);
		gl.glVertex2f(182.5f, 433f);
		gl.glVertex2f(182.5f, 447f);
		gl.glVertex2f(163.5f, 447f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 432f);
		gl.glVertex2f(174f, 432f);
		gl.glVertex2f(174f, 448f);
		gl.glVertex2f(173f, 448f);
		gl.glEnd();

		// 6th Floor Design - Door and Window

		// Part 2, 1st Building, 6th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 370f); // Inlook
		gl.glVertex2f(191f, 370f);
		gl.glVertex2f(191f, 400f);
		gl.glVertex2f(141f, 400f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 377f);
		gl.glVertex2f(153f, 377f);
		gl.glVertex2f(153f, 393f);
		gl.glVertex2f(141f, 393f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 378f);
		gl.glVertex2f(152.5f, 378f);
		gl.glVertex2f(152.5f, 392f);
		gl.glVertex2f(141f, 392f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 377f);
		gl.glVertex2f(144f, 377f);
		gl.glVertex2f(144f, 393f);
		gl.glVertex2f(143f, 393f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 377f);
		gl.glVertex2f(183f, 377f);
		gl.glVertex2f(183f, 393f);
		gl.glVertex2f(163f, 393f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 378f);
		gl.glVertex2f(182.5f, 378f);
		gl.glVertex2f(182.5f, 392f);
		gl.glVertex2f(163.5f, 392f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 377f);
		gl.glVertex2f(174f, 377f);
		gl.glVertex2f(174f, 393f);
		gl.glVertex2f(173f, 393f);
		gl.glEnd();

		// 5th Floor Design - Door and Window

		// Part 2, 1st Building, 5th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 315f); // Part 2
		gl.glVertex2f(191f, 315f);
		gl.glVertex2f(191f, 345f);
		gl.glVertex2f(141f, 345f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 322f);
		gl.glVertex2f(153f, 322f);
		gl.glVertex2f(153f, 338f);
		gl.glVertex2f(141f, 338f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 323f);
		gl.glVertex2f(152.5f, 323f);
		gl.glVertex2f(152.5f, 337f);
		gl.glVertex2f(141f, 337f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 322f);
		gl.glVertex2f(144f, 322f);
		gl.glVertex2f(144f, 338f);
		gl.glVertex2f(143f, 338f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 322f);
		gl.glVertex2f(183f, 322f);
		gl.glVertex2f(183f, 338f);
		gl.glVertex2f(163f, 338f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 323f);
		gl.glVertex2f(182.5f, 323f);
		gl.glVertex2f(182.5f, 337f);
		gl.glVertex2f(163.5f, 337f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 322f);
		gl.glVertex2f(174f, 322f);
		gl.glVertex2f(174f, 338f);
		gl.glVertex2f(173f, 338f);
		gl.glEnd();

		// 4th Floor Design - Door and Window

		// Part 2, 1st Building, 4th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 260f); // Part 2
		gl.glVertex2f(191f, 260f);
		gl.glVertex2f(191f, 290f);
		gl.glVertex2f(141f, 290f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 267f);
		gl.glVertex2f(153f, 267f);
		gl.glVertex2f(153f, 283f);
		gl.glVertex2f(141f, 283f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 268f);
		gl.glVertex2f(152.5f, 268f);
		gl.glVertex2f(152.5f, 282f);
		gl.glVertex2f(141f, 282f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 267f);
		gl.glVertex2f(144f, 267f);
		gl.glVertex2f(144f, 283f);
		gl.glVertex2f(143f, 283f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 267f);
		gl.glVertex2f(183f, 267f);
		gl.glVertex2f(183f, 283f);
		gl.glVertex2f(163f, 283f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 268f);
		gl.glVertex2f(182.5f, 268f);
		gl.glVertex2f(182.5f, 282f);
		gl.glVertex2f(163.5f, 282f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 267f);
		gl.glVertex2f(174f, 267f);
		gl.glVertex2f(174f, 283f);
		gl.glVertex2f(173f, 283f);
		gl.glEnd();

		// 3rd Floor Design - Door and Window

		// Part 2, 1st Building, 3rd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 205f); // Part 2
		gl.glVertex2f(191f, 205f);
		gl.glVertex2f(191f, 235f);
		gl.glVertex2f(141f, 235f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 212f);
		gl.glVertex2f(153f, 212f);
		gl.glVertex2f(153f, 228f);
		gl.glVertex2f(141f, 228f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 213f);
		gl.glVertex2f(152.5f, 213f);
		gl.glVertex2f(152.5f, 227f);
		gl.glVertex2f(141f, 227f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 212f);
		gl.glVertex2f(144f, 212f);
		gl.glVertex2f(144f, 228f);
		gl.glVertex2f(143f, 228f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 212f);
		gl.glVertex2f(183f, 212f);
		gl.glVertex2f(183f, 228f);
		gl.glVertex2f(163f, 228f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 213f);
		gl.glVertex2f(182.5f, 213f);
		gl.glVertex2f(182.5f, 227f);
		gl.glVertex2f(163.5f, 227f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 212f);
		gl.glVertex2f(174f, 212f);
		gl.glVertex2f(174f, 228f);
		gl.glVertex2f(173f, 228f);
		gl.glEnd();

		// 2nd Floor Design - Door and Window

		// Part 2, 1st Building, 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 150f); // Part 2
		gl.glVertex2f(191f, 150f);
		gl.glVertex2f(191f, 180f);
		gl.glVertex2f(141f, 180f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 157f);
		gl.glVertex2f(153f, 157f);
		gl.glVertex2f(153f, 173f);
		gl.glVertex2f(141f, 173f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 158f);
		gl.glVertex2f(152.5f, 158f);
		gl.glVertex2f(152.5f, 172f);
		gl.glVertex2f(141f, 172f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 157f);
		gl.glVertex2f(144f, 157f);
		gl.glVertex2f(144f, 173f);
		gl.glVertex2f(143f, 173f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 157f);
		gl.glVertex2f(183f, 157f);
		gl.glVertex2f(183f, 173f);
		gl.glVertex2f(163f, 173f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 158f);
		gl.glVertex2f(182.5f, 158f);
		gl.glVertex2f(182.5f, 172f);
		gl.glVertex2f(163.5f, 172f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 157f);
		gl.glVertex2f(174f, 157f);
		gl.glVertex2f(174f, 173f);
		gl.glVertex2f(173f, 173f);
		gl.glEnd();

		// 1st Floor Design - Door and Window

		// Part 2, 1st Building, 1st floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(141f, 95f); // Part 2
		gl.glVertex2f(191f, 95f);
		gl.glVertex2f(191f, 125f);
		gl.glVertex2f(141f, 125f);
		gl.glEnd();

		// Door 2 (d2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71);
		gl.glVertex2f(196f, 90f); // d2
		gl.glVertex2f(206f, 90f);
		gl.glVertex2f(206f, 125f);
		gl.glVertex2f(196f, 125f);
		gl.glEnd();

		// Door 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(201f, 100f); // d2 small section
		gl.glVertex2f(202f, 100f);
		gl.glVertex2f(202f, 115f);
		gl.glVertex2f(201f, 115f);
		gl.glEnd();

		// Window 1 (w1)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(141f, 102f); // w1
		gl.glVertex2f(153f, 102f);
		gl.glVertex2f(153f, 118f);
		gl.glVertex2f(141f, 118f);
		gl.glEnd();

		// Window 1, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(141f, 103f);
		gl.glVertex2f(152.5f, 103f);
		gl.glVertex2f(152.5f, 117f);
		gl.glVertex2f(141f, 117f);
		gl.glEnd();

		// Window 1 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(143f, 102f);
		gl.glVertex2f(144f, 102f);
		gl.glVertex2f(144f, 118f);
		gl.glVertex2f(143f, 118f);
		gl.glEnd();

		// Window 2 (w2)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(163f, 102f);
		gl.glVertex2f(183f, 102f);
		gl.glVertex2f(183f, 118f);
		gl.glVertex2f(163f, 118f);
		gl.glEnd();

		// Window 2, Inner details
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(163.5f, 103f);
		gl.glVertex2f(182.5f, 103f);
		gl.glVertex2f(182.5f, 117f);
		gl.glVertex2f(163.5f, 117f);
		gl.glEnd();

		// Window 2 Small section
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(173f, 102f);
		gl.glVertex2f(174f, 102f);
		gl.glVertex2f(174f, 118f);
		gl.glVertex2f(173f, 118f);
		gl.glEnd();

		////////////////

		// 2nd Building Main Part

		// Outer part of the 2nd building (gray)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204); // Light gray
		gl.glVertex2f(200f, 120f);
		gl.glVertex2f(287f, 120f);
		gl.glVertex2f(287f, 395f);
		gl.glVertex2f(200f, 395f);
		gl.glEnd();

		// Inner part of the 2nd building (lighter gray)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Very light gray
		gl.glVertex2f(202.5f, 120f); // Slightly inset from outer walls
		gl.glVertex2f(284f, 120f);
		gl.glVertex2f(284f, 380f); // Slightly shorter height
		gl.glVertex2f(202.5f, 380f);
		gl.glEnd();

		// 1st floor design for 2nd building (red parts)

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red part
		gl.glVertex2f(205f, 125f); // bottom-left
		gl.glVertex2f(281f, 125f); // bottom-right
		gl.glVertex2f(281f, 155f); // top-right
		gl.glVertex2f(205f, 155f); // top-left
		gl.glEnd();

		// Inner red part of 1st floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red part
		gl.glVertex2f(281f, 125f);
		gl.glVertex2f(267f, 125f);
		gl.glVertex2f(267f, 155f);
		gl.glVertex2f(281f, 155f);
		gl.glEnd();

		// Door 1
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71); // Door color
		gl.glVertex2f(205f, 120f); // bottom-left
		gl.glVertex2f(215f, 120f); // bottom-right
		gl.glVertex2f(215f, 155f); // top-right
		gl.glVertex2f(205f, 155f); // top-left
		gl.glEnd();

		// Door window
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(209f, 132f); // bottom-left
		gl.glVertex2f(210f, 132f); // bottom-right
		gl.glVertex2f(210f, 147f); // top-right
		gl.glVertex2f(209f, 147f); // top-left
		gl.glEnd();

		// Window 1
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(223f, 131f); // bottom-left
		gl.glVertex2f(243f, 131f); // bottom-right
		gl.glVertex2f(243f, 147f); // top-right
		gl.glVertex2f(223f, 147f); // top-left
		gl.glEnd();

		// Window 1 inner color
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color
		gl.glVertex2f(223.5f, 132f); // bottom-left
		gl.glVertex2f(242.5f, 132f); // bottom-right
		gl.glVertex2f(242.5f, 146f); // top-right
		gl.glVertex2f(223.5f, 146f); // top-left
		gl.glEnd();

		// Window 1 inner window frame
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(233.1f, 131f); // bottom-left
		gl.glVertex2f(234f, 131f); // bottom-right
		gl.glVertex2f(234f, 147f); // top-right
		gl.glVertex2f(233.1f, 147f); // top-left
		gl.glEnd();

		// Window 2
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(253f, 131f); // bottom-left
		gl.glVertex2f(273f, 131f); // bottom-right
		gl.glVertex2f(273f, 147f); // top-right
		gl.glVertex2f(253f, 147f); // top-left
		gl.glEnd();

		// Window 2 inner color
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color
		gl.glVertex2f(253.5f, 132f); // bottom-left
		gl.glVertex2f(272.5f, 132f); // bottom-right
		gl.glVertex2f(272.5f, 146f); // top-right
		gl.glVertex2f(253.5f, 146f); // top-left
		gl.glEnd();

		// Window 2 inner window frame
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(263.1f, 131f); // bottom-left
		gl.glVertex2f(264f, 131f); // bottom-right
		gl.glVertex2f(264f, 147f); // top-right
		gl.glVertex2f(263.1f, 147f); // top-left
		gl.glEnd();

		// Floor Division for 2nd building (1st floor)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204); // Light grey color
		gl.glVertex2f(202.5f, 160f); // bottom-left
		gl.glVertex2f(284f, 160f); // bottom-right
		gl.glVertex2f(284f, 175f); // top-right
		gl.glVertex2f(202.5f, 175f); // top-left
		gl.glEnd();

		// 2nd floor design for 2nd building (red part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(205f, 180f); // bottom-left
		gl.glVertex2f(281f, 180f); // bottom-right
		gl.glVertex2f(281f, 210f); // top-right
		gl.glVertex2f(205f, 210f); // top-left
		gl.glEnd();

		// Inner red part of 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(281f, 180f); // bottom-left
		gl.glVertex2f(264f, 180f); // bottom-right
		gl.glVertex2f(264f, 210f); // top-right
		gl.glVertex2f(281f, 210f); // top-left
		gl.glEnd();

		// Door for 2nd building 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71); // Brown color for the door
		gl.glVertex2f(205f, 175f); // bottom-left
		gl.glVertex2f(215f, 175f); // bottom-right
		gl.glVertex2f(215f, 210f); // top-right
		gl.glVertex2f(205f, 210f); // top-left
		gl.glEnd();

		// Door window for 2nd building 2nd floor (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(209f, 187f); // bottom-left
		gl.glVertex2f(210f, 187f); // bottom-right
		gl.glVertex2f(210f, 202f); // top-right
		gl.glVertex2f(209f, 202f); // top-left
		gl.glEnd();

		// Window 1 for 2nd building 2nd floor (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(223f, 186f); // bottom-left
		gl.glVertex2f(243f, 186f); // bottom-right
		gl.glVertex2f(243f, 202f); // top-right
		gl.glVertex2f(223f, 202f); // top-left
		gl.glEnd();

		// Window 1 (colored part) for 2nd building 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color for the window
		gl.glVertex2f(223.5f, 187f); // bottom-left
		gl.glVertex2f(242.5f, 187f); // bottom-right
		gl.glVertex2f(242.5f, 201f); // top-right
		gl.glVertex2f(223.5f, 201f); // top-left
		gl.glEnd();

		// Small part of window 1 (white section) for 2nd building 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(233.1f, 186f); // bottom-left
		gl.glVertex2f(234f, 186f); // bottom-right
		gl.glVertex2f(234f, 202f); // top-right
		gl.glVertex2f(233.1f, 202f); // top-left
		gl.glEnd();

		// Window 2 for 2nd building 2nd floor (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for window
		gl.glVertex2f(253f, 186f); // bottom-left
		gl.glVertex2f(273f, 186f); // bottom-right
		gl.glVertex2f(273f, 202f); // top-right
		gl.glVertex2f(253f, 202f); // top-left
		gl.glEnd();

		// Window 2 (colored part) for 2nd building 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color for window
		gl.glVertex2f(253.5f, 187f); // bottom-left
		gl.glVertex2f(272.5f, 187f); // bottom-right
		gl.glVertex2f(272.5f, 201f); // top-right
		gl.glVertex2f(253.5f, 201f); // top-left
		gl.glEnd();

		// Small part of window 2 (white section) for 2nd building 2nd floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(263.1f, 186f); // bottom-left
		gl.glVertex2f(264f, 186f); // bottom-right
		gl.glVertex2f(264f, 202f); // top-right
		gl.glVertex2f(263.1f, 202f); // top-left
		gl.glEnd();

		// Dividing the floor into sections (gray color)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204); // Gray color for floor divider
		gl.glVertex2f(202.5f, 215f); // bottom-left
		gl.glVertex2f(284f, 215f); // bottom-right
		gl.glVertex2f(284f, 230f); // top-right
		gl.glVertex2f(202.5f, 230f); // top-left
		gl.glEnd();

		// 3rd Floor Design (red part) for 2nd building
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(205f, 235f); // bottom-left
		gl.glVertex2f(281f, 235f); // bottom-right
		gl.glVertex2f(281f, 265f); // top-right
		gl.glVertex2f(205f, 265f); // top-left
		gl.glEnd();

		// 3rd Floor (red part) continuation for 2nd building
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(281f, 235f); // bottom-left
		gl.glVertex2f(264f, 235f); // bottom-right
		gl.glVertex2f(264f, 265f); // top-right
		gl.glVertex2f(281f, 265f); // top-left
		gl.glEnd();

		// Door for 2nd building 3rd floor (brown color)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71); // Brown color for door
		gl.glVertex2f(205f, 230f); // bottom-left
		gl.glVertex2f(215f, 230f); // bottom-right
		gl.glVertex2f(215f, 265f); // top-right
		gl.glVertex2f(205f, 265f); // top-left
		gl.glEnd();

		// Door for 2nd building 3rd floor (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(209f, 242f); // bottom-left
		gl.glVertex2f(210f, 242f); // bottom-right
		gl.glVertex2f(210f, 257f); // top-right
		gl.glVertex2f(209f, 257f); // top-left
		gl.glEnd();

		// Window 1 for 3rd floor of the 2nd building (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(223f, 241f); // bottom-left
		gl.glVertex2f(243f, 241f); // bottom-right
		gl.glVertex2f(243f, 257f); // top-right
		gl.glVertex2f(223f, 257f); // top-left
		gl.glEnd();

		// Window 1 for 3rd floor (blue part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Blue color
		gl.glVertex2f(223.5f, 242f); // bottom-left
		gl.glVertex2f(242.5f, 242f); // bottom-right
		gl.glVertex2f(242.5f, 256f); // top-right
		gl.glVertex2f(223.5f, 256f); // top-left
		gl.glEnd();

		// Window 1 for 3rd floor (small white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(233.1f, 241f); // bottom-left
		gl.glVertex2f(234f, 241f); // bottom-right
		gl.glVertex2f(234f, 257f); // top-right
		gl.glVertex2f(233.1f, 257f); // top-left
		gl.glEnd();

		// Window 2 for 3rd floor of the 2nd building (white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(253f, 241f); // bottom-left
		gl.glVertex2f(273f, 241f); // bottom-right
		gl.glVertex2f(273f, 257f); // top-right
		gl.glVertex2f(253f, 257f); // top-left
		gl.glEnd();

		// Window 2 for 3rd floor (blue part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Blue color
		gl.glVertex2f(253.5f, 242f); // bottom-left
		gl.glVertex2f(272.5f, 242f); // bottom-right
		gl.glVertex2f(272.5f, 256f); // top-right
		gl.glVertex2f(253.5f, 256f); // top-left
		gl.glEnd();

		// Window 2 for 3rd floor (small white part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(263.1f, 241f); // bottom-left
		gl.glVertex2f(264f, 241f); // bottom-right
		gl.glVertex2f(264f, 257f); // top-right
		gl.glVertex2f(263.1f, 257f); // top-left
		gl.glEnd();

		// Floor division for 3rd floor of the 2nd building
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204); // Light gray color
		gl.glVertex2f(202.5f, 270f); // bottom-left
		gl.glVertex2f(284f, 270f); // bottom-right
		gl.glVertex2f(284f, 285f); // top-right
		gl.glVertex2f(202.5f, 285f); // top-left
		gl.glEnd();

		// 4th floor design (red part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(205f, 290f); // bottom-left
		gl.glVertex2f(281f, 290f); // bottom-right
		gl.glVertex2f(281f, 320f); // top-right
		gl.glVertex2f(205f, 320f); // top-left
		gl.glEnd();

		// 4th floor design continuation (red part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color
		gl.glVertex2f(281f, 290f); // bottom-left
		gl.glVertex2f(264f, 290f); // bottom-right
		gl.glVertex2f(264f, 320f); // top-right
		gl.glVertex2f(281f, 320f); // top-left
		gl.glEnd();

		// Door for 4th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71); // Brown color
		gl.glVertex2f(205f, 285f); // bottom-left
		gl.glVertex2f(215f, 285f); // bottom-right
		gl.glVertex2f(215f, 320f); // top-right
		gl.glVertex2f(205f, 320f); // top-left
		gl.glEnd();

		// Door (white part) for 4th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(209f, 297f); // bottom-left
		gl.glVertex2f(210f, 297f); // bottom-right
		gl.glVertex2f(210f, 312f); // top-right
		gl.glVertex2f(209f, 312f); // top-left
		gl.glEnd();

		// Window 1 (w1) 2nd building 4th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(223f, 296f); // bottom-left
		gl.glVertex2f(243f, 296f); // bottom-right
		gl.glVertex2f(243f, 312f); // top-right
		gl.glVertex2f(223f, 312f); // top-left
		gl.glEnd();

		// Window 1 (w1) 2nd building 4th floor (colored part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color
		gl.glVertex2f(223.5f, 297f); // bottom-left
		gl.glVertex2f(242.5f, 297f); // bottom-right
		gl.glVertex2f(242.5f, 311f); // top-right
		gl.glVertex2f(223.5f, 311f); // top-left
		gl.glEnd();

		// Window 1 (w1) 2nd building 4th floor (white line)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(233.1f, 296f); // bottom-left
		gl.glVertex2f(234f, 296f); // bottom-right
		gl.glVertex2f(234f, 312f); // top-right
		gl.glVertex2f(233.1f, 312f); // top-left
		gl.glEnd();

		// Window 2 (w2) 2nd building 4th floor
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(253f, 297f); // bottom-left
		gl.glVertex2f(273f, 297f); // bottom-right
		gl.glVertex2f(273f, 312f); // top-right
		gl.glVertex2f(253f, 312f); // top-left
		gl.glEnd();

		// Window 2 (w2) 2nd building 4th floor (colored part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color
		gl.glVertex2f(253.5f, 297f); // bottom-left
		gl.glVertex2f(272.5f, 297f); // bottom-right
		gl.glVertex2f(272.5f, 311f); // top-right
		gl.glVertex2f(253.5f, 311f); // top-left
		gl.glEnd();

		// Window 2 (w2) 2nd building 4th floor (white line)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color
		gl.glVertex2f(263.1f, 297f); // bottom-left
		gl.glVertex2f(264f, 297f); // bottom-right
		gl.glVertex2f(264f, 312f); // top-right
		gl.glVertex2f(263.1f, 312f); // top-left
		gl.glEnd();

		// Floor divided (5th floor)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204); // Gray color for floor
		gl.glVertex2f(202.5f, 325f); // bottom-left of the floor
		gl.glVertex2f(284f, 325f); // bottom-right of the floor
		gl.glVertex2f(284f, 340f); // top-right of the floor
		gl.glVertex2f(202.5f, 340f); // top-left of the floor
		gl.glEnd();

		// 5th floor design (red part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color for the 5th floor portion
		gl.glVertex2f(205f, 345f); // bottom-left of the red portion
		gl.glVertex2f(281f, 345f); // bottom-right of the red portion
		gl.glVertex2f(281f, 375f); // top-right of the red portion
		gl.glVertex2f(205f, 375f); // top-left of the red portion
		gl.glEnd();

		// 5th floor design (red part)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119); // Red color for the 5th floor portion
		gl.glVertex2f(281f, 345f); // bottom-left of the second red portion
		gl.glVertex2f(264f, 345f); // bottom-right of the second red portion
		gl.glVertex2f(264f, 375f); // top-right of the second red portion
		gl.glVertex2f(281f, 375f); // top-left of the second red portion
		gl.glEnd();

		// Door and Window (5th Floor)

		// Door
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71); // Brown color for the door
		gl.glVertex2f(205f, 340f); // bottom-left of the door
		gl.glVertex2f(215f, 340f); // bottom-right of the door
		gl.glVertex2f(215f, 375f); // top-right of the door
		gl.glVertex2f(205f, 375f); // top-left of the door
		gl.glEnd();

		// Door inner frame (white)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for door frame
		gl.glVertex2f(209f, 352f); // bottom-left of the door frame
		gl.glVertex2f(210f, 352f); // bottom-right of the door frame
		gl.glVertex2f(210f, 367f); // top-right of the door frame
		gl.glVertex2f(209f, 367f); // top-left of the door frame
		gl.glEnd();

		// Window 1 (5th floor)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for the window
		gl.glVertex2f(223f, 351f); // bottom-left of window 1
		gl.glVertex2f(243f, 351f); // bottom-right of window 1
		gl.glVertex2f(243f, 367f); // top-right of window 1
		gl.glVertex2f(223f, 367f); // top-left of window 1
		gl.glEnd();

		// Window 1 inner frame (blue)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color for window 1 frame
		gl.glVertex2f(223.5f, 352f); // bottom-left of window 1 frame
		gl.glVertex2f(242.5f, 352f); // bottom-right of window 1 frame
		gl.glVertex2f(242.5f, 366f); // top-right of window 1 frame
		gl.glVertex2f(223.5f, 366f); // top-left of window 1 frame
		gl.glEnd();

		// Window 1 frame separator
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for separator
		gl.glVertex2f(233.1f, 351f); // left side of separator
		gl.glVertex2f(234f, 351f); // right side of separator
		gl.glVertex2f(234f, 367f); // top-right of separator
		gl.glVertex2f(233.1f, 367f); // top-left of separator
		gl.glEnd();

		// Window 2 (5th floor)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for window 2
		gl.glVertex2f(253f, 352f); // bottom-left of window 2
		gl.glVertex2f(273f, 352f); // bottom-right of window 2
		gl.glVertex2f(273f, 367f); // top-right of window 2
		gl.glVertex2f(253f, 367f); // top-left of window 2
		gl.glEnd();

		// Window 2 inner frame (blue)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 128, (byte) 197, (byte) 215); // Light blue color for window 2 frame
		gl.glVertex2f(253.5f, 352f); // bottom-left of window 2 frame
		gl.glVertex2f(272.5f, 352f); // bottom-right of window 2 frame
		gl.glVertex2f(272.5f, 366f); // top-right of window 2 frame
		gl.glVertex2f(253.5f, 366f); // top-left of window 2 frame
		gl.glEnd();

		// Window 2 frame separator
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255); // White color for separator
		gl.glVertex2f(263.1f, 352f); // left side of separator
		gl.glVertex2f(264f, 352f); // right side of separator
		gl.glVertex2f(264f, 367f); // top-right of separator
		gl.glVertex2f(263.1f, 367f); // top-left of separator
		gl.glEnd();

		/// Tower 2nd Building//
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 140, (byte) 140, (byte) 140);
		gl.glVertex2f(273, 395);
		gl.glVertex2f(274, 395);
		gl.glVertex2f(274, 520);
		gl.glVertex2f(273, 520);
		gl.glEnd();

		/// Tower 2nd Building//
		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102);
		drawCircle(gl, 3, 6, 273, 500); // Assuming circle method defined elsewhere
		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217);
		drawCircle(gl, 2.5f, 5, 273, 500);

		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102);
		drawCircle(gl, 2, 4, 276, 485);
		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217);
		drawCircle(gl, 1.5f, 3, 276, 485);
		// Tower End//

		gl.glBegin(GL2.GL_TRIANGLE_FAN); /// triangle tree 2nd Building///
		gl.glColor3ub((byte) 75, (byte) 35, (byte) 5);
		gl.glVertex2f(290, 90);
		gl.glVertex2f(295, 90);
		gl.glVertex2f(295, 120);
		gl.glVertex2f(290, 120);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(280, 120);
		gl.glVertex2f(305, 120);
		gl.glVertex2f(292.5f, 180);
		gl.glVertex2f(292.5f, 180);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(281, 135);
		gl.glVertex2f(304, 135);
		gl.glVertex2f(292.5f, 190);
		gl.glVertex2f(292.5f, 190);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(282, 150);
		gl.glVertex2f(303, 150);
		gl.glVertex2f(292.5f, 180);
		gl.glVertex2f(292.5f, 180);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(283, 160);
		gl.glVertex2f(302, 160);
		gl.glVertex2f(292.5f, 190);
		gl.glVertex2f(292.5f, 190);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(284, 170);
		gl.glVertex2f(301, 170);
		gl.glVertex2f(292.5f, 200);
		gl.glVertex2f(292.5f, 200);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(285, 180);
		gl.glVertex2f(300, 180);
		gl.glVertex2f(292.5f, 210);
		gl.glVertex2f(292.5f, 210);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(286, 190);
		gl.glVertex2f(299, 190);
		gl.glVertex2f(292.5f, 260);
		gl.glVertex2f(292.5f, 260);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		gl.glVertex2f(286, 200);
		gl.glVertex2f(299, 200);
		gl.glVertex2f(292.5f, 270);
		gl.glVertex2f(292.5f, 270);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); /// white part///
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(575, 90);
		gl.glVertex2f(635, 90);
		gl.glVertex2f(635, 475);
		gl.glVertex2f(575, 475);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(577.5f, 90); // 3rd Building main part 2
		gl.glVertex2f(631, 90);
		gl.glVertex2f(631, 460);
		gl.glVertex2f(577.5f, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(635, 90); // 3rd Building 2nd part 2
		gl.glVertex2f(665, 90);
		gl.glVertex2f(665, 420);
		gl.glVertex2f(635, 420);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(635, 90); // 3rd Building 2nd part 1
		gl.glVertex2f(662.5f, 90);
		gl.glVertex2f(662.5f, 410);
		gl.glVertex2f(635, 410);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(637, 370); // part 2 3rd Building 6th floor2..........outlook
		gl.glVertex2f(662.5f, 370);
		gl.glVertex2f(662.5f, 400);
		gl.glVertex2f(637, 400);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 123, (byte) 88, (byte) 71);
		gl.glVertex2f(638, 365); // part 2 3rd Building 6th floor2..........outlook
		gl.glVertex2f(648, 365);
		gl.glVertex2f(648, 400);
		gl.glVertex2f(638, 400);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex2f(643, 365); // Door 3rd Building 6th floor2..........outlook-1
		gl.glVertex2f(644, 365);
		gl.glVertex2f(644, 392);
		gl.glVertex2f(643, 392);
		gl.glEnd();

		///////////////////
		gl.glBegin(GL2.GL_QUADS); // 3rd Building 3rd Building floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 90);
		gl.glVertex2f(667, 90);
		gl.glVertex2f(667, 145);
		gl.glVertex2f(615, 145);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 2nd floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 145);
		gl.glVertex2f(667, 145);
		gl.glVertex2f(667, 200);
		gl.glVertex2f(615, 200);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 3rd floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 200);
		gl.glVertex2f(667, 200);
		gl.glVertex2f(667, 255);
		gl.glVertex2f(615, 255);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 4th floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 255);
		gl.glVertex2f(667, 255);
		gl.glVertex2f(667, 310);
		gl.glVertex2f(615, 310);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 5th floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 310);
		gl.glVertex2f(667, 310);
		gl.glVertex2f(667, 365);
		gl.glVertex2f(615, 365);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 6th floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 365);
		gl.glVertex2f(667, 365);
		gl.glVertex2f(667, 420);
		gl.glVertex2f(615, 420);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3rd Building 7th floor1
		gl.glColor3ub((byte) 204, (byte) 204, (byte) 204);
		gl.glVertex2f(615, 420);
		gl.glVertex2f(667, 420);
		gl.glVertex2f(667, 475);
		gl.glVertex2f(615, 475);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 7 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 420);
		gl.glVertex2f(592f, 420);
		gl.glVertex2f(592f, 460);
		gl.glVertex2f(577.5f, 460);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 6 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 365);
		gl.glVertex2f(592f, 365);
		gl.glVertex2f(592f, 405);
		gl.glVertex2f(577.5f, 405);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 5 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 310);
		gl.glVertex2f(592f, 310);
		gl.glVertex2f(592f, 350);
		gl.glVertex2f(577.5f, 350);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 4 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 255);
		gl.glVertex2f(592f, 255);
		gl.glVertex2f(592f, 295);
		gl.glVertex2f(577.5f, 295);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 200);
		gl.glVertex2f(592f, 200);
		gl.glVertex2f(592f, 240);
		gl.glVertex2f(577.5f, 240);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 2 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 145);
		gl.glVertex2f(592f, 145);
		gl.glVertex2f(592f, 185);
		gl.glVertex2f(577.5f, 185);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 3rd Building main part3
		gl.glColor3ub((byte) 177, (byte) 124, (byte) 119);
		gl.glVertex2f(577.5f, 90);
		gl.glVertex2f(592f, 90);
		gl.glVertex2f(592f, 130);
		gl.glVertex2f(577.5f, 130);
		gl.glEnd();

		////// .........................................

		gl.glBegin(GL2.GL_QUADS); // 7 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 420f);
		gl.glVertex2f(631f, 420f);
		gl.glVertex2f(631f, 460f);
		gl.glVertex2f(620f, 460f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 6 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 365f);
		gl.glVertex2f(631f, 365f);
		gl.glVertex2f(631f, 405f);
		gl.glVertex2f(620f, 405f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 5 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 310f);
		gl.glVertex2f(631f, 310f);
		gl.glVertex2f(631f, 350f);
		gl.glVertex2f(620f, 350f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 4 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 255f);
		gl.glVertex2f(631f, 255f);
		gl.glVertex2f(631f, 295f);
		gl.glVertex2f(620f, 295f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 200f);
		gl.glVertex2f(631f, 200f);
		gl.glVertex2f(631f, 240f);
		gl.glVertex2f(620f, 240f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 2 3rd Building main part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(620f, 145f);
		gl.glVertex2f(631f, 145f);
		gl.glVertex2f(631f, 185f);
		gl.glVertex2f(620f, 185f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // door
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(610f, 90f);
		gl.glVertex2f(631f, 90f);
		gl.glVertex2f(631f, 130f);
		gl.glVertex2f(610f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // door 1 main
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(620f, 90f);
		gl.glVertex2f(621f, 90f);
		gl.glVertex2f(621f, 130f);
		gl.glVertex2f(620f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // door 1 main
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(621f, 100f);
		gl.glVertex2f(622f, 100f);
		gl.glVertex2f(622f, 120f);
		gl.glVertex2f(621f, 120f);
		gl.glEnd();

		/////// .......................
		gl.glBegin(GL2.GL_QUADS); // 7 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 420f);
		gl.glVertex2f(635f, 420f);
		gl.glVertex2f(635f, 421f);
		gl.glVertex2f(575f, 421f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 6 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 365f);
		gl.glVertex2f(662.5f, 365f);
		gl.glVertex2f(662.5f, 366f);
		gl.glVertex2f(575f, 366f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 5 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 310f);
		gl.glVertex2f(635f, 310f);
		gl.glVertex2f(635f, 311f);
		gl.glVertex2f(575f, 311f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 4 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 255f);
		gl.glVertex2f(635f, 255f);
		gl.glVertex2f(635f, 256f);
		gl.glVertex2f(575f, 256f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 199f);
		gl.glVertex2f(635f, 199f);
		gl.glVertex2f(635f, 200f);
		gl.glVertex2f(575f, 200f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 2 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 145f);
		gl.glVertex2f(635f, 145f);
		gl.glVertex2f(635f, 146f);
		gl.glVertex2f(575f, 146f);
		gl.glEnd();

		////// .......................
		gl.glBegin(GL2.GL_QUADS); // 7 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 405f);
		gl.glVertex2f(635f, 405f);
		gl.glVertex2f(635f, 406f);
		gl.glVertex2f(575f, 406f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 6 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 350f);
		gl.glVertex2f(635f, 350f);
		gl.glVertex2f(635f, 351f);
		gl.glVertex2f(575f, 351f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 5 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 295f);
		gl.glVertex2f(635f, 295f);
		gl.glVertex2f(635f, 296f);
		gl.glVertex2f(575f, 296f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 4 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 240f);
		gl.glVertex2f(635f, 240f);
		gl.glVertex2f(635f, 241f);
		gl.glVertex2f(575f, 241f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 185f);
		gl.glVertex2f(635f, 185f);
		gl.glVertex2f(635f, 186f);
		gl.glVertex2f(575f, 186f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 2 3rd Building main part3
		gl.glColor3ub((byte) 179, (byte) 179, (byte) 179);
		gl.glVertex2f(575f, 130f);
		gl.glVertex2f(635f, 130f);
		gl.glVertex2f(635f, 131f);
		gl.glVertex2f(575f, 131f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // part2 last black line
		gl.glColor3ub((byte) 89, (byte) 89, (byte) 89);
		gl.glVertex2f(665f, 90f);
		gl.glVertex2f(666f, 90f);
		gl.glVertex2f(666f, 460f);
		gl.glVertex2f(665f, 460f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 7 2nd part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 420f);
		gl.glVertex2f(662.5f, 420f);
		gl.glVertex2f(662.5f, 460f);
		gl.glVertex2f(635f, 460f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 6 2nd part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 365f);
		gl.glVertex2f(662.5f, 365f);
		gl.glVertex2f(662.5f, 405f);
		gl.glVertex2f(635f, 405f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 5 part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 310f);
		gl.glVertex2f(662.5f, 310f);
		gl.glVertex2f(662.5f, 350f);
		gl.glVertex2f(635f, 350f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 4 2nd part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 255f);
		gl.glVertex2f(662.5f, 255f);
		gl.glVertex2f(662.5f, 295f);
		gl.glVertex2f(635f, 295f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 3 2nd part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 200f);
		gl.glVertex2f(662.5f, 200f);
		gl.glVertex2f(662.5f, 240f);
		gl.glVertex2f(635f, 240f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 2 2nd part3
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119);
		gl.glVertex2f(635f, 145f);
		gl.glVertex2f(662.5f, 145f);
		gl.glVertex2f(662.5f, 185f);
		gl.glVertex2f(635f, 185f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 3rd Building main part3
		gl.glColor3ub((byte) 143, (byte) 175, (byte) 175);
		gl.glVertex2f(635f, 90f);
		gl.glVertex2f(662.5f, 90f);
		gl.glVertex2f(662.5f, 130f);
		gl.glVertex2f(635f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 main part3
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(642f, 90f);
		gl.glVertex2f(643f, 90f);
		gl.glVertex2f(643f, 130f);
		gl.glVertex2f(642f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 main part3
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 77);
		gl.glVertex2f(652f, 90f);
		gl.glVertex2f(653f, 90f);
		gl.glVertex2f(653f, 130f);
		gl.glVertex2f(652f, 130f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 main part3
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(643f, 100f);
		gl.glVertex2f(644f, 100f);
		gl.glVertex2f(644f, 120f);
		gl.glVertex2f(643f, 120f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS); // 1 main part3
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242);
		gl.glVertex2f(653f, 100f);
		gl.glVertex2f(654f, 100f);
		gl.glVertex2f(654f, 120f);
		gl.glVertex2f(653f, 120f);
		gl.glEnd();

		// Partition of Blue Line
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51); // Blue partition
		gl.glVertex2f(635f, 90f);
		gl.glVertex2f(636f, 90f);
		gl.glVertex2f(636f, 460f);
		gl.glVertex2f(635f, 460f);
		gl.glEnd();

		// End of 3rd Building

		// Tower 2nd Building
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 140, (byte) 140, (byte) 140); // Tower base color
		gl.glVertex2f(635f, 475f);
		gl.glVertex2f(636f, 475f);
		gl.glVertex2f(636f, 600f);
		gl.glVertex2f(635f, 600f);
		gl.glEnd();

		// Circle (Tower parts)
		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102); // Dark gray for the first circle
		drawCircle(gl, 3f, 6f, 635f, 577f);

		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217); // Light gray for the second circle
		drawCircle(gl, 2.5f, 5f, 635f, 577f);

		gl.glColor3ub((byte) 102, (byte) 102, (byte) 102); // Dark gray for the third circle
		drawCircle(gl, 2f, 4f, 638f, 560f);

		gl.glColor3ub((byte) 217, (byte) 217, (byte) 217); // Light gray for the fourth circle
		drawCircle(gl, 1.5f, 3f, 638f, 560f);


		// End of Tower

		// 4th Building Top Main Part 2
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White part (light gray)
		gl.glVertex2f(483.5f, 440f);
		gl.glVertex2f(555.2f, 440f);
		gl.glVertex2f(555.2f, 472f);
		gl.glVertex2f(483.5f, 472f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119); // Gray part
		gl.glVertex2f(483.5f, 440f);
		gl.glVertex2f(555.2f, 440f);
		gl.glVertex2f(555.2f, 467f);
		gl.glVertex2f(483.5f, 467f);
		gl.glEnd();

		// White part (Building's sides)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray part
		gl.glVertex2f(475f, 119f);
		gl.glVertex2f(563.5f, 119f);
		gl.glVertex2f(563.5f, 442f);
		gl.glVertex2f(475f, 442f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 85, (byte) 119, (byte) 119); // Gray part (building side)
		gl.glVertex2f(477.5f, 119f);
		gl.glVertex2f(561f, 119f);
		gl.glVertex2f(561f, 434f);
		gl.glVertex2f(477.5f, 434f);
		gl.glEnd();

		// 4th Building 1st floor (y = 150 to y = 154)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 150f);
		gl.glVertex2f(563f, 150f);
		gl.glVertex2f(563f, 154f);
		gl.glVertex2f(475f, 154f);
		gl.glEnd();

		// 4th Building 2nd floor (y = 190 to y = 193)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 190f);
		gl.glVertex2f(563f, 190f);
		gl.glVertex2f(563f, 193f);
		gl.glVertex2f(475f, 193f);
		gl.glEnd();

		// 4th Building 3rd floor (y = 230 to y = 234)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 230f);
		gl.glVertex2f(563f, 230f);
		gl.glVertex2f(563f, 234f);
		gl.glVertex2f(475f, 234f);
		gl.glEnd();

		// 4th Building 4th floor (y = 270 to y = 273)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 270f);
		gl.glVertex2f(563f, 270f);
		gl.glVertex2f(563f, 273f);
		gl.glVertex2f(475f, 273f);
		gl.glEnd();

		// 4th Building 5th floor (y = 310 to y = 314)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 310f);
		gl.glVertex2f(563f, 310f);
		gl.glVertex2f(563f, 314f);
		gl.glVertex2f(475f, 314f);
		gl.glEnd();

		// 4th Building 6th floor (y = 310 to y = 314)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 310f);
		gl.glVertex2f(563f, 310f);
		gl.glVertex2f(563f, 314f);
		gl.glVertex2f(475f, 314f);
		gl.glEnd();

		// 4th Building 7th floor (y = 350 to y = 353)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 350f);
		gl.glVertex2f(563f, 350f);
		gl.glVertex2f(563f, 353f);
		gl.glVertex2f(475f, 353f);
		gl.glEnd();

		// 4th Building 8th floor (y = 390 to y = 394)
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // Light gray
		gl.glVertex2f(475f, 390f);
		gl.glVertex2f(563f, 390f);
		gl.glVertex2f(563f, 394f);
		gl.glVertex2f(475f, 394f);
		gl.glEnd();

		// 4th Building white vertical lines
		// White line at x = 491
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White
		gl.glVertex2f(491f, 119f);
		gl.glVertex2f(491.5f, 119f);
		gl.glVertex2f(491.5f, 440f);
		gl.glVertex2f(491f, 440f);
		gl.glEnd();

		// White line at x = 505
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White
		gl.glVertex2f(505f, 119f);
		gl.glVertex2f(505.5f, 119f);
		gl.glVertex2f(505.5f, 440f);
		gl.glVertex2f(505f, 440f);
		gl.glEnd();

		// White line at x = 519
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White
		gl.glVertex2f(519f, 119f);
		gl.glVertex2f(519.5f, 119f);
		gl.glVertex2f(519.5f, 440f);
		gl.glVertex2f(519f, 440f);
		gl.glEnd();

		// White line at x = 533
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White
		gl.glVertex2f(533f, 119f);
		gl.glVertex2f(533.5f, 119f);
		gl.glVertex2f(533.5f, 440f);
		gl.glVertex2f(533f, 440f);
		gl.glEnd();

		// White line at x = 547
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 242, (byte) 242, (byte) 242); // White
		gl.glVertex2f(547f, 119f);
		gl.glVertex2f(547.5f, 119f);
		gl.glVertex2f(547.5f, 440f);
		gl.glVertex2f(547f, 440f);
		gl.glEnd();

		// 4th Building Door (1st floor) at y = 140 to y = 144
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51); // Dark blue
		gl.glVertex2f(505f, 140f);
		gl.glVertex2f(533f, 140f);
		gl.glVertex2f(533f, 144f);
		gl.glVertex2f(505f, 144f);
		gl.glEnd();

		// 4th Building white vertical line at x = 533
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51); // Dark blue
		gl.glVertex2f(532f, 119f);
		gl.glVertex2f(533.6f, 119f);
		gl.glVertex2f(533.6f, 140f);
		gl.glVertex2f(532f, 140f);
		gl.glEnd();

		// 4th Building white vertical line at x = 519
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51); // Dark blue
		gl.glVertex2f(518f, 119f);
		gl.glVertex2f(519.6f, 119f);
		gl.glVertex2f(519.6f, 140f);
		gl.glVertex2f(518f, 140f);
		gl.glEnd();

		// 4th Building white vertical line at x = 505
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 0, (byte) 26, (byte) 51); // Dark blue
		gl.glVertex2f(505f, 119f);
		gl.glVertex2f(506.6f, 119f);
		gl.glVertex2f(506.6f, 140f);
		gl.glVertex2f(505f, 140f);
		gl.glEnd();

		// Hospital signboard

		// Set the color for the text (dark blue)
		gl.glColor3ub((byte) 0, (byte) 51, (byte) 204);

		// Draw the characters one by one at specified positions
		gl.glRasterPos2i(504, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'H');

		gl.glRasterPos2i(509, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'O');

		gl.glRasterPos2i(513, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'S');

		gl.glRasterPos2i(517, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'P');

		gl.glRasterPos2i(521, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'I');

		gl.glRasterPos2i(525, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'T');

		gl.glRasterPos2i(529, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'A');

		gl.glRasterPos2i(534, 455);
		glut.glutBitmapCharacter(GLUT.BITMAP_TIMES_ROMAN_10, 'L');
		// End of signboard text drawing
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 52, (byte) 67, (byte) 74); // Food Court main part 2
		gl.glVertex2f(373, 90);
		gl.glVertex2f(466, 90);
		gl.glVertex2f(466, 442);
		gl.glVertex2f(373, 442);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 91, (byte) 102, (byte) 108); // Food Court main part 2
		gl.glVertex2f(377.5f, 90);
		gl.glVertex2f(461, 90);
		gl.glVertex2f(461, 440);
		gl.glVertex2f(377.5f, 440);
		gl.glEnd();

		// Floor divided
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 52, (byte) 67, (byte) 74);
		gl.glVertex2f(282.5f, 270); // Middle divided left right
		gl.glVertex2f(466, 270);
		gl.glVertex2f(466, 281);
		gl.glVertex2f(282.5f, 281);
		gl.glEnd();

		// Stairs Top
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 46, (byte) 47); // 3
		gl.glVertex2f(371, 442);
		gl.glVertex2f(467.5f, 442);
		gl.glVertex2f(467.5f, 451);
		gl.glVertex2f(371, 451);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 20, (byte) 25, (byte) 33); // 2
		gl.glVertex2f(371, 454);
		gl.glVertex2f(467.5f, 454);
		gl.glVertex2f(467.5f, 451);
		gl.glVertex2f(371, 451);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 46, (byte) 47); // 1
		gl.glVertex2f(367, 454);
		gl.glVertex2f(471.5f, 454);
		gl.glVertex2f(471.5f, 463);
		gl.glVertex2f(367, 463);
		gl.glEnd();

		// Stairs Top End
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 72, (byte) 85, (byte) 93);
		gl.glVertex2f(377.5f, 433);
		gl.glVertex2f(461, 433);
		gl.glVertex2f(461, 440);
		gl.glVertex2f(377.5f, 440);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Black Food Court Right part
		gl.glVertex2f(382, 290);
		gl.glVertex2f(457, 290);
		gl.glVertex2f(457, 429);
		gl.glVertex2f(382, 429);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 254, (byte) 246, (byte) 223); // White color Food Court Right part
		gl.glVertex2f(383.5f, 293.6f);
		gl.glVertex2f(455.4f, 293.6f);
		gl.glVertex2f(455.4f, 425.4f);
		gl.glVertex2f(383.5f, 425.4f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Middle line 2
		gl.glVertex2f(382, 314.2f);
		gl.glVertex2f(457, 314.2f);
		gl.glVertex2f(457, 310.2f);
		gl.glVertex2f(382, 310.2f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Middle line 1
		gl.glVertex2f(368, 409.1f);
		gl.glVertex2f(457, 409.1f);
		gl.glVertex2f(457, 405.1f);
		gl.glVertex2f(368, 405.1f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Middle line for sign board
		gl.glVertex2f(368, 429.1f);
		gl.glVertex2f(382, 429.1f);
		gl.glVertex2f(382, 426);
		gl.glVertex2f(368, 426);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Partition of Blue line
		gl.glVertex2f(402, 290);
		gl.glVertex2f(400.6f, 290);
		gl.glVertex2f(400.6f, 426);
		gl.glVertex2f(402, 426);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Partition of Blue line
		gl.glVertex2f(420, 290);
		gl.glVertex2f(418.6f, 290);
		gl.glVertex2f(418.6f, 426);
		gl.glVertex2f(420, 426);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Partition of Blue line
		gl.glVertex2f(438, 290);
		gl.glVertex2f(436.5f, 290);
		gl.glVertex2f(436.5f, 426);
		gl.glVertex2f(438, 426);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 52, (byte) 67, (byte) 74); // Food Court board Black
		gl.glVertex2f(388.5f, 255.6f);
		gl.glVertex2f(450.2f, 255.6f);
		gl.glVertex2f(450.2f, 283.4f);
		gl.glVertex2f(388.5f, 283.4f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 182, (byte) 138, (byte) 94); // Food Court board
		gl.glVertex2f(386.5f, 258.3f);
		gl.glVertex2f(448.3f, 258.3f);
		gl.glVertex2f(448.3f, 287f);
		gl.glVertex2f(386.5f, 287f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 250, (byte) 201, (byte) 143); // Middle line 1
		gl.glVertex2f(386.5f, 281.2f);
		gl.glVertex2f(448.3f, 281.2f);
		gl.glVertex2f(448.3f, 279f);
		gl.glVertex2f(386.5f, 279f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 250, (byte) 201, (byte) 143); // Middle line 1
		gl.glVertex2f(386.5f, 273.2f);
		gl.glVertex2f(448.3f, 273.2f);
		gl.glVertex2f(448.3f, 271f);
		gl.glVertex2f(386.5f, 271f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 250, (byte) 201, (byte) 143); // Middle line 1
		gl.glVertex2f(386.5f, 265.2f);
		gl.glVertex2f(448.3f, 265.2f);
		gl.glVertex2f(448.3f, 263f);
		gl.glVertex2f(386.5f, 263f);
		gl.glEnd();

		gl.glColor3ub((byte) 31, (byte) 46, (byte) 53);
		gl.glRasterPos2i(400, 270);

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 45, (byte) 51); // Middle line
		gl.glVertex2f(377.6f, 241.4f);
		gl.glVertex2f(460.9f, 241.4f);
		gl.glVertex2f(460.9f, 239.2f);
		gl.glVertex2f(377.6f, 239.2f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 45, (byte) 51); // Middle line
		gl.glVertex2f(377.6f, 230.4f);
		gl.glVertex2f(460.9f, 230.4f);
		gl.glVertex2f(460.9f, 228.2f);
		gl.glVertex2f(377.6f, 228.2f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 45, (byte) 51); // Middle line
		gl.glVertex2f(377.6f, 220.4f);
		gl.glVertex2f(460.9f, 220.4f);
		gl.glVertex2f(460.9f, 218.2f);
		gl.glVertex2f(377.6f, 218.2f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 45, (byte) 51); // Middle line
		gl.glVertex2f(377.6f, 210.5f);
		gl.glVertex2f(460.9f, 210.5f);
		gl.glVertex2f(460.9f, 208.4f);
		gl.glVertex2f(377.6f, 208.4f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 45, (byte) 51); // Middle line
		gl.glVertex2f(377.6f, 200.5f);
		gl.glVertex2f(460.9f, 200.5f);
		gl.glVertex2f(460.9f, 198.4f);
		gl.glVertex2f(377.6f, 198.4f);
		gl.glEnd();

		// Door
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 104, (byte) 111, (byte) 119); // Black color Food court door
		gl.glVertex2f(397, 110);
		gl.glVertex2f(442, 110);
		gl.glVertex2f(442, 230);
		gl.glVertex2f(397, 230);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 34, (byte) 45, (byte) 51); // Food Court black color
		gl.glVertex2f(398.5f, 110);
		gl.glVertex2f(440, 110);
		gl.glVertex2f(440, 226.5f);
		gl.glVertex2f(398.5f, 226.5f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 254, (byte) 242, (byte) 205); // Food Court Right part
		gl.glVertex2f(401.5f, 110);
		gl.glVertex2f(437, 110);
		gl.glVertex2f(437, 220.5f);
		gl.glVertex2f(401.5f, 220.5f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54); // Door top part
		gl.glVertex2f(399, 118.7f);
		gl.glVertex2f(440, 118.7f);
		gl.glVertex2f(440, 124.6f);
		gl.glVertex2f(399, 124.6f);
		gl.glEnd();

		// Partition of Blue line
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 34, (byte) 45, (byte) 51);
		gl.glVertex2f(420, 110);
		gl.glVertex2f(416.6f, 110);
		gl.glVertex2f(416.6f, 226);
		gl.glVertex2f(420, 226);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 104, (byte) 111, (byte) 119);
		gl.glVertex2f(421, 110);
		gl.glVertex2f(419.6f, 110);
		gl.glVertex2f(419.6f, 226);
		gl.glVertex2f(421, 226);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 34, (byte) 45, (byte) 51);
		gl.glVertex2f(424, 110);
		gl.glVertex2f(420.6f, 110);
		gl.glVertex2f(420.6f, 226);
		gl.glVertex2f(424, 226);
		gl.glEnd();

		// Food Court left part
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 91, (byte) 102, (byte) 108);
		gl.glVertex2f(288, 110);
		gl.glVertex2f(373, 110);
		gl.glVertex2f(373, 270);
		gl.glVertex2f(288, 270);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(293, 120);
		gl.glVertex2f(368, 120);
		gl.glVertex2f(368, 259);
		gl.glVertex2f(293, 259);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 254, (byte) 246, (byte) 223);
		gl.glVertex2f(295, 124);
		gl.glVertex2f(366, 124);
		gl.glVertex2f(366, 256);
		gl.glVertex2f(295, 256);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(294.5f, 190);
		gl.glVertex2f(368, 190);
		gl.glVertex2f(368, 187);
		gl.glVertex2f(294.5f, 187);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(350, 120);
		gl.glVertex2f(348.6f, 120);
		gl.glVertex2f(348.6f, 256);
		gl.glVertex2f(350, 256);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(332, 120);
		gl.glVertex2f(330.6f, 120);
		gl.glVertex2f(330.6f, 256);
		gl.glVertex2f(332, 256);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(314, 120);
		gl.glVertex2f(312.6f, 120);
		gl.glVertex2f(312.6f, 256);
		gl.glVertex2f(314, 256);
		gl.glEnd();

		// Top Boundary
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(284.3f, 280);
		gl.glVertex2f(282.5f, 280);
		gl.glVertex2f(282.5f, 321);
		gl.glVertex2f(284.3f, 321);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(302, 280);
		gl.glVertex2f(300.2f, 280);
		gl.glVertex2f(300.2f, 321);
		gl.glVertex2f(302, 321);
		gl.glEnd();

		// Middle lines
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(282.5f, 314);
		gl.glVertex2f(372, 314);
		gl.glVertex2f(372, 309.6f);
		gl.glVertex2f(282.5f, 309.6f);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 27, (byte) 45, (byte) 54);
		gl.glVertex2f(282.5f, 304);
		gl.glVertex2f(372, 304);
		gl.glVertex2f(372, 300);
		gl.glVertex2f(282.5f, 300);
		gl.glEnd();

		// Stairs
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 46, (byte) 47);
		gl.glVertex2f(284, 90); // Bottom3
		gl.glVertex2f(471.5f, 90);
		gl.glVertex2f(471.5f, 99);
		gl.glVertex2f(284, 99);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 20, (byte) 25, (byte) 33);
		gl.glVertex2f(286, 102); // Middle2
		gl.glVertex2f(468, 102);
		gl.glVertex2f(468, 99);
		gl.glVertex2f(286, 99);
		gl.glEnd();

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 33, (byte) 46, (byte) 47);
		gl.glVertex2f(286, 102); // Top1
		gl.glVertex2f(468, 102);
		gl.glVertex2f(468, 110);
		gl.glVertex2f(286, 110);
		gl.glEnd();

		// Right side circle tree in Food Court
		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		drawCircle(gl, 3, 6, 468, 101);
		drawCircle(gl, 5, 12, 463, 111);
		drawCircle(gl, 5, 12, 473, 111);
		drawCircle(gl, 5, 12, 478, 106);
		drawCircle(gl, 5, 12, 458, 121);

		gl.glColor3ub((byte) 181, (byte) 106, (byte) 76);
		drawCircle(gl, 5, 12, 463, 123);

		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		drawCircle(gl, 5, 12, 464, 123);
		drawCircle(gl, 5, 12, 468, 141);
		drawCircle(gl, 4, 10, 463, 136);

		gl.glColor3ub((byte) 181, (byte) 106, (byte) 76);
		drawCircle(gl, 4, 10, 465, 134);

		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		drawCircle(gl, 4, 10, 466, 133);
		drawCircle(gl, 5, 12, 478, 131);
		drawCircle(gl, 5, 12, 473, 131);

		gl.glColor3ub((byte) 181, (byte) 106, (byte) 76);
		drawCircle(gl, 5, 12, 472, 126);

		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		drawCircle(gl, 5, 12, 471, 125);
		drawCircle(gl, 5, 12, 483, 121);

		gl.glColor3ub((byte) 181, (byte) 106, (byte) 76);
		drawCircle(gl, 5, 12, 478, 116);

		gl.glColor3ub((byte) 139, (byte) 146, (byte) 22);
		drawCircle(gl, 5, 12, 477, 116);
		drawCircle(gl, 5, 12, 468, 119);
		drawCircle(gl, 5, 12, 480, 141);

		// Food court components
		gl.glColor3ub((byte) 227, (byte) 91, (byte) 137);
		drawCircle(gl, 1, 2, 468, 119);
		drawCircle(gl, 1, 2, 468, 133);
		drawCircle(gl, 1, 2, 478, 133);
		drawCircle(gl, 1, 2, 483, 128);
		drawCircle(gl, 1, 2, 457, 119);
		drawCircle(gl, 1, 2.5f, 463, 134);
		drawCircle(gl, 1, 1.5f, 464, 110);
		drawCircle(gl, 1, 2.5f, 478, 106);
		drawCircle(gl, 1, 3, 483, 119);
		drawCircle(gl, 1, 3, 473, 103);

		// Trees beside Food Court
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 75, (byte) 35, (byte) 5);
		gl.glVertex2f(473, 90);
		gl.glVertex2f(476, 90);
		gl.glVertex2f(475, 100);
		gl.glVertex2f(470, 130);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 75, (byte) 35, (byte) 5);
		gl.glVertex2f(473, 90);
		gl.glVertex2f(476, 90);
		gl.glVertex2f(472, 100);
		gl.glVertex2f(466, 110);
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor3ub((byte) 75, (byte) 35, (byte) 5);
		gl.glVertex2f(472, 90);
		gl.glVertex2f(476, 90);
		gl.glVertex2f(477, 110);
		gl.glVertex2f(480, 140);
		gl.glEnd();

		if (p <= 800) {
            p += 0.1;
        } else {
            p = -10;
        }

        if (j <= 800) {
            j += 0.3;
        } else {
            j = -250;
        }

        if (k <= 800) {
            k += 0.3;
        } else {
            k = -100;
        }

        // Draw Bus 1
        drawBus(gl, j, new float[]{255, 81, 76});

        // Draw Bus 2
        drawBus(gl, k, new float[]{43, 58, 139});
        
//        // Sun
//        gl.glColor3ub((byte) 253, (byte) 183, (byte) 77);
//        drawCircle(gl, 18, 36, 400, 705);
//        gl.glColor3ub((byte) 253, (byte) 183, (byte) 77);
//        drawCircle(gl, 16.5f, 33, 400, 705);
//        gl.glColor3ub((byte) 253, (byte) 183, (byte) 77);
//        drawCircle(gl, 14.5f, 30, 400, 705);
//        gl.glColor3ub((byte) 253, (byte) 183, (byte) 77);
//        drawCircle(gl, 12.5f, 27, 400, 705);
        

        // Megh 1 covering sun
        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 13, 20, 400, 665);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 11, 18, 400, 665);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 10, 20, 410, 675);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 10, 20, 412, 672);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 13, 20, 410, 655);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 9, 20, 420, 680);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, 420, 679);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, 420, 650);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, 420, 652);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 9, 20, 430, 685);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, 430, 683);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, 425, 655);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, 435, 657);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, 440, 675);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 8, 18, 445, 665);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, 443, 663);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 18, 18, 420, 664);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 18, 25, 417, 665);

        // Megh 2
        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 13, 20, p + 200, 745);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 11, 18, p + 200, 745);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 10, 20, p + 210, 755);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 10, 20, p + 212, 762);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 13, 20, p + 210, 735);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 9, 20, p + 220, 750);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 220, 759);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, p + 220, 756);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 220, 752);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, p + 230, 765);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 230, 761);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, p + 225, 745);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 235, 747);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 9, 20, p + 240, 755);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 8, 18, p + 243, 745);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 240, 743);
        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 8, 18, p + 230, 733);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 230, 738);
        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 8, 18, p + 220, 723);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 8, 18, p + 220, 728);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 23, 9, p + 245, 744);
        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 21, 10, p + 240, 720);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 21, 11, p + 238, 723);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 18, 18, p + 210, 744);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 18, 25, p + 220, 745);

        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 10, 20, p + 235, 715);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 10, 20, p + 235, 719);

        // Megh 3
        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 9, 15, p + 20, 685);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 14, p + 21, 685);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 7, 16, p + 30, 695);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 7, 14, p + 32, 692);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 12, 16, p + 28, 680);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 10, 15, p + 45, 690);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 9, 13, p + 43, 685);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 15, 18, p + 48, 670);

        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 6, 14, p + 30, 680);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 13, p + 30, 677);

        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 6, 14, p + 38, 668);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 13, p + 36, 667);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 11, 15, p + 29, 668);

        // Megh 4
        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 9, 15, 590, 695);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 14, 591, 695);

        gl.glColor3ub((byte) 232, (byte) 241, (byte) 255);
        drawCircle(gl, 7, 16, 600, 705);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 7, 14, 602, 702);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 12, 16, 598, 690);

        gl.glColor3ub((byte) 221, (byte) 229, (byte) 247);
        drawCircle(gl, 10, 15, 615, 700);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 9, 13, 613, 695);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 15, 18, 618, 680);

        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 6, 14, 600, 690);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 13, 600, 687);

        gl.glColor3ub((byte) 173, (byte) 197, (byte) 224);
        drawCircle(gl, 6, 14, 608, 678);
        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 6, 13, 606, 677);

        gl.glColor3ub((byte) 252, (byte) 254, (byte) 255);
        drawCircle(gl, 11, 15, 599, 678);
    }
	
	private void drawStreetLamp(GL2 gl, float x, float y) {
	    // Vẽ cột đèn đường (hình chữ nhật)
	    gl.glColor3f(0.5f, 0.5f, 0.5f); // Màu xám cho cột đèn
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glVertex2f(x, y); // Góc dưới trái
	    gl.glVertex2f(x + 3.0f, y); // Góc dưới phải
	    gl.glVertex2f(x + 3.0f, y + 50f); // Góc trên phải
	    gl.glVertex2f(x, y + 50f); // Góc trên trái
	    gl.glEnd();

	    // Vẽ bóng đèn (hình tròn) 
	    gl.glColor3f(1.0f, 1.0f, 0.0f); // Màu vàng cho bóng đèn
	    gl.glBegin(GL2.GL_POLYGON);
	    int numSegments = 70; // Giảm số lượng đoạn cho bóng đèn nhỏ hơn
	    float radius = 2.0f; // Bán kính nhỏ hơn nhiều
	    float centerX = x + 1.0f; // Vị trí trung tâm bóng đèn
	    float centerY = y + 50.5f; // Đặt bóng đèn lên trên cột đèn

	    for (int i = 0; i < numSegments; i++) {
	        double angle = 2 * Math.PI * i / numSegments;
	        float posX = (float) (centerX + Math.cos(angle) * radius);
	        float posY = (float) (centerY + Math.sin(angle) * radius);
	        gl.glVertex2f(posX, posY);
	    }
	    gl.glEnd();
	}

    private void drawBus(GL2 gl, float position, float[] color) {
        // Body
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3ub((byte) color[0], (byte) color[1], (byte) color[2]);
        gl.glVertex2f(position + 10, 80);
        gl.glVertex2f(position + 90, 80);
        gl.glVertex2f(position + 90, 105);
        gl.glVertex2f(position + 10, 105);
        gl.glEnd();

        // Lower Part
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3ub((byte) color[0], (byte) color[1], (byte) color[2]);
        gl.glVertex2f(position + 10, 55);
        gl.glVertex2f(position + 92, 55);
        gl.glVertex2f(position + 92, 80);
        gl.glVertex2f(position + 10, 80);
        gl.glEnd();

        // Top Green
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3ub((byte) 0, (byte) 51, (byte) 0);
        gl.glVertex2f(position + 11, 81);
        gl.glVertex2f(position + 89, 81);
        gl.glVertex2f(position + 89, 102);
        gl.glVertex2f(position + 11, 102);
        gl.glEnd();

        // Draw windows
        for (int i = 0; i < 7; i++) {
            float xStart = position + 12 + i * 10;
            float xEnd = xStart + 8;
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3ub((byte) 230, (byte) 255, (byte) 255);
            gl.glVertex2f(xStart, 85);
            gl.glVertex2f(xEnd, 85);
            gl.glVertex2f(xEnd, 100);
            gl.glVertex2f(xStart, 100);
            gl.glEnd();
        }

        // Wheels
        gl.glColor3ub((byte) 255, (byte) 255, (byte) 204);
        drawCircle(gl, 5, 10, position + 25, 55); // Rear wheel
        drawCircle(gl, 5, 10, position + 78, 55); // Front wheel

        // Text bus 
        gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
        gl.glRasterPos2f(position + 40, 65);
        glutBitmapCharacter(gl, 'N');
        glutBitmapCharacter(gl, 'L');
        glutBitmapCharacter(gl, 'U');
    }

    
}
