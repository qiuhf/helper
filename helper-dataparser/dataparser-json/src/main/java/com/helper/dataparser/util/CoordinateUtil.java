package com.helper.dataparser.util;

/**
 * @author sz_qiuhf@163.com
 **/
public class CoordinateUtil {

    private CoordinateUtil() {
    }

    /**
     * 将坐标转换为字符串，用','隔开
     *
     * @param coordinate 坐标
     * @return 坐标编码
     */
    public static String convertToCode(int[] coordinate) {
        if (coordinate.length != 2) {
            throw new IllegalArgumentException(String.format("(%s)，invalid coordinates!", (Object) coordinate));
        }
        return convertToCode(coordinate[0], coordinate[1]);
    }

    /**
     * 将坐标转换为字符串，用','隔开
     *
     * @param x 层级
     * @param y 深度
     * @return 坐标编码
     */
    public static String convertToCode(int x, int y) {
        return x + "," + y;
    }

    /**
     * 将字符串坐标转换为数组
     *
     * @param coordinateCode 坐标编码
     * @return 解码后的坐标
     */
    public static int[] convertToCoordinate(String coordinateCode) {
        String[] position = coordinateCode.split(",");
        if (position.length != 2) {
            throw new IllegalArgumentException(String.format("(%s)，Illegal coordinates!", coordinateCode));
        }
        return new int[]{Integer.parseInt(position[0]), Integer.parseInt(position[1])};
    }

}
