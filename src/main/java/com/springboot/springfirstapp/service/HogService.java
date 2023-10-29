package com.springboot.springfirstapp.service;

import java.lang.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.core.MatOfFloat;

public class HogService {
    public String ProcessImage(String string) {
        System.out.println("Start");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Load image
        Mat image = Imgcodecs.imread("/root/practice/stockpic.jpg");

        // Convert image to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Calculate HOG features
        HOGDescriptor hog = new HOGDescriptor(new Size(64, 128), new Size(16, 16), new Size(8, 8), new Size(8, 8), 9);
        MatOfFloat features = new MatOfFloat();
        hog.compute(gray, features);

        // Display HOG features
        System.out.println(features.dump());
        return "Complete";

    }    
}
