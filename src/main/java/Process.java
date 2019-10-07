import com.google.common.eventbus.Subscribe;

import static java.lang.Math.max;


public class Process  implements Runnable {
    private Thread thread;
    private EventBusService bus;
    private boolean alive;
    private boolean dead;
    private static int nbProcess = 0;
    private int id = Process.nbProcess++;
    private int horlogeLamport;

    public Process(String name){

        this.bus = EventBusService.getInstance();
        this.bus.registerSubscriber(this); // Auto enregistrement sur le bus afin que les methodes "@Subscribe" soient invoquees automatiquement.


        this.thread = new Thread(this);
        this.thread.setName(name);
        this.alive = true;
        this.dead = false;
        this.thread.start();
        this.horlogeLamport = 0;
    }

    // Declaration de la methode de callback invoquee lorsqu'un message de type Bidule transite sur le bus
    @Subscribe
    public void onTrucSurBus(Bidule b){
        System.out.println(Thread.currentThread().getName() + " receives: " + b.getMachin() + " for " + this.thread.getName());
        horlogeLamport++;
    }

    @Subscribe
    public void receive(Message message)
    {
        System.out.println(Thread.currentThread().getName() + " receives: " + message.getInfo() + " for " + this.thread.getName());
        horlogeLamport = max(message.getEstampille(), horlogeLamport) + 1;
    }

    public void send(Message message){
        System.out.println(Thread.currentThread().getName() + " send : " + message.getInfo());
    }




    public void run(){
        int loop = 0;

        System.out.println(Thread.currentThread().getName() + " id :" + this.id);

        while(this.alive){
            System.out.println(Thread.currentThread().getName() + " Loop : " + loop);
            try{
                Thread.sleep(500);

                if(Thread.currentThread().getName().equals("P1")){
                    horlogeLamport++;
                    Message message = new Message("ga", horlogeLamport);
                    send(message);
                    bus.postEvent(message);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            loop++;
        }

        // liberation du bus
        this.bus.unRegisterSubscriber(this);
        this.bus = null;
        System.out.println(Thread.currentThread().getName() + " stoped");
        this.dead = true;
    }

    public void waitStoped(){
        while(!this.dead){
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        this.alive = false;
    }
}