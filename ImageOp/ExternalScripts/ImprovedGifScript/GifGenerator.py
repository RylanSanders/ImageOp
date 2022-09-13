import glob
from PIL import Image

import numpy as np
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("duration")
args = parser.parse_args()

def make_gif(frame_folder):
    frames = [Image.open(image) for image in glob.glob(f"{frame_folder}/*.png")]
    frame_one = frames[0]
    frame_one.save("./ImprovedGifScript/result.gif", format="GIF", append_images=frames,
               save_all=True, duration=100, loop=0)
               
make_gif("./Images")


