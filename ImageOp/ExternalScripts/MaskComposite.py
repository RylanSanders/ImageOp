import cv2 as cv
import numpy as np
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("mask")
args = parser.parse_args()

# Load two images
img_mask = cv.imread(args.mask)
src = cv.imread(args.input_path)
# I want to put logo on top-left corner, So I create a ROI
rows,cols,channels = src.shape
roi = img_mask[0:rows, 0:cols]
# Now create a mask of logo and create its inverse mask also
img2gray = cv.cvtColor(src,cv.COLOR_BGR2GRAY)
ret, mask = cv.threshold(img2gray, 10, 255, cv.THRESH_BINARY)
mask_inv = cv.bitwise_not(mask)
# Now black-out the area of logo in ROI
img1_bg = cv.bitwise_and(roi,roi,mask = mask_inv)
# Take only region of logo from logo image.
img2_fg = cv.bitwise_and(img_mask,src,mask = mask)
# Put logo in ROI and modify the main image
dst = cv.add(img1_bg,img2_fg)
img_mask[0:rows, 0:cols ] = dst
cv.imwrite(args.output_path, img_mask)