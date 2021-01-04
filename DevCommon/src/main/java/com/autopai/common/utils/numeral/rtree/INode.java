package com.autopai.common.utils.numeral.rtree;

public interface INode 
{
	public RTNode getParent();
	public String getUniqueId();
	public int getLevel();
	public Rectangle getNodeRectangle();
	public boolean isLeaf();
    public boolean isRoot();
    public boolean isIndex();
}
