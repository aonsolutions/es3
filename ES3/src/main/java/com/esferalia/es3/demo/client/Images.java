package com.esferalia.es3.demo.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle  {

	@Source("resources/image.png")
    ImageResource image();

	@Source("resources/video.jpg")
    ImageResource video();
	
	@Source("resources/audio.jpg")
    ImageResource audio();
	
	@Source("resources/Esferalia.jpg")
    ImageResource esferalia();
  }
