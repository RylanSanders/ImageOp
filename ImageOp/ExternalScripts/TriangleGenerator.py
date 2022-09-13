import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np
import cv2 as cv
import argparse
import sys

np.set_printoptions(threshold=sys.maxsize)
parser = argparse.ArgumentParser()
parser.add_argument("input_path")
parser.add_argument("output_path")
parser.add_argument("arguments")
args = parser.parse_args()

def string_to_points(string):
    point_text = string.split("p")
    to_ret= []
    for point in point_text:
        nums = point.split(" ")
        to_ret.append(np.array([int(nums[0]), int(nums[1])]))
    return np.asarray(to_ret)

points = ""
with open(args.input_path, 'r') as f:
    points = f.read()

import triangle as tr
# https://rufat.be/triangle/API.html for the second arg to B
A = dict(vertices=np.array(string_to_points(points)))
B = tr.triangulate(A,args.arguments +'en')
tr.compare(plt, A, B)
plt.show()
print(string_to_points(points).shape)
print(np.array(((0, 0), (1, 0), (1, 1), (0, 1))).shape)
with open(args.output_path, 'w') as f:
    f.write(str(B['neighbors']))