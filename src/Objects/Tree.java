package Objects;


public class Tree {
	
	private int x, y, type, aniIndex, aniTick;
	
	//must have type => it make the tree paint interactive with the color (Blue value in Level.java and HelpMethods.java)
	public Tree(int x, int y, int type) {
		this.type = type;
		this.setX(x);
		this.setY(y);
		this.aniIndex = 0;
	}
	
	public void update() {
		aniTick++;
		if (aniTick >= 40) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public void setAniIndex(int aniIndex) {
		this.aniIndex = aniIndex;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
