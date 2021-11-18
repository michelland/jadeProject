package agent;

import jade.lang.acl.ACLMessage;

    public class Beliefs {
        protected String name;
        protected ACLMessage msg;
        protected int battery_pourcentage = 100;
        protected int nb_sample = 0;
        protected int heure = 0;
        protected int x;
        protected int y;
        protected Status status;

        public Beliefs(String name, int _x, int _y){
            setName(name);
            x = _x;
            y = _y;
            status = Status.RUNNING;
        }

        public String getName() {
            return name;
        }
        public ACLMessage getMsg() {
            return msg;
        }
        
        public int getBattery_pourcentage() {
            return battery_pourcentage;
        }

        public int getNb_sample() {
            return nb_sample;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public Status getStatus() {
            return status;
        }

        public int getHeure() {
            return heure;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setMsg(ACLMessage msg) {
            this.msg = msg;
        }

        public void setBattery_pourcentage(int battery_pourcentage) {
            this.battery_pourcentage = battery_pourcentage;
        }

        public void setNb_sample(int nb_sample) {
            this.nb_sample = nb_sample;
        }

        public void setHeure(int heure) {
            this.heure = heure;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }
