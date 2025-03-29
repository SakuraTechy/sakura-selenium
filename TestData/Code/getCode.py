import ddddocr
import time
import sys

begin=time.time()
ocr = ddddocr.DdddOcr()

def func(file,name):
    with open(file,'rb') as f:

        img_bytes = f.read()

    res = ocr.classification(img_bytes)

    file = open(name, mode='w')
    file.write(res)

    finish=time.time()
    print("结果：")
    print("用时：%s 秒" % str(finish-begin))
    print(res+"=")
    return res

if __name__ == '__main__':
    func(sys.argv[1],sys.argv[2])
    # func('D:\Jenkins\workspace\Sakura.Web.UI.Automation.Test\TestData\Code\getCode.png','D:\Jenkins\workspace\Sakura.Web.UI.Automation.Test\TestData\Code\getCode.txt')
