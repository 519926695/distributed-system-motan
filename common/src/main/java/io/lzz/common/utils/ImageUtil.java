package io.lzz.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
	private final static String fName = ".jpg";
	/**
	 * 
	 * @Title compress
	 * @Description 压缩图片
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String compress(File file) {

		return scale(file, 1.0);
	}

	/**
	 * 
	 * @Title compress
	 * @Description 按比例缩放图片
	 * @param file
	 * @param resize
	 * @return
	 * @throws Exception
	 */
	public static String scale(File file, double resize) {

		String path = file.getParentFile().getAbsolutePath();
		String toFile = path + File.separator + RandomUtils.getRandomAlphaString(5) + fName;
		try {
			Thumbnails.of(file).scale(resize).toFile(toFile);
		} catch (IOException e) {
			io.lzz.common.utils.SlfLogService.error(e);
		}

		return toFile;
	}

	/**
	 * 
	 * @Title tailoring
	 * @Description 以图片中心裁剪图片
	 * @param file
	 * @param size
	 *            裁剪后大小 size*size
	 * @return
	 * @throws Exception
	 */
	public static String tailoring(File file, int size) {
		return tailoring(file, size, size);
	}

	/**
	 * 
	 * @Title tailoring
	 * @Description 以图片中心裁剪图片
	 * @param file
	 * @param width
	 *            裁剪后大小 width*height
	 * @return
	 * @throws Exception
	 */
	public static String tailoring(File file, int width, int height) {

		String path = file.getParentFile().getAbsolutePath();
		String toFile = path + File.separator + RandomUtils.getRandomAlphaString(5) + fName;
		try {
			BufferedImage sourceImg = ImageIO.read(file);
			int leng = sourceImg.getWidth() < sourceImg.getHeight() ? sourceImg.getWidth() : sourceImg.getHeight();
			Thumbnails.of(sourceImg).sourceRegion(Positions.CENTER, leng, leng).size(width, height).keepAspectRatio(false).toFile(toFile);
		} catch (IOException e) {
			io.lzz.common.utils.SlfLogService.error(e);
		}
		return toFile;
	}

	public static void main(String[] args) {
		compress(new File("C:\\Users\\51992\\Desktop\\1522390561026-jBpJQ.png"));
	}
}
