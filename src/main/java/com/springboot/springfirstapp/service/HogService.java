package com.springboot.springfirstapp.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;



//@Service
public class HogService {

    public ResponseEntity ProcessImage(MultipartFile multipartFile) {
        try {
            File imageFile = File.createTempFile(UUID.randomUUID().toString(),".png");
            multipartFile.transferTo(imageFile);
            System.out.println("Start");
            // Load image
            Mat image = Imgcodecs.imread(imageFile.getAbsolutePath()); // will be replaced with request body

            // Convert image to grayscale
            Mat gray = new Mat();
            Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

            // Calculate HOG features
            HOGDescriptor hog = new HOGDescriptor();
            hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());
            MatOfRect foundLocations = new MatOfRect();
            MatOfDouble foundWeights = new MatOfDouble();

            hog.detectMultiScale(
                    gray,
                    foundLocations,
                    foundWeights,
                    0,
                    new Size(8,8),
                    new Size(32,32),
                    1.05,
                    2,
                    false);

            List<Rect> rectangles = foundLocations.toList();
    // have to do some swapping to figure out why there is a problem
            for (int i = 0; i < rectangles.size(); i++) {
                Imgproc.rectangle(image,
                        new Point(rectangles.get(i).x,rectangles.get(i).y),
                        new Point(rectangles.get(i).x + rectangles.get(i).width,
                                rectangles.get(i).y + rectangles.get(i).height),
                        new Scalar (255,0,0), 2, 1, 0);
            }
            // Display HOG features
            // System.out.println(features.dump());
            File result = File.createTempFile( UUID.randomUUID().toString(),".png");
            File persist = new File("/home/tmp/vision_microservice_hog/src/main/resources/result.png");
            Imgcodecs.imwrite(persist.getAbsolutePath(),image);
            Imgcodecs.imwrite(result.getAbsolutePath(), image);
            byte[] content = null;
            try {
                content = Files.readAllBytes(result.toPath());
            } catch (final IOException e) {
            }
            MultipartFile mfResult = new MockMultipartFile( UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(), "image/png", content);
            System.out.println("complete");
            imageReplacement();
            // delete later
            //System.out.println(Arrays.toString(mfResult.getBytes()));


            return ResponseEntity.ok(mfResult.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

    public void imageReplacement() {
        System.out.println("start replacement");

        //Get the background of the original instance image by doing $original cropped instance image * (1 - original instance segmentation binary mask)$.
        String originalPath = "/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/original_image.jpg";
        Mat originalImage = Imgcodecs.imread(originalPath);
        String originalMaskPath = "/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/original_mask.jpg";
        Mat originalMask = Imgcodecs.imread(originalMaskPath); 
       
        Mat originalMaskCorrectType = new Mat();
        originalMask.convertTo(originalMaskCorrectType, CvType.CV_16UC1);
        Mat originalImage16Type = new Mat();
        originalImage.convertTo(originalImage16Type, CvType.CV_16UC1);
        System.out.println("Original Image bfeore converting: " + originalMask.size() + " " + originalMask.type());

        System.out.println("Original Mask before converting: " + originalMask.size() + " " + originalMask.type());

        System.out.println("Original Image after converting: " + originalImage16Type.size() + " " + originalImage16Type.type());

        System.out.println("Original Mask after converting: " + originalMaskCorrectType.size() + " " + originalMaskCorrectType.type());

        Mat invertedOriginalMask = new Mat();
        Core.bitwise_not(originalMask, invertedOriginalMask);
        System.out.println("Inverted Og Mask: " + invertedOriginalMask.size() + " " + invertedOriginalMask.type());

        Mat thresholdedMat = new Mat();
        // convert the invertedOriginalMask to grayscale
        Imgproc.cvtColor(invertedOriginalMask, thresholdedMat, Imgproc.COLOR_BGR2GRAY);

        System.out.println("Inverted Og Mask after converting to grayscale: " + thresholdedMat.size() + " " + thresholdedMat.type());

        // Apply thresholding to make sure all the values are 1 or 0
        double thresholdValue = 128; // Adjust the threshold value as needed
        double maxValue = 255; // Maximum pixel value for the binary mask
        
        Core.inRange(thresholdedMat, new Scalar(thresholdValue), new Scalar(maxValue), thresholdedMat);
        Imgcodecs.imwrite("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/inverted_og_mask.jpg", thresholdedMat);

        System.out.println("Inverted mask after thresholding: " + thresholdedMat.type());

        // load the binary segmentation mask from the photos
        Mat thresholdedMask = Imgcodecs.imread("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/inverted_og_mask.jpg");
    
        System.out.println("Inverted mask type after changing: " + thresholdedMask.type());
        
        Mat originalBg = new Mat();
        Core.bitwise_and(originalImage, thresholdedMask, originalBg);
        Imgcodecs.imwrite("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/og_bg.jpg", originalBg);

        System.out.println("OG BG: " + originalBg.size() + " " + originalBg.type());
       


        //Get the instance of the synthesized instance image by doing $synthesized instance image * synthesized instance segmentation binary mask$.
        String synthPath = "/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/generated_image.jpg";
        String synthMaskPath = "/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/generated_mask.jpg";
       
        Mat synthImage = Imgcodecs.imread(synthPath);
        Mat synthMask =  Imgcodecs.imread(synthMaskPath);

        System.out.println("Synthesized image type: " + synthImage.type());
        System.out.println("Synthesized mask type: " + synthMask.type());

        // turn the synth mask into grayscale, threshold it, and write it to a file
        Mat synthThreshMask = new Mat();
        Imgproc.cvtColor(synthMask, synthThreshMask, Imgproc.COLOR_BGR2GRAY);
        Core.inRange(synthThreshMask, new Scalar(thresholdValue), new Scalar(maxValue), synthThreshMask);
        Imgcodecs.imwrite("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/synth_mask.jpg", synthThreshMask);
        System.out.println("Synth mask after thresholding: " + synthThreshMask.type());

        // pull the synth mask from the file and convert it to the correct type
        Mat synthMaskCorrectType = Imgcodecs.imread("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/synth_mask.jpg");
    
        Mat synthInstance = new Mat();
        Core.bitwise_and(synthImage, synthMaskCorrectType, synthInstance);
        Imgcodecs.imwrite("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/synth_instance.jpg", synthInstance);
        
    
        //Add up the background of the original instance image and the instance of the synthesized instance image to get the replaced instance image.
        Mat newInstance = new Mat();
        Core.add(originalBg, synthInstance, newInstance);
    
        // Get a mismatch of the two segmentation masks by computing $original instance binary mask - the intersection of two masks$.
        Mat intersection = new Mat();
        Core.bitwise_and(originalMask, synthMask, intersection);
        Mat mismatch = new Mat();
        Core.subtract(originalMask, intersection, mismatch);
        
        // image write function 
        String outputPath = "/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/output_image.jpg";
        Imgcodecs.imwrite(outputPath, newInstance);
        Imgcodecs.imwrite("/home/hog-repo/vision_microservice_hog/src/test/java/com/springboot/springfirstapp/mismatch.jpg", mismatch);
        System.out.println("completed replacement");
        //Consolidate an inpainting request (video UUID, frame number, instance ID, replaced instance image, segmentation mask mismatch) and put it into the “Inpainting request queue”
        // return newInstance and mismatch

        
    } 

}
