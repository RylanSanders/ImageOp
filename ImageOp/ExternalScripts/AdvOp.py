import cv2 as cv
import numpy as np
import argparse

ops = {
'opening':cv.MORPH_OPEN,
'closing':cv.MORPH_CLOSE,
'morphological_gradient':cv.MORPH_GRADIENT,
'top_hat':cv.MORPH_TOPHAT,
'black_hat':cv.MORPH_BLACKHAT
}

parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("operation")
parser.add_argument("kernel")
parser.add_argument("iterations")
args = parser.parse_args()


img = cv.imread(args.input_path)

kernel = np.ones((int(args.kernel),int(args.kernel)),np.uint8)
result = cv.morphologyEx(img, ops[args.operation], kernel)

cv.imwrite(args.output_path, result)