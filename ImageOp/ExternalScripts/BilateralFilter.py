import cv2 as cv


#Noise Reduction, not much difference
img = cv.imread('bookV1.jpg')

res = cv.bilateralFilter(img, 9, 300, 300)
cv.imshow('res',res)
cv.waitKey(0)
cv.destroyAllWindows()