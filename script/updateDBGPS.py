'''
此脚本用于更新转换GPS坐标格式
'''

import piexif
from PIL import Image
import os
import pymysql
import json

def convert_to_dms(rational_list):
    """将 rational 列表转换为度分秒格式的字符串"""
    degrees, minutes = rational_list[0], rational_list[1]
    seconds = rational_list[2][0] / float(rational_list[2][1])  # 处理秒的小数部分

    # 计算度、分、秒
    degrees = int(degrees[0] / degrees[1])
    minutes = int(minutes[0] / minutes[1])
    seconds = round(seconds, 2)  # 保留两位小数

    return [f"{degrees}", f"{minutes:2d}", f"{seconds:5.2f}"]

def convert_to_double(rational_list):
    """将 rational 列表转换为十进制度数"""
    degrees, minutes, seconds = rational_list
    return (degrees[0] / degrees[1]) + (minutes[0] / minutes[1]) / 60 + (seconds[0] / seconds[1]) / 3600

def getInfo(filepath):
    img = Image.open(filepath)
    
    # 检查图像是否包含 Exif 信息
    if 'exif' in img.info:
        exif_dict = piexif.load(img.info['exif'])
        
        # 检查是否存在 GPS 信息
        if 'GPS' in exif_dict:
            # 检查 GPS 经度信息
            if piexif.GPSIFD.GPSLongitude in exif_dict['GPS']:
                longitude_raw = exif_dict['GPS'][piexif.GPSIFD.GPSLongitude]
            else:
                print("GPS Longitude not found in Exif data.")
                return {}
            
            # 检查 GPS 纬度信息
            if piexif.GPSIFD.GPSLatitude in exif_dict['GPS']:
                latitude_raw = exif_dict['GPS'][piexif.GPSIFD.GPSLatitude]
            else:
                print("GPS Latitude not found in Exif data.")
                return {}
            
            # 检查 GPS 海拔高度信息
            if piexif.GPSIFD.GPSAltitude in exif_dict['GPS']:
                altitude_raw = exif_dict['GPS'][piexif.GPSIFD.GPSAltitude]
            else:
                print("GPS Altitude not found in Exif data.")
                return {}

            # 转换海拔高度为米
            altitude = altitude_raw[0] / 1000.0 if altitude_raw[1] == 1000 else None

            # 转换经纬度为度分秒格式和十进制度数
            longitude_dms = convert_to_dms(longitude_raw)
            latitude_dms = convert_to_dms(latitude_raw)
            longitude = convert_to_double(longitude_raw)
            latitude = convert_to_double(latitude_raw)

            gps_info = {
                "gpsInfoDms": {
                    "altitude": [f"{altitude:.3f}"] if altitude is not None else [f"{altitude_raw[0] / altitude_raw[1]}"],
                    "latitude": latitude_dms,
                    "longitude": longitude_dms
                },
                "gpsInfoRaw": {
                    "altitude": [f"{altitude_raw[0] / altitude_raw[1]}"] if altitude_raw[1] != 0 else [],
                    "latitude": [f"{d[0]}/{d[1]}" for d in latitude_raw],
                    "longitude": [f"{l[0]}/{l[1]}" for l in longitude_raw]
                },
                "gpsInfoDouble": {
                    "altitude": altitude if altitude is not None else altitude_raw[0] / altitude_raw[1],
                    "latitude": latitude,
                    "longitude": longitude
                }
            }

            return gps_info
        else:
            print("GPS information not found in Exif data.")
            return {}
    else:
        # 如果图像不包含 Exif 信息，则返回空字典
        print("Exif data not found in image.")
        return {}

def connect_to_database(host, port, user, password, database):
    """连接到MySQL数据库"""
    connection = pymysql.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    )
    return connection

def get_image_records_from_database(connection):
    """从数据库中获取图像记录"""
    cursor = connection.cursor()
    cursor.execute("SELECT image_id, image_name, original_filename FROM image")
    image_records = cursor.fetchall()
    return image_records

def update_gps_info_in_database(connection, image_id, gps_info):
    """更新数据库中的GPS信息"""
    cursor = connection.cursor()
    cursor.execute("UPDATE image SET gps = %s WHERE image_id = %s", (json.dumps(gps_info), image_id))
    connection.commit()

def main():
    # 数据库连接信息
    host = "127.0.0.1"
    port = 3306
    user = "root"
    password = "12345678"
    database = "image_viewer"

    # 本地图片路径
    local_image_path = "D://PANORAMA/"

    # 连接到数据库
    connection = connect_to_database(host, port, user, password, database)

    # 获取图像记录
    image_records = get_image_records_from_database(connection)

    for image_record in image_records:
        image_id, image_name, original_filename = image_record
        image_path = os.path.join(local_image_path, original_filename)
        
        # 检查本地文件是否存在
        if os.path.exists(image_path):
            # 调用getInfo函数获取GPS信息
            gps_info = getInfo(image_path)
            
            if gps_info:
                # 更新数据库中的GPS信息
                update_gps_info_in_database(connection, image_id, gps_info)
                print(f"Updated GPS info for image_id {image_id}")
            else:
                print(f"No GPS info found for image_id {image_id}")
        else:
            print(f"Local image file not found for image_id {image_id}")

    # 关闭数据库连接
    connection.close()

if __name__ == "__main__":
    main()
