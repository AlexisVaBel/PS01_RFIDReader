package abelx.api.serial;

public interface ISerialWriter {
	void	setDataToWrite(byte[] data);
	byte	writeNext();
	boolean allWritten();
}
