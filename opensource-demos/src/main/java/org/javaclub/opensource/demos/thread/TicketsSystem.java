package org.javaclub.opensource.demos.thread;

class TicketsSystem
{
	public static void main(String[] args)
	{
		SellThread st=new SellThread();
		
		new Thread(st).start();
		try
		{
			Thread.sleep(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		st.b=true;
		new Thread(st).start();
		//new Thread(st).start();
		//new Thread(st).start();
	}
}

class SellThread implements Runnable
{
	int tickets=100;
	Object obj=new Object();
	boolean b=false;
	public void run()
	{
		if(b==false)
		{
			while(true)
				sell();
		}
		else
		{
			while(true)
			{
				synchronized(obj)
				{
					try
					{
						Thread.sleep(10);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					synchronized(this)
					{
						if(tickets>0)
						{
							
							System.out.println("obj:"+Thread.currentThread().getName()+
									" sell tickets:"+tickets);
							tickets--;
						}
					}
				}
			}
		}
	}
	public synchronized void sell()
	{
		synchronized(obj)
		{
			if(tickets>0)
			{
				try
				{
					Thread.sleep(10);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				System.out.println("sell():"+Thread.currentThread().getName()+
						" sell tickets:"+tickets);
				tickets--;
			}
		}
	}
}