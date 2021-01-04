package com.autopai.common.utils.numeral.rtree;


import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Test1
{
	private static final String TAG = "RTree_Test1";
	public static void main(String[] args)
	{
		float[] f = {10, 20, 40, 70,	//0
			     30, 10, 70, 15,
			     100, 70, 110, 80,		//2
			     0, 50, 30, 55,
			     13, 21, 54, 78,		//4
//			     3, 8, 23, 34,
//			     200, 29, 202, 50,
//			     34, 1, 35, 1,			//7
//			     201, 200, 234, 203,
//			     56, 69, 58, 70,		//9
//			     2, 67, 270, 102,
//			     1, 10, 310, 20,		//11
//			     23, 12, 345, 120,
//			     5, 34, 100, 340,
//			     19,100,450,560,	//14
//			     12,340,560,450
//			     34,45,190,590,
//			     24,47,770,450,	//17
//			     
//			     91,99,390,980,
//			     89,10,99,100,	//19
//			     10,29,400,990,
//			     110,220,220,330,
//			     123,24,234,999	//22
		};
		
		MemoryPageFile file = new MemoryPageFile();
		RTree tree = new RTree(2, 0.4f, 3, file, Constants.RTREE_QUADRATIC);

        ArrayList<Rectangle> rects = new ArrayList<>();
		int j = 0;
		//插入结点
		for(int i = 0; i < f.length;)
		{
			Point p1 = new Point(new float[]{f[i++],f[i++]});//左下
			Point p2 = new Point(new float[]{f[i++],f[i++]});//右上
			final Rectangle rectangle = new Rectangle(p1, p2);
            rects.add(rectangle);
			Log.e(TAG, "insert " + j + "th " + rectangle + "......");
			tree.insert(rectangle, -2);
			Log.e(TAG, tree.file.readNode(0).toString());
			j++;
		}
		Log.e(TAG, "Insert finished.");
		Log.e(TAG,"---------------------------------");

		Log.e(TAG,"----------------traverse by level-----------------");

		for (RTNode node : tree.traverseByLevel()) {
			Log.e(TAG, node.toString());
		}
        Log.e(TAG,"----------------traverse by level end-----------------");

		//查询节点
        int pageSize = tree.getPageSize();
		int type = tree.getTreeType();
		Rectangle rectangleT = rects.get(3);
        List<Data> result = tree.intersection_Rectangles(rectangleT, null);

		//删除结点
		Log.e(TAG,"---------------------------------");
		Log.e(TAG,"Begin delete.......");
		
		j = 0;
		for(int i = 0; i < f.length;) {
			Point p1 = new Point(new float[]{f[i++],f[i++]});
			Point p2 = new Point(new float[]{f[i++],f[i++]});
			final Rectangle rectangle = new Rectangle(p1, p2);
			Log.e(TAG, "delete " + j + "th " + rectangle + "......");
			tree.delete(rectangle);
			Log.e(TAG, tree.file.readNode(0).toString());
			j++;
		}

		Log.e(TAG,"---------------------------------");
		Log.e(TAG,"Delete finished.");


		Log.e(TAG,"----------------traverse by level after delete-----------------");

		for (RTNode node : tree.traverseByLevel()) {
			Log.e(TAG, node.toString());
		}
        Log.e(TAG,"----------------traverse by level-----------------");
	}
}
