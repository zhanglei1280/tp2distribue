public class Message {
    private String info;
    private int estampille;

    public Message(String info, int estampille) {
        this.info = info;
        this.estampille = estampille;
    }

    public String getInfo() {
        return this.info;
    }

    public int getEstampille() {
        return this.estampille;
    }

    public void setEstampille(int estampille)
    {
        this.estampille = estampille;
    }

    public String toString() {
        return "Ga Bu Zo Meu: " + this.info;
    }

}