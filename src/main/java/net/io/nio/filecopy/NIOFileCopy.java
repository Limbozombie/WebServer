package net.io.nio.filecopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 271636872@qq.com
 * @since 2020/9/9 21:08
 */
public class NIOFileCopy {

	public void copyFileBuffer(File source, File target) {

		try (
				FileChannel sourceChannel = new FileInputStream(source).getChannel();
				FileChannel targetChannel = new FileOutputStream(target).getChannel()
		) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			while (sourceChannel.read(byteBuffer) != -1) {
				//读模式转为写模式
				byteBuffer.flip();
				//不能保证每次都把byteBuffer内容全部write完,需要判断是否还有剩余
				while (byteBuffer.hasRemaining()) {
					targetChannel.write(byteBuffer);
				}
				//写模式转为读模式
				byteBuffer.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void copyFileTransfer(File source, File target) {

		try (
				FileChannel sourceChannel = new FileInputStream(source).getChannel();
				FileChannel targetChannel = new FileOutputStream(target).getChannel()
		) {
			long transferred = 0;
			long size = sourceChannel.size();
			// transferTo  不能保证一次就 传输完
			while (transferred != size) {
				transferred += sourceChannel.transferTo(0, size, targetChannel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
