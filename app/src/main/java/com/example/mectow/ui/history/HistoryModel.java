package com.example.mectow.ui.history;

public class HistoryModel {
        String time , catagor , field , service , cash;

        public HistoryModel() {
        }

        public HistoryModel(String time, String catagor, String field, String service, String cash) {
            this.time = time;
            this.catagor = catagor;
            this.field = field;
            this.service = service;
            this.cash = cash;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCatagor() {
            return catagor;
        }

        public void setCatagor(String catagor) {
            this.catagor = catagor;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }
    }
