package app.owlcms.uievents;

public class TimerEvent {
    private String fopName;

    public TimerEvent() {
    }

    /**
     * Class SetTime.
     */
    static public class SetTime extends TimerEvent {

        private Integer timeRemaining;

        /**
         * Instantiates a new sets the time.
         *
         * @param timeRemaining the time remaining
         */
        public SetTime(Integer timeRemaining) {
            super();
            this.timeRemaining = timeRemaining;
        }

        /**
         * Gets the time remaining.
         *
         * @return the time remaining
         */
        public Integer getTimeRemaining() {
            return timeRemaining;
        }

    }
    
    /**
     * Class StartTime.
     */
    static public class StartTime extends TimerEvent {

        private Integer timeRemaining;
        private boolean silent;

        /**
         * Instantiates a new start time.
         *
         * @param timeRemaining the time remaining
         * @param silent
         */
        public StartTime(Integer timeRemaining, boolean silent) {
            super();
            this.timeRemaining = timeRemaining;
            this.silent = silent;
        }

        /**
         * Gets the time remaining.
         *
         * @return the time remaining
         */
        public Integer getTimeRemaining() {
            return timeRemaining;
        }

        public boolean isSilent() {
            return silent;
        }

    }

    /**
     * Class StopTime.
     */
    static public class StopTime extends TimerEvent {

        private int timeRemaining;

        /**
         * Instantiates a new stop time.
         *
         * @param timeRemaining the time remaining
         */
        public StopTime(int timeRemaining) {
            super();
            this.timeRemaining = timeRemaining;
        }

        /**
         * Gets the time remaining.
         *
         * @return the time remaining
         */
        public Integer getTimeRemaining() {
            return timeRemaining;
        }
    }

    public String getFopName() {
        return fopName;
    }

    public void setFopName(String fopName) {
        this.fopName = fopName;
    }

}
