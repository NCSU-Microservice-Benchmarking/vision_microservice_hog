# Use the official OpenJDK 21 image as the base image
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Set the working directory inside the container
WORKDIR /app

# Set DNS servers (if necessary)
RUN echo "nameserver 8.8.8.8" >> /etc/resolv.conf
RUN echo "nameserver 8.8.4.4" >> /etc/resolv.conf

# Copy src/main/resources/open

COPY src/main/resources/lib/libopencv_java4100.so /usr/lib
COPY src/main/resources/lib/libopencv_calib3d.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_core.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_dnn.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_features2d.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_flann.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_gapi.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_highgui.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_imgcodecs.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_imgproc.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_java480.so /usr/lib
COPY src/main/resources/lib/libopencv_java490.so /usr/lib
COPY src/main/resources/lib/libopencv_ml.so /usr/lib
COPY src/main/resources/lib/libopencv_ml.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_objdetect.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_photo.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_stitching.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_videoio.so.410 /usr/lib
COPY src/main/resources/lib/libopencv_video.so.410 /usr/lib

COPY src/main/resources/lib/opencv-4100.jar /usr/lib
# COPY src/main/resources/lib/libopencv_java4100.so /usr/lib

ENV LD_LIBRARY_PATH /usr/lib

# Copy the JAR file to the working directory
COPY target/spring-first-app-0.0.1-SNAPSHOT.jar /app


# Expose port 5000
EXPOSE 5000

# Set the entry point for the container
ENTRYPOINT ["java", "-cp", "/usr/lib/opencv-4100.jar", "-jar", "spring-first-app-0.0.1-SNAPSHOT.jar"]
