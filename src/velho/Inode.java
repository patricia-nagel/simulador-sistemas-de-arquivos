package velho;

import java.util.ArrayList;
import java.util.List;

public class Inode {
    String owner;
    long creationTime;
    long lastAccessTime;
    long lastUpdateTime;
    boolean isDirectory;
    long size;
    List<Integer> blocks;

    public Inode(String owner, boolean isDirectory) {
        this.owner = owner;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = this.creationTime;
        this.lastUpdateTime = this.creationTime;
        this.isDirectory = isDirectory;
        this.size = 0;
        this.blocks = new ArrayList<>();
    }

}
