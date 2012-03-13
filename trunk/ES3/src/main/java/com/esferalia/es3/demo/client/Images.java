package com.esferalia.es3.demo.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle  {
	
	@Source("resources/mission.png")
    ImageResource mission();

	@Source("resources/image.png")
    ImageResource image();

	@Source("resources/video.png")
    ImageResource video();
	
	@Source("resources/audio.png")
    ImageResource audio();
	
	@Source("resources/gps.png")
    ImageResource gps();
	
	@Source("resources/doc.png")
    ImageResource doc();
	
	@Source("resources/esferalia_logo.png")
    ImageResource esferalia();

  }
