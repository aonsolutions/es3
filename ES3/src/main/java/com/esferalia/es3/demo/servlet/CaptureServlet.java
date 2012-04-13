package com.esferalia.es3.demo.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esferalia.es3.demo.server.directory.DirectoryManager;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

public class CaptureServlet extends HttpServlet {

	public static final String TIME = "time";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		String file = uri.substring(uri.lastIndexOf('/') + 1);
		String id = file.substring(0, file.lastIndexOf('.'));
		
		long time = Long.valueOf(req.getParameter(TIME));
		long timestamp = time - 1500;

		String videoFile = req.getContextPath() + "/mission/49/video/" + id
				+ ".HD.flv";
		URL videoURL = new URL("http", "localhost", req.getServerPort(),
				videoFile);

		IContainer container = null;
		IStreamCoder videoCoder = null;
		try {
			// create a Xuggler container object
			container = IContainer.make();
			if (container.open(videoURL.toString(), IContainer.Type.READ, null) < 0)
				throw new IllegalArgumentException("could not open file: "
						+ videoFile);

			// query how many streams the call to open found
			int numStreams = container.getNumStreams();

			// and iterate through the streams to find the first video stream
			for (int i = 0; i < numStreams; i++) {
				// find the stream object

				IStream stream = container.getStream(i);

				// get the pre-configured decoder that can decode this stream;
				IStreamCoder coder = stream.getStreamCoder();

				if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
					// Now we have found the video stream in this file. Let's
					// open up our decoder so it can do work
					videoCoder = coder;
					if (videoCoder.open() < 0)
						throw new RuntimeException(
								"could not open video decoder for container: "
										+ videoFile);

					IVideoResampler resampler = null;
					if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
						// if this stream is not in BGR24, we're going to need
						// to
						// convert it. The VideoResampler does that for us.

						resampler = IVideoResampler.make(videoCoder.getWidth(),
								videoCoder.getHeight(),
								IPixelFormat.Type.BGR24, videoCoder.getWidth(),
								videoCoder.getHeight(),
								videoCoder.getPixelType());
						if (resampler == null)
							throw new RuntimeException(
									"could not create color space resampler for: "
											+ videoFile);
					}

					if (container.seekKeyFrame(i, timestamp, 0) < 0)
						throw new RuntimeException("could not seek video "
								+ videoFile + " to : " + timestamp
								+ " microseconds");

					IPacket packet = IPacket.make();
					if (container.readNextPacket(packet) < 0)
						throw new RuntimeException(
								"could not read next packet from:" + videoFile);

					// We allocate a new picture to get the data out of Xuggle

					IVideoPicture picture = IVideoPicture.make(
							videoCoder.getPixelType(), videoCoder.getWidth(),
							videoCoder.getHeight());

					int offset = 0;
					while (offset < packet.getSize()) {
						// Now, we decode the video, checking for any errors.

						int bytesDecoded = videoCoder.decodeVideo(picture,
								packet, offset);
						if (bytesDecoded < 0)
							throw new RuntimeException(
									"got error decoding video in: " + videoFile);
						offset += bytesDecoded;

						// Some decoders will consume data in a packet, but will
						// not be able to construct a full video picture yet.
						// Therefore you should always check if you got a
						// complete picture
						// from the decode.

						if (picture.isComplete()) {
							IVideoPicture newPic = picture;

							// If the resampler is not null, it means we didn't
							// get the video in BGR24 format and need to convert
							// it into BGR24 format.

							if (resampler != null) {
								// we must resample
								newPic = IVideoPicture
										.make(resampler.getOutputPixelFormat(),
												picture.getWidth(),
												picture.getHeight());
								if (resampler.resample(newPic, picture) < 0)
									throw new RuntimeException(
											"could not resample video from: "
													+ videoFile);
							}

							if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
								throw new RuntimeException(
										"could not decode video as BGR 24 bit data in: "
												+ videoFile);

							// convert the BGR24 to an Java buffered image

							BufferedImage javaImage = Utils
									.videoPictureToImage(newPic);
							
							// FIXME: Delete after demo
							try {
								File tmpFile = new File(String.format(
										"%sSobrevolando Machu Picchu %04d.png",
										DirectoryManager.TEMP_DIR,
										(int) Math.floor(time/1000)));
								OutputStream tmpOut = new FileOutputStream(tmpFile);
								ImageIO.write(javaImage, "png", tmpOut);
								tmpOut.close();
							}
							catch (Throwable t ) {
								t.printStackTrace();
							}

							OutputStream out = resp.getOutputStream();
							ImageIO.write(javaImage, "png", out);

						}
					}
					return;
				}

			}
			throw new RuntimeException(
					"could not find video stream in container: " + videoFile);

		} finally {
			if (videoCoder != null) {
				videoCoder.close();
			}
			if (container != null) {
				container.close();
			}
		}

	}
}
