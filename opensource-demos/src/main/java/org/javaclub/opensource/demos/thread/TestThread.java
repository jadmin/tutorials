package org.javaclub.opensource.demos.thread;

class TestThread
{
	public static void main(String[] args)
	{
		Thread1 t1=new Thread1();
		t1.start();
		int index=0;
		while(true)
		{
			if(index++==500)
			{
				t1.stopThread();
				t1.interrupt();
				break;
			}
			System.out.println(Thread.currentThread().getName());
		}
		System.out.println("main() exit");
	}
}

class Thread1 extends Thread
{
	private boolean bStop=false;
	public synchronized void run()
	{
		while(!bStop)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				//e.printStackTrace();
				if(bStop)
					return;
			}
			System.out.println(getName());
		}
	}
	public void stopThread()
	{
		bStop=true;
	}
}