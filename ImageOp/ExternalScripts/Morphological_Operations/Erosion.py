import cv2 as cv
import numpy as np
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("kernel")
parser.add_argument("iterations")
args = parser.parse_args()


img = cv.imread(args.input_path)

kernel = np.ones((int(args.kernel),int(args.kernel)),np.uint8)
erosion = cv.erode(img,kernel,iterations = int(args.iterations))

cv.imwrite(args.output_path, erosion)