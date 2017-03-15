package org.javaclub.opensource.demos.thread;

class MultiThread
{
	public static void main(String[] args)
	{
		MyThread mt=new MyThread();
		/*new Thread(mt).start();
		new Thread(mt).start();
		new Thread(mt).start();
		new Thread(mt).start();*/
		mt.getThread().start();
		mt.getThread().start();
		mt.getThread().start();
		mt.getThread().start();
		//mt.setDaemon(true);
		//mt.setPriority(Thread.MAX_PRIORITY);
		//mt.start();
		int index=0;
		while(true)
		{
			/*if(index++==1000)
				break;*/
			System.out.println(index);
			System.out.println("main:"+Thread.currentThread().getName());
		}
	}
}

class MyThread //implements Runnable//extends Thread
{
	int index=0;
	private class InnerThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				System.out.println(Thread.currentThread().getName()+":"+index++);
			}
		}
	}
	Thread getThread()
	{
		return new InnerThread();
	}
	/*public void run()
	{
		while(true)
		{
			System.out.println(Thread.currentThread().getName()+":"+index++);
			//yield();
		}
	}*/
}