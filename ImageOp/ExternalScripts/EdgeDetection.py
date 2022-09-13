import cv2
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("threshold1")
parser.add_argument("threshold2")
args = parser.parse_args()

img = cv2.imread(args.input_path, cv2.IMREAD_GRAYSCALE)

edges = cv2.Canny(img, threshold1=float(args.threshold1), threshold2=float(args.threshold2))
cv2.imwrite(args.output_path, edges)


'''
from PIL import Image  
import PIL
import argparse
from skimage import feature
from skimage import io
import numpy
import imageio
parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("sigma")
args = parser.parse_args()
pic = io.imread(args.input_path,as_gray=True)
pic2 = numpy.uint8(pic)*255
io.imshow(pic2)
io.show()
edges = feature.canny(pic2, sigma=args.sigma, low_threshold=0.1, high_threshold=0.3)
edges = edges.save(args.output_path) 

'''