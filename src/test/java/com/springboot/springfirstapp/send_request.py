#!/usr/bin/env python3
import requests

if __name__ == "__main__":
    host = "http://localhost:5000"
    # encode the image as a binary string 
#    image = open("bus.png","rb").read()
    # put the image in a multipart form
 #   files = {"image":image}
    # send a post request to the detect endpoint
   # response = requests.post(host+"/model-hog-people",files=files)
    response = requests.post(host+"/hello")
    # check the response status code
    print(response.status_code)
    # save the image
    # open("result.png","wb").write(response.content)
