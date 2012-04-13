package com.esferalia.es3.demo.servlet;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esferalia.es3.demo.server.directory.DirectoryManager;

public class ImageServlet extends HttpServlet {
	
	public static final String RADIANS_PARM = "radians";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();
		String servletPath = req.getServletPath();
		String imagePath = uri.substring(contextPath.length()
				+ servletPath.length());
		
		InputStream is = this.getClass().getResourceAsStream(imagePath);
		BufferedImage image  = ImageIO.read(is);
		
		String radians = req.getParameter(RADIANS_PARM);
		if ( radians != null && !radians.isEmpty() ) {
			
			AffineTransform tx = new AffineTransform();
			tx.rotate(Double.parseDouble(radians) + Math.PI/2, image.getWidth()/2, image.getHeight()/2);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	        image = op.filter(image, null);
		}
        
		
		OutputStream out = resp.getOutputStream();
		ImageIO.write(image, "png", out);
		
	}
}
