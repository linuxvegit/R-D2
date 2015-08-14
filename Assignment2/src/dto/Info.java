package dto;

public class Info<T> {
	T infomation;
	boolean writable;
	boolean readable;

	public Info(T information, boolean writable, boolean readable) {
		this.infomation = information;
		this.writable = writable;
		this.readable = readable;
	}

	public Info(T information) {
		this(information, true, true);
	}

	public T getInfomation() {
		return infomation;
	}

	public void setInfomation(T infomation) {
		this.infomation = infomation;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isReadable() {
		return readable;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	@Override
	public String toString() {
		return "Info [infomation=" + infomation + ", writable=" + writable
				+ ", readable=" + readable + "]";
	}

}
